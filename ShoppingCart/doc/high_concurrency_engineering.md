# High-Concurrency Engineering: ShoppingCart Service

> Target audience: senior backend engineers who want to understand the *why* and *how* behind
> each change — not just the config values, but the failure modes, trade-offs, and the sequence
> that makes them safe to adopt incrementally.

---

## Table of Contents

1. [Baseline problem](#1-baseline-problem)
2. [Approach 2 — HikariCP Connection Pool Tuning](#2-approach-2--hikaricp-connection-pool-tuning)
3. [Approach 1 — JDK 21 Virtual Threads](#3-approach-1--jdk-21-virtual-threads)
4. [Approach 3 — Redis Caching](#4-approach-3--redis-caching)
5. [Approach 4 — Distributed Lock (Redisson)](#5-approach-4--distributed-lock-redisson)
6. [Approach 7 — Horizontal Scaling + Nginx](#6-approach-7--horizontal-scaling--nginx)
7. [Approach 5 — MySQL Read Replica Routing](#7-approach-5--mysql-read-replica-routing)
8. [Interaction Map](#8-interaction-map)
9. [What was NOT done and why](#9-what-was-not-done-and-why)

---

## 1. Baseline Problem

The original service was a single-threaded Spring Boot 2.4.5 monolith:

```
HTTP thread pool (Tomcat default: 200 platform threads)
    → Spring MVC
    → Service layer (no locking)
    → HikariCP (default pool: 10 connections)
    → MySQL (single primary, default max_connections: 151)
```

Under C10K load the first bottleneck hit is the connection pool. With 10 JDBC connections, the
200 Tomcat threads all queue waiting for a connection. Latency grows linearly; p99 climbs to
seconds. The second bottleneck is MySQL write contention: cart and order endpoints do a
read-modify-write without locking, so concurrent users oversell or corrupt each other's carts.

The fixes are layered — each one removes a ceiling that the next one would otherwise hit.

---

## 2. Approach 2 — HikariCP Connection Pool Tuning

### What changed

```properties
spring.datasource.hikari.maximum-pool-size=50
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.connection-timeout=3000       # ms — fail fast, don't queue forever
spring.datasource.hikari.idle-timeout=600000           # 10 min — evict idle connections
spring.datasource.hikari.max-lifetime=1800000          # 30 min — recycle before MySQL kills them
```

### Why 50

MySQL default `max_connections` is 151. With headroom for replication, monitoring, and migrations:

```
safe pool per instance = (max_connections - 10) / number_of_app_instances
                       = (151 - 10) / 1 = ~140 max, 50 is conservative and leaves room to scale out
```

If you later run 3 instances behind Nginx each with pool-size=50, that's 150 connections — right
at the MySQL ceiling. Either raise `max_connections` on MySQL (requires `innodb_buffer_pool_size`
headroom) or drop pool-size to 40 per instance.

### connection-timeout = 3000 ms

This is the time a thread waits for a connection from the pool before throwing
`SQLTimeoutException`. The default is 30 seconds — which means a traffic spike causes a 30-second
queue of stuck threads, burning memory and Tomcat capacity. Failing fast at 3 seconds lets the
caller get an error immediately and retry or circuit-break.

### max-lifetime < MySQL wait_timeout

MySQL default `wait_timeout` is 28800 seconds (8 hours). If a connection sits idle longer than
`wait_timeout`, MySQL closes it server-side. HikariCP doesn't know this until the next use —
resulting in `Connection is closed` exceptions. Setting `max-lifetime=1800000` (30 min) recycles
connections before MySQL kills them.

### Gotcha: pool-size is per JVM

HikariCP pool-size is per application instance, not per cluster. When you scale out horizontally,
total MySQL connections = `pool_size × instance_count`. Always recalculate after adding instances.

---

## 3. Approach 1 — JDK 21 Virtual Threads

### What changed

```properties
# application.properties — uncomment when running on JDK 21+
# spring.threads.virtual.enabled=true
```

```xml
<!-- pom.xml — set to 21 when deploying with virtual threads -->
<java.version>17</java.version>
```

### Why this helps

Tomcat's default thread model: one platform thread per HTTP request. Platform threads are OS
threads — each costs ~1 MB of stack, and context-switching is expensive. With 200 threads, if
each request spends 50 ms waiting on a JDBC call, throughput is capped at 4000 req/sec in the
best case.

Virtual threads (Project Loom) are heap-allocated coroutines managed by the JVM. When a virtual
thread blocks on I/O (JDBC, HTTP, Redis), the JVM unmounts it from its carrier thread and parks
it. The carrier thread picks up another virtual thread. This means:

- 10,000+ concurrent requests can be in flight simultaneously
- Each still writes blocking JDBC code — no reactive/callback hell
- Stack is allocated lazily, so memory per virtual thread is ~a few KB

### The JDBC exception

Virtual threads do NOT fix a saturated connection pool. If 10,000 virtual threads all try to
acquire one of 50 JDBC connections simultaneously, they still queue on the pool. The fix is
the two together: virtual threads handle the I/O wait cheaply, HikariCP tuning ensures enough
connections are available.

### Spring Boot 3.2 wires this automatically

Setting `spring.threads.virtual.enabled=true` replaces Tomcat's `ThreadPoolExecutor` with
`Executors.newVirtualThreadPerTaskExecutor()`. No code changes. It also configures the async
executor and scheduled task executor to use virtual threads.

### Why it's commented out

The CI/CD and local dev environment is JDK 17. Running `spring.threads.virtual.enabled=true`
on JDK 17 throws at startup. Uncomment when deploying on JDK 21+.

---

## 4. Approach 3 — Redis Caching

### What changed

Three cache regions with per-region TTLs, backed by Redis:

| Cache name   | Key            | TTL    | What it caches                        |
|--------------|----------------|--------|---------------------------------------|
| `tokens`     | token string   | 15 min | `AuthenticationToken → User` lookup   |
| `products`   | `"all"`        | 5 min  | Full product list from DB             |
| `categories` | `"all"`        | 30 min | Full category list from DB            |

### Why token caching matters most

Every protected endpoint calls `authenticationService.getUser(token)`, which does a DB round-trip.
Under high concurrency this one query dominates the read load. Caching it in Redis reduces it to
a sub-millisecond hash lookup for the 15-minute session window.

```java
@Cacheable(value = "tokens", key = "#token", unless = "#result == null")
public User getUser(String token) { ... }

@CacheEvict(value = "tokens", key = "#authenticationToken.token")
public void saveConfirmationToken(AuthenticationToken authenticationToken) { ... }
```

The `unless = "#result == null"` guard prevents caching failed lookups (bad tokens), which
would cause all requests with that bad token to get a null cache hit and never re-check the DB
even if the token was later created.

### Serialization security: BasicPolymorphicTypeValidator

`GenericJackson2JsonRedisSerializer` with `LaissezFaireSubTypeValidator` (the default) embeds
the Java class name in the JSON and will deserialize any class on retrieval. An attacker who can
write to Redis can inject a gadget chain (e.g., via `commons-collections`) to execute arbitrary
code on deserialization.

We replaced it with `BasicPolymorphicTypeValidator` scoped to known packages:

```java
PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
        .allowIfSubType("com.yen.ShoppingCart")
        .allowIfSubType("java.util")
        .allowIfSubType("java.lang")
        .build();
mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
```

Any class outside these packages will throw `InvalidTypeIdException` on deserialization —
the gadget chain can't load.

### Cache invalidation

Product and category caches use `allEntries = true` eviction because the key is `"all"` (we
cache the entire list, not individual items). When a product is added or updated, the entire
list is evicted and rebuilt on the next read.

For fine-grained caching (cache individual products by ID), you'd switch to
`@CacheEvict(key = "#productId")` and `@Cacheable(key = "#productId")` — but that requires
invalidating related list caches separately. YAGNI until list caches become a problem.

### Redis as a single point of failure

If Redis goes down, Spring Cache falls back to calling the underlying method (no cache hit ≠
error). `disableCachingNullValues()` prevents the cache from storing nulls, which combined
with Redis downtime means every request hits MySQL — same as before Redis, not worse.

For production, run Redis Sentinel or Redis Cluster.

---

## 5. Approach 4 — Distributed Lock (Redisson)

### The race condition

`addToCart` and `placeOrder` both do read-modify-write without synchronization:

```
Thread A: read cart → [item1]
Thread B: read cart → [item1]
Thread A: add item2, save → [item1, item2]
Thread B: add item3, save → [item1, item3]   ← item2 is lost
```

`synchronized` fixes this in a single JVM. Once you have multiple instances behind a load
balancer, `synchronized` does nothing — threads in different JVMs contend freely.

### Redisson RLock

Redisson implements the Redlock algorithm over Redis. `getLock("cart:user:42")` creates a
distributed lock whose state is in Redis and is visible to all app instances.

```java
RLock lock = redissonClient.getLock("cart:user:" + user.getId());
try {
    lock.lock(10, TimeUnit.SECONDS);   // lease time: auto-expire after 10s if unlock fails
    // critical section
} finally {
    if (lock.isHeldByCurrentThread()) lock.unlock();
}
```

### Lock key granularity

The key `"cart:user:<id>"` scopes the lock per user. Two users checking out simultaneously
get different keys and proceed in parallel — only the same user serialized. This is the right
granularity for a shopping cart: the user's cart is private, so no two requests for the same
user should interleave.

For flash-sale inventory (shared stock counter), you'd use `"product:stock:<productId>"` —
every buyer for the same SKU contends. That requires a different lock strategy (optimistic
locking or a counter in Redis with DECR) to avoid a bottleneck.

### Lease time vs. watchdog

`lock.lock(10, TimeUnit.SECONDS)` sets a 10-second lease. If the JVM crashes mid-critical-section,
Redis expires the lock after 10 seconds automatically — preventing permanent deadlock.

Redisson also has a watchdog: if you call `lock.lock()` without a lease time, Redisson
automatically extends the lock every 10 seconds (configurable via `lockWatchdogTimeout`) while
the thread is alive. We use explicit lease times to bound worst-case hold duration.

### The `isHeldByCurrentThread()` guard

`lock.unlock()` throws `IllegalMonitorStateException` if the current thread doesn't hold the
lock (e.g., the lease expired mid-operation). The guard prevents the exception from masking
the real error.

If `isHeldByCurrentThread()` returns false, it means the critical section took longer than the
lease — a real problem. Log this case in production.

### Redis as a write-path dependency

With the lock in Redis, Redis downtime blocks all cart writes (lock acquisition will throw).
Options:
- Accept the downtime risk (Redis is operationally reliable at 5 nines)
- Use `tryLock` with a timeout and degrade gracefully (return 503)
- Use Redis Sentinel/Cluster for HA

---

## 6. Approach 7 — Horizontal Scaling + Nginx

### What changed

- `docker-compose-ha.yml`: run `--scale app=N` to launch N app instances
- `nginx/nginx.conf`: `least_conn` upstream, proxy headers, health endpoint
- Spring Boot Actuator `/actuator/health` exposed for readiness probes

### Statelessness prerequisite

The app is already stateless: auth tokens are in MySQL, no local session objects, no
in-process caches that would diverge between instances (Redis is the shared cache). Scaling
out is safe.

If you ever add an in-memory cache (e.g., `ConcurrentHashMap` in a Spring bean), it will
diverge between instances — `instance-1`'s eviction won't reach `instance-2`. Always use
Redis for shared state.

### least_conn vs. round_robin

`round_robin` (Nginx default) distributes requests evenly by count. `least_conn` distributes
by the number of active connections per upstream — better for uneven workloads where some
requests (e.g., checkout) are much slower than others (e.g., product list). Under high
concurrency, `least_conn` keeps the slowest instance from falling further behind.

### Health check / readiness

The load balancer must remove an instance from rotation during startup (before the JPA pool
is ready) and during shutdown (in-flight requests must complete). Spring Boot Actuator exposes:

- `/actuator/health` — liveness: is the JVM alive?
- `/actuator/health/readiness` — readiness: is the app ready to serve traffic?

Nginx doesn't natively do active health checks in the open-source version — use
`nginx_upstream_check_module` or move to HAProxy / AWS ALB which have native active probing.

### Sticky sessions

This service has no sticky-session requirement (stateless auth tokens). If you add server-sent
events or WebSocket, you need `ip_hash` or cookie-based stickiness in Nginx, or move to a
message-bus-backed pub/sub.

---

## 7. Approach 5 — MySQL Read Replica Routing

### What changed

`DataSourceConfig.java` implements `AbstractRoutingDataSource`:

```java
@Override
protected Object determineCurrentLookupKey() {
    return TransactionSynchronizationManager.isCurrentTransactionReadOnly()
            ? "replica"
            : "primary";
}
```

Enabled only when `app.datasource.replica.enabled=true`. Off by default — the standard
Spring Boot auto-configured `DataSource` is used when disabled.

Service read methods annotated with `@Transactional(readOnly = true)`:

```java
// ProductService
@Transactional(readOnly = true)
@Cacheable(value = "products", key = "'all'")
public List<ProductDto> listProducts() { ... }
```

### How routing works end-to-end

1. Spring wraps the service method in a `TransactionInterceptor` because of `@Transactional`.
2. `TransactionInterceptor` sets `readOnly=true` on the transaction synchronization.
3. `AbstractRoutingDataSource.determineCurrentLookupKey()` is called when the first JDBC
   connection is needed — it reads the flag and returns `"replica"`.
4. HikariCP pool for the replica provides the connection.
5. All SQL in the method goes to the replica.

### readOnly + cache interaction

`@Transactional(readOnly = true)` and `@Cacheable` interact in a specific order:
Spring AOP wraps `@Transactional` outside `@Cacheable` by default (higher order). So:

1. Transaction opens (read-only flag set → routing picks replica)
2. Cache check — if hit, method body doesn't run, transaction still commits (empty)
3. If cache miss, method runs → SQL goes to replica → result cached

The empty transaction on cache hits is cheap. The key point: the routing decision is made
at connection acquisition time, not annotation parse time. As long as the transaction is open
and readOnly when the connection is first needed, routing works correctly.

### Replication lag

MySQL replication is asynchronous by default. The replica can be milliseconds to seconds
behind the primary. This means:

- A product just added may not appear in a `/product` list for up to a few seconds
- An order just placed will not show in `/order` immediately if `listOrders` routes to replica

Mitigations:
1. Accept eventual consistency for listings (most e-commerce does)
2. For user-visible writes ("you just placed order #123"), use the primary for the immediate
   read-after-write (`@Transactional` without `readOnly`)
3. Use `WAIT_FOR_EXECUTED_GTID_SET()` to block until a specific binlog position is replicated
   (MySQL 5.7+) — expensive but consistent

### When to enable

Enable the replica when:
- Read QPS on the primary is causing lock contention that slows down writes
- Replication infrastructure (AWS RDS Multi-AZ, or a replica VM) is in place
- You've measured replication lag under prod-like write load and it's acceptable

Enabling prematurely adds operational cost (monitoring replica lag, failover) for no benefit.

---

## 8. Interaction Map

```
Request arrives at Nginx
    │
    ├─ least_conn → App Instance 1 or 2 or N
    │
    └─ App Instance
        ├─ Auth check
        │   └─ Redis (tokens cache) → HIT: O(1), no DB
        │                           → MISS: MySQL primary (then cache for 15 min)
        │
        ├─ GET /product or /category
        │   └─ Redis cache → HIT: O(1)
        │                  → MISS: MySQL replica (@Transactional readOnly=true)
        │
        ├─ POST /cart (addToCart)
        │   └─ Redisson lock "cart:user:<id>"
        │       └─ MySQL primary (write)
        │       └─ release lock
        │
        └─ POST /order/place
            └─ Redisson lock "order:user:<id>"
                └─ MySQL primary (write order + items, delete cart)
                └─ release lock
```

---

## 9. What Was NOT Done and Why

| Omission | Reason |
|---|---|
| Virtual threads enabled by default | Requires JDK 21; CI is JDK 17. Uncomment `spring.threads.virtual.enabled=true` when deploying on 21+. |
| Redis Cluster / Sentinel | Single Redis is sufficient for dev/staging. Add Sentinel before going to production. |
| Per-product stock locking | No stock column in `Product` model. When stock tracking is added, use `"product:stock:<id>"` lock or MySQL `SELECT ... FOR UPDATE`. |
| Read replica in docker-compose-ha.yml | MySQL replication requires GTID config; out of scope for the compose file. Use AWS RDS with a read replica in production. |
| Circuit breaker (Resilience4j) | Not added — the service doesn't call external systems in the hot path beyond Stripe (checkout only). Add if Stripe latency spikes start affecting non-checkout endpoints. |
| Async order confirmation email | Not implemented — no email service in scope. Add `@Async` + `ThreadPoolTaskExecutor` before adding an email provider, then graduate to RabbitMQ for durability. |
