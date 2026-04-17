# C10K Scaling Ideas for BookingHub

> How to evolve the booking system to handle 10,000 concurrent clients.

---

## Current Architecture Bottlenecks

The system uses Spring Boot + Tomcat (blocking servlet model), JDBC/JPA, and a `ReentrantLock` held **during DB I/O**. Under C10K load this collapses:

| Bottleneck | Details |
|---|---|
| **Blocking I/O** | Each Tomcat thread is held for the full request: lock wait + DB query + DB write ≈ 30–100 ms |
| **Lock held during DB I/O** | `ResourceLockManager` serializes ALL booking creates per resource while DB queries run |
| **Tiny connection pool** | Only 20 Hikari connections in prod → 9,980 threads starving |
| **No backpressure** | No rate limiting, circuit breakers, or admission control — system crashes silently |

**Net effect at 10K concurrent clients:** queue explosion → memory exhaustion → crash.

---

## 22 Ideas, Ranked by Impact

### 🔴 Tier 1 — Fix the I/O Model (Root Cause)

**1. WebFlux + R2DBC (Reactive Rewrite)**
- Replace `spring-boot-starter-web` (Tomcat) with `spring-boot-starter-webflux` (Netty).
- Replace JDBC/JPA with R2DBC reactive repositories.
- Use `Mono`/`Flux` throughout the service layer.
- Effect: A handful of event-loop threads serve 10K connections. Threads are released during I/O waits — the canonical C10K fix.
- Trade-off: Largest refactor; entire service/repo layer changes.

**2. Virtual Threads — JDK 21 Project Loom** ⭐ Best ROI
- Add `spring.threads.virtual.enabled=true` to `application.yml`.
- JDK 21 mounts virtual threads on OS carrier threads; blocking I/O suspends the virtual thread, not the carrier.
- Effect: Blocking code stays unchanged; the JVM handles millions of lightweight threads. Minimum code change of any Tier 1 option.
- Trade-off: `ReentrantLock` pins carrier threads — replace with `Semaphore` or restructure critical sections.

**3. Tune Tomcat Thread Pool (Quick Win)**
- Add to `application.yml`:
  ```yaml
  server:
    tomcat:
      threads:
        max: 800
      accept-count: 2000
  ```
- Effect: Handles bursts better before queuing. Doesn't fix blocking root cause but immediate improvement.
- Trade-off: Memory rises (~1 MB stack × 800 threads = 800 MB).

**4. Spring MVC Async (DeferredResult / CompletableFuture)**
- Annotate controller methods to return `DeferredResult<ResponseEntity>` or `CompletableFuture<ResponseEntity>`.
- Tomcat servlet thread is released immediately; work continues on a separate executor.
- Effect: Tomcat accepts far more connections without exhausting the servlet pool. No full reactive rewrite required.

---

### 🔴 Tier 2 — Reduce Lock Contention

**5. Optimistic Locking (Use Existing `@Version` Field)**
- The `@Version` field on `Booking` already exists but is unused.
- Remove `PESSIMISTIC_WRITE` + `ReentrantLock` for the happy path; use DB-level CAS via `@Version`.
- On `OptimisticLockException`, retry once (most bookings succeed on first attempt).
- Effect: Zero lock contention for the common case — threads are no longer serialized per resource.
- Trade-off: Retry logic needed; slightly more complex conflict handling.

**6. Narrow the Lock Scope**
- Currently the lock covers: conflict query + INSERT + idempotency INSERT.
- Move the idempotency INSERT **outside** the lock — it doesn't need serialization.
- Effect: Lock hold time drops from ~80 ms to ~30 ms, directly reducing queue depth.

**7. DB-Only Serialization (Remove JVM Lock)**
- Replace `ReentrantLock` with PostgreSQL `pg_advisory_lock(hash(resourceId))` or `SELECT ... FOR UPDATE`.
- Effect: Lock lives in the DB, not the JVM. App servers become stateless and can scale horizontally.

---

### 🔴 Tier 3 — Database Scalability

**8. Increase Hikari Connection Pool**
- Change `maximum-pool-size` from 20 → 100–200 in `application.yml`.
- Add `minimum-idle: 20` to avoid cold-start latency.
- Note: Postgres default `max_connections = 100` — raise it or use PgBouncer (see below).

**9. PgBouncer (Connection Pooler)**
- Place PgBouncer in front of PostgreSQL in **transaction-pooling mode**.
- Postgres sees a small fixed connection count; the app opens thousands of logical connections.
- Effect: Decouples app concurrency from Postgres `max_connections` — critical for C10K.

**10. Read Replica + CQRS Split**
- Route `GET /bookings` and `GET /resources` to a **read replica**.
- Route writes (POST, DELETE) to the primary.
- Effect: Read traffic (~80%) bypasses write locks entirely, freeing the primary for booking inserts.

**11. Partition `booking` Table by `resourceId`**
- Range or hash partition the `booking` table by `resourceId`.
- Effect: Lock contention and index scans are isolated per partition — different resources don't compete.

---

### 🟠 Tier 4 — In-Process Queuing & Batching

**12. Per-Resource Async Queue**
- Each resource gets a bounded `LinkedBlockingQueue` + one consumer thread.
- Booking requests are enqueued; the consumer processes them sequentially — no synchronization needed.
- Backpressure: when queue is full, return HTTP 429 immediately.
- Effect: Eliminates lock contention entirely; natural backpressure.
- Trade-off: Async response pattern required (polling or WebSocket for result).

**13. Request Coalescing / Batch Inserts**
- Aggregate booking requests within a 5–10 ms window for the same resource.
- One conflict query + batch INSERT replaces N sequential query+insert cycles.
- Effect: Reduces total DB round trips significantly under sustained high load.

---

### 🟠 Tier 5 — Backpressure, Rate Limiting & Circuit Breaking

**14. Rate Limiting (Token Bucket)**
- Add Resilience4j `RateLimiter` or Spring Cloud Gateway throttling.
- Example: 10 booking attempts/second per user or per resource.
- Effect: Prevents thundering herd from overwhelming the lock queue.

**15. Circuit Breaker (Resilience4j)**
- Wrap DB calls with a circuit breaker.
- When failure rate > threshold → open circuit → return `503` immediately instead of queuing.
- Effect: Fast failure under overload; prevents cascading queue buildup.

**16. Semaphore Bulkhead (Bounded Concurrency)**
- Add a `Semaphore(permits = 500)` at the controller level.
- Request #501 receives HTTP 429 immediately.
- Effect: Explicit backpressure — system degrades gracefully rather than crashing.

**17. Admission Control at Load Balancer / API Gateway**
- Use NGINX `limit_req_zone` or AWS API Gateway throttling upstream.
- Effect: Overload never reaches the JVM — offload rejection to infrastructure.

---

### 🟡 Tier 6 — Caching & Reduced DB Round Trips

**18. Distributed Cache (Redis) for Resources**
- Replace the in-process `ResourceCache` (`ConcurrentHashMap`) with Redis.
- Effect: Multiple app instances share one cache; resource lookups skip the DB cluster-wide.

**19. Cache Booking Conflict Windows in Redis**
- Store booked time slots per resource in a Redis sorted set (`ZADD`).
- Conflict detection becomes a Redis range query (`ZRANGEBYSCORE`) instead of a DB query with `PESSIMISTIC_WRITE`.
- Effect: Eliminates the most expensive DB operation from the booking create hot path.

**20. Idempotency Keys in Redis (with TTL)**
- Move idempotency key lookup/store from the `idempotency_key` DB table to Redis.
- Effect: Removes one DB round trip from every booking create; enables horizontal scaling.

---

### 🟡 Tier 7 — Horizontal Scaling

**21. Distributed Lock (Redis `SET NX PX`)**
- The current `ReentrantLock` in `ResourceLockManager` is JVM-local — breaks with multiple app instances.
- Replace with Redis distributed lock (`SET resourceId NX PX 5000`).
- Effect: Enables running 3–10 stateless app instances behind a load balancer, multiplying capacity linearly.

**22. Kubernetes HPA (Horizontal Pod Autoscaler)**
- Containerize the app; configure HPA to scale pods based on CPU utilization or custom request-rate metrics.
- Effect: Infrastructure scales out automatically as load grows, scales in when it drops.

---

## Summary Table

| # | Idea | Effort | Impact | Keeps Current Code Style |
|---|------|:------:|:------:|:------------------------:|
| 2 | Virtual Threads (Loom) | 🟢 Trivial | 🔴 High | ✅ |
| 3 | Tune Tomcat thread pool | 🟢 Trivial | 🟠 Medium | ✅ |
| 7 | Increase Hikari pool | 🟢 Trivial | 🟠 Medium | ✅ |
| 6 | Narrow lock scope | 🟢 Low | 🟠 Medium | ✅ |
| 5 | Optimistic locking (`@Version`) | 🟡 Medium | 🔴 High | ✅ |
| 4 | MVC Async (DeferredResult) | 🟡 Medium | 🟠 Medium | Partial |
| 7 | DB-only lock (pg_advisory_lock) | 🟡 Medium | 🟠 Medium | ✅ |
| 9 | PgBouncer | 🟡 Medium | 🟠 Medium | ✅ (infra) |
| 14 | Rate limiting (Resilience4j) | 🟡 Medium | 🟠 Medium | ✅ |
| 15 | Circuit breaker | 🟡 Medium | 🟠 Medium | ✅ |
| 20 | Idempotency keys in Redis | 🟡 Medium | 🟠 Medium | ✅ |
| 21 | Distributed lock (Redis) | 🟡 Medium | 🔴 High (multi-instance) | ✅ |
| 22 | Kubernetes HPA | 🟡 Medium | 🔴 High | ✅ (infra) |
| 10 | Read replica + CQRS | 🔴 High | 🟠 Medium | ✅ |
| 12 | Per-resource async queue | 🔴 High | 🔴 High | ❌ |
| 18 | Redis resource cache | 🔴 High | 🟠 Medium | ✅ |
| 19 | Redis conflict cache | 🔴 High | 🟠 Medium | ✅ |
| 1 | WebFlux + R2DBC | 🔴 High | 🔴 Highest | ❌ (rewrite) |

---

## Recommended Quick-Win Path

> Achieves C10K readiness without a full architecture rewrite.

```
Step 1 — Enable JDK 21 Virtual Threads
         spring.threads.virtual.enabled=true
         → 1 config line, biggest single-line ROI

Step 2 — Tune Tomcat
         server.tomcat.threads.max=800
         server.tomcat.accept-count=2000

Step 3 — Narrow lock scope
         Move idempotency INSERT outside the ReentrantLock critical section
         → Cuts lock hold time from ~80ms to ~30ms

Step 4 — Switch to Optimistic Locking
         Use existing @Version field on Booking
         Remove PESSIMISTIC_WRITE query + ReentrantLock
         Add retry-on-OptimisticLockException
         → Eliminates per-resource serialization

Step 5 — Database layer
         Increase Hikari pool: maximum-pool-size=150, minimum-idle=20
         Add PgBouncer in transaction mode

Step 6 — Add Resilience4j Bulkhead
         Semaphore(500) at controller level → HTTP 429 when overloaded
         → Graceful degradation instead of crash
```
