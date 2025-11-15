# Chat Application System Design

## Overview
A Slack-like chat application supporting group chat and person-to-person messaging with message history, built with Vue.js frontend and Spring Boot backend.

## Architecture

```
┌─────────────────┐      WebSocket/HTTP      ┌─────────────────┐
│   Vue.js        │◄────────────────────────►│  Spring Boot    │
│   Frontend      │                           │   Backend       │
└─────────────────┘                           └────────┬────────┘
                                                       │
                                          ┌────────────┼────────────┐
                                          │            │            │
                                    ┌─────▼────┐ ┌────▼─────┐ ┌───▼────┐
                                    │  Redis   │ │ PostgreSQL│ │ WebSocket│
                                    │  (Msg)   │ │ (History) │ │  Mgr   │
                                    └──────────┘ └───────────┘ └────────┘
```

## Core Components

### 1. Channel Design Strategy

#### Channel Types
- **Group Channels**: `group:{groupId}`
- **Direct Messages**: `dm:{userId1}:{userId2}` (sorted IDs to ensure consistency)

#### Redis Key Structure

```
# Active Channel Members (Set)
channel:{channelId}:members = Set<userId>

# User's Active Channels (Set)
user:{userId}:channels = Set<channelId>

# Recent Messages in Channel (List)
channel:{channelId}:messages = List<messageJson>

# User Online Status (Hash)
users:online = Hash<userId, connectionId>

# Typing Indicators (Set with TTL)
channel:{channelId}:typing = Set<userId> (TTL: 5s)
```

#### Channel Topic Mapping
```
WebSocket Topic Pattern:
- Subscribe: /topic/channel/{channelId}
- Send: /app/chat/{channelId}
- User queue: /user/queue/messages
```

### 2. Database Schema (PostgreSQL)

```sql
-- Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    display_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Channels Table
CREATE TABLE channels (
    id BIGSERIAL PRIMARY KEY,
    channel_type VARCHAR(20) NOT NULL, -- 'GROUP' or 'DIRECT'
    name VARCHAR(100), -- null for direct messages
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_channel_type CHECK (channel_type IN ('GROUP', 'DIRECT'))
);

CREATE INDEX idx_channels_type ON channels(channel_type);

-- Channel Members Table
CREATE TABLE channel_members (
    channel_id BIGINT REFERENCES channels(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_read_at TIMESTAMP,
    PRIMARY KEY (channel_id, user_id)
);

CREATE INDEX idx_channel_members_user ON channel_members(user_id);

-- Messages Table (Permanent Storage)
CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    channel_id BIGINT REFERENCES channels(id) ON DELETE CASCADE,
    sender_id BIGINT REFERENCES users(id),
    content TEXT NOT NULL,
    message_type VARCHAR(20) DEFAULT 'TEXT', -- TEXT, IMAGE, FILE
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_messages_channel ON messages(channel_id, created_at DESC);
CREATE INDEX idx_messages_sender ON messages(sender_id);
```

### 3. Backend Architecture (Spring Boot)

#### Key Components

**WebSocketConfig.java**
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple broker for topics
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:8080")
                .withSockJS();
    }
}
```

**Channel Service Architecture**
```
ChatController
    ├── sendMessage()           → /app/chat/{channelId}
    ├── createChannel()         → REST API
    └── getChannelHistory()     → REST API

ChatService
    ├── processMessage()        → Validate & broadcast
    ├── saveToRedis()           → Cache recent messages
    ├── persistToDatabase()     → Long-term storage
    └── broadcastToChannel()    → WebSocket publish

ChannelService
    ├── createGroupChannel()    → Create group
    ├── createDirectChannel()   → Create/get DM channel
    ├── addMember()             → Add user to channel
    └── getChannelMessages()    → Load from Redis/DB
```

#### Redis Integration Strategy

**RedisService.java**
```java
@Service
public class RedisService {

    // Store recent messages (last 100)
    public void cacheMessage(String channelId, Message message) {
        String key = "channel:" + channelId + ":messages";
        redisTemplate.opsForList().leftPush(key, message);
        redisTemplate.opsForList().trim(key, 0, 99); // Keep last 100
    }

    // Track online users
    public void setUserOnline(Long userId, String sessionId) {
        redisTemplate.opsForHash().put("users:online",
            userId.toString(), sessionId);
    }

    // Channel membership
    public void addUserToChannel(Long userId, String channelId) {
        redisTemplate.opsForSet().add(
            "channel:" + channelId + ":members", userId.toString());
        redisTemplate.opsForSet().add(
            "user:" + userId + ":channels", channelId);
    }
}
```

### 4. Message Flow

#### Sending a Message
```
1. User sends message via WebSocket → /app/chat/{channelId}
2. ChatController receives message
3. ChatService processes:
   a. Validate user is member of channel
   b. Create message object with timestamp
   c. Save to Redis (recent cache)
   d. Save to PostgreSQL (permanent)
   e. Broadcast to /topic/channel/{channelId}
4. All subscribed clients receive message
```

#### Loading Message History
```
1. User requests history → GET /api/channels/{channelId}/messages?page=0&size=50
2. ChannelService checks:
   a. Try Redis first (recent 100 messages)
   b. If need more, query PostgreSQL
3. Return paginated messages
4. Frontend displays messages
```

### 5. Frontend Architecture (Vue.js)

#### Key Components

```
src/
├── components/
│   ├── ChannelList.vue       # List of channels
│   ├── ChatWindow.vue        # Main chat interface
│   ├── MessageList.vue       # Display messages
│   ├── MessageInput.vue      # Input box
│   └── UserList.vue          # Online users
├── services/
│   ├── websocket.service.js  # WebSocket connection
│   ├── chat.service.js       # Chat API calls
│   └── auth.service.js       # Authentication
├── store/
│   ├── channels.js           # Channel state
│   ├── messages.js           # Messages state
│   └── users.js              # User state
└── views/
    ├── ChatView.vue          # Main chat view
    └── LoginView.vue         # Login page
```

#### WebSocket Service
```javascript
// websocket.service.js
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

class WebSocketService {
    connect(userId) {
        this.socket = new SockJS('http://localhost:8080/ws');
        this.stompClient = Stomp.over(this.socket);

        this.stompClient.connect(
            { userId: userId },
            () => this.onConnected(),
            (error) => this.onError(error)
        );
    }

    subscribeToChannel(channelId, callback) {
        return this.stompClient.subscribe(
            `/topic/channel/${channelId}`,
            callback
        );
    }

    sendMessage(channelId, message) {
        this.stompClient.send(
            `/app/chat/${channelId}`,
            JSON.stringify(message)
        );
    }
}
```

### 6. Channel Creation Logic

#### Group Channel
```java
public Channel createGroupChannel(String name, Long creatorId, List<Long> memberIds) {
    // 1. Create channel in DB
    Channel channel = new Channel();
    channel.setChannelType("GROUP");
    channel.setName(name);
    channel.setCreatedBy(creatorId);
    channelRepository.save(channel);

    // 2. Add members to DB
    for (Long memberId : memberIds) {
        ChannelMember cm = new ChannelMember(channel.getId(), memberId);
        channelMemberRepository.save(cm);
    }

    // 3. Initialize Redis
    String channelId = "group:" + channel.getId();
    for (Long memberId : memberIds) {
        redisService.addUserToChannel(memberId, channelId);
    }

    return channel;
}
```

#### Direct Message Channel
```java
public Channel getOrCreateDirectChannel(Long userId1, Long userId2) {
    // Sort IDs for consistency
    Long smallerId = Math.min(userId1, userId2);
    Long largerId = Math.max(userId1, userId2);

    // 1. Check if channel exists
    Channel existing = channelRepository
        .findDirectChannel(smallerId, largerId);

    if (existing != null) {
        return existing;
    }

    // 2. Create new DM channel
    Channel channel = new Channel();
    channel.setChannelType("DIRECT");
    channel.setCreatedBy(userId1);
    channelRepository.save(channel);

    // 3. Add both users
    channelMemberRepository.save(
        new ChannelMember(channel.getId(), smallerId));
    channelMemberRepository.save(
        new ChannelMember(channel.getId(), largerId));

    // 4. Initialize Redis
    String channelId = "dm:" + smallerId + ":" + largerId;
    redisService.addUserToChannel(smallerId, channelId);
    redisService.addUserToChannel(largerId, channelId);

    return channel;
}
```

## Implementation Phases

### Phase 1: MVP (Minimum Viable Product)
- [ ] Basic WebSocket connection
- [ ] Single group chat room
- [ ] Send/receive text messages
- [ ] Redis message caching (last 100 messages)
- [ ] PostgreSQL message persistence
- [ ] Simple Vue.js UI

### Phase 2: Core Features
- [ ] User authentication & registration
- [ ] Multiple group channels
- [ ] Direct messaging (1-to-1)
- [ ] Channel creation & joining
- [ ] Message history loading
- [ ] Online/offline status

### Phase 3: Enhanced Features
- [ ] Typing indicators
- [ ] Read receipts
- [ ] User profiles
- [ ] Message search
- [ ] File/image uploads
- [ ] Notifications

## Key Design Decisions

### Why Redis + PostgreSQL?
- **Redis**: Fast in-memory cache for recent messages, reduces DB load
- **PostgreSQL**: Reliable persistent storage for message history

### Why not Kafka?
For simplicity, Redis Pub/Sub is sufficient. Kafka would be beneficial if:
- Need message replay from any point in time
- Processing millions of messages/second
- Complex stream processing requirements

For this MVP, Redis is simpler and adequate.

### Channel ID Strategy
- **Group**: Sequential DB ID → `group:{id}`
- **Direct**: Sorted user IDs → `dm:{userId1}:{userId2}`
- Ensures consistency and easy lookup

### Message Retention
- **Redis**: Last 100 messages per channel (configurable)
- **PostgreSQL**: Indefinite (can add archival strategy later)

## API Endpoints

### REST APIs
```
POST   /api/auth/register          # Register user
POST   /api/auth/login             # Login
GET    /api/channels               # Get user's channels
POST   /api/channels/group         # Create group channel
POST   /api/channels/direct        # Create/get DM channel
GET    /api/channels/{id}/messages # Get message history
POST   /api/channels/{id}/members  # Add member to channel
```

### WebSocket Endpoints
```
CONNECT /ws                        # WebSocket handshake
SEND    /app/chat/{channelId}      # Send message
SUBSCRIBE /topic/channel/{channelId} # Receive messages
```

## Configuration

### application.yml (Spring Boot)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/chatapp
    username: postgres
    password: password
  redis:
    host: localhost
    port: 6379
  websocket:
    max-session-idle-timeout: 300000

server:
  port: 8080
```

### Environment Variables
```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=chatapp
REDIS_HOST=localhost
REDIS_PORT=6379
FRONTEND_URL=http://localhost:8080
```

## Next Steps

1. **Setup Infrastructure**
   - Install PostgreSQL
   - Install Redis
   - Create database schema

2. **Backend Development**
   - Initialize Spring Boot project
   - Configure WebSocket
   - Implement Redis service
   - Create REST controllers

3. **Frontend Development**
   - Initialize Vue.js project
   - Setup WebSocket service
   - Create chat UI components

4. **Testing**
   - Test WebSocket connections
   - Test message persistence
   - Test multi-user scenarios

5. **Deployment**
   - Dockerize application
   - Setup CI/CD
   - Deploy to cloud (optional)

## Scalability Considerations (Future)

- **Horizontal Scaling**: Add Redis Cluster for distributed caching
- **Load Balancing**: Use sticky sessions for WebSocket connections
- **Message Partitioning**: Shard channels across multiple Redis instances
- **CDN**: Serve static assets and media files
- **Microservices**: Split into auth, chat, and notification services
