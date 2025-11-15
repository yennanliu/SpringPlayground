# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Slack-like chat application with Spring Boot backend and Vue.js frontend, supporting group chat and direct messaging with message history. The system uses Redis for caching recent messages and PostgreSQL for persistent storage.

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

## Repository Structure

- **backend/ChatAppV2/** - Spring Boot application (Java 17, Spring Boot 3.5.7)
- **frontend/chatAppV2UI/** - Vue.js 3 application with Vite
- **DESIGN.md** - Comprehensive system design documentation including database schema, API endpoints, and implementation phases

## Backend (Spring Boot)

### Technology Stack
- Java 17
- Spring Boot 3.5.7
- Maven wrapper (mvnw)
- Spring WebSocket for real-time communication
- PostgreSQL for persistent storage (planned)
- Redis for message caching (planned)

### Common Commands

**Build the project:**
```bash
cd backend/ChatAppV2
./mvnw clean install
```

**Run the backend:**
```bash
cd backend/ChatAppV2
./mvnw spring-boot:run
```

**Run tests:**
```bash
cd backend/ChatAppV2
./mvnw test
```

**Run a specific test:**
```bash
cd backend/ChatAppV2
./mvnw test -Dtest=YourTestClass
```

### Configuration
- Main configuration: `backend/ChatAppV2/src/main/resources/application.properties`
- Default server port: 8080 (configurable)
- Main application class: `com.yen.ChatAppV2.ChatAppV2Application`

### WebSocket Architecture
- Connection endpoint: `/ws`
- Message broker destinations: `/topic` (broadcast), `/queue` (user-specific)
- Application prefix: `/app`
- Channel message pattern: `/app/chat/{channelId}` → `/topic/channel/{channelId}`

## Frontend (Vue.js)

### Technology Stack
- Vue 3.5.22
- Vite 7.1.11
- Node.js 20.19.0+ or 22.12.0+

### Common Commands

**Install dependencies:**
```bash
cd frontend/chatAppV2UI
npm install
```

**Run development server:**
```bash
cd frontend/chatAppV2UI
npm run dev
```

**Build for production:**
```bash
cd frontend/chatAppV2UI
npm run build
```

**Preview production build:**
```bash
cd frontend/chatAppV2UI
npm run preview
```

### Frontend Structure (Planned)
- `src/components/` - UI components (ChannelList, ChatWindow, MessageList, MessageInput, UserList)
- `src/services/` - WebSocket and API services
- `src/store/` - State management for channels, messages, and users
- `src/views/` - Main views (ChatView, LoginView)

## Channel Design Strategy

### Channel Types
- **Group Channels**: `group:{groupId}` - Multiple users in a group conversation
- **Direct Messages**: `dm:{userId1}:{userId2}` - One-on-one conversations (user IDs are sorted)

### Redis Key Structure
```
channel:{channelId}:members         # Set<userId> - Active channel members
user:{userId}:channels              # Set<channelId> - User's active channels
channel:{channelId}:messages        # List<messageJson> - Recent messages (last 100)
users:online                        # Hash<userId, connectionId> - Online status
channel:{channelId}:typing          # Set<userId> - Typing indicators (TTL: 5s)
```

## Database Schema

### Core Tables
- **users**: User accounts (id, username, email, display_name)
- **channels**: Chat channels (id, channel_type, name, created_by)
- **channel_members**: Channel membership (channel_id, user_id, joined_at, last_read_at)
- **messages**: Message history (id, channel_id, sender_id, content, message_type, created_at)

See DESIGN.md for complete schema with indexes.

## Message Flow

### Sending a Message
1. User sends via WebSocket → `/app/chat/{channelId}`
2. Backend validates user membership
3. Save to Redis (recent cache - last 100 messages)
4. Save to PostgreSQL (permanent storage)
5. Broadcast to `/topic/channel/{channelId}`
6. All subscribed clients receive the message

### Loading Message History
1. Request: `GET /api/channels/{channelId}/messages?page=0&size=50`
2. Check Redis first (recent 100 messages)
3. Query PostgreSQL if more history needed
4. Return paginated messages

## API Endpoints (Planned)

### REST APIs
```
POST   /api/auth/register              # Register user
POST   /api/auth/login                 # Login
GET    /api/channels                   # Get user's channels
POST   /api/channels/group             # Create group channel
POST   /api/channels/direct            # Create/get DM channel
GET    /api/channels/{id}/messages     # Get message history
POST   /api/channels/{id}/members      # Add member to channel
```

### WebSocket Endpoints
```
CONNECT    /ws                             # WebSocket handshake
SEND       /app/chat/{channelId}           # Send message
SUBSCRIBE  /topic/channel/{channelId}      # Receive messages
```

## Development Status

The project is in early stages with basic Spring Boot and Vue.js scaffolding. Most features described in DESIGN.md are planned but not yet implemented. Refer to DESIGN.md for:
- Implementation phases (MVP → Core Features → Enhanced Features)
- Detailed component designs
- Configuration examples
- Scalability considerations

## Key Design Decisions

### Why Redis + PostgreSQL?
- **Redis**: Fast in-memory cache for recent messages (reduces DB load)
- **PostgreSQL**: Reliable persistent storage for complete message history

### Channel ID Strategy
- **Group**: Sequential DB ID → `group:{id}`
- **Direct**: Sorted user IDs → `dm:{userId1}:{userId2}`
- Ensures consistency and easy lookup

### Message Retention
- **Redis**: Last 100 messages per channel (configurable)
- **PostgreSQL**: Indefinite retention

## Important Notes from Global Config

When working with library paths (e.g., `@lib`), do not save compiled JavaScript or TypeScript declaration files. CDK handles these during deployment.
