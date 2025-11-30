# Backend Development Guide

This file provides guidance for working with the Spring Boot backend.

## Project Overview

This is the Spring Boot backend for the Chat Application V2 - a Slack-like chat system with WebSocket support, Redis caching, and PostgreSQL persistence.

## Development TODO

**IMPORTANT**: Follow the comprehensive implementation plan in the main project TODO:
- See: `/Users/jerryliu/SpringPlayground/springChatRoomV2/BACKEND_TODO.md`

This document outlines:
- Phase 1: MVP (Basic messaging with Redis & PostgreSQL)
- Phase 2: Core Features (Auth, JWT, multiple channels, DM)
- Phase 3: Enhanced Features (Typing, read receipts, search, files)

## Current Status: Phase 1 MVP ✅ COMPLETED

### Implemented Components:
- ✅ Database entities (User, Channel, ChannelMember, Message)
- ✅ Repository layer (all 4 repositories)
- ✅ WebSocket configuration with STOMP
- ✅ Redis configuration & RedisService
- ✅ WebSocketEventListener for connection tracking
- ✅ ChatService with Redis caching integration
- ✅ ChannelService for channel management
- ✅ All REST controllers (User, Channel, Message, Chat)
- ✅ Exception handling
- ✅ DTOs (ChatMessage, MessageRequest, Channel-related)

### Ready for Testing:
- WebSocket messaging
- Message persistence (PostgreSQL)
- Message caching (Redis - last 100)
- Channel creation (Group & Direct)
- User management

## Next Phase: Phase 2 - Core Features

Focus on implementing:
1. **Spring Security** configuration
2. **JWT** authentication & authorization
3. **User authentication** (register/login)
4. **Password hashing** with BCrypt
5. **WebSocket authentication**
6. **Enhanced channel management**

## Project Structure

```
src/main/java/com/yen/ChatAppV2/
├── config/              # WebSocket, Redis, Security configs
├── controller/          # REST & WebSocket controllers
├── dto/                 # Data Transfer Objects
├── exception/           # Custom exceptions & handler
├── model/              # JPA entities
├── repository/         # Spring Data repositories
└── service/            # Business logic
```

## Key Design Patterns

### Channel IDs
- **Group**: `group:{channelId}` (numeric DB ID)
- **Direct**: `dm:{userId1}:{userId2}` (sorted user IDs)

### Redis Key Structure
```
channel:{channelId}:members       # Set<userId>
user:{userId}:channels            # Set<channelId>
channel:{channelId}:messages      # List<messageJson> (last 100)
users:online                      # Hash<userId, sessionId>
channel:{channelId}:typing        # Set<userId> with TTL
```

### WebSocket Endpoints
```
Connection: /ws
Send: /app/chat/{channelId}
Subscribe: /topic/channel/{channelId}
```

## Testing Checklist (Phase 1)

Before moving to Phase 2, verify:
- [ ] Database schema created successfully
- [ ] All entities properly mapped
- [ ] WebSocket connection establishes
- [ ] Messages can be sent via WebSocket
- [ ] Messages are persisted to PostgreSQL
- [ ] Messages are cached in Redis
- [ ] Messages broadcast to subscribed clients
- [ ] Message history can be retrieved via REST API
- [ ] Exception handling works correctly
- [ ] CORS configured properly for frontend

## Running the Application

### Prerequisites:
- Java 17
- PostgreSQL & Redis running (via Docker Compose in project root)
- Maven wrapper included

### Commands:
```bash
# Build
./mvnw clean install

# Run
./mvnw spring-boot:run

# Run tests
./mvnw test
```

### Configuration:
- Main config: `src/main/resources/application.yml`
- Database: PostgreSQL at localhost:5432 (database: chatapp)
- Redis: localhost:6379
- Server port: 8080

## Development Guidelines

1. **Always read the main BACKEND_TODO.md** before implementing new features
2. **Follow the phase order** - complete Phase 1 testing before Phase 2
3. **Use Lombok** annotations (@Data, @RequiredArgsConstructor, @Slf4j)
4. **Log important events** at appropriate levels (INFO for business events, DEBUG for details)
5. **Handle exceptions properly** - use custom exceptions with GlobalExceptionHandler
6. **Validate input** using Jakarta validation annotations
7. **Write transactional methods** when modifying multiple entities
8. **Cache strategically** - Redis for hot data, PostgreSQL for persistence

## Redis Integration

All Redis operations go through `RedisService`:
- Message caching (automatic trimming to last 100)
- User online/offline tracking
- Channel membership caching
- Typing indicators with TTL

## Common Issues

### Redis Connection
- Ensure Redis container is running: `docker-compose ps`
- Check logs: `docker-compose logs redis`

### Database Issues
- Verify PostgreSQL: `docker exec chatapp-postgres pg_isready`
- Check connection: `application.yml` settings

### WebSocket Connection
- CORS must allow frontend origin
- SockJS fallback enabled for older browsers

## Architecture References

See project documentation:
- `../../DESIGN.md` - System architecture & design decisions
- `../../BACKEND_TODO.md` - Complete implementation roadmap
- `../../CLAUDE.md` - Project-level instructions

## Phase 2 Implementation Notes

When implementing Phase 2, remember:
- Add JWT dependencies to pom.xml
- Update User entity with password field
- Implement SecurityConfig with filter chain
- Create JwtService for token generation/validation
- Create AuthController and AuthService
- Update WebSocketConfig for JWT authentication
- Add @PreAuthorize annotations where needed

See BACKEND_TODO.md lines 777-1474 for detailed Phase 2 implementation.
