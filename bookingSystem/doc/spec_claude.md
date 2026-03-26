# Booking System Spec
> Phase 1 - Simple & Solid

## Steps

1. Define domain entities (Booking, Resource)
2. Implement repository layer with Spring Data JPA
3. Build service layer with concurrency control
4. Create REST controllers with idempotency

## Run

<details>
<summary>App</summary>

```bash
#---------------------------
# Run app
#---------------------------

# build
mvn clean package -DskipTests

# run
java -jar target/bookingSystem-0.0.1-SNAPSHOT.jar
```

</details>

## Architecture

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Client    │────▶│  Spring Boot│────▶│ PostgreSQL  │
└─────────────┘     │   (App)     │     │   (DB)      │
                    └─────────────┘     └─────────────┘
```

**Key Principles:**
- Single server deployment
- Database as source of truth
- DB-level locking for concurrency
- Idempotency via DB table

## API

| API | Type | Purpose | Example | Comment |
| --- | ---- | ------- | ------- | ------- |
| `POST /api/bookings` | Create | Create booking | `curl -X POST -H "Idempotency-Key: abc" -d '{"resourceId":"r1","userId":"u1","startTime":"...","endTime":"..."}'` | Requires idempotency key |
| `GET /api/bookings/{id}` | Read | Get booking | `curl /api/bookings/123` | |
| `PUT /api/bookings/{id}` | Update | Modify booking | `curl -X PUT -d '{"status":"CANCELLED"}'` | Optimistic lock via version |
| `DELETE /api/bookings/{id}` | Delete | Cancel booking | `curl -X DELETE` | Soft delete |
| `GET /api/bookings` | List | Query bookings | `curl /api/bookings?userId=u1&page=0&size=20` | Pagination required |
| `GET /api/resources` | List | Available resources | `curl /api/resources?type=room` | |
| `POST /api/resources` | Create | Add resource | Admin only | |

## Data Model

```sql
-- Resource (what can be booked)
CREATE TABLE resource (
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    type        VARCHAR(50),
    capacity    INT DEFAULT 1,
    active      BOOLEAN DEFAULT TRUE
);

-- Booking
CREATE TABLE booking (
    id              VARCHAR(36) PRIMARY KEY,
    resource_id     VARCHAR(36) NOT NULL REFERENCES resource(id),
    user_id         VARCHAR(100) NOT NULL,
    start_time      TIMESTAMP NOT NULL,
    end_time        TIMESTAMP NOT NULL,
    status          VARCHAR(20) DEFAULT 'CONFIRMED',
    version         INT DEFAULT 0,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Idempotency (prevents duplicate requests)
CREATE TABLE idempotency_key (
    key             VARCHAR(100) PRIMARY KEY,
    response        TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_booking_resource_time ON booking(resource_id, start_time, end_time);
CREATE INDEX idx_booking_user ON booking(user_id);
```

## Important Concepts

### 1. Idempotency
Prevent duplicate bookings from retries/double-clicks.

```
Client ──▶ [Idempotency-Key: uuid] ──▶ Server
                                         │
                                   Check DB table
                                         │
                          ┌──────────────┴──────────────┐
                          ▼                             ▼
                    Key exists?                    Key not found
                          │                             │
                    Return cached                 Process request
                    response                      Store key + response
```

### 2. Concurrency Control (Preventing Double Booking)

**Pessimistic Lock (DB-level)**
```sql
SELECT * FROM booking
WHERE resource_id = ? AND status = 'CONFIRMED'
  AND start_time < ? AND end_time > ?
FOR UPDATE;
```

**Optimistic Lock (App-level)**
```java
@Version
private Integer version;
```

### 3. Booking Flow

```
1. Receive request with Idempotency-Key
2. Check idempotency_key table
   - If exists → return stored response
3. Begin transaction
4. Lock check: SELECT ... FOR UPDATE (check time overlap)
5. If conflict → rollback, return 409
6. Insert booking
7. Store idempotency key + response
8. Commit transaction
9. Return success
```

## Tech Stack

| Component | Choice | Reason |
| --------- | ------ | ------ |
| Language | Java 21 | Virtual threads, modern features |
| Framework | Spring Boot 3.x | Production-ready |
| Database | PostgreSQL | ACID, reliable |
| API Docs | SpringDoc OpenAPI | Auto Swagger |

## Config

```yaml
# application.yml
spring:
  threads:
    virtual:
      enabled: true
  datasource:
    url: jdbc:postgresql://localhost:5432/booking
    hikari:
      maximum-pool-size: 20
  jpa:
    hibernate:
      ddl-auto: validate
```

## Concurrency Patterns

### 1. ConcurrentHashMap (ResourceCache)
Thread-safe in-memory cache for resources.

```java
// computeIfAbsent - load only if not cached
Resource r = cache.getOrLoad(id, key -> repository.findById(key));

// compute - atomic update
cache.update(id, r -> { r.setName("new"); return r; });
```

### 2. Atomic Operations (BookingStats)
Lock-free counters for high-throughput statistics.

```java
// AtomicLong - precise count with get/set
private final AtomicLong totalBookings = new AtomicLong(0);

// LongAdder - high-contention increment-only
private final LongAdder conflictCount = new LongAdder();

// Per-resource counters
private final ConcurrentHashMap<String, LongAdder> resourceBookings;
```

### 3. Lock Strategies (ResourceLockManager)
Fine-grained locking for critical sections.

```java
// Per-resource lock (prevents double booking)
lockManager.withLock(resourceId, () -> {
    // Only one thread can book this resource at a time
    return createBooking(request);
});

// Try with timeout (non-blocking)
Result r = lockManager.tryWithLock(resourceId, 5000, () -> action());

// Read-write lock (multiple readers, single writer)
lockManager.withReadLock(() -> listResources());
lockManager.withWriteLock(() -> updateResource());
```

## Ref

- https://roadmap.sh/backend
- https://spring.io/projects/spring-boot
