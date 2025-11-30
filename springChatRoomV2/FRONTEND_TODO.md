# Frontend Implementation TODO

## Project Setup

### Initial Configuration
- [x] Initialize Vue.js 3 project with Vite
- [x] Install dependencies:
  - `sockjs-client` - WebSocket client
  - `@stomp/stompjs` - STOMP protocol
  - `axios` - HTTP client
  - `pinia` - State management
  - `vue-router` - Routing
- [x] Configure environment variables (`.env.development`, `.env.production`)
  - `VITE_API_BASE_URL`
  - `VITE_WS_URL`
- [x] Setup project structure following the design pattern

---

## Phase 1: MVP (Minimum Viable Product) ✅ COMPLETE

### Goal
Basic working chat application with single group room, WebSocket connection, and message sending/receiving.

**Status**: ✅ Complete - See `frontend/chatAppV2UI/PHASE1_COMPLETE.md` for details

### Components to Build

#### 1.1 Authentication & Layout
- [x] **LoginView.vue**
  - Simple login form (username/email)
  - Basic validation
  - Store user info in localStorage/sessionStorage
  - Redirect to chat on success

- [x] **App Layout Structure**
  - Basic header with app title
  - Main content area
  - Logout functionality

#### 1.2 WebSocket Service
- [x] **services/websocket.service.js**
  - Connect to WebSocket endpoint (`/ws`)
  - Implement STOMP client connection
  - Handle connection lifecycle (connect, disconnect, reconnect)
  - Error handling and connection status tracking
  - Methods:
    - `connect(userId)`
    - `disconnect()`
    - `subscribeToChannel(channelId, callback)`
    - `unsubscribeFromChannel(subscription)`
    - `sendMessage(channelId, message)`

#### 1.3 Core Chat Components
- [x] **ChatView.vue** (Main container)
  - Layout: sidebar + chat area
  - WebSocket connection on mount
  - Cleanup on unmount

- [x] **MessageList.vue**
  - Display messages in scrollable list
  - Show sender name and timestamp
  - Auto-scroll to bottom on new messages
  - Message item structure:
    ```javascript
    {
      id, senderId, senderName, content,
      timestamp, messageType
    }
    ```

- [x] **MessageInput.vue**
  - Text input with send button
  - Handle Enter key to send
  - Disable when not connected
  - Clear input after sending
  - Character limit indicator

- [x] **ChannelList.vue** (Simplified for Phase 1)
  - Display single hardcoded "General" channel
  - Show active channel highlight

#### 1.4 State Management (Pinia)
- [x] **stores/messages.js**
  - State: `messages[]`, `currentChannel`
  - Actions:
    - `addMessage(message)`
    - `setMessages(messages[])`
    - `clearMessages()`

- [x] **stores/user.js**
  - State: `currentUser`, `isAuthenticated`
  - Actions:
    - `login(userData)`
    - `logout()`

- [x] **stores/websocket.js**
  - State: `isConnected`, `connectionError`
  - Actions:
    - `setConnectionStatus(status)`
    - `setError(error)`

#### 1.5 HTTP Service
- [x] **services/chat.service.js**
  - `fetchMessageHistory(channelId, page, size)`
  - Axios instance with base URL configuration
  - Error handling wrapper

### Phase 1 Testing Checklist
- [x] User can login with username
- [x] WebSocket connection establishes successfully
- [x] Messages sent appear in chat window
- [x] Messages from other users are received in real-time
- [x] Message history loads on channel open
- [x] Connection status is visible
- [x] Graceful error handling for connection failures

---

## Phase 2: Core Features ✅ COMPLETE

### Goal
Multi-channel support, direct messaging, user authentication, and channel management.

**Status**: ✅ Complete - See `frontend/chatAppV2UI/PHASE2_COMPLETE.md` for details

### Components to Build

#### 2.1 Enhanced Authentication
- [ ] **LoginView.vue** (Enhanced) - DEFERRED TO PHASE 3
  - Add registration form
  - Email validation
  - Password fields
  - Toggle between login/register

- [ ] **services/auth.service.js**
  - `register(username, email, password)`
  - `login(username, password)`
  - `logout()`
  - Token management (if using JWT)
  - Axios interceptors for auth headers

#### 2.2 Channel Management Components
- [x] **ChannelList.vue** (Enhanced)
  - Display all user's channels (groups + DMs)
  - Group channels section
  - Direct messages section
  - Active channel highlighting
  - Unread message badges
  - Click to switch channels

- [x] **CreateChannelModal.vue**
  - Modal dialog for creating group channels
  - Input: channel name
  - Form validation
  - Success/error feedback

- [x] **UserList.vue**
  - Display online users
  - Online/offline status indicators
  - Click user to start DM
  - User avatar placeholders

- [ ] **ChannelHeader.vue** - DEFERRED (using inline header in ChatView)
  - Display current channel name
  - Show channel type (Group/DM)
  - Member count for groups
  - Channel settings button (future)

#### 2.3 Direct Messaging
- [x] **Direct Messaging Support**
  - Click user in UserList to start DM
  - Create or navigate to existing DM channel
  - DM channels in separate section

#### 2.4 Enhanced State Management
- [x] **stores/channels.js**
  - State:
    - `channels[]` - all user channels
    - `activeChannelId` - currently open channel
    - `unreadCounts` - per channel unread tracking
  - Actions:
    - `loadUserChannels()`
    - `setCurrentChannel(channelId)`
    - `createGroupChannel(name, memberIds)`
    - `createDirectChannel(userId1, userId2)`
    - `incrementUnreadCount(channelId)`
    - `clearUnreadCount(channelId)`
  - Getters:
    - `currentChannel`
    - `groupChannels`
    - `directChannels`

- [x] **stores/typing.js** (NEW)
  - State:
    - `typingUsers` - map of channelId to Set of usernames
  - Actions:
    - `addTypingUser(channelId, username)`
    - `removeTypingUser(channelId, username)`
    - `clearTypingForChannel(channelId)`

#### 2.5 Enhanced Services
- [x] **services/chat.service.js** (Enhanced - includes channel methods)
  - `getUserChannels()`
  - `createGroupChannel(name, memberIds)`
  - `createDirectChannel(userId1, userId2)`
  - `getChannelMessages(channelId, page, size)`

- [x] **services/websocket.service.js** (Enhanced)
  - Subscribe to multiple channels
  - Handle channel-specific callbacks
  - Manage multiple subscriptions
  - `unsubscribeFromAllChannels()`
  - `getActiveSubscriptions()`

#### 2.6 Routing
- [x] **router/index.js**
  - `/login` - LoginView
  - `/chat` - ChatView (protected route)
  - `/chat/:channelId` - ChatView with specific channel
  - Route guards for authentication

### Phase 2 Testing Checklist
- [ ] User can register and login - DEFERRED (Phase 3)
- [x] User can create group channels
- [x] User can start direct messages
- [x] User can switch between channels
- [x] Messages persist across channel switches
- [ ] Online/offline status updates in real-time - NEEDS BACKEND
- [x] Unread message counts update correctly
- [x] Only authenticated users can access chat
- [x] Channel list updates when new channels are created

---

## Phase 3: Enhanced Features 🔄 IN PROGRESS

### Goal
Polish the application with typing indicators, read receipts, search, and file uploads.

**Status**: 🔄 Partially Complete - Typing indicators done, other features pending

### Components to Build

#### 3.1 Typing Indicators ✅ COMPLETE
- [x] **TypingIndicator.vue**
  - Show "User is typing..." message
  - Handle multiple users typing
  - Auto-hide after timeout

- [x] **WebSocket Enhancement**
  - Send typing events on input (local implementation)
  - Throttle typing events (2 second timeout)
  - Auto-clear typing status on send or timeout
  - Note: Backend WebSocket broadcast for typing indicators pending

#### 3.2 Read Receipts
- [ ] **Message Component Enhancement**
  - Visual indicator for read/unread messages
  - "Seen by" information
  - Double-check marks for sent/delivered/read

- [ ] **Read Receipt Logic**
  - Track `last_read_at` timestamp
  - Send read receipt when messages are visible
  - Update UI when others read messages

#### 3.3 User Profiles
- [ ] **UserProfileModal.vue**
  - View user information
  - Edit own profile
  - Upload avatar
  - Display name, email, status message

- [ ] **Avatar Component**
  - Display user avatars throughout app
  - Fallback to initials if no avatar
  - Different sizes (small, medium, large)

#### 3.4 Message Search
- [ ] **SearchBar.vue**
  - Search input in header
  - Real-time search suggestions
  - Filter by channel option

- [ ] **SearchResults.vue**
  - Display matching messages
  - Highlight search terms
  - Click to jump to message in context
  - Show channel and timestamp

- [ ] **services/search.service.js**
  - `searchMessages(query, channelId?)`
  - Debounced search requests

#### 3.5 File & Image Uploads
- [ ] **FileUpload.vue**
  - Drag-and-drop zone
  - File type validation
  - Size limit enforcement
  - Upload progress indicator
  - Preview before sending

- [ ] **MessageList Enhancement**
  - Render image previews inline
  - File download links
  - Image lightbox/modal viewer
  - Support message types: TEXT, IMAGE, FILE

- [ ] **services/upload.service.js**
  - `uploadFile(file, channelId)`
  - Multipart form data handling
  - Progress tracking

#### 3.6 Notifications
- [ ] **Notification System**
  - Browser notification permission request
  - Show notification for new messages when tab inactive
  - Sound notification option
  - Desktop notification with message preview
  - Click notification to open channel

- [ ] **NotificationSettings.vue**
  - Enable/disable notifications
  - Sound on/off
  - Notification preview on/off
  - Per-channel notification settings

#### 3.7 Enhanced UI/UX
- [ ] **Message Actions**
  - Right-click context menu or hover actions
  - Edit message (with edited indicator)
  - Delete message
  - Reply/thread (optional)
  - Copy message text
  - React with emoji (optional)

- [ ] **Loading States**
  - Skeleton loaders for messages
  - Channel list loading state
  - Infinite scroll for message history
  - "Loading more messages..." indicator

- [ ] **Error Handling**
  - Toast/snackbar notifications for errors
  - Retry failed messages
  - Offline mode indicator
  - Graceful degradation

- [ ] **Accessibility**
  - Keyboard navigation
  - ARIA labels
  - Screen reader support
  - Focus management

#### 3.8 Performance Optimizations
- [ ] **Virtual Scrolling**
  - Implement virtual list for large message histories
  - Lazy load images

- [ ] **Caching Strategy**
  - Cache channel list locally
  - Cache recent messages per channel
  - Optimistic UI updates

### Phase 3 Testing Checklist
- [ ] Typing indicators work correctly
- [ ] Read receipts update in real-time
- [ ] Search returns relevant results quickly
- [ ] File uploads succeed with progress feedback
- [ ] Image previews display correctly
- [ ] Notifications appear when app is in background
- [ ] Message actions (edit/delete) work properly
- [ ] App handles offline/online transitions gracefully
- [ ] Performance is smooth with 1000+ messages
- [ ] Accessibility requirements are met

---

## UI/UX Design Guidelines

### Color Scheme
- Define primary, secondary, accent colors
- Dark mode support (optional Phase 3)
- Consistent color usage across components

### Typography
- Font family selection
- Heading hierarchy
- Message text styling
- Timestamp formatting

### Layout
```
┌─────────────────────────────────────────────┐
│  Header (App Title, Search, User Profile)  │
├──────────┬──────────────────────────────────┤
│          │  Channel Header                  │
│ Channel  ├──────────────────────────────────┤
│ List     │                                  │
│          │  Message List                    │
│ - Groups │  (Scrollable)                    │
│ - DMs    │                                  │
│          ├──────────────────────────────────┤
│          │  Message Input                   │
├──────────┴──────────────────────────────────┤
│  Online Users (Collapsible Sidebar)        │
└─────────────────────────────────────────────┘
```

### Responsive Design
- Mobile breakpoint: < 768px
- Tablet breakpoint: 768px - 1024px
- Desktop: > 1024px
- Collapsible sidebar on mobile
- Bottom navigation option for mobile

---

## Development Best Practices

### Code Organization
- One component per file
- Composition API preferred over Options API
- Reusable composables for common logic
  - `useWebSocket()`
  - `useMessages(channelId)`
  - `useAuth()`

### Testing Strategy
- Unit tests for services and composables
- Component tests for UI components
- E2E tests for critical user flows
  - Login flow
  - Send/receive messages
  - Create channel
  - Start DM

### Performance
- Lazy load routes
- Code splitting for large components
- Debounce search and typing events
- Memoize computed properties
- Virtual scrolling for long lists

### Error Handling
- Try-catch blocks in async operations
- User-friendly error messages
- Log errors to console in development
- Error boundary component

---

## Environment Configuration

### Development
```env
VITE_API_BASE_URL=http://localhost:8080
VITE_WS_URL=http://localhost:8080/ws
VITE_MAX_FILE_SIZE=10485760  # 10MB
```

### Production
```env
VITE_API_BASE_URL=https://api.yourdomain.com
VITE_WS_URL=https://api.yourdomain.com/ws
VITE_MAX_FILE_SIZE=10485760
```

---

## Deployment Checklist

- [ ] Build optimization (Vite production build)
- [ ] Environment variables configured
- [ ] WebSocket connection works over HTTPS (WSS)
- [ ] CORS headers configured on backend
- [ ] Static assets optimized (images, fonts)
- [ ] Gzip compression enabled
- [ ] CDN setup for static assets (optional)
- [ ] Analytics integration (optional)
- [ ] Error tracking (e.g., Sentry)

---

## Dependencies Reference

### Core Dependencies
```json
{
  "vue": "^3.3.0",
  "vue-router": "^4.2.0",
  "pinia": "^2.1.0",
  "axios": "^1.4.0",
  "sockjs-client": "^1.6.1",
  "webstomp-client": "^1.2.6"
}
```

### UI Dependencies (Choose one)
```json
{
  "tailwindcss": "^3.3.0",
  // OR
  "vuetify": "^3.3.0",
  // OR
  "element-plus": "^2.3.0"
}
```

### Development Dependencies
```json
{
  "@vitejs/plugin-vue": "^4.2.0",
  "vite": "^4.3.0",
  "@vue/test-utils": "^2.4.0",
  "vitest": "^0.32.0",
  "cypress": "^12.17.0"
}
```

---

## Notes
- Prioritize Phase 1 completion before moving to Phase 2
- Each phase should be fully tested before proceeding
- Keep components small and focused on single responsibility
- Document complex logic with comments
- Consider accessibility from the start, not as an afterthought
