# Employee System — Backend

Spring Boot 2.4.5 REST API backed by MySQL. Handles employee management, departments, tickets, vacations, and check-ins.

## Run

```bash
# from this directory
cd EmployeeSystem

# build
mvn package

# run
java -jar target/EmployeeSystem-0.0.1-SNAPSHOT.jar
```

Or via Docker Compose from the repo root:

```bash
docker-compose up
```

## API

| Endpoint | Purpose |
|---|---|
| `http://localhost:9998/swagger-ui.html` | Interactive API docs |
| `http://localhost:9998/actuator/health` | Health check |
| `http://localhost:9998/actuator/metrics` | Metrics (HikariCP pool, JVM, etc.) |

## Tests

```bash
cd EmployeeSystem

mvn test                                          # all tests
mvn test -Dtest=UserServiceTest                   # single class
mvn test -Dtest=UserServiceTest#testSignIn        # single method
mvn jacoco:report                                 # coverage report → target/site/jacoco/
```

---

## High Concurrency Design

This section documents the changes made to make the system production-ready under concurrent load, and the reasoning behind each one.

### Problem areas identified

The original codebase had several patterns that are harmless under low load but fail under concurrency:

- List endpoints loaded entire tables into JVM memory and filtered in Java (`findAll()` + stream)
- No database indexes on any foreign key or lookup column
- Write methods had no transaction boundaries — concurrent writers could interleave
- Token authentication hit the database on every single request
- `GET /users/` returned multi-megabyte photo BLOBs for every user in the list
- Connection pool was at the Spring Boot default of 10 connections

---

### Changes

#### 1. Eliminate in-memory table scans

**Why:** `findAll()` loads every row into the JVM heap. Three services did this — `CheckinService`, `VacationService`, `UserService` — each executing a full table scan per HTTP request and filtering in a Java stream. Under concurrent load this multiplies linearly.

**What changed:** Added derived query methods to each repository so the `WHERE` clause runs in MySQL, not in the application.

```java
// Before
List<Checkin> all = checkinRepository.findAll();
return all.stream().filter(c -> c.getUserId().equals(userId)).collect(...);

// After
return checkinRepository.findByUserId(userId);
```

Added `@Index` annotations to the affected columns so MySQL can satisfy these queries with an index seek rather than a full scan: `check_in.user_id`, `vacation.user_id`, `vacation.status`, `users.manager_id`, `users.departement_id`, `users.email` (unique), and `tokens.token` (unique).

---

#### 2. Transaction boundaries on writes

**Why:** Without `@Transactional`, a partial failure mid-method leaves the database in an inconsistent state. More critically, `TicketService.updateTicket()` did `deleteById()` followed by `save()` as two separate operations — a concurrent reader between those two calls would see a missing ticket.

**What changed:** Added `@Transactional` to all write methods across every service. Rewrote `updateTicket` as a read-modify-write within a single transaction (no delete).

---

#### 3. Exclude photo BLOBs from the user list

**Why:** `GET /users/` returned the full `User` JPA entity, including a `byte[]` photo field stored as a database BLOB. With many concurrent requests, this floods the JVM heap and saturates network bandwidth for data callers don't need.

**What changed:** Introduced a `UserSummary` JPA interface projection with only the seven non-BLOB fields. The list endpoint now calls `findAllProjectedBy()` which instructs Hibernate to `SELECT` only those columns. Photos are still retrievable individually via `GET /users/photo/{id}`.

---

#### 4. Paginate all list endpoints

**Why:** Unbounded list queries grow with the dataset. A `GET /tickets/` that returns 10 rows today returns 100,000 rows a year from now, blocking the thread and exhausting heap.

**What changed:** All list endpoints now accept `?page=0&size=20` query parameters and return a Spring `Page<T>` response. Default page size is 20.

```
GET /ticket/?page=0&size=20
GET /checkin/?page=0&size=20
GET /vacation/?page=0&size=20
GET /users/?page=0&size=20
```

---

#### 5. Connection pool tuning

**Why:** Spring Boot 2.x defaults to HikariCP with a maximum pool size of 10. Under moderate concurrency (20+ simultaneous requests each needing a DB connection), threads queue waiting for a connection — this alone causes cascading timeouts before any other bottleneck is reached.

**What changed:** Raised `maximum-pool-size` to 30, set `minimum-idle` to 10, and enabled HikariCP metrics via Actuator so pool saturation is visible in production.

---

#### 6. JWT — stateless authentication

**Why:** The original auth system stored a UUID token in the database and performed a `findTokenByToken()` lookup on every authenticated request. Under load this doubles the number of DB queries (one for business logic, one for auth validation). That column also had no index.

**What changed:** Replaced UUID DB tokens with HS256 JWTs. Sign-in now returns a JWT signed with a server secret. Every subsequent request validates the JWT signature in memory — no database hit for authentication at all. The secret is injected via the `JWT_SECRET` environment variable (with a dev-only fallback).

```
# production deployment
JWT_SECRET=<base64-encoded-256-bit-key>
```

Token expiry defaults to 24 hours (`jwt.expiration-ms=86400000`).

---

#### 7. Caffeine in-process caching

**Why:** Even with JWT auth, each request still performs a `findById` to load the full `User` object. Departments and option schemas are read on almost every page render and never change in normal operation.

**What changed:** Added a `CacheConfig` with a `SimpleCacheManager` backed by Caffeine, with per-cache TTLs:

| Cache | Key | TTL | Eviction |
|---|---|---|---|
| `tokens` | JWT string | 30 min | `@CacheEvict` on any user write |
| `departments` | — / departmentId | 5 min | `@CacheEvict` on add/update |
| `department` | departmentId | 5 min | `@CacheEvict` on add/update |
| `option-schemas` | `'all'` / `'active'` | 5 min | — (read-only data) |

When a user is updated, all entries in the `tokens` cache are evicted so stale user objects are never served.

---

#### 8. Rate limiting

**Why:** Without rate limiting, a single client can saturate the check-in endpoint or trigger mass email sends with no back-pressure.

**What changed:** Added a `RateLimitInterceptor` (Bucket4j 7.x token bucket algorithm) applied globally. Each unique client IP gets 200 requests per minute. Buckets are stored in a Caffeine cache with a 2-minute idle expiry so inactive IPs do not accumulate in memory. Returns HTTP 429 when the limit is exceeded.

---

### Dependency summary

| Library | Version | Purpose |
|---|---|---|
| `spring-boot-starter-cache` | managed | Spring Cache abstraction |
| `caffeine` | managed (2.x) | In-process cache implementation |
| `jjwt-api/impl/jackson` | 0.11.5 | JWT generation and validation |
| `bucket4j-core` | 7.6.0 | Token bucket rate limiting |

---

### Architecture after changes

```
Request
  └─ RateLimitInterceptor (429 if over 200 req/min per IP)
       └─ Controller
            └─ AuthenticationService.authenticate(token)
                 └─ JwtUtil.isTokenValid()  ← in-memory, no DB
            └─ Service  (@Transactional)
                 └─ Repository  (derived query with DB index)
                 └─ Caffeine cache  (departments, option-schemas, token→User)
```
