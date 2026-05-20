# High Concurrency Design — Employee System Backend

## Current State

### What's already in place
- Two dedicated `ThreadPoolTaskExecutor` beans (`emailTaskExecutor`, `notificationTaskExecutor`) in `AsyncConfig`
- `@Async("emailTaskExecutor")` on all `EmailService` methods, returning `CompletableFuture<Void>`
- `VacationService.addVacation()` is marked `@Async` (though this is a bug — see §1 below)

### What's missing

The system has no caching, no DB-side filtering for list queries, no connection pool tuning, and no `@Transactional` guards on write paths. These are the dominant bottlenecks under load.

---

## Problems & Fixes (by priority)

---

### 1. In-Memory Full-Table Scans on Every Request (Critical)

Three services do `findAll()` then filter in Java. Every concurrent request loads the **entire table** into memory.

| Service | Method | Table scanned |
|---|---|---|
| `CheckinService` | `getCheckinByUserId(userId)` | `check_in` |
| `VacationService` | `getVacationByUserId(userId)` | `vacation` |
| `UserService` | `getSubordinatesById(managerId)` | `users` |
| `OptionSchemaService` | `getAllActiveOptions()` | `option_schema` |

**Fix — add derived query methods to repositories:**

```java
// CheckinRepository
List<Checkin> findByUserId(Integer userId);

// VacationRepository
List<Vacation> findByUserId(Integer userId);

// UserRepository
List<User> findByManagerId(Integer managerId);

// OptionSchemaRepository
List<OptionSchema> findByActiveTrue();
```

Replace the `findAll()` + stream calls in the service layer with these. MySQL handles the `WHERE` clause; no rows are loaded that won't be returned.

**Also add DB indexes** to these FK columns (none currently exist):

```java
// Checkin.java
@Table(name = "check_in", indexes = @Index(columnList = "user_id"))

// Vacation.java
@Table(name = "vacation", indexes = {
    @Index(columnList = "user_id"),
    @Index(columnList = "status")
})

// User.java
@Table(name = "users", indexes = {
    @Index(columnList = "manager_id"),
    @Index(columnList = "departement_id"),
    @Index(name = "idx_email", columnList = "email", unique = true)
})

// AuthenticationToken — token lookup happens on every authenticated request
@Table(name = "tokens", indexes = @Index(columnList = "token", unique = true))
```

---

### 2. `@Async` on `VacationService.addVacation()` Is a Bug (Critical)

The entire `addVacation()` method (including `vacationRepository.save(vacation)`) runs on an async thread. The controller returns `201 Created` before the row is written. If the executor queue is full, the save is silently dropped.

**Fix — separate the write from the notification:**

```java
// VacationService.java — remove @Async from the method itself
@Transactional
public void addVacation(VacationDto vacationDto) {
    Vacation vacation = new Vacation();
    BeanUtils.copyProperties(vacationDto, vacation);
    vacation.setStatus(VacationStatus.PENDING.getName());
    vacationRepository.save(vacation);  // synchronous, guaranteed

    // fire-and-forget email is fine here — runs on emailTaskExecutor
    emailService.sendAdminNotificationAsync(
        adminEmail,
        vacation.getUserId(),
        vacation.getType(),
        vacation.getStartDate().toString(),
        vacation.getEndDate().toString()
    );
}
```

---

### 3. Missing `@Transactional` on Write Paths (High)

`TicketService.updateTicket()` does a `deleteById()` followed by `save()` as two separate DB operations. A concurrent reader between those two calls sees a missing ticket.

```java
// TicketService.java — current (broken)
public void updateTicket(TicketDto ticketDto) {
    Ticket ticket = new Ticket();
    ticketRepository.deleteById(ticketDto.getId());   // gap here
    BeanUtils.copyProperties(ticketDto, ticket);
    ticketRepository.save(ticket);
}
```

**Fix — replace with a read-modify-write pattern inside a transaction:**

```java
@Transactional
public void updateTicket(TicketDto ticketDto) {
    Ticket ticket = ticketRepository.findById(ticketDto.getId())
        .orElseThrow(() -> new RuntimeException("Ticket not found: " + ticketDto.getId()));
    BeanUtils.copyProperties(ticketDto, ticket, "id");
    ticketRepository.save(ticket);
}
```

Add `@Transactional` to all other write methods in `UserService`, `DepartmentService`, `CheckinService`, and `VacationService`.

---

### 4. Token Validation Hits DB on Every Authenticated Request (High)

`AuthenticationService.authenticate(token)` → `TokenRepository.findTokenByToken(token)` is called on every endpoint that checks auth. Under 1000 concurrent requests, this is 1000 extra DB queries.

**Option A — in-process cache (simplest, single node)**

Add Spring Cache + Caffeine:

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>
```

```java
// AuthenticationService.java
@Cacheable(value = "tokens", key = "#token", unless = "#result == null")
public User getUser(String token) { ... }

@CacheEvict(value = "tokens", key = "#token")
public void invalidateToken(String token) { ... }
```

```yaml
# application.properties
spring.cache.caffeine.spec=maximumSize=10000,expireAfterWrite=30m
```

**Option B — Redis (multi-node, distributed)**

Store `token → userId` in Redis with a TTL. Eliminates DB token lookups entirely and survives horizontal scaling.

---

### 5. `GET /users/` Returns Photo BLOBs for All Users (High)

`UserController.getUsers()` returns the full `User` entity including the `photo` byte array. Under concurrent load, this floods the JVM heap and serializes megabytes per request.

**Fix — use a projection that excludes the photo field:**

```java
// UserSummary.java (new interface projection)
public interface UserSummary {
    Integer getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    Role getRole();
    Integer getDepartementId();
    Integer getManagerId();
}

// UserRepository.java
List<UserSummary> findAllProjectedBy();

// UserController.java
@GetMapping("/")
public ResponseEntity<List<UserSummary>> getUsers() {
    return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
}
```

Photos are only fetched when explicitly requested via `GET /users/photo/{userId}`.

---

### 6. No HikariCP Connection Pool Tuning (Medium)

Spring Boot 2.x defaults to a pool of 10 connections. Under concurrent load this becomes the bottleneck before the CPU is even touched.

**Add to `application.properties`:**

```properties
# Start with these; tune based on observed pool wait times (HikariCP metrics via /actuator)
spring.datasource.hikari.maximum-pool-size=30
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.connection-timeout=3000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.leak-detection-threshold=5000
```

Expose HikariCP metrics via actuator to observe pool saturation:

```properties
management.metrics.enable.hikaricp=true
```

---

### 7. No Caching for Static/Slow-Changing Data (Medium)

`DepartmentService.getDepartments()`, `OptionSchemaService.getAllOptions()`, and `getAllActiveOptions()` hit the DB on every call. These tables rarely change.

```java
@Service
public class DepartmentService {

    @Cacheable("departments")
    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    @CacheEvict(value = "departments", allEntries = true)
    public void addDepartment(DepartmentDto departmentDto) { ... }

    @CacheEvict(value = "departments", allEntries = true)
    public void updateDepartment(DepartmentDto departmentDto) { ... }
}
```

Same pattern for `OptionSchemaService`. Use short TTL (e.g., 5 minutes) or evict on write.

---

### 8. No Pagination on List Endpoints (Medium)

All `getAll*()` calls return unbounded lists. As the DB grows, `GET /checkin/`, `GET /vacation/`, `GET /tickets/` will OOM the JVM or time out.

**Fix — add `Pageable` to repository + controller:**

```java
// TicketRepository
Page<Ticket> findAll(Pageable pageable);

// TicketController
@GetMapping("/")
public ResponseEntity<Page<Ticket>> getTickets(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
    return new ResponseEntity<>(ticketService.getTickets(pageable), HttpStatus.OK);
}
```

---

### 9. Migrate Auth to JWT (Lower Priority, Bigger Change)

The current token model requires a DB lookup on every authenticated call (see §4). JWT is stateless — the token is validated by signature verification in memory, with no DB round-trip.

**Trade-offs:**

| | Current (DB token) | JWT |
|---|---|---|
| Auth cost per request | 1 DB query | CPU only (signature verify) |
| Token revocation | Instant (delete row) | Hard (need blocklist or short TTL) |
| Horizontal scaling | Needs shared DB or Redis | Stateless, trivially scalable |
| Implementation effort | Low | Medium |

For an HR system where sessions should be revocable, a short-lived JWT (15–30 min) + refresh token stored in Redis is the standard approach.

---

### 10. Rate Limiting (Lower Priority)

No rate limiting exists. A single client can flood the check-in endpoint or trigger mass email sends.

**Option A — Spring's `HandlerInterceptor` + Bucket4j (in-process):**

```xml
<dependency>
    <groupId>com.github.vladimir-bukhtoyarov</groupId>
    <artifactId>bucket4j-core</artifactId>
</dependency>
```

**Option B — API Gateway layer (Nginx, Kong, AWS API GW)** — preferred if there are multiple services.

---

## Summary — Recommended Order of Attack

| Priority | Change | Effort | Impact |
|---|---|---|---|
| 1 | Fix in-memory `findAll()` scans → derived queries + DB indexes | Low | Very High |
| 2 | Fix `@Async` bug in `VacationService.addVacation()` | Very Low | High (correctness) |
| 3 | Add `@Transactional` to all write methods; fix `TicketService.updateTicket()` | Low | High |
| 4 | Exclude photo BLOB from `GET /users/` | Low | High |
| 5 | HikariCP pool tuning | Very Low | Medium–High |
| 6 | Token cache (Caffeine or Redis) | Low–Medium | High |
| 7 | Cache departments + option schema | Low | Medium |
| 8 | Paginate list endpoints | Medium | Medium |
| 9 | Migrate to JWT | High | High (at scale) |
| 10 | Rate limiting | Medium | Medium |

Items 1–5 are purely code/config changes with no new infrastructure and can be done in a single sprint.
Items 6–8 add Caffeine (in-process) or Redis (distributed) caching.
Items 9–10 are architectural changes warranting their own feature branches.
