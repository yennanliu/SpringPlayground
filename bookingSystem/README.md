# Booking System

A simple and elegant booking system built with Spring Boot. Handles high traffic with idempotency and concurrency control.

## Features

- **Resource Management** - CRUD operations for bookable resources (rooms, desks, equipment)
- **Booking Management** - Create, view, update, and cancel bookings
- **Conflict Detection** - Prevents double-booking with database-level locking
- **Idempotency** - Duplicate requests return cached responses (via `Idempotency-Key` header)
- **REST API** - Full CRUD with pagination and filtering
- **Swagger UI** - Interactive API documentation
- **Web UI** - Modern dark-themed frontend

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 17+ |
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
├── config/          # OpenAPI configuration
├── controller/      # REST controllers
├── dto/             # Request/Response objects
├── entity/          # JPA entities
├── exception/       # Error handling
├── repository/      # Data access layer
└── service/         # Business logic
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
