# Container Architecture — ShoppingCart Service

> Audience: senior backend engineers onboarding to the container setup.
> Covers how each container is built, how they connect, how health is managed,
> and why specific design decisions were made.

---

## Table of Contents

1. [Overview](#1-overview)
2. [Image Build — multi-stage Dockerfile](#2-image-build--multi-stage-dockerfile)
3. [Dev stack — docker-compose.yml](#3-dev-stack--docker-composeyml)
4. [HA stack — docker-compose-ha.yml](#4-ha-stack--docker-compose-hayml)
5. [Service dependency and health chain](#5-service-dependency-and-health-chain)
6. [MySQL replication wiring](#6-mysql-replication-wiring)
7. [Nginx configuration rationale](#7-nginx-configuration-rationale)
8. [Environment and profile system](#8-environment-and-profile-system)
9. [CI pipeline](#9-ci-pipeline)
10. [Operating runbook](#10-operating-runbook)

---

## 1. Overview

Two compose configurations exist for different purposes:

| File | Purpose | Services |
|---|---|---|
| `docker-compose.yml` | Local dev, single instance | app, mysql, redis, vue |
| `docker-compose-ha.yml` | HA / staging, horizontally scaled | nginx, app×N, mysql-primary, mysql-replica, redis |

Both share the same backend Docker image (multi-stage build). The difference is topology:
how many app instances run, whether a load balancer fronts them, and whether MySQL has a replica.

```
┌─────────────────────────────────────────────────────────────┐
│  Dev stack (docker-compose.yml)                             │
│                                                             │
│  [Vue :8080] → [App :9999] → [MySQL :3306]                  │
│                           → [Redis :6379]                   │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│  HA stack (docker-compose-ha.yml)                           │
│                                                             │
│  [Client] → [Nginx :80]  ──least_conn──→ [App-1 :9999]     │
│                          │              → [App-2 :9999]     │
│                          │              → [App-N :9999]     │
│                          │                    │             │
│                          │              [Redis :6379]        │
│                          │         (cache + dist. lock)     │
│                          │                                  │
│                          │     [MySQL-primary :3306]        │
│                          │            │  binlog/GTID        │
│                          │     [MySQL-replica :3307]        │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 2. Image Build — multi-stage Dockerfile

```dockerfile
# Stage 1: full Maven + JDK (only for compilation)
FROM maven:3.9-eclipse-temurin-17 AS build
COPY pom.xml .
RUN mvn dependency:go-offline    # layer cached separately from source
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: slim JRE runtime only
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring
COPY --from=build target/ShoppingCart-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", ...]
```

### Why two stages

The build stage contains Maven, the JDK compiler, all source code, and test classes — none of
which belong in production. The runtime stage contains only the JRE and the JAR. This gives:

- **Image size**: ~500 MB (single-stage with Maven) → ~200 MB (JRE-only alpine)
- **Attack surface**: no compiler, no build tools, no source code in the final image
- **Startup time**: `mvn spring-boot:run` (recompile ~3 min) → `java -jar` (~15 s)

### Layer caching strategy

`pom.xml` is copied and `dependency:go-offline` is run **before** `src/` is copied.
Docker only re-runs the dependency download layer when `pom.xml` changes — which is rare.
Source code changes only invalidate the `mvn package` layer, not the dependency layer.
This makes incremental builds fast (~30 s vs ~3 min cold).

### JVM flags

| Flag | Reason |
|---|---|
| `-XX:+UseContainerSupport` | Reads cgroup CPU/memory limits instead of host totals. Without this, the JVM on a 32-core host allocates a heap sized for 32 cores even if the container limit is 2 CPUs. Default in JDK 11+ but explicit is safer. |
| `-XX:MaxRAMPercentage=75.0` | Uses up to 75% of container memory for heap. The remaining 25% is for the JVM's off-heap (metaspace, code cache, thread stacks, Netty/NIO buffers). |
| `-Djava.security.egd=file:/dev/./urandom` | Speeds up `SecureRandom` initialization. On low-entropy CI/container environments, `/dev/random` blocks for seconds during Tomcat startup waiting for entropy. `/dev/urandom` is non-blocking and cryptographically sufficient for session ID generation. |

### Non-root user

The `spring` user is created and used in the runtime stage. Running as root in a container
is a security risk — if the JVM is exploited, the attacker gets root inside the container,
which can lead to container escape. Running as a non-root user limits the blast radius.

---

## 3. Dev Stack — `docker-compose.yml`

### Services

```yaml
services:
  app:    # Spring Boot (built from Backend/Dockerfile)
  vue:    # Vue.js dev server (hot reload via volume mount)
  mysql:  # MySQL 8.0
  redis:  # Redis 7 with AOF persistence
```

### Startup order

Docker Compose `depends_on` with `condition: service_healthy` enforces a strict startup chain:

```
redis (healthcheck: redis-cli ping)
    ↓ healthy
mysql (healthcheck: mysqladmin ping)
    ↓ healthy
app   (healthcheck: wget /actuator/health)
```

Without `condition: service_healthy`, `depends_on` only waits for the container to **start**,
not for the service inside to be **ready**. MySQL takes 10–30 s to initialize on first run;
if the app starts before MySQL is ready, every JPA `@PostConstruct` / schema validation fails.

### Redis persistence

```yaml
command: redis-server --save 60 1 --loglevel warning
```

`--save 60 1` = write an RDB snapshot if at least 1 key changed in the last 60 seconds.
This means cache data survives a container restart without requiring the app to re-warm
all cache entries from MySQL. Without persistence, every restart produces a cold cache
and a MySQL read spike.

### Why vue uses a volume mount but app does not

The Vue dev server (`npm run serve`) supports hot module replacement — it watches the
filesystem and pushes changes to the browser without restarting. The volume mount
`./Frondend/ecommerce-ui:/app` makes the host source visible inside the container.

The Spring Boot app uses a built JAR. If you want hot reload for the backend in dev,
use `spring-boot-devtools` with the `mvn spring-boot:run` command (not the Docker image).
The image is for a production-like environment; devtools live-reload doesn't work well
across Docker volume mounts.

---

## 4. HA Stack — `docker-compose-ha.yml`

### Services

```yaml
services:
  nginx:          # Load balancer (least_conn, rate limit, keepalive)
  app:            # Spring Boot (N replicas via --scale app=N)
  redis:          # Redis 7, maxmemory 256mb, allkeys-lru
  mysql-primary:  # MySQL 8.0, GTID binlog, server-id=1
  mysql-replica:  # MySQL 8.0, GTID, server-id=2, read-only=ON
```

### Scaling

```bash
docker-compose -f docker-compose-ha.yml up --build --scale app=3
```

Nginx resolves `app:9999` to all running replicas via Docker's internal DNS. When you scale
to 3, Docker creates 3 containers named `shoppingcart-app-1`, `shoppingcart-app-2`, etc.,
all reachable at the `app` hostname via DNS round-robin. Nginx's `least_conn` then distributes
across whichever are healthy.

### Per-instance pool size adjustment

```yaml
SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE: 20
```

With 3 instances, total MySQL connections = `3 × 20 = 60`. MySQL's `max_connections=200`
leaves headroom for migrations, monitoring, and the replica. When you change `--scale`, also
recalculate: `pool_size = (max_connections - 10) / instance_count`.

### Read replica activation

```yaml
APP_DATASOURCE_REPLICA_ENABLED: "true"
```

Setting this env var activates `DataSourceConfig` (which is `@ConditionalOnProperty`).
Spring Boot's relaxed binding maps `APP_DATASOURCE_REPLICA_ENABLED` →
`app.datasource.replica.enabled`. When enabled, `@Transactional(readOnly=true)` methods
route to `mysql-replica`; all writes go to `mysql-primary`.

---

## 5. Service Dependency and Health Chain

### Health check commands

| Service | Health command | Why this command |
|---|---|---|
| mysql / mysql-primary / mysql-replica | `mysqladmin ping -h localhost --silent` | Checks the TCP listener and that MySQL is accepting connections. `--silent` suppresses the "mysqld is alive" output so exit code alone is used. |
| redis | `redis-cli ping` | Returns `PONG` on success. Exits non-zero if Redis isn't listening. |
| app | `wget -qO- http://localhost:9999/actuator/health` | Spring Boot Actuator checks all health indicators: DataSource connection, Redis connection, disk space. Only returns `UP` when all pass. |
| nginx | `wget -qO- http://localhost/nginx-health` | A static 200 response served by Nginx itself — verifies Nginx is running, not the upstream apps. |

### start_period

`start_period: 30s` on MySQL, `30s` on the app. During `start_period`, failed healthchecks
don't count against `retries`. This prevents containers from being marked `unhealthy` during
normal slow initialization (MySQL data directory init on first run takes 20–30 s).

### depends_on propagation

In the HA stack:
```
mysql-primary → healthy
    ↓
mysql-replica → starts (its init script then waits for primary internally)
    ↓
redis → healthy (in parallel with MySQL)
    ↓
app → healthy (waits for both mysql-primary and redis)
    ↓
nginx → starts (waits for app healthy)
```

---

## 6. MySQL Replication Wiring

### Why GTID-based replication

Classic binlog replication requires the replica to know the exact `(binlog_file, position)`
of the primary at the moment it starts replicating. In a Docker Compose setup where both
containers start "simultaneously", capturing this position reliably is hard. GTID (Global
Transaction ID) uses a UUID-based transaction identifier that both primary and replica agree
on — the replica can auto-position itself with `MASTER_AUTO_POSITION=1` without knowing the
binlog file and offset.

### Primary configuration

```yaml
command: >
  --server-id=1
  --log-bin=mysql-bin
  --binlog-format=ROW
  --gtid-mode=ON
  --enforce-gtid-consistency=ON
```

`--binlog-format=ROW` logs the actual row changes (not the SQL statements). This is more
reliable for replication because row-based events are deterministic — the replica doesn't
need to re-execute SQL that might produce different results.

### Init scripts

**`mysql/primary-init.sql`** (runs at first container startup via `docker-entrypoint-initdb.d`):

```sql
CREATE USER IF NOT EXISTS 'repl'@'%' IDENTIFIED BY 'repl_password';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
```

**`mysql/replica-init.sh`** (runs at first container startup):

```bash
# Wait for primary to accept connections
until mysql -hmysql-primary -uroot -e "SELECT 1" >/dev/null 2>&1; do sleep 3; done

mysql -uroot <<SQL
  CHANGE MASTER TO
    MASTER_HOST='mysql-primary',
    MASTER_USER='repl',
    MASTER_PASSWORD='repl_password',
    MASTER_AUTO_POSITION=1;
  START SLAVE;
SQL
```

The replica init script runs *after* this container's own MySQL instance is up (Docker's
entrypoint guarantee), but waits in a loop until the primary is reachable. This handles the
race where both containers start at the same time.

### Verify replication is running

```bash
docker exec -it shoppingcart-mysql-replica-1 mysql -uroot -e "SHOW SLAVE STATUS\G"
# Look for: Slave_IO_Running: Yes, Slave_SQL_Running: Yes
```

### Important caveats

- Init scripts in `docker-entrypoint-initdb.d` only run when the data directory is **empty**
  (i.e., first startup). If you `docker-compose down` without `-v`, the data persists and
  init scripts do not re-run on the next `up`. Use `docker-compose down -v` to reset volumes
  and re-trigger initialization.
- Replication lag on a dev laptop is typically <100 ms. Under heavy write load it can grow.
  Monitor with `Seconds_Behind_Master` in `SHOW SLAVE STATUS`.

---

## 7. Nginx Configuration Rationale

### `least_conn` upstream

```nginx
upstream shoppingcart {
    least_conn;
    server app:9999;
    keepalive 32;
}
```

`round_robin` (default) distributes requests by count. `least_conn` distributes by the number
of active connections, which is better when requests have unequal processing time (checkout
is slow, product listing is fast). Under load, this prevents the slowest instance from
accumulating more and more pending requests while others are idle.

### keepalive to upstream

`keepalive 32` keeps 32 idle TCP connections open to app instances. Without this, every
proxied request opens a new TCP connection to the app — a TCP handshake (~1 ms) that adds
up at high concurrency. `proxy_http_version 1.1` and `proxy_set_header Connection ""` are
required for keepalive to work with HTTP/1.1.

### Rate limiting

```nginx
limit_req_zone $binary_remote_addr zone=api:10m rate=100r/s;
limit_req zone=api burst=200 nodelay;
```

Limits each client IP to 100 requests/second with a burst window of 200. `nodelay` means
burst requests are processed immediately (not delayed to enforce the rate) — excess requests
beyond the burst are dropped with 503. This protects against single-source floods without
adding latency to legitimate bursts.

### `proxy_next_upstream`

```nginx
proxy_next_upstream error timeout http_502 http_503;
proxy_next_upstream_tries 2;
```

If the first upstream instance returns a connection error or 502/503, Nginx immediately
retries the request on a different instance. `tries 2` limits retries to prevent a single
slow request from hammering all instances.

---

## 8. Environment and Profile System

### Spring Boot profile: `docker`

`application.properties` contains dev/localhost defaults.
`application-docker.properties` overrides hostnames for containerized environments.

The `docker` profile is activated by `SPRING_PROFILES_ACTIVE=docker` in docker-compose.
Spring Boot merges `application.properties` and `application-docker.properties`, with the
profile-specific file taking precedence.

This pattern avoids having to pass every individual property as an env var in compose.
Only secrets (Stripe keys) and toggle flags (replica.enabled) are passed as env vars.

### Env var → property mapping (Spring Boot relaxed binding)

Spring Boot maps environment variables to properties using relaxed binding rules:

| Env var | Property |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `spring.profiles.active` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `spring.jpa.hibernate.ddl-auto` |
| `APP_DATASOURCE_REPLICA_ENABLED` | `app.datasource.replica.enabled` |
| `APP_DATASOURCE_PRIMARY_URL` | `app.datasource.primary.url` |
| `SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE` | `spring.datasource.hikari.maximum-pool-size` |

Underscores in env var names are converted to dots and hyphens. ALL_CAPS is also folded to lowercase.

### Secrets

Stripe keys (`STRIPE_PUBLIC_KEY`, `STRIPE_SECRET_KEY`) are read from the host environment:

```yaml
environment:
  STRIPE_PUBLIC_KEY: ${STRIPE_PUBLIC_KEY:-}   # empty default, not an error
  STRIPE_SECRET_KEY: ${STRIPE_SECRET_KEY:-}
```

Set them in a `.env` file in the `ShoppingCart/` directory (never commit this file):

```bash
# ShoppingCart/.env
STRIPE_PUBLIC_KEY=pk_test_...
STRIPE_SECRET_KEY=sk_test_...
```

Docker Compose automatically loads `.env` in the same directory as the compose file.

---

## 9. CI Pipeline

### Workflow file

`.github/workflows/docker_shopCartService.yml`

### Three-job design

```
unit-tests ──→ docker-build ──→ integration
```

Each job is a gate for the next. Running them in series keeps the pipeline readable and
ensures failures are diagnosed at the earliest (cheapest) stage:

| Job | Cost | What it catches |
|---|---|---|
| `unit-tests` | ~1 min | Logic bugs, compile errors, broken test setup |
| `docker-build` | ~4 min (cached: ~1 min) | Broken Dockerfile, missing deps, non-root user check |
| `integration` | ~5 min | Service wiring bugs, compose misconfiguration, API regressions |

### Docker layer cache in CI

```yaml
- uses: docker/build-push-action@v5
  with:
    cache-from: type=gha
    cache-to: type=gha,mode=max
```

GitHub Actions' cache is used as the Docker layer store. On first run, all layers are
pushed to the cache. On subsequent runs, unchanged layers (especially the Maven dependency
layer) are pulled from cache — cutting build time from ~4 min to ~1 min.

The `integration` job restores the same cache so it doesn't re-download and re-compile;
it uses the already-built image.

### Health-gate before smoke tests

The integration job waits for `docker compose ps app | grep "healthy"` before running any
curl commands. This is important: without the wait, the smoke tests race with app startup
and fail with connection refused even though the app is fine.

### Smoke test coverage

| Test | Endpoint | Validates |
|---|---|---|
| Product list | `GET /product/` | MySQL read path + cache |
| Category list | `GET /category/` | MySQL read path + cache |
| Actuator health | `GET /actuator/health` | All health indicators (DB, Redis, disk) |
| User register | `POST /user/register` | MySQL write path |
| User signin | `POST /user/signin` | Auth token generation, Redis token cache |

### Tear down

`docker compose down -v` runs unconditionally (`if: always()`). The `-v` flag removes
named volumes, so each CI run starts with an empty MySQL data directory — init scripts
re-run, schema is clean, test data doesn't leak between runs.

---

## 10. Operating Runbook

### Start dev stack

```bash
cd ShoppingCart
docker-compose up --build          # first run (builds image)
docker-compose up                  # subsequent runs (uses cached image)
```

### Start HA stack (3 app instances)

```bash
cd ShoppingCart
docker-compose -f docker-compose-ha.yml up --build --scale app=3
```

### Enable read-replica routing

```bash
APP_DATASOURCE_REPLICA_ENABLED=true \
  docker-compose -f docker-compose-ha.yml up --build --scale app=2
```

### Reset all data (clean slate)

```bash
docker-compose down -v             # removes named volumes (mysql-data, redis-data)
docker-compose up --build
```

### Verify Redis cache is working

```bash
# Inside the redis container:
docker exec -it shoppingcart-redis-1 redis-cli KEYS "*"
# After a GET /product/ request, you should see: products::all
```

### Verify replication lag

```bash
docker exec -it shoppingcart-mysql-replica-1 mysql -uroot \
  -e "SHOW SLAVE STATUS\G" | grep -E "Seconds_Behind|Running|Error"
```

### Check distributed locks

```bash
# During a checkout, a lock key should briefly appear:
docker exec -it shoppingcart-redis-1 redis-cli KEYS "order:user:*"
docker exec -it shoppingcart-redis-1 redis-cli KEYS "cart:user:*"
```

### Scale app instances at runtime (no downtime)

```bash
docker-compose -f docker-compose-ha.yml up --scale app=5 --no-recreate
# Nginx picks up new instances automatically via Docker DNS
```

### Useful health endpoints

| Endpoint | Purpose |
|---|---|
| `http://localhost:9999/actuator/health` | Dev stack: full health (DB, Redis, disk) |
| `http://localhost/actuator/health` | HA stack: same, via Nginx |
| `http://localhost/nginx-health` | HA stack: Nginx itself is alive |
| `http://localhost:9999/swagger-ui/index.html` | Dev stack: Swagger UI |
