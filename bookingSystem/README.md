# Booking System

A simple and elegant booking system built with Spring Boot. Handles high traffic with idempotency and concurrency control.

## Features

- **Resource Management** - CRUD operations for bookable resources (rooms, desks, equipment)
- **Booking Management** - Create, view, update, and cancel bookings
- **Conflict Detection** - Prevents double-booking with database-level locking
- **Idempotency** - Duplicate requests return cached responses (via `Idempotency-Key` header)
- **C10K Scalability** - Virtual Threads (JDK 21 Project Loom) for 10,000+ concurrent clients
- **REST API** - Full CRUD with pagination and filtering
- **Swagger UI** - Interactive API documentation
- **Web UI** - Modern Airbnb-inspired frontend

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 21 (Virtual Threads / Project Loom) |
| Framework | Spring Boot 3.x |
| Database | PostgreSQL (prod) / H2 (dev) |
| API Docs | SpringDoc OpenAPI |

## Quick Start

```bash
# Clone and run
mvn spring-boot:run
```

## Endpoints

| URL | Description |
|-----|-------------|
| http://localhost:8080 | Web UI |
| http://localhost:8080/swagger-ui.html | API Documentation |
| http://localhost:8080/h2-console | Database Console (dev) |

> **H2 Console**: Use JDBC URL `jdbc:h2:mem:booking`, username `sa`, no password

## API Overview

### Resources

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/resources` | Create resource |
| `GET` | `/api/resources` | List resources |
| `GET` | `/api/resources/{id}` | Get resource |
| `PUT` | `/api/resources/{id}` | Update resource |
| `DELETE` | `/api/resources/{id}` | Delete resource |

### Bookings

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/bookings` | Create booking |
| `GET` | `/api/bookings` | List bookings |
| `GET` | `/api/bookings/{id}` | Get booking |
| `PUT` | `/api/bookings/{id}` | Update booking |
| `DELETE` | `/api/bookings/{id}` | Cancel booking |

## Usage Examples

### Create a Resource

```bash
curl -X POST http://localhost:8080/api/resources \
  -H "Content-Type: application/json" \
  -d '{"name": "Conference Room A", "type": "room", "capacity": 10}'
```

### Create a Booking

```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -H "Idempotency-Key: unique-key-123" \
  -d '{
    "resourceId": "<resource-id>",
    "userId": "user1",
    "startTime": "2026-04-01T10:00:00",
    "endTime": "2026-04-01T11:00:00"
  }'
```

### List Bookings with Filters

```bash
curl "http://localhost:8080/api/bookings?userId=user1&page=0&size=10"
```

## Project Structure

```
src/main/java/com/yen/bookingSystem/
├── concurrency/     # Thread-safe components
│   ├── ResourceCache.java      # ConcurrentHashMap cache
│   ├── BookingStats.java       # Atomic counters
│   └── ResourceLockManager.java # Lock strategies
├── config/          # OpenAPI configuration
├── controller/      # REST controllers
├── dto/             # Request/Response objects
├── entity/          # JPA entities
├── exception/       # Error handling
├── repository/      # Data access layer
└── service/         # Business logic
```

## C10K Scalability — Virtual Threads (JDK 21 Project Loom)

The system is configured to handle **10,000+ concurrent clients** using JDK 21 Virtual Threads,
enabled by a single property in `application.yml`:

```yaml
spring:
  threads:
    virtual:
      enabled: true
```

### Why it works

Traditional Tomcat assigns one **OS thread** per HTTP request. OS threads are expensive — each
consumes ~1 MB of stack memory and requires a kernel context switch when blocked. With a pool
capped at ~200 threads, the 201st concurrent request must wait. Under C10K load this queue
explodes and the server crashes.

**Virtual threads** (Project Loom) are JVM-managed, not OS-managed. They are:

- **Cheap** — a few hundred bytes each; you can have millions active simultaneously
- **Suspendable** — when a virtual thread blocks on I/O (DB query, network call), the JVM
  *unmounts* it from its carrier OS thread; that carrier is immediately free to run another virtual
  thread
- **Transparent** — blocking code (`JDBC`, `ReentrantLock`, `HttpClient`) works unchanged; no
  `async`/`Mono`/`CompletableFuture` rewrites needed

```
Before (OS threads)                After (Virtual Threads)
─────────────────────              ───────────────────────
Request → OS Thread (1 MB)         Request → Virtual Thread (~1 KB)
Thread blocks on DB query    →     VT suspends, carrier freed
Carrier thread sits idle           Carrier picks up next VT
Pool exhausted at ~200 req         Millions of VTs, ~10 carrier threads
C10K → queue explosion             C10K → handled
```

### What is NOT affected

- All existing code is unchanged — `ReentrantLock`, JDBC, `@Transactional` all work as before
- `ReentrantLock` does **not** pin carrier threads (only legacy `synchronized` blocks do)
- The concurrency logic (`ResourceLockManager`, `BookingStats`, `ResourceCache`) remains correct

### Load Testing

A self-contained load test script verifies C10K behavior with no external tools required:

```bash
# Start the app
mvn spring-boot:run

# In a second terminal — fires 10,000 concurrent requests
java scripts/C10kLoadTest.java

# Custom count
java scripts/C10kLoadTest.java 5000
```

The script uses virtual threads itself, fires a 60/40 mix of booking creates and reads,
and prints throughput, latency percentiles (p50/p95/p99), and a pass/fail verdict.

## Concurrency Patterns

### ConcurrentHashMap (ResourceCache)
```java
// Thread-safe cache with computeIfAbsent
cache.getOrLoad(id, key -> loadFromDb(key));
```

### Atomic Operations (BookingStats)
```java
// Lock-free counters
AtomicLong total = new AtomicLong();    // precise count
LongAdder conflicts = new LongAdder();   // high-throughput
```

### Lock Strategies (ResourceLockManager)
```java
// Per-resource lock
lockManager.withLock(resourceId, () -> createBooking());

// Read-write lock
lockManager.withReadLock(() -> listResources());
lockManager.withWriteLock(() -> updateResource());
```

### Stats Endpoint
```bash
curl http://localhost:8080/api/stats
```
```json
{
  "bookings": { "total": 10, "active": 8, "cancelled": 2, "conflicts": 3 },
  "cache": { "resourcesCached": 5 },
  "locks": { "activeLocks": 2 }
}
```

## Configuration

### Development (default)

Uses H2 in-memory database. No setup required.

### Production

Set environment variables and activate prod profile:

```bash
export DB_USER=postgres
export DB_PASS=yourpassword
java -jar target/bookingSystem-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## Documentation

- [Spec](doc/spec_claude.md) - System specification
- [Plan](doc/plan.md) - Implementation plan
- [C10K Scaling Ideas](doc/c10k-scaling-ideas.md) - All approaches considered for 10K concurrency
