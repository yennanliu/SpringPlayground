# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot WebSocket chat room application supporting both single-instance and cluster deployments. Uses STOMP protocol over WebSocket with Redis pub/sub for distributed messaging across multiple instances.

**Tech Stack:**
- Java 17
- Spring Boot 3.0.12
- WebSocket + STOMP
- Redis (for clustering)
- SockJS (client-side WebSocket fallback)
- Maven

**UI Access:** http://localhost:8080

## Build and Run Commands

### Single Instance Mode
```bash
# Compile and package
mvn package -DskipTests

# Run application
java -jar target/springChatRoom-0.0.1-SNAPSHOT.jar
```

### Cluster Mode
```bash
# Start Redis (macOS with Homebrew)
brew services start redis

# Test Redis connection
redis-cli ping

# Compile and run (same as single mode)
mvn package -DskipTests
java -jar target/springChatRoom-0.0.1-SNAPSHOT.jar

# Run multiple instances by changing server.port in application.properties
```

### Testing
```bash
# Run all tests
mvn test

# Run single test class
mvn test -Dtest=ChatControllerTest

# Skip tests during build
mvn package -DskipTests
```

## Architecture

### WebSocket/STOMP Configuration

**Endpoints:**
- WebSocket connection: `/ws` (with SockJS fallback)
- Application destination prefix: `/app` (client sends here)
- Broker destinations: `/topic` (public), `/private` (user-specific)

**Message Types:**
- `CHAT` - Public chat messages
- `JOIN` - User joins chat room
- `LEAVE` - User leaves chat room
- `PRIVATE` - Direct messages between users

### Redis-Based Clustering

The application uses Redis pub/sub to synchronize messages across multiple instances:

**Redis Channels (application.properties):**
- `redis.channel.msgToAll` → `websocket.msgToAll` - Public messages
- `redis.channel.userStatus` → `websocket.userStatus` - Join/leave events
- `redis.channel.private` → `websocket.privateMsg` - Private messages
- `redis.set.onlineUsers` → `websocket.onlineUsers` - Redis Set for tracking online users

**Message Flow:**
1. Instance receives WebSocket message from client
2. Controller publishes to Redis channel via `redisTemplate.convertAndSend()`
3. All instances (including sender) receive from Redis via `RedisListenerHandle`
4. Each instance broadcasts to its connected WebSocket clients via `ChatService`

This ensures users connected to different instances can communicate in real-time.

### Key Components

**Controllers:**
- `ChatController` - WebSocket message handlers (`/app/chat.*` endpoints)
- `UserController` - REST API for online users (`/online-users`)

**Services:**
- `ChatService` - Business logic for sending messages via STOMP

**Redis Integration:**
- `RedisListenerBean` - Configures Redis message listeners for pub/sub channels
- `RedisListenerHandle` - Receives Redis messages and routes to `ChatService`

**Listeners:**
- `WebSocketEventListener` - Handles WebSocket connect/disconnect lifecycle
  - On disconnect: removes user from Redis Set, publishes LEAVE event

**Configuration:**
- `WebSocketConfig` - STOMP endpoint and broker configuration

**Models:**
- `ChatMessage` - Main message model with type, sender, recipient, content, timestamp
- `OnlineUser` - Tracks connected users
- `Message` - Simplified message structure

### Important Implementation Details

**Private Messaging:**
- Messages stored in Redis Sets: `websocket.privateMsg.{recipient}.{sender}`
- Uses `simpMessagingTemplate.convertAndSendToUser()` for targeted delivery
- History retrieved by querying both sender→recipient and recipient→sender Sets

**User Session Management:**
- Online users stored in Redis Set `websocket.onlineUsers`
- Accessible across all instances for consistent state
- WebSocket session attributes track username

**Horizontal Scaling:**
- Stateless application instances - all state in Redis
- No direct inter-instance communication
- New instances auto-join cluster by subscribing to Redis channels

## Development Notes

- **Lombok** is used - ensure IDE has Lombok plugin installed
- **FastJSON** (alibaba) for JSON serialization
- Don't save compiled JS/d.ts files in @lib path (per global instructions)
- Redis must be running for cluster mode; single mode works without Redis
