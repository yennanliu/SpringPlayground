## Technical Specification: Elegant Booking System (EBS)

This specification outlines a streamlined, high-performance booking backend built with **Spring Boot 3.x**. The focus is on handling high concurrency with data integrity while keeping the architecture "clean and lean."

---

### 1. System Architecture & Tech Stack
To ensure the system is both simple and robust, we will use a **Stateless REST API** approach.

* **Language:** Java 21 (for Virtual Threads/Project Loom support).
* **Framework:** Spring Boot 3.x.
* **Database:** PostgreSQL (Relational integrity is vital for bookings).
* **Caching/Concurrency:** Redis (for distributed locking and high-traffic throttling).
* **API Documentation:** SpringDoc OpenAPI (Swagger UI).
* **Build Tool:** Maven or Gradle.

---

### 2. High-Traffic & Idempotency Strategy

#### High Traffic Management
* **Virtual Threads:** Enable `spring.threads.virtual.enabled=true` to handle thousands of concurrent HTTP requests without thread exhaustion.
* **Optimistic Locking:** Use the `@Version` annotation on the `Booking` entity to prevent "lost updates" without locking the database table.
* **Rate Limiting:** Implement a simple Bucket4j or Redis-based rate limiter to protect the `POST /bookings` endpoint.

#### Idempotency
To prevent duplicate bookings (e.g., a user clicks "Book" twice), we will implement an **Idempotency Key** pattern:
1.  The client generates a unique UUID (`Idempotency-Key`) for each transaction.
2.  The backend stores this key in Redis for 24 hours.
3.  If a request arrives with an existing key, the backend returns the cached original response instead of creating a new record.

---

### 3. Data Model (Core Entities)

| Entity | Fields | Description |
| :--- | :--- | :--- |
| **Resource** | `id`, `name`, `description`, `capacity` | The item being booked (e.g., Room, Desk, Table). |
| **Booking** | `id`, `resource_id`, `user_id`, `start_time`, `end_time`, `status`, `version` | The transaction record. |

---

### 4. API Endpoints (CRUDL)

All endpoints will be documented via Swagger at `/swagger-ui.html`.

#### Resource Management
* `GET /api/v1/resources`: List all available resources.
* `POST /api/v1/resources`: Create a new resource (Admin).

#### Booking Operations
* **Create:** `POST /api/v1/bookings`
    * *Header:* `Idempotency-Key: <UUID>`
    * *Logic:* Check availability -> Distributed Lock -> Save -> Release Lock.
* **Read:** `GET /api/v1/bookings/{id}`
* **Update:** `PUT /api/v1/bookings/{id}` (e.g., change time or cancel).
* **Delete:** `DELETE /api/v1/bookings/{id}` (Hard or Soft delete).
* **List:** `GET /api/v1/bookings?userId={id}`

---

### 5. Simple & Elegant Code Structure

```text
com.example.booking
├── config             # Swagger & Redis Configurations
├── controller         # REST Entry points
├── dto                # Request/Response payloads
├── exception          # Global Error Handling (@ControllerAdvice)
├── model              # JPA Entities
├── repository         # Spring Data JPA Repositories
└── service            # Business Logic & Idempotency checks
```

---

### 6. Swagger Integration
Include the following dependency in `pom.xml` for automatic documentation:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

> **Note on UI:** Since this is a pure backend app, the UI can be a simple `index.html` using `fetch()` to call these endpoints. The CORS policy will be configured in Spring Security to allow your specific frontend domain.
