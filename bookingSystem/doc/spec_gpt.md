# 🧾 Booking System Backend Spec (Spring Boot)

## 1. 🎯 Goals

* Provide a **pure backend REST API**
* Handle **high traffic safely**
* Ensure **idempotent booking operations**
* Keep design **simple, maintainable, and elegant**
* Support **CRUDL (Create, Read, Update, Delete, List)**
* Provide **Swagger/OpenAPI documentation**
* UI can be built separately using HTML/JS

---

## 2. 🏗️ High-Level Architecture

```
[ Client (HTML/JS) ]
          |
          v
[ Spring Boot REST API ]
          |
          v
[ Service Layer ]
          |
          v
[ Repository Layer ]
          |
          v
[ Database (PostgreSQL / MySQL) ]
```

### Key Principles

* Stateless APIs
* Layered architecture
* Minimal dependencies
* Clear separation of concerns

---

## 3. 🧩 Core Domain

### 3.1 Entities

#### Booking

```java
Booking {
  UUID id
  String userId
  String resourceId   // e.g. room, seat, service
  LocalDateTime startTime
  LocalDateTime endTime
  BookingStatus status
  String idempotencyKey
  LocalDateTime createdAt
  LocalDateTime updatedAt
}
```

#### Resource

```java
Resource {
  String id
  String name
  String type
  Boolean active
}
```

#### IdempotencyRecord

```java
IdempotencyRecord {
  String key
  String requestHash
  String responseBody
  HttpStatus statusCode
  LocalDateTime createdAt
}
```

---

## 4. ⚙️ Functional Requirements

### 4.1 CRUDL APIs

#### Booking APIs

| Method | Endpoint             | Description                |
| ------ | -------------------- | -------------------------- |
| POST   | `/api/bookings`      | Create booking             |
| GET    | `/api/bookings/{id}` | Get booking                |
| PUT    | `/api/bookings/{id}` | Update booking             |
| DELETE | `/api/bookings/{id}` | Cancel/Delete booking      |
| GET    | `/api/bookings`      | List bookings (filterable) |

#### Resource APIs

| Method | Endpoint              | Description     |
| ------ | --------------------- | --------------- |
| POST   | `/api/resources`      | Create resource |
| GET    | `/api/resources/{id}` | Get resource    |
| PUT    | `/api/resources/{id}` | Update resource |
| DELETE | `/api/resources/{id}` | Delete resource |
| GET    | `/api/resources`      | List resources  |

---

### 4.2 Filtering (List API)

Support query params:

```
GET /api/bookings?userId=123&resourceId=room1&status=CONFIRMED&page=0&size=20
```

---

## 5. 🔁 Idempotent Request Processing

### Approach

* Client sends header:

```
Idempotency-Key: <unique-key>
```

### Flow

1. Check if key exists in `IdempotencyRecord`
2. If exists → return stored response
3. If not:

   * Process request
   * Store response + request hash
   * Return response

### Notes

* Use **request body hash** to detect misuse
* TTL cleanup job for old keys
* Apply only to **POST (Create Booking)**

---

## 6. 🚀 High Traffic Design

### 6.1 Database

* Use indexes:

  * `(resourceId, startTime, endTime)`
  * `(userId)`
* Use connection pooling (HikariCP)

### 6.2 Concurrency Control

#### Option A (Simple & Effective)

* DB constraint + transactional check

```sql
SELECT * FROM bookings
WHERE resource_id = ?
AND status = 'CONFIRMED'
AND (start_time < ? AND end_time > ?)
FOR UPDATE;
```

#### Option B (Optimistic Lock)

* Version field on Booking

---

### 6.3 Horizontal Scalability

* Stateless services → scale with load balancer
* Use Redis (optional) for:

  * Idempotency cache
  * Rate limiting

---

### 6.4 Rate Limiting (Optional)

* Per user or IP
* Use token bucket algorithm

---

## 7. 📦 Service Layer Design

### BookingService

```java
interface BookingService {
  Booking createBooking(CreateBookingRequest request);
  Booking getBooking(UUID id);
  Booking updateBooking(UUID id, UpdateBookingRequest request);
  void deleteBooking(UUID id);
  Page<Booking> listBookings(Filter filter);
}
```

---

## 8. 🗄️ Repository Layer

Use Spring Data JPA:

```java
interface BookingRepository extends JpaRepository<Booking, UUID> {

  @Query("SELECT b FROM Booking b WHERE ...")
  Page<Booking> findWithFilters(...);

}
```

---

## 9. 🌐 REST API Design

### Example: Create Booking

**Request**

```json
POST /api/bookings
Headers:
  Idempotency-Key: abc-123

{
  "userId": "u1",
  "resourceId": "room1",
  "startTime": "2026-03-26T10:00:00",
  "endTime": "2026-03-26T11:00:00"
}
```

**Response**

```json
{
  "id": "uuid",
  "status": "CONFIRMED"
}
```

---

## 10. 📚 Swagger / OpenAPI

Use:

```xml
springdoc-openapi-starter-webmvc-ui
```

### Config

```java
@Configuration
public class OpenApiConfig {
}
```

### Access

```
/swagger-ui.html
```

---

## 11. ⚠️ Error Handling

### Standard Error Response

```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "message": "Resource already booked",
  "path": "/api/bookings"
}
```

### Use:

* `@ControllerAdvice`
* Custom exceptions:

  * `BookingConflictException`
  * `ResourceNotFoundException`

---

## 12. 🔐 Validation

Use:

* `@Valid`
* `@NotNull`, `@Future`, etc.

Example:

```java
@NotNull
@Future
LocalDateTime startTime;
```

---

## 13. 🧪 Testing

* Unit tests (Service layer)
* Integration tests (API + DB)
* Test idempotency behavior
* Load testing (e.g. 1000 req/sec scenarios)

---

## 14. 🧹 Simplicity Guidelines

* Avoid microservices initially
* Keep single DB
* Avoid over-engineering (no CQRS unless needed)
* Prefer readability over abstraction

---

## 15. 📈 Future Enhancements

* Authentication (JWT)
* Distributed locks (Redis)
* Event-driven booking (Kafka)
* Caching layer
* Audit logs

---

## ✅ Summary

This design gives you:

* Clean RESTful backend
* Strong **idempotency guarantees**
* Safe **concurrent booking handling**
* Scalable under **high traffic**
* Simple and maintainable structure
* Fully documented via Swagger

---
