# WebSocket & STOMP Integration Guide

## Overview

This document provides a comprehensive guide to the WebSocket and STOMP protocol implementation in the springChatRoomV2 project. The implementation supports real-time bidirectional communication between the backend and frontend for instant messaging functionality.

## Architecture

```
┌─────────────────┐     WebSocket/STOMP      ┌─────────────────┐
│   Frontend      │◄──────────────────────►│  ChatController │
│   (Vue.js)      │                          │   (@Controller)  │
└─────────────────┘                          └────────┬────────┘
                                                      │
                                                      ▼
                                              ┌──────────────┐
                                              │ ChatService  │
                                              └──────┬───────┘
                                                     │
                                    ┌────────────────┼────────────────┐
                                    │                │                │
                              ┌─────▼─────┐   ┌────▼────┐   ┌───────▼───────┐
                              │  Redis    │   │ PostgreSQL│   │ SimpMessaging│
                              │  Cache    │   │  Database │   │   Template   │
                              └───────────┘   └──────────┘   └──────────────┘
```

## Configuration

### 1. WebSocketConfig.java

**Location**: `/backend/ChatAppV2/src/main/java/com/yen/ChatAppV2/config/WebSocketConfig.java`

**Configuration Details**:

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple in-memory broker for topics and queues
        config.enableSimpleBroker("/topic", "/queue");

        // Set application destination prefix
        config.setApplicationDestinationPrefixes("/app");

        // Set user destination prefix
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Main WebSocket endpoint with SockJS fallback
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        // Additional endpoint without SockJS for native WebSocket clients
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }
}
```

**Key Features**:
- **STOMP Protocol**: Simple Text Oriented Messaging Protocol for message framing
- **SockJS Fallback**: Provides compatibility for browsers without WebSocket support
- **CORS Enabled**: Allows connections from any origin (configurable for production)
- **Multiple Endpoints**: Supports both SockJS and native WebSocket connections

### 2. SecurityConfig.java

**Location**: `/backend/ChatAppV2/src/main/java/com/yen/ChatAppV2/config/SecurityConfig.java`

**WebSocket Security**:
- WebSocket endpoint `/ws/**` is configured to be accessible
- CORS is properly configured to allow frontend connections
- JWT authentication filter is in place for REST endpoints

**Important**: In production, restrict `allowedOriginPatterns` to specific domains:
```java
.setAllowedOriginPatterns("http://localhost:5173", "https://yourdomain.com")
```

## Message Flow

### Sending Messages

**Client → Server Flow**:

1. **Frontend sends message**:
   ```javascript
   stompClient.send("/app/chat/group:1", {}, JSON.stringify({
     channelId: "group:1",
     senderId: 1,
     content: "Hello World",
     messageType: "TEXT"
   }));
   ```

2. **ChatController receives message**:
   - Endpoint: `@MessageMapping("/chat/{channelId}")`
   - Sets channelId from path parameter
   - Calls `ChatService.processAndSendMessage()`

3. **ChatService processes message**:
   - Validates user is a member of the channel
   - Retrieves sender information
   - Saves message to PostgreSQL
   - Caches message in Redis (last 100 messages)
   - Creates DTO with timestamp
   - Broadcasts via `SimpMessagingTemplate`

4. **Message broadcasted to subscribers**:
   - Destination: `/topic/channel/{channelId}`
   - All subscribed clients receive the message

### Receiving Messages

**Server → Client Flow**:

1. **Frontend subscribes to channel**:
   ```javascript
   stompClient.subscribe("/topic/channel/group:1", (message) => {
     const messageData = JSON.parse(message.body);
     // Handle received message
   });
   ```

2. **Server broadcasts message**:
   - `SimpMessagingTemplate` sends to `/topic/channel/{channelId}`
   - All clients subscribed to that topic receive the message

3. **Frontend receives and displays**:
   - Parse JSON payload
   - Update UI with new message

## Endpoints

### WebSocket Connection

**URL**: `ws://localhost:8080/ws` (with SockJS) or `ws://localhost:8080/ws` (native)

**Connection Headers**:
```javascript
{
  userId: "1"  // Optional: User ID for connection tracking
}
```

### Application Destinations (Client → Server)

| Destination | Purpose | Request Format |
|-------------|---------|----------------|
| `/app/chat/{channelId}` | Send message | `MessageRequest` |
| `/app/typing/{channelId}/start` | Start typing indicator | `TypingRequest` |
| `/app/typing/{channelId}/stop` | Stop typing indicator | `TypingRequest` |

### Subscription Topics (Server → Client)

| Topic | Purpose | Response Format |
|-------|---------|-----------------|
| `/topic/channel/{channelId}` | Receive messages | `ChatMessageDTO` |
| `/topic/channel/{channelId}/typing` | Typing indicators | Typing event |
| `/topic/channel/{channelId}/edit` | Message edits | `ChatMessageDTO` |
| `/topic/channel/{channelId}/delete` | Message deletions | `Long` (messageId) |

## Data Transfer Objects (DTOs)

### MessageRequest (Client → Server)

```java
{
  "channelId": "group:1",      // Channel identifier
  "senderId": 1,               // User ID of sender
  "content": "Hello World",    // Message content
  "messageType": "TEXT"        // TEXT | IMAGE | FILE
}
```

**Validation**:
- `channelId`: Required, not blank
- `senderId`: Required, not null
- `content`: Required, not blank, max 5000 characters
- `messageType`: Defaults to TEXT

### ChatMessageDTO (Server → Client)

```java
{
  "id": 1,                           // Message ID (database)
  "channelId": "group:1",            // Channel identifier
  "senderId": 1,                     // User ID of sender
  "senderName": "Alice",             // Display name of sender
  "content": "Hello World",          // Message content
  "messageType": "TEXT",             // TEXT | IMAGE | FILE
  "timestamp": "2025-02-20T10:30:00" // ISO 8601 timestamp
}
```

## Integration with Services

### ChatService

**Key Method**: `processAndSendMessage(MessageRequest request)`

**Responsibilities**:
1. Validate user membership
2. Retrieve sender information
3. Save to PostgreSQL (via `MessageRepository`)
4. Cache in Redis (via `RedisService`)
5. Create DTO
6. Broadcast to subscribers

**Error Handling**:
- `UnauthorizedException`: User not a member of channel
- `NotFoundException`: User or channel not found

### RedisService

**WebSocket-Related Operations**:

```java
// Message caching (last 100 messages)
cacheMessage(String channelId, Message message)
getCachedMessages(String channelId, int limit)

// Online user tracking
setUserOnline(Long userId, String sessionId)
setUserOffline(Long userId)
isUserOnline(Long userId)
getOnlineUsers()

// Typing indicators
addTypingUser(String key, Long userId, long timeoutMs)
removeTypingUser(String key, Long userId)
```

### WebSocketEventListener

**Purpose**: Tracks WebSocket connection lifecycle

**Events**:
- `SessionConnectedEvent`: User connects, marked online in Redis
- `SessionDisconnectEvent`: User disconnects, marked offline in Redis

**Usage**: Pass `userId` in connection headers for tracking:
```javascript
stompClient.connect({ userId: "1" }, onConnected, onError);
```

## Testing

### Unit Tests

**File**: `ChatControllerTest.java`

**Coverage**:
- ✅ Message sending through WebSocket
- ✅ Channel ID override from path
- ✅ Different message types (TEXT, IMAGE, FILE)
- ✅ Group channels
- ✅ Direct message channels
- ✅ Long content handling

**Run Tests**:
```bash
./mvnw test -Dtest=ChatControllerTest
```

### Integration Tests

**File**: `WebSocketIntegrationTest.java` (requires full environment)

**Coverage**:
- WebSocket connection establishment
- Message sending and receiving
- Multiple clients broadcasting
- Different message types
- Channel isolation
- Typing indicators

**Note**: Integration tests require PostgreSQL and Redis to be running.

## Frontend Integration Guide

### 1. Install Dependencies

```bash
npm install @stomp/stompjs sockjs-client
```

### 2. Create WebSocket Service

```javascript
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export class WebSocketService {
  constructor() {
    this.client = null;
    this.subscriptions = new Map();
  }

  connect(userId, onConnected, onError) {
    this.client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      connectHeaders: {
        userId: userId.toString()
      },
      onConnect: onConnected,
      onStompError: onError,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    });

    this.client.activate();
  }

  disconnect() {
    if (this.client) {
      this.client.deactivate();
    }
  }

  subscribeToChannel(channelId, callback) {
    if (!this.client) return;

    const subscription = this.client.subscribe(
      `/topic/channel/${channelId}`,
      (message) => {
        const data = JSON.parse(message.body);
        callback(data);
      }
    );

    this.subscriptions.set(channelId, subscription);
    return subscription;
  }

  unsubscribeFromChannel(channelId) {
    const subscription = this.subscriptions.get(channelId);
    if (subscription) {
      subscription.unsubscribe();
      this.subscriptions.delete(channelId);
    }
  }

  sendMessage(channelId, message) {
    if (!this.client || !this.client.connected) {
      console.error('WebSocket not connected');
      return;
    }

    this.client.publish({
      destination: `/app/chat/${channelId}`,
      body: JSON.stringify(message)
    });
  }

  startTyping(channelId, userId, username) {
    this.client.publish({
      destination: `/app/typing/${channelId}/start`,
      body: JSON.stringify({ userId, username })
    });
  }

  stopTyping(channelId, userId) {
    this.client.publish({
      destination: `/app/typing/${channelId}/stop`,
      body: JSON.stringify({ userId })
    });
  }
}
```

### 3. Use in Vue Component

```vue
<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { WebSocketService } from '@/services/WebSocketService';

const messages = ref([]);
const wsService = new WebSocketService();

onMounted(() => {
  // Connect to WebSocket
  wsService.connect(
    currentUser.value.id,
    () => {
      console.log('Connected to WebSocket');
      // Subscribe to channel
      wsService.subscribeToChannel('group:1', (message) => {
        messages.value.push(message);
      });
    },
    (error) => {
      console.error('WebSocket error:', error);
    }
  );
});

onUnmounted(() => {
  wsService.unsubscribeFromChannel('group:1');
  wsService.disconnect();
});

function sendMessage(content) {
  wsService.sendMessage('group:1', {
    channelId: 'group:1',
    senderId: currentUser.value.id,
    content: content,
    messageType: 'TEXT'
  });
}
</script>
```

## Channel Types

### Group Channels

**Format**: `group:{groupId}`

**Example**: `group:1`, `group:123`

**Usage**:
- Multi-user conversations
- Team channels
- Public/private groups

### Direct Message Channels

**Format**: `dm:{userId1}:{userId2}` (IDs are sorted)

**Example**: `dm:1:2`, `dm:5:10`

**Usage**:
- One-on-one conversations
- Always sort user IDs: `Math.min(id1, id2):Math.max(id1, id2)`

## Security Considerations

### Current Configuration (Development)

- ✅ CORS allows all origins (`*`)
- ✅ WebSocket endpoint accessible without authentication
- ✅ User membership validated at service layer

### Production Recommendations

1. **Restrict CORS origins**:
   ```java
   .setAllowedOriginPatterns("https://yourdomain.com")
   ```

2. **Implement WebSocket authentication**:
   - Add JWT validation in STOMP handshake
   - Use `StompHeaderAccessor` to validate tokens
   - Create custom `ChannelInterceptor`

3. **Rate limiting**:
   - Implement message rate limiting per user
   - Prevent spam and abuse

4. **Input validation**:
   - Already implemented via `@Valid` annotations
   - Content length limited to 5000 characters

## Troubleshooting

### Connection Issues

**Problem**: Cannot connect to WebSocket

**Solutions**:
- Check if server is running on correct port (8080)
- Verify CORS configuration
- Check browser console for errors
- Try SockJS fallback if native WebSocket fails

### Messages Not Received

**Problem**: Sent messages don't appear

**Solutions**:
- Verify subscription topic matches broadcast topic
- Check if user is a member of the channel
- Inspect backend logs for errors
- Verify PostgreSQL and Redis are running

### Security Errors (400/403)

**Problem**: WebSocket upgrade fails with HTTP 400/403

**Solutions**:
- Check Spring Security configuration
- Verify `/ws/**` is in `permitAll()` list
- Review CORS settings

## Performance Considerations

### Redis Caching

- **Message Cache**: Last 100 messages per channel
- **Automatic Trimming**: Prevents memory overflow
- **Fast Retrieval**: Recent messages loaded from cache

### Database Optimization

- **Indexed Queries**: `channelId` and `createdAt` indexed
- **Paginated History**: Use `Pageable` for message history
- **Batch Operations**: Consider bulk inserts for high-volume channels

### Scalability

**Current Setup** (Single Server):
- In-memory STOMP broker
- Suitable for small to medium deployments

**Future Enhancements** (Multiple Servers):
- Use external message broker (RabbitMQ/ActiveMQ)
- Implement Redis Pub/Sub for distributed messaging
- Add load balancer with sticky sessions

## Monitoring

### Key Metrics

- WebSocket connections (active/total)
- Messages per second
- Message delivery latency
- Redis cache hit ratio
- Database query performance

### Logging

**Log Levels**:
- `INFO`: Connection events, message broadcasts
- `DEBUG`: Detailed WebSocket frames, typing indicators
- `ERROR`: Connection failures, service errors

**Configuration** (`application.yml`):
```yaml
logging:
  level:
    com.yen.ChatAppV2: DEBUG
    org.springframework.messaging: DEBUG
    org.springframework.web.socket: DEBUG
```

## API Summary

### REST Endpoints (Message History)

```
GET /api/messages/{channelId}?page=0&size=50
  - Retrieve paginated message history
  - Returns: Page<ChatMessageDTO>

PUT /api/messages/{messageId}
  - Edit existing message
  - Request: { content: "new content" }
  - Returns: ChatMessageDTO

DELETE /api/messages/{messageId}
  - Delete message (soft delete)
  - Returns: 204 No Content
```

### WebSocket Endpoints

```
CONNECT ws://localhost:8080/ws
  - Establish WebSocket connection
  - Headers: { userId: "1" }

SEND /app/chat/{channelId}
  - Send message to channel
  - Body: MessageRequest

SUBSCRIBE /topic/channel/{channelId}
  - Receive messages from channel
  - Payload: ChatMessageDTO
```

## Testing Checklist

### Backend

- [x] WebSocket configuration verified
- [x] STOMP endpoints registered
- [x] ChatController message handling tested
- [x] ChatService integration verified
- [x] Redis caching tested
- [x] Database persistence tested
- [x] Exception handling tested
- [x] CORS configuration verified

### Frontend (To be tested)

- [ ] WebSocket connection establishes
- [ ] Messages can be sent
- [ ] Messages are received in real-time
- [ ] Multiple tabs receive same messages
- [ ] Typing indicators work
- [ ] Connection reconnects on failure
- [ ] Graceful error handling

## Conclusion

The WebSocket and STOMP implementation provides a robust, scalable foundation for real-time messaging in the springChatRoomV2 application. The architecture cleanly separates concerns between:

1. **Transport Layer**: WebSocketConfig handles connections
2. **Controller Layer**: ChatController routes messages
3. **Service Layer**: ChatService processes business logic
4. **Data Layer**: Redis caching + PostgreSQL persistence

This design supports current requirements while allowing easy enhancement for future features like file sharing, video calls, and advanced presence indicators.

---

**Last Updated**: 2026-02-20
**Version**: 1.0
**Status**: Phase 1 MVP Complete ✅
