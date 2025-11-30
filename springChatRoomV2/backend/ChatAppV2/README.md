# ChatApp V2 - Backend API

A Slack-like real-time chat application backend built with Spring Boot, featuring WebSocket messaging, JWT authentication, file sharing, and comprehensive REST APIs.

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 16
- Redis 7

### Running the Application

1. **Start the databases** (using Docker):
```bash
cd ../..  # Navigate to project root
docker-compose up -d
```

2. **Run the application**:
```bash
./mvnw spring-boot:run
```

3. **Access the application**:
- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs JSON**: http://localhost:8080/v3/api-docs

## 📚 API Documentation

The complete API documentation is available via **Swagger UI** at http://localhost:8080/swagger-ui.html

### API Endpoints Overview

#### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

#### Channels
- `GET /api/channels` - Get user's channels
- `POST /api/channels/group` - Create group channel
- `POST /api/channels/direct` - Create/get direct message channel
- `POST /api/channels/{id}/members` - Add member to channel

#### Messages
- `GET /api/messages/channel/{channelId}` - Get channel message history (paginated)
- `POST /api/messages/{messageId}/read` - Mark message as read
- `GET /api/messages/channel/{channelId}/unread` - Get unread count
- `PUT /api/messages/{messageId}` - Edit message (owner only)
- `DELETE /api/messages/{messageId}` - Delete message (owner only)

#### Search
- `GET /api/search/messages` - Search messages globally or in specific channel

#### Files
- `POST /api/files/upload` - Upload file/image
- `GET /api/files/download/{fileName}` - Download file

#### Users
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/online` - Get online users
- `PUT /api/users/{id}/profile` - Update user profile
- `POST /api/users/{id}/avatar` - Upload avatar image

#### WebSocket (Real-time)
- `CONNECT /ws` - WebSocket handshake
- `SEND /app/chat/{channelId}` - Send message to channel
- `SEND /app/typing/{channelId}/start` - Start typing indicator
- `SEND /app/typing/{channelId}/stop` - Stop typing indicator
- `SUBSCRIBE /topic/channel/{channelId}` - Receive channel messages
- `SUBSCRIBE /topic/channel/{channelId}/typing` - Receive typing events
- `SUBSCRIBE /topic/channel/{channelId}/edit` - Receive message edits
- `SUBSCRIBE /topic/channel/{channelId}/delete` - Receive message deletes

## 🏗️ Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Client Layer                             │
│  (Vue.js Frontend / Mobile Apps / Third-party Clients)      │
└───────────────────────┬─────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        │               │               │
   HTTP/REST      WebSocket/STOMP    File Upload
        │               │               │
┌───────▼───────────────▼───────────────▼─────────────────────┐
│                  Spring Boot Backend                         │
│  ┌──────────────────────────────────────────────────────┐   │
│  │          Security Layer (JWT Auth)                   │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │              Controller Layer                        │   │
│  │  • REST Controllers (Auth, Channel, Message, etc.)   │   │
│  │  • WebSocket Controllers (Chat, Typing)              │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │               Service Layer                          │   │
│  │  • ChatService        • ChannelService               │   │
│  │  • AuthService        • UserService                  │   │
│  │  • FileStorageService • SearchService                │   │
│  │  • ReadReceiptService • TypingIndicatorService       │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │            Repository Layer (JPA)                    │   │
│  │  • UserRepository     • ChannelRepository            │   │
│  │  • MessageRepository  • ChannelMemberRepository      │   │
│  └──────────────────────────────────────────────────────┘   │
└──────────────────┬───────────────────┬─────────────────────┘
                   │                   │
        ┌──────────▼──────┐   ┌───────▼────────┐
        │   PostgreSQL    │   │     Redis      │
        │  (Persistence)  │   │   (Caching)    │
        └─────────────────┘   └────────────────┘
```

### Technology Stack

- **Framework**: Spring Boot 3.5.7
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: PostgreSQL 16 (persistent storage)
- **Cache**: Redis 7 (real-time data, typing indicators)
- **Security**: Spring Security + JWT
- **WebSocket**: Spring WebSocket with STOMP
- **API Documentation**: SpringDoc OpenAPI 3 (Swagger)
- **ORM**: Hibernate/JPA

### Design Patterns

1. **Layered Architecture**: Controller → Service → Repository
2. **DTO Pattern**: Data Transfer Objects for API contracts
3. **Repository Pattern**: JPA repositories for data access
4. **Dependency Injection**: Spring IoC container
5. **Builder Pattern**: Lombok @Builder for object creation
6. **Strategy Pattern**: Multiple message types (TEXT, IMAGE, FILE)

## 📁 Project Structure

```
src/main/java/com/yen/ChatAppV2/
├── config/                      # Configuration classes
│   ├── OpenApiConfig.java       # Swagger/OpenAPI configuration
│   ├── RedisConfig.java         # Redis configuration
│   ├── SecurityConfig.java      # Spring Security + JWT
│   └── WebSocketConfig.java     # WebSocket/STOMP configuration
│
├── controller/                  # REST & WebSocket controllers
│   ├── AuthController.java      # Authentication endpoints
│   ├── ChannelController.java   # Channel management
│   ├── ChatController.java      # WebSocket message handling
│   ├── FileController.java      # File upload/download
│   ├── MessageRestController.java # Message operations
│   ├── SearchController.java    # Message search
│   └── UserController.java      # User management
│
├── dto/                         # Data Transfer Objects
│   ├── AuthResponse.java        # JWT token response
│   ├── ChannelDTO.java          # Channel data
│   ├── ChatMessageDTO.java      # Message data
│   ├── CreateDirectChannelRequest.java
│   ├── CreateGroupChannelRequest.java
│   ├── FileUploadResponse.java
│   ├── LoginRequest.java
│   ├── MessageRequest.java
│   ├── RegisterRequest.java
│   ├── TypingRequest.java
│   └── UpdateProfileRequest.java
│
├── model/                       # JPA Entity classes
│   ├── Channel.java             # Chat channel entity
│   ├── ChannelMember.java       # Channel membership
│   ├── ChannelType.java         # Enum: DIRECT, GROUP
│   ├── Message.java             # Chat message entity
│   ├── MessageType.java         # Enum: TEXT, IMAGE, FILE
│   └── User.java                # User entity
│
├── repository/                  # JPA Repositories
│   ├── ChannelMemberRepository.java
│   ├── ChannelRepository.java
│   ├── MessageRepository.java
│   └── UserRepository.java
│
├── security/                    # Security components
│   ├── CustomUserDetailsService.java  # Load user for auth
│   ├── JwtAuthenticationFilter.java   # JWT request filter
│   └── JwtService.java                # JWT token generation/validation
│
├── service/                     # Business logic layer
│   ├── AuthService.java         # Authentication logic
│   ├── ChannelService.java      # Channel management
│   ├── ChatService.java         # Message processing
│   ├── FileStorageService.java  # File operations
│   ├── ReadReceiptService.java  # Read tracking
│   ├── SearchService.java       # Message search
│   ├── TypingIndicatorService.java # Typing events
│   └── UserService.java         # User management
│
└── ChatAppV2Application.java    # Spring Boot main class

src/main/resources/
├── application.yml              # Application configuration
└── application-test.yml         # Test configuration

src/test/java/                   # Unit & integration tests
├── controller/
├── repository/
└── service/
```

## 🗄️ Database Schema

### Core Tables

**users**
- `id` (PK) - User ID
- `username` - Unique username
- `email` - Unique email
- `password` - Encrypted password (BCrypt)
- `display_name` - Display name
- `avatar_url` - Avatar image URL
- `created_at` - Registration timestamp
- `last_seen_at` - Last activity timestamp

**channels**
- `id` (PK) - Channel ID
- `channel_type` - DIRECT or GROUP
- `name` - Channel name (nullable for direct messages)
- `created_at` - Creation timestamp
- `created_by` (FK) - User who created the channel

**channel_members**
- `id` (PK) - Membership ID
- `channel_id` (FK) - Channel reference
- `user_id` (FK) - User reference
- `joined_at` - Join timestamp
- `last_read_at` - Last read message timestamp

**messages**
- `id` (PK) - Message ID
- `channel_id` (FK) - Channel reference
- `sender_id` (FK) - User who sent the message
- `content` - Message content
- `message_type` - TEXT, IMAGE, or FILE
- `created_at` - Send timestamp
- `edited_at` - Edit timestamp (nullable)
- `is_deleted` - Soft delete flag

### Indexes

- `idx_channel_members_channel` - On `channel_id` for fast member lookup
- `idx_channel_members_user` - On `user_id` for user's channels
- `idx_messages_channel` - On `channel_id` for message history
- `idx_messages_created_at` - For chronological sorting
- `idx_users_username` - For username lookups
- `idx_users_email` - For email lookups

## 🔐 Security

### JWT Authentication

1. **Registration/Login**: User provides credentials → Receives JWT token
2. **Token Format**: `Authorization: Bearer <JWT_TOKEN>`
3. **Token Expiration**: 24 hours (configurable via `jwt.expiration`)
4. **Token Contents**: User ID, username, expiration

### Password Security

- **Algorithm**: BCrypt with default strength (10 rounds)
- **Storage**: Only encrypted passwords stored in database
- **No plain text**: Passwords never logged or exposed

### Protected Endpoints

All `/api/**` endpoints except:
- `/api/auth/register`
- `/api/auth/login`
- `/api/users/**` (for demo purposes - should be protected in production)

WebSocket and Swagger UI are also accessible without authentication.

## 📨 WebSocket Communication

### Connection Flow

```
1. Client connects to ws://localhost:8080/ws
2. Client subscribes to channels:
   - /topic/channel/{channelId}
   - /topic/channel/{channelId}/typing
   - /topic/channel/{channelId}/edit
   - /topic/channel/{channelId}/delete
3. Client sends messages to:
   - /app/chat/{channelId}
   - /app/typing/{channelId}/start
   - /app/typing/{channelId}/stop
4. Server broadcasts to all subscribers
```

### Message Format

**Send Message**:
```json
{
  "channelId": 1,
  "senderId": 123,
  "content": "Hello world",
  "messageType": "TEXT"
}
```

**Receive Message**:
```json
{
  "id": 456,
  "channelId": 1,
  "senderId": 123,
  "senderName": "John Doe",
  "content": "Hello world",
  "messageType": "TEXT",
  "createdAt": "2025-11-30T19:00:00",
  "edited": false,
  "deleted": false
}
```

## 🎯 Key Features

### Phase 1 - MVP (Completed)
✅ User registration and management
✅ Channel creation (group & direct)
✅ Real-time messaging via WebSocket
✅ Message history with pagination
✅ PostgreSQL persistence
✅ Redis caching for recent messages

### Phase 2 - Authentication (Completed)
✅ JWT-based authentication
✅ Secure password storage (BCrypt)
✅ Token-based API protection
✅ User profile management

### Phase 3 - Enhanced Features (Completed)
✅ Typing indicators
✅ Read receipts and unread counts
✅ Message search (global & channel-specific)
✅ File upload/download
✅ Image sharing
✅ Message editing
✅ Message deletion (soft delete)
✅ Avatar uploads
✅ Online user tracking

### Phase 4 - Documentation (Completed)
✅ Swagger/OpenAPI integration
✅ Interactive API documentation
✅ Comprehensive README

## 🧪 Testing

### Run All Tests
```bash
./mvnw test
```

### Run Specific Test
```bash
./mvnw test -Dtest=AuthServiceTest
```

### Test Coverage

- **Unit Tests**: Service layer with Mockito
- **Integration Tests**: Controller layer with Spring context
- **Repository Tests**: JPA repositories with H2
- **Total Tests**: 65 tests (all passing)

### Test Categories

- `AuthServiceTest` - Authentication logic
- `ChatServiceTest` - Message processing
- `TypingIndicatorServiceTest` - Typing events
- `ReadReceiptServiceTest` - Read tracking
- `SearchServiceTest` - Message search
- `FileStorageServiceTest` - File operations
- `UserServiceProfileTest` - Profile management
- `AuthControllerIntegrationTest` - End-to-end auth flow

## ⚙️ Configuration

### Environment Variables

```bash
# Database
DB_PASSWORD=password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# JWT
JWT_SECRET=your-secret-key-here

# File Upload
FILE_UPLOAD_DIR=./uploads
```

### Application Properties

Key configurations in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/chatapp
  redis:
    host: localhost
    port: 6379
  servlet:
    multipart:
      max-file-size: 10MB

server:
  port: 8080

jwt:
  expiration: 86400000  # 24 hours

file:
  upload-dir: ./uploads
```

## 🚀 Deployment Considerations

### Production Checklist

- [ ] Update `JWT_SECRET` to a strong random key
- [ ] Enable HTTPS/TLS
- [ ] Configure CORS for production domains
- [ ] Set up database connection pooling
- [ ] Configure Redis persistence
- [ ] Set up file storage (S3, CDN)
- [ ] Enable logging and monitoring
- [ ] Configure rate limiting
- [ ] Protect `/api/users` endpoints
- [ ] Set up database backups
- [ ] Configure WebSocket scaling (Redis pub/sub)

### Scaling Strategies

1. **Horizontal Scaling**: Multiple backend instances with sticky sessions
2. **Database**: PostgreSQL read replicas for query load
3. **Cache**: Redis Cluster for distributed caching
4. **WebSocket**: Redis pub/sub for cross-instance messaging
5. **File Storage**: External storage (S3, CDN) instead of local filesystem
6. **Load Balancing**: Nginx/HAProxy with WebSocket support

## 📝 Development

### Code Style

- Follow Java naming conventions
- Use Lombok annotations to reduce boilerplate
- DTOs for API contracts, Entities for database
- Service layer for business logic, Controllers for HTTP/WebSocket handling
- Comprehensive JavaDoc for public methods

### Adding a New Feature

1. Create entity class (if needed) in `model/`
2. Create repository interface in `repository/`
3. Create DTOs in `dto/`
4. Implement service logic in `service/`
5. Create controller endpoints in `controller/`
6. Add Swagger annotations for documentation
7. Write unit and integration tests
8. Update this README

## 🐛 Troubleshooting

### Port 8080 already in use
```bash
# Find process using port 8080
lsof -i :8080
# Kill the process
kill -9 <PID>
```

### Database connection error
```bash
# Check if PostgreSQL is running
docker-compose ps
# Check logs
docker-compose logs postgres
```

### Redis connection error
```bash
# Check if Redis is running
docker-compose ps
# Test Redis connection
docker exec -it redis redis-cli ping
```

### Swagger UI shows 500 error
- Ensure SpringDoc version is compatible with Spring Boot version
- Check for annotation conflicts in controllers
- Review application logs for stack traces

## 📄 License

Apache 2.0

## 👥 Contributors

ChatApp Team

## 📞 Support

For issues and questions, please check:
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs
- Project DESIGN.md for detailed specifications
