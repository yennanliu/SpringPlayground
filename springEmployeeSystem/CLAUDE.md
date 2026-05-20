# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Full-stack employee management system: Spring Boot 2.4.5 backend (Java 8) + Vue 2 frontend, backed by MySQL.

- **Backend** runs on port `9998`
- **Frontend** runs on port `8080`
- **Swagger UI**: `http://localhost:9998/swagger-ui.html`
- **Actuator health**: `http://localhost:9998/actuator/health`

## Commands

### Backend (`backend/EmployeeSystem`)

```bash
# Build
mvn package

# Run built jar
java -jar target/EmployeeSystem-0.0.1-SNAPSHOT.jar

# Run tests
mvn test

# Run a single test class
mvn test -Dtest=UserServiceTest

# Run a single test method
mvn test -Dtest=UserServiceTest#testUpdateUserWithPhoto_Success

# Generate coverage report (output: target/site/jacoco/index.html)
mvn jacoco:report
```

### Frontend (`frontend/employee-system-ui`)

```bash
npm run serve   # dev server
npm run build   # production build
npm run lint    # lint
```

### Docker (from repo root)

```bash
docker-compose up
```

## Architecture

### Backend

Standard Spring layered architecture: **Controller → Service → Repository → JPA entity**.

```
EmployeeSystem/
  controller/   # REST endpoints
  service/      # business logic
  repository/   # Spring Data JPA interfaces
  model/        # JPA entities + dto/ subdirectory
  config/       # WebConfig (CORS), AsyncConfig, MessageStrings
  common/       # ApiResponse wrapper
  enums/        # Role, TicketStatus, VacationStatus, ResponseStatus
  exception/    # AuthenticationFailException, CustomException
  util/         # Helper (null checks)
```

**Domain entities**: `User`, `Department`, `Ticket`, `Vacation`, `Checkin`, `AuthenticationToken`, `OptionSchema`.

**Auth flow**: Token-based, not JWT. On signup/signin, a UUID `AuthenticationToken` is stored in the `tokens` table and linked to the `User`. Protected endpoints receive the token as a `?token=` query param. `AuthenticationService.authenticate(token)` validates it.

**User roles**: `USER`, `MANAGER`, `ADMIN` (enum `Role`). Role is stored as a string column.

**Password hashing**: MD5 via `UserService.hashPassword()`.

**`ApiResponse`**: Standard success/message wrapper for mutating endpoints. Use `isSuccess()` (not `getSuccess()`) when checking in tests.

**Partial updates**: `UserService.updateUserFields()` only updates non-null DTO fields, preserving existing values. Follow this pattern for other entity updates.

**User photos**: Stored as `@Lob byte[]` in the `users` table. Retrieved via `GET /users/photo/{userId}`.

**Known DB issue**: `Ticket.description` is `columnDefinition = "TEXT"`. If the app fails to start against an existing schema, run:
```sql
ALTER TABLE tickets MODIFY COLUMN description TEXT;
```

### Frontend

Vue 2 SPA using Vue Router (history mode). All API calls go to the backend at `localhost:9998` via Axios.

Views are organized by domain under `src/views/`: `User/`, `Department/`, `Ticket/`, `Vacation/`, `Checkin/`, `Admin/`. Reusable pieces live in `src/components/`.

Route pattern: public list/detail views at `/users/`, `/departments/`, `/tickets/`; admin edit routes under `/admin/`.

### Infrastructure

`docker-compose.yml` at root starts three services: `app` (Spring Boot), `vue` (Vue dev server), `mysql` (MySQL 5.7). The app container uses `SPRING_JPA_HIBERNATE_DDL_AUTO=create` — schema is recreated on each Docker startup. Local dev uses `ddl-auto=update`.

## Key Configuration

`application.properties` connects to `mysql://localhost:3306/employee_system` with no password (matches Docker). Mail uses Mailtrap SMTP (test credentials in the file — safe for dev, not prod).
