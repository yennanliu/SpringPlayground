# Frontend Claude Instructions

This file provides guidance specific to the frontend Vue.js application.

## Project Context

This is the frontend for a Slack-like chat application built with:
- Vue 3 (Composition API)
- Vite
- Pinia (state management)
- Vue Router
- SockJS + STOMP for WebSocket communication
- Axios for HTTP requests

## Implementation Progress Tracking

**IMPORTANT**: Always refer to the root-level TODO file for implementation tracking:

📋 **Main TODO**: [`../../FRONTEND_TODO.md`](../../FRONTEND_TODO.md)

This file contains:
- Phase 1 (MVP) - ✅ COMPLETE
- Phase 2 (Core Features) - ✅ COMPLETE
- Phase 3 (Enhanced Features) - 🔄 PENDING

### Current Status

#### ✅ Phase 1 Complete
All Phase 1 tasks have been implemented and documented in `PHASE1_COMPLETE.md`:
- Basic WebSocket connection
- Single group chat room
- Send/receive text messages
- Simple Vue.js UI
- Pinia state management
- Basic authentication (username only)

#### ✅ Phase 2 Complete
All Phase 2 tasks have been implemented and documented in `PHASE2_COMPLETE.md`:
- Multi-channel support (group channels + DMs)
- Channel creation with CreateChannelModal
- Channel switching with WebSocket subscription management
- UserList sidebar with online users
- Typing indicators with TypingIndicator component
- Enhanced state management (channels store, typing store)
- Unread count tracking
- Three-column layout (Channels | Chat | Users)

#### 🔄 Phase 3 - Next Steps
Refer to [`../../FRONTEND_TODO.md`](../../FRONTEND_TODO.md) Phase 3 section for upcoming features:
- Read receipts
- User profiles with avatars
- Message search functionality
- File & image uploads
- Browser notifications
- Message actions (edit/delete/reply)
- Enhanced UI/UX improvements
- Performance optimizations

## Development Workflow

### Before Starting New Features

1. **Check TODO file**: Review [`../../FRONTEND_TODO.md`](../../FRONTEND_TODO.md) for the next priority task
2. **Read phase documentation**: Check `PHASE1_COMPLETE.md` and `PHASE2_COMPLETE.md` for existing implementations
3. **Understand architecture**: Review `../../DESIGN.md` for system design
4. **Check backend status**: Review `../../BACKEND_TODO.md` for API availability

### When Implementing Features

1. **Follow Vue 3 best practices**: Use Composition API, reactive refs, computed properties
2. **Use existing stores**: Leverage channels, messages, user, websocket, typing stores
3. **Maintain consistency**: Follow existing component patterns and styling
4. **Update documentation**: Document new features in appropriate phase completion files
5. **Test thoroughly**: Test WebSocket connections, state updates, and UI interactions

### Code Organization

```
src/
├── components/          # Reusable Vue components
│   ├── ChannelList.vue
│   ├── CreateChannelModal.vue
│   ├── MessageInput.vue
│   ├── MessageList.vue
│   ├── TypingIndicator.vue
│   └── UserList.vue
├── views/              # Page-level components
│   ├── ChatView.vue
│   └── LoginView.vue
├── stores/             # Pinia state management
│   ├── channels.js
│   ├── messages.js
│   ├── typing.js
│   ├── user.js
│   └── websocket.js
├── services/           # API and WebSocket services
│   ├── chat.service.js
│   └── websocket.service.js
├── router/             # Vue Router configuration
│   └── index.js
├── App.vue             # Root component
└── main.js             # Application entry point
```

## Key Design Decisions

### WebSocket Communication
- Using SockJS for fallback support
- STOMP protocol for message framing
- Subscribe pattern: `/topic/channel/{channelId}`
- Send pattern: `/app/chat/{channelId}`

### State Management Strategy
- **channels store**: All channel-related state (list, current, unread counts)
- **messages store**: Message list per channel
- **user store**: Current user authentication and profile
- **websocket store**: Connection status and errors
- **typing store**: Typing indicator state per channel

### Channel ID Format
- Group channels: `group:{id}` (e.g., `group:general`)
- Direct messages: `dm:{userId1}:{userId2}` (sorted IDs)

### Component Communication
- Props down, events up pattern
- Store for shared state across components
- Composables for reusable logic (future)

## Common Tasks

### Adding a New Component

1. Create component file in `src/components/`
2. Use `<script setup>` for Composition API
3. Import required stores with `useXStore()`
4. Define props with `defineProps()`
5. Define emits with `defineEmits()`
6. Add scoped styles in `<style scoped>`

### Adding a New Store

1. Create store file in `src/stores/`
2. Use `defineStore()` from Pinia
3. Define state as `ref()` or `reactive()`
4. Define getters as `computed()`
5. Define actions as functions
6. Return all state, getters, and actions

### Integrating New API Endpoint

1. Add method to appropriate service file
2. Use axios instance from `chat.service.js`
3. Handle errors with try-catch
4. Return data or throw error
5. Call from component or store action

### Adding WebSocket Event Handler

1. Update `websocket.service.js` if needed
2. Subscribe in ChatView or component
3. Add callback to handle incoming messages
4. Update appropriate store
5. UI updates automatically via reactivity

## Testing Guidelines

### Manual Testing Checklist
- [ ] Login/logout flow works
- [ ] WebSocket connects and shows status
- [ ] Messages send and receive in real-time
- [ ] Channel switching works correctly
- [ ] Typing indicators appear and disappear
- [ ] Unread counts update properly
- [ ] UI is responsive on mobile
- [ ] No console errors

### Multi-User Testing
- Open app in multiple browser tabs/windows
- Use different usernames in each
- Test real-time message delivery
- Test typing indicators across users
- Test channel switching sync

## Troubleshooting

### WebSocket Connection Issues
- Check backend is running on http://localhost:8080
- Verify CORS is enabled on backend
- Check browser console for connection errors
- Verify `.env.development` has correct `VITE_WS_URL`

### State Not Updating
- Ensure using `ref()` or `reactive()` for state
- Check if store actions are being called
- Verify computed properties have correct dependencies
- Use Vue DevTools to inspect store state

### Messages Not Appearing
- Check WebSocket subscription is active
- Verify correct channel ID is being used
- Check message format matches expected structure
- Look for errors in browser console

## Style Guidelines

### CSS Approach
- Scoped styles in components
- Global styles in `App.vue`
- Utility classes for common patterns
- Consistent color scheme (purple gradient theme)

### Color Palette
- Primary: `#667eea` (purple)
- Secondary: `#764ba2` (darker purple)
- Background: `#f5f5f5` (light gray)
- Sidebar: `#2c2f33` (dark gray - Discord-inspired)
- Text: `#333` (dark), `#666` (medium), `#999` (light)
- Success: `#43b581` (green)
- Error: `#f23f42` (red)
- Warning: `#ff9800` (orange)

### Component Styling Patterns
- Use flexbox for layouts
- Border radius: `4px` (small), `8px` (medium), `12px` (large)
- Padding: `8px`, `12px`, `16px`, `24px`
- Transitions: `0.2s` or `0.3s` ease
- Hover effects on interactive elements

## Important Notes

- **Never commit**: `.env`, `dist/`, `node_modules/`
- **Always use**: Environment variables for URLs and secrets
- **Prefer**: Composition API over Options API
- **Remember**: Test on multiple browsers (Chrome, Firefox, Safari)
- **Consider**: Mobile experience (responsive design)
- **Document**: Complex logic and non-obvious code

## Quick Reference Links

- [Vue 3 Docs](https://vuejs.org/)
- [Pinia Docs](https://pinia.vuejs.org/)
- [Vue Router Docs](https://router.vuejs.org/)
- [Vite Docs](https://vitejs.dev/)
- [STOMP.js Docs](https://stomp-js.github.io/stomp-websocket/)

## Getting Help

When stuck:
1. Check the TODO file for context
2. Review existing component implementations
3. Check DESIGN.md for system architecture
4. Look at PHASE1_COMPLETE.md and PHASE2_COMPLETE.md for patterns
5. Use Vue DevTools to inspect state
6. Check browser console for errors

---

**Last Updated**: 2025-11-30
**Current Phase**: Phase 2 Complete, Ready for Phase 3
