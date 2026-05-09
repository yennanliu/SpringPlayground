# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Full-stack e-commerce shopping cart application with Stripe payment integration.

- **Backend**: Spring Boot 2.4.5 (Java 11+), MySQL, Swagger 2.x, Stripe API
- **Frontend**: Vue.js (`Frondend/ecommerce-ui/`)
- **Endpoints**: BE on `localhost:9999`, FE on `localhost:8080`

## Commands

### Backend (Spring Boot)
```bash
cd Backend
mvn package                          # build JAR
mvn spring-boot:run                  # run dev server
mvn test                             # run all tests
mvn test -Dtest=CartControllerTest   # run a single test class
java -jar target/ShoppingCart-0.0.1-SNAPSHOT.jar  # run built JAR
```

### Frontend (Vue.js)
```bash
cd Frondend/ecommerce-ui
npm install
npm run serve    # dev server at localhost:8080
npm run build    # production build
```

### Docker (full stack)
```bash
docker-compose up   # starts app (9999), vue (8080), mysql (3306)
```

### Database
- MySQL 5.7, database: `shopping_cart`
- JPA `ddl-auto=update` (local) / `create` (docker)
- SQL seed scripts in `Backend/sql/`

## Architecture

### Backend Package Structure (`Backend/src/main/java/com/yen/ShoppingCart/`)

```
controller/   → REST endpoints (CartController, OrderController, ProductController, etc.)
service/      → Business logic (one service per domain)
repository/   → Spring Data JPA interfaces
model/        → JPA entities (User, Product, Cart, Order, OrderItem, Category, WishList, AuthenticationToken)
config/       → WebConfig (CORS), MessageStrings
common/       → ApiResponse (standard response wrapper)
enums/        → Role, ResponseStatus
exception/    → Custom exceptions
util/         → Helper utilities
```

### Authentication Flow
Token-based auth using `AuthenticationToken` entity. Tokens stored in DB via `TokenRepository`. Controllers validate tokens by calling `AuthenticationService` before processing protected requests — there is no Spring Security; auth is manual per-endpoint.

### API Pattern
All controllers return `ApiResponse` (success flag + message) or domain DTOs. Swagger UI available at `localhost:9999/swagger-ui.html`.

### High-Concurrency Work (current branch)
The `ShoppingCart-dev-008-high-concurrency` branch adds Redis-based distributed locking and JDK 21 Virtual Threads for C10K load handling. See `doc/` for architecture diagrams.

## Configuration

`Backend/src/main/resources/application.properties`:
- Stripe keys: `STRIPE_PUBLIC_KEY` and `STRIPE_SECRET_KEY` (no quotes around values)
- `BASE_URL` must end with `/` (e.g., `http://localhost:8080/`) for Stripe redirect to work
- Default DB: `jdbc:mysql://localhost:3306/shopping_cart`, user `root`, empty password
