# Chat App V2 - Frontend

A modern, feature-rich chat application built with Vue 3, featuring real-time messaging, user profiles, direct messaging, and group channels.

## Features

### Phase 1: MVP (Complete)
- User authentication (username-based)
- Real-time WebSocket messaging (SockJS + STOMP)
- Group channel support
- Message history persistence
- Online user list
- Responsive UI design

### Phase 2: Enhanced Features (Complete)
- Direct messaging (DM) between users
- Channel creation and management
- Unread message badges
- Typing indicators
- Channel switching
- Message timestamps

### Phase 3: Advanced UI/UX (Complete)
- **User Profiles & Avatars**
  - Universal avatar component with 4 size variants
  - Custom avatar upload with validation
  - Profile management (display name, email, status)
  - Online status indicators
  - Click-to-view profiles

- **Message Actions**
  - Edit your own messages (inline editing)
  - Delete messages with confirmation
  - Copy message to clipboard
  - Hover-activated action menu
  - "(edited)" indicator on modified messages

- **Notification System**
  - Toast notifications (success, error, warning, info)
  - Auto-dismiss with manual close option
  - Stacked notifications
  - Smooth animations

- **UI Enhancements**
  - Avatar integration throughout app
  - Confirmation dialogs for destructive actions
  - Polished message bubbles with gradients
  - Responsive design for mobile and desktop

## Technology Stack

### Core
- **Vue 3.5.22** - Progressive JavaScript framework with Composition API
- **Vite 7.1.11** - Next-generation frontend build tool
- **Vue Router 4.6.3** - Official router for Vue.js
- **Pinia 3.0.4** - State management for Vue

### Communication
- **@stomp/stompjs 7.2.1** - STOMP protocol over WebSocket
- **sockjs-client 1.6.1** - WebSocket fallback library
- **Axios 1.13.2** - HTTP client for REST API calls

### Development
- **Node.js 20.19.0+ or 22.12.0+** - JavaScript runtime
- **Vite Plugin Vue DevTools 8.0.3** - Vue.js debugging tools

## Project Structure

```
chatAppV2UI/
├── src/
│   ├── assets/           # Static assets (CSS, images)
│   ├── components/       # Reusable Vue components
│   │   ├── Avatar.vue                  # Universal avatar component
│   │   ├── ChannelList.vue             # Channel sidebar
│   │   ├── ConfirmDialog.vue           # Confirmation dialogs
│   │   ├── CreateChannelModal.vue      # Channel creation
│   │   ├── MessageActions.vue          # Message action menu
│   │   ├── MessageInput.vue            # Message composition
│   │   ├── MessageList.vue             # Message display
│   │   ├── ToastNotification.vue       # Toast notifications
│   │   ├── TypingIndicator.vue         # Typing status
│   │   ├── UserList.vue                # Online users sidebar
│   │   └── UserProfileModal.vue        # Profile management
│   ├── router/           # Vue Router configuration
│   │   └── index.js
│   ├── services/         # Business logic and API services
│   │   ├── chat.service.js       # Chat API calls
│   │   └── websocket.service.js  # WebSocket management
│   ├── stores/           # Pinia state management
│   │   ├── channels.js     # Channel state
│   │   ├── messages.js     # Message state
│   │   ├── user.js         # User state
│   │   └── websocket.js    # WebSocket state
│   ├── views/            # Page-level components
│   │   ├── ChatView.vue
│   │   └── LoginView.vue
│   ├── App.vue           # Root component
│   └── main.js           # Application entry point
├── public/               # Public static assets
├── .claude/              # Claude Code configuration
│   └── CLAUDE.md         # Development guidelines
├── DESIGN.md             # System design documentation
├── FRONTEND_TODO.md      # Feature roadmap
├── PHASE1_COMPLETE.md    # Phase 1 documentation
├── PHASE2_COMPLETE.md    # Phase 2 documentation
├── PHASE3_COMPLETE.md    # Phase 3 documentation
├── package.json          # Dependencies and scripts
├── vite.config.js        # Vite configuration
└── README.md             # This file
```

## Prerequisites

- Node.js 20.19.0+ or 22.12.0+
- npm (comes with Node.js)
- Backend server running on http://localhost:8080 (for full functionality)

## Getting Started

### 1. Install Dependencies

```bash
npm install
```

### 2. Run Development Server

```bash
npm run dev
```

The application will start at `http://localhost:5173` (default Vite port).

### 3. Login

- Navigate to `http://localhost:5173` (automatically redirects to login)
- Enter any username (3-20 characters)
- Examples: "alice", "bob", "john123"

**Note:** Without the backend server, you can still test the UI, but real-time messaging won't work.

## Available Scripts

### Development

```bash
npm run dev
```
Starts the development server with hot module replacement (HMR).
- URL: http://localhost:5173
- Auto-reloads on file changes
- Shows compile errors in browser and console

### Production Build

```bash
npm run build
```
Creates an optimized production build in the `dist/` directory.
- Minified and tree-shaken code
- Optimized assets
- Source maps excluded (configurable)

### Build Statistics (Latest)
```
✓ 203 modules transformed
dist/assets/index-BxsAwCkh.css   28.35 kB │ gzip:  4.89 kB
dist/assets/index-Bizju-l4.js   246.96 kB │ gzip: 87.34 kB
✓ built in 647ms
```

### Preview Production Build

```bash
npm run preview
```
Serves the production build locally for testing.
- URL: http://localhost:4173 (default)
- Tests production-optimized code
- Does NOT include HMR

## Development Workflow

### 1. IDE Setup

**Recommended:** [VS Code](https://code.visualstudio.com/)

**Extensions:**
- [Vue - Official](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (disable Vetur if installed)
- [ESLint](https://marketplace.visualstudio.com/items?itemName=dbaeumer.vscode-eslint) (optional)
- [Prettier](https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode) (optional)

### 2. Browser Setup

**Chromium-based browsers (Chrome, Edge, Brave):**
- [Vue.js devtools](https://chromewebstore.google.com/detail/vuejs-devtools/nhdogjmejiglipccpnnnanhbledajbpd)
- [Enable Custom Object Formatter](http://bit.ly/object-formatters) in DevTools Settings

**Firefox:**
- [Vue.js devtools](https://addons.mozilla.org/en-US/firefox/addon/vue-js-devtools/)
- [Enable Custom Object Formatter](https://fxdx.dev/firefox-devtools-custom-object-formatters/)

### 3. File Organization

When creating new components or features:

1. **Components** (`src/components/`) - Reusable UI components
   - Use PascalCase for filenames (e.g., `MyComponent.vue`)
   - Keep components focused and single-responsibility
   - Use `<script setup>` syntax for Composition API

2. **Views** (`src/views/`) - Page-level components
   - Route-specific components
   - Compose smaller components

3. **Stores** (`src/stores/`) - State management
   - One store per domain (users, messages, channels)
   - Use Composition API style with Pinia

4. **Services** (`src/services/`) - Business logic
   - API calls, WebSocket management
   - Keep UI logic in components, business logic in services

## Debugging

### Browser DevTools

**Vue DevTools:**
- Inspect component hierarchy
- View component state and props
- Track Pinia store state
- Monitor custom events
- Time-travel debugging

**Console Logging:**
The app includes extensive console logging:
- WebSocket connection status
- Incoming/outgoing messages
- Channel switches
- User actions

**Network Tab:**
- Monitor WebSocket frames
- Check HTTP API calls
- Inspect request/response payloads

### Common Issues

**Issue: WebSocket connection fails**
- Ensure backend server is running on http://localhost:8080
- Check CORS configuration in backend
- Verify WebSocket endpoint: `/ws`

**Issue: Messages not appearing**
- Check WebSocket connection status (header indicator)
- Verify you're subscribed to the correct channel
- Check console for errors

**Issue: Hot reload not working**
- Restart dev server: `Ctrl+C` then `npm run dev`
- Clear browser cache
- Check for syntax errors in Vue files

## Testing

### Manual Testing

1. **Login Flow:**
   - Enter username → Redirects to chat view
   - Verify WebSocket connects (green "Connected" status)

2. **Messaging:**
   - Send message in general channel
   - Verify message appears in message list
   - Check timestamp formatting

3. **Channels:**
   - Create new group channel
   - Switch between channels
   - Verify message history loads

4. **Direct Messages:**
   - Click user in user list
   - Select "Send Direct Message"
   - Verify DM channel created

5. **User Profiles:**
   - Click avatar in header (own profile)
   - Edit display name, email, status
   - Upload avatar image
   - Verify changes persist (localStorage)
   - Click user in user list to view their profile

6. **Message Actions:**
   - Hover over your own message → See edit/copy/delete buttons
   - Edit message → Verify "(edited)" indicator appears
   - Copy message → Verify clipboard contains text
   - Delete message → Confirm in dialog → Verify message removed

7. **Notifications:**
   - Perform actions → Verify toast notifications appear
   - Check auto-dismiss after 3 seconds
   - Click X to manually close

### Testing Without Backend

You can test the UI components without the backend:
- Login flow works (frontend-only validation)
- UI components render correctly
- State management (Pinia stores)
- Routing between login and chat views
- Message input and display (local only)
- Profile editing (localStorage)

**Limited functionality:**
- No real-time messaging
- No message persistence
- No multi-user simulation
- WebSocket shows "Disconnected" status

## Configuration

### Vite Configuration

Edit `vite.config.js` to customize:

```javascript
export default defineConfig({
  server: {
    port: 5173,        // Dev server port
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

### Environment Variables

Create `.env` files for different environments:

**.env.development:**
```
VITE_API_URL=http://localhost:8080
VITE_WS_URL=http://localhost:8080/ws
```

**.env.production:**
```
VITE_API_URL=https://api.yourdomain.com
VITE_WS_URL=https://api.yourdomain.com/ws
```

Access in code:
```javascript
const apiUrl = import.meta.env.VITE_API_URL
```

## Architecture Overview

### State Management (Pinia)

- **userStore** - Current user, authentication, profile
- **messagesStore** - Messages for current channel, message actions
- **channelsStore** - Channel list, current channel, unread counts
- **websocketStore** - WebSocket connection state

### Component Communication

- **Props** - Parent to child data flow
- **Events** - Child to parent communication
- **Pinia Stores** - Global state sharing
- **Provide/Inject** - Deep component tree data passing (when needed)

### WebSocket Flow

```
User sends message
     ↓
MessageInput emits event
     ↓
ChatView calls websocketService.sendMessage()
     ↓
STOMP sends to /app/chat/{channelId}
     ↓
Backend processes
     ↓
Backend broadcasts to /topic/channel/{channelId}
     ↓
websocketService receives message
     ↓
Adds to messagesStore
     ↓
MessageList reactively updates
```

## Performance Considerations

### Current Optimizations
- Vue 3 Composition API (smaller bundle, better tree-shaking)
- Vite for fast HMR and optimized builds
- Computed properties for reactive filtering/sorting
- Event delegation for message actions
- Teleported modals (proper z-index, no layout thrashing)

### Future Optimizations (Not Yet Implemented)
- Virtual scrolling for large message lists
- Lazy loading images
- Message pagination
- Code splitting by route
- Service worker for offline support

## Browser Compatibility

### Supported Browsers
- ✅ Chrome/Edge 90+ (latest recommended)
- ✅ Firefox 88+ (latest recommended)
- ✅ Safari 14+ (latest recommended)
- ✅ Mobile browsers (iOS Safari 14+, Chrome Mobile)

### Required Browser Features
- ES6+ JavaScript
- WebSocket API
- Clipboard API (for copy message)
- CSS Grid and Flexbox
- CSS Custom Properties

## Deployment

### Build for Production

```bash
npm run build
```

Output: `dist/` directory

### Deploy to Static Hosting

**Netlify:**
```bash
npm run build
netlify deploy --dir=dist --prod
```

**Vercel:**
```bash
npm run build
vercel --prod
```

**Nginx:**
```nginx
server {
    listen 80;
    server_name yourdomain.com;
    root /path/to/dist;

    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

### Environment Configuration

Ensure production environment variables are set:
- Backend API URL
- WebSocket URL
- Any feature flags

## Documentation

- **DESIGN.md** - Overall system design and architecture
- **FRONTEND_TODO.md** - Feature roadmap and progress tracking
- **PHASE1_COMPLETE.md** - Phase 1 MVP features documentation
- **PHASE2_COMPLETE.md** - Phase 2 enhanced features documentation
- **PHASE3_COMPLETE.md** - Phase 3 advanced UI/UX documentation
- **.claude/CLAUDE.md** - Development guidelines for Claude Code

## Contributing

### Code Style

- Use Vue 3 Composition API (`<script setup>`)
- Follow Vue.js style guide
- Keep components under 300 lines when possible
- Extract reusable logic into composables
- Use TypeScript-style JSDoc comments for better IDE support

### Git Workflow

1. Work on feature branches
2. Keep commits atomic and descriptive
3. Build before committing: `npm run build`
4. Test manually before pushing

## Troubleshooting

### Port Already in Use

```bash
# Kill process on port 5173 (macOS/Linux)
lsof -ti:5173 | xargs kill -9

# Change port in vite.config.js or use --port flag
npm run dev -- --port 3000
```

### Build Fails

```bash
# Clear node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
npm run build
```

### WebSocket Connection Issues

1. Verify backend is running: `curl http://localhost:8080/ws`
2. Check CORS configuration in backend
3. Check browser console for WebSocket errors
4. Try different browser (test CORS/WebSocket support)

## License

Private project - All rights reserved

## Support

For issues or questions:
1. Check existing documentation (DESIGN.md, PHASE*_COMPLETE.md)
2. Review browser console for errors
3. Check Vue DevTools for component state
4. Verify backend server is running and accessible

---

**Last Updated:** 2025-11-30
**Version:** 1.0.0 (Phase 3 Complete)
**Status:** Production Ready
