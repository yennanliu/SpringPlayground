# Implementation Plan
> Booking System - Phase 1

## Overview

| Item | Detail |
| ---- | ------ |
| Spec | `doc/spec_claude.md` |
| Stack | Java 21, Spring Boot 3.x, PostgreSQL |
| Goal | Working CRUD API with concurrency control |

## Tasks

### 1. Project Setup
- [ ] Update `pom.xml` with dependencies
  - Spring Web
  - Spring Data JPA
  - PostgreSQL driver
  - SpringDoc OpenAPI
  - Lombok
- [ ] Configure `application.yml`
- [ ] Create package structure

### 2. Domain Layer
- [ ] `Resource` entity
- [ ] `Booking` entity (with `@Version`)
- [ ] `IdempotencyKey` entity
- [ ] `BookingStatus` enum

### 3. Repository Layer
- [ ] `ResourceRepository`
- [ ] `BookingRepository` (with custom query for overlap check)
- [ ] `IdempotencyKeyRepository`

### 4. Service Layer
- [ ] `ResourceService` - basic CRUD
- [ ] `BookingService`
  - Create with idempotency + lock
  - Read / Update / Delete
  - List with filters

### 5. Controller Layer
- [ ] `ResourceController` - REST endpoints
- [ ] `BookingController` - REST endpoints with `Idempotency-Key` header

### 6. Error Handling
- [ ] `GlobalExceptionHandler` (`@ControllerAdvice`)
- [ ] Custom exceptions
  - `ResourceNotFoundException`
  - `BookingConflictException`
  - `IdempotencyKeyExistsException`

### 7. Testing
- [ ] Unit tests for service layer
- [ ] Integration tests for API

## File Structure

```
src/main/java/com/yen/bookingSystem/
├── config/
│   └── OpenApiConfig.java
├── controller/
│   ├── ResourceController.java
│   └── BookingController.java
├── dto/
│   ├── CreateBookingRequest.java
│   ├── UpdateBookingRequest.java
│   ├── BookingResponse.java
│   └── ResourceDto.java
├── entity/
│   ├── Resource.java
│   ├── Booking.java
│   ├── IdempotencyKey.java
│   └── BookingStatus.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── BookingConflictException.java
├── repository/
│   ├── ResourceRepository.java
│   ├── BookingRepository.java
│   └── IdempotencyKeyRepository.java
└── service/
    ├── ResourceService.java
    └── BookingService.java
```

## Execution Order

```
1. Project Setup ──▶ 2. Domain ──▶ 3. Repository ──▶ 4. Service ──▶ 5. Controller ──▶ 6. Error Handling ──▶ 7. Test
```

## Status

| Task | Status |
| ---- | ------ |
| 1. Project Setup | Done |
| 2. Domain Layer | Done |
| 3. Repository Layer | Done |
| 4. Service Layer | Done |
| 5. Controller Layer | Done |
| 6. Error Handling | Done |
| 7. Testing | Pending |

## Notes

- Removed Lombok due to Java 23 Valhalla compatibility issues
- Using plain Java with explicit getters/setters
- H2 in-memory DB for development
- Swagger UI available at `/swagger-ui.html`
