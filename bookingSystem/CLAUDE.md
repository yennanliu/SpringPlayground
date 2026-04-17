# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run locally (H2 in-memory, dev profile)
mvn spring-boot:run

# Run with production profile (PostgreSQL)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"

# Build
mvn clean package

# Run all tests
mvn test

# Run tests with coverage report
mvn test jacoco:report
```

## Architecture

Spring Boot 3 REST API for resource booking/reservation. Java 17, no Lombok (removed for Java 23 Valhalla compatibility — use explicit getters/setters).

**Layers:** `Controller → Service → Repository → JPA Entity`

**Controllers** (`controller/`): `BookingController`, `ResourceController`, `StatsController`

**Services** (`service/`):
- `BookingService` — conflict detection with pessimistic DB locks, idempotency via `Idempotency-Key` header (cached in DB)
- `ResourceService` — resource CRUD with thread-safe cache

**Concurrency** (`concurrency/`): Specialized components separate from service layer:
- `ResourceCache` — `ConcurrentHashMap` with `computeIfAbsent` pattern
- `BookingStats` — `AtomicLong`/`LongAdder` for high-throughput counters
- `ResourceLockManager` — per-resource `ReentrantLock` + `ReentrantReadWriteLock` for fine-grained locking

**Profiles:**
- `dev` (default): H2 in-memory, DDL `create-drop`, SQL logging on, H2 console at `http://localhost:8080/h2-console`
- `prod`: PostgreSQL via env vars `DB_USER`/`DB_PASS`, DDL `validate`, Hikari pool max 20

**Key behaviors:**
- Bookings use soft deletes (status → `CANCELLED`, never hard-deleted)
- `Booking` entity has a `version` field for optimistic locking readiness
- Conflict detection uses `PESSIMISTIC_WRITE` lock mode at the DB level
- Swagger UI: `http://localhost:8080/swagger-ui.html`
