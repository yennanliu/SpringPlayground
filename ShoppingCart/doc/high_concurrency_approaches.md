# High-Concurrency Approaches for ShoppingCart Service

## Context

The current ShoppingCart is a Spring Boot 2.4.5 monolith backed by a single MySQL instance.
All business logic runs synchronously in the HTTP thread pool. Auth tokens are checked against the DB
on every request. Cart/Order writes go directly to MySQL with no buffering or locking primitives.

The branch `ShoppingCart-dev-008-high-concurrency` already introduced:
- JDK 21 Virtual Threads (via `spring.threads.virtual.enabled=true`)
- Redis-based distributed locking (applied in the sibling `bookingSystem` module as a reference)

---

## Approach 1 — JDK 21 Virtual Threads

### What it is
Replace the default Tomcat thread pool (bounded, ~200 threads) with JDK 21 virtual threads.
Each HTTP request gets its own lightweight virtual thread instead of a platform thread.
Spring Boot 3.2+ enables this with one config line; for Spring Boot 2.x it requires a custom `TomcatProtocolHandlerCustomizer`.

### Pros
- Near-zero code change — no async/reactive rewrite needed
- Handles blocking I/O cheaply; each virtual thread suspends on DB/Stripe calls without burning a platform thread
- Massive throughput gain for I/O-bound workloads (which this service is)

### Cons
- Does NOT help if the bottleneck is MySQL connection pool exhaustion — virtual threads still wait on a JDBC connection
- Spring Boot 2.4.5 (current) requires manual wiring; upgrading to Spring Boot 3.2+ is cleaner
- CPU-bound sections (e.g., Stripe signature verification) see no benefit

### Effort
Low — 1–2 days including upgrade validation and load testing.

### How to implement
```yaml
# Spring Boot 3.2+ application.properties
spring.threads.virtual.enabled=true
```
For Spring Boot 2.x, register a `TomcatProtocolHandlerCustomizer` bean that calls
`protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor())`.

---

## Approach 2 — Connection Pool Tuning (HikariCP)

### What it is
HikariCP is already the default pool. Under high concurrency the default pool size (10) becomes
the bottleneck long before MySQL saturates. Tune pool size, timeout, and leak detection.

### Pros
- Immediate gains, zero code change
- Pairs with any other approach

### Cons
- MySQL itself has a `max_connections` ceiling (~151 by default); over-provisioning pools crashes the DB
- Does not solve write contention on hot rows (e.g., product stock)

### Effort
Trivial — 1–2 hours of config + load test iteration.

### How to implement
```properties
spring.datasource.hikari.maximum-pool-size=50
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.connection-timeout=3000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```
Set MySQL `max_connections` to at least `(number of app instances × pool size) + 10`.

---

## Approach 3 — Redis Caching for Read-Heavy Paths

### What it is
Cache product listings, category lists, and product detail reads in Redis with a short TTL.
Auth token validation (currently a DB lookup on every request) is the highest-frequency read
and is a prime candidate.

### Pros
- Reduces MySQL read load by 80–95% for product/category endpoints
- Token cache makes auth O(1) in Redis instead of a DB round-trip per request
- Works well with the existing synchronous Spring MVC model

### Cons
- Cache invalidation complexity: product updates must evict relevant cache keys
- Adds operational dependency on Redis
- Stale reads possible within TTL window (acceptable for product listings, not for stock levels)

### Effort
Medium — 3–5 days. Add `spring-boot-starter-data-redis`, annotate service methods with
`@Cacheable`/`@CacheEvict`, configure TTLs per entity type.

### How to implement
```xml
<!-- pom.xml -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
```java
@Cacheable(value = "products", key = "#categoryId")
public List<Product> listByCategory(int categoryId) { ... }

@CacheEvict(value = "products", allEntries = true)
public void updateProduct(Product p) { ... }
```
Auth token lookup in `AuthenticationService.authenticate()` is the single highest-value target:
cache `token → userId` with a 15-minute TTL.

---

## Approach 4 — Distributed Locking for Inventory / Cart Writes (Redis Redisson)

### What it is
Product stock updates and cart-checkout sequences involve read-modify-write cycles that are
vulnerable to race conditions under concurrency. Use Redisson's `RLock` (a Redis-backed
distributed lock) to serialize writes on the same resource.

### Pros
- Prevents overselling (critical for flash sales / high-demand products)
- Works across multiple app instances (vs. `synchronized` which only works in-process)
- Redisson supports fair locks, try-lock with timeout, and watchdog auto-renewal

### Cons
- Adds latency per write (Redis round-trip + lock acquire time)
- Deadlock risk if lock release is not in a `finally` block
- Redis becomes a write-path dependency; Redis downtime blocks checkouts

### Effort
Medium — 3–4 days. Reference implementation already exists in `bookingSystem/redis/RedisOverbookingGuard.java`.

### How to implement
```java
RLock lock = redissonClient.getLock("product:stock:" + productId);
try {
    if (lock.tryLock(3, 10, TimeUnit.SECONDS)) {
        // decrement stock, save order
    }
} finally {
    if (lock.isHeldByCurrentThread()) lock.unlock();
}
```
Apply at: `OrderService.placeOrder()` and `CartService.addToCart()` for hot products.

---

## Approach 5 — Read Replica for MySQL

### What it is
Spin up one or more MySQL read replicas. Route all SELECT queries to replicas and all writes
to the primary. Spring's `AbstractRoutingDataSource` or a proxy (ProxySQL, AWS RDS read replica)
handles routing transparently.

### Pros
- Scales read throughput linearly with replica count
- No application code change if using a proxy layer
- Reduces lock contention on the primary

### Cons
- Replication lag means replicas can return stale data (seconds behind primary)
- Order/cart reads immediately after a write may miss the write (requires sticky routing or short delay)
- Adds operational complexity (replica lag monitoring, failover)

### Effort
Medium–High — 1 week including infrastructure setup, testing replication lag impact, and
implementing routing logic.

### How to implement
With `AbstractRoutingDataSource`: define two `DataSource` beans (primary, replica), implement
`determineCurrentLookupKey()` to return `replica` for read-only transactions
(`@Transactional(readOnly = true)`).

---

## Approach 6 — Async / Message Queue for Non-Critical Writes

### What it is
Decouple non-latency-sensitive operations (order confirmation email, wishlist sync, analytics
events) from the HTTP request path by publishing to a message queue (Kafka or RabbitMQ).
The HTTP response returns immediately; a consumer processes the work asynchronously.

### Pros
- HTTP response time drops to only the critical write (DB order insert)
- Queue absorbs traffic spikes; consumers process at their own rate
- Natural retry mechanism for flaky downstream services (e.g., email provider)

### Cons
- Eventual consistency: the caller cannot confirm email delivery within the same request
- Adds Kafka/RabbitMQ as an infrastructure dependency
- Consumer failure and dead-letter queue handling must be designed carefully

### Effort
High — 1–2 weeks for Kafka/RabbitMQ setup, producer/consumer code, and dead-letter handling.

### How to implement
Start with Spring's built-in `@Async` + `ThreadPoolTaskExecutor` for in-process async (no
infrastructure cost, but no durability). Graduate to RabbitMQ when durability is required:
```java
// Publisher
rabbitTemplate.convertAndSend("order.exchange", "order.placed", orderEvent);

// Consumer
@RabbitListener(queues = "order.notifications")
public void handleOrderPlaced(OrderEvent event) { emailService.sendConfirmation(event); }
```

---

## Approach 7 — Horizontal Scaling + Stateless Design

### What it is
Run multiple instances of the Spring Boot app behind a load balancer (Nginx, AWS ALB).
Requires the service to be fully stateless — no in-memory session state, no local file uploads,
no in-process caches that can diverge between instances.

### Pros
- Linear throughput scaling by adding instances
- Zero-downtime rolling deploys
- Works with any of the above approaches

### Cons
- Auth `AuthenticationToken` is already DB-backed so it is stateless — no change needed there
- Any in-process cache (Approach 3 variant) must move to Redis to stay consistent across instances
- File upload paths (if added later) must use shared storage (S3/GCS), not local disk

### Effort
Low if the app is already stateless (it is) — 1–3 days for load balancer config, health check
endpoint, and Docker/K8s deployment manifests.

### How to implement
Add a health endpoint:
```java
// Already available via Spring Boot Actuator
management.endpoints.web.exposure.include=health
```
Then deploy behind Nginx upstream or a Kubernetes Deployment with `replicas: N`.

---

## Approach 8 — Database Sharding / Partitioning

### What it is
Partition large tables (Order, Cart, Product) by a shard key (e.g., `userId % N`) across
multiple MySQL instances. Each instance owns a subset of rows.

### Pros
- Scales write throughput horizontally — no single MySQL write bottleneck
- Reduces index size per shard, improving query performance

### Cons
- Cross-shard queries (e.g., admin reports across all orders) become complex or impossible
- Application must implement shard routing logic (or use ShardingSphere/Vitess)
- Schema migrations must be applied to all shards
- Highest operational complexity of all approaches listed here

### Effort
Very High — 3–6 weeks minimum. Recommended only after exhausting Approaches 1–7.

### How to implement
Use Apache ShardingSphere as a JDBC proxy — it intercepts queries and routes to the correct
shard transparently, with minimal application code change.

---

## Summary & Recommended Sequence

| # | Approach | Effort | Impact | Risk |
|---|---|---|---|---|
| 2 | HikariCP pool tuning | Trivial | High (removes immediate bottleneck) | Low |
| 1 | JDK 21 Virtual Threads | Low | High (I/O concurrency) | Low |
| 7 | Horizontal scaling | Low | High (linear scale-out) | Low |
| 3 | Redis caching (reads + token) | Medium | High (DB read relief) | Low–Med |
| 4 | Redis distributed lock (writes) | Medium | High (correctness under load) | Med |
| 5 | MySQL read replica | Med–High | Medium (read scale-out) | Med |
| 6 | Async / message queue | High | Medium (latency reduction) | Med |
| 8 | DB sharding | Very High | Very High (write scale-out) | Very High |

**Recommended order of implementation:**

1. **Tuning first** (Approach 2) — remove the low-hanging bottleneck with no code change.
2. **Virtual Threads** (Approach 1) — upgrade Spring Boot to 3.2+ and enable; huge I/O concurrency gain.
3. **Redis cache** (Approach 3) — cache token validation and product reads; pairs with any scale-out.
4. **Distributed lock** (Approach 4) — protect cart/order writes from race conditions before scaling out.
5. **Horizontal scaling** (Approach 7) — once stateless and cache-consistent, add more instances.
6. **Read replica** (Approach 5) — when single-primary reads become a bottleneck after step 5.
7. **Message queue** (Approach 6) — when non-critical write latency becomes a user-visible problem.
8. **Sharding** (Approach 8) — only if write volume outgrows a single primary after all above steps.
