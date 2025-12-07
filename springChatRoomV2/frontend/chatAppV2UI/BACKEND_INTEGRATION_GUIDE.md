# Backend Integration Guide

## Current Status

✅ Frontend is **production-ready** for all implemented features
⚠️ **Backend Integration Required** - JWT authentication needs to be integrated

## Backend Requirements Analysis

### Authentication Flow

The backend uses **JWT (JSON Web Token)** authentication with these endpoints:

```
POST /api/auth/register
POST /api/auth/login
```

### Security Configuration

From `SecurityConfig.java`, these endpoints are **permitAll** (no auth required):
- `/api/auth/**` - Registration and login
- `/ws/**` - WebSocket connections
- `/api/users/**` - User management
- `/swagger-ui/**`, `/v3/api-docs/**` - API documentation

All other endpoints (`/api/channels/**`, `/api/messages/**`) require JWT authentication via `Authorization: Bearer <token>` header.

## Frontend Changes Needed

### 1. Update Authentication Service ✅ (Ready to implement)

**File**: `src/services/auth.service.js` (NEW)

```javascript
import { apiClient } from './chat.service'

class AuthService {
  /**
   * Register a new user
   * @param {Object} userData - {username, email, password}
   * @returns {Promise<Object>} {token, user}
   */
  async register(userData) {
    const response = await apiClient.post('/api/auth/register', {
      username: userData.username,
      email: userData.email,
      password: userData.password
    })

    // Store token
    if (response.data.token) {
      localStorage.setItem('authToken', response.data.token)
    }

    return response.data
  }

  /**
   * Login user
   * @param {Object} credentials - {username, password}
   * @returns {Promise<Object>} {token, user}
   */
  async login(credentials) {
    const response = await apiClient.post('/api/auth/login', {
      username: credentials.username,
      password: credentials.password
    })

    // Store token
    if (response.data.token) {
      localStorage.setItem('authToken', response.data.token)
    }

    return response.data
  }

  /**
   * Logout user
   */
  logout() {
    localStorage.removeItem('authToken')
  }

  /**
   * Get stored token
   */
  getToken() {
    return localStorage.setItem('authToken')
  }
}

export default new AuthService()
```

### 2. Update chat.service.js ✅ (Already configured)

**Current implementation** already includes:
```javascript
// Request interceptor adds auth token
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})
```

✅ **No changes needed** - auth token is automatically added to all API requests!

### 3. Update LoginView.vue 🔄 (Needs update)

**Current**: Simple username-only login (no password)
**Needed**: Add password field and call backend authentication API

**Changes required**:
```vue
<template>
  <!-- Add password field -->
  <input
    type="password"
    v-model="password"
    placeholder="Password"
    required
  />

  <!-- Add toggle for register/login -->
  <button @click="toggleMode">
    {{ isLoginMode ? 'Need an account? Register' : 'Have an account? Login' }}
  </button>
</template>

<script setup>
import authService from '../services/auth.service'

const isLoginMode = ref(true)
const password = ref('')

async function handleSubmit() {
  try {
    if (isLoginMode.value) {
      const response = await authService.login({
        username: username.value,
        password: password.value
      })
      userStore.login(response.user)
    } else {
      const response = await authService.register({
        username: username.value,
        email: email.value,
        password: password.value
      })
      userStore.login(response.user)
    }
    router.push('/chat')
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Authentication failed'
  }
}
</script>
```

### 4. Update user.js store 🔄 (Minor update)

**Current**: Generates fake user ID with `Date.now()`
**Needed**: Use real user data from backend response

```javascript
function login(userData) {
  currentUser.value = {
    id: userData.id,  // From backend response
    username: userData.username,
    email: userData.email,
    displayName: userData.displayName || userData.username,
    avatarUrl: userData.avatarUrl || null,
    statusMessage: userData.statusMessage || '',
    isOnline: true
  }
  localStorage.setItem('currentUser', JSON.stringify(currentUser.value))
}
```

### 5. WebSocket Authentication 🔄 (May need update)

**Current**: Sends `userId` in connect headers
**Check**: Does backend WebSocket require JWT token?

If backend requires JWT for WebSocket:
```javascript
// In websocket.service.js
this.client = new Client({
  webSocketFactory: () => new SockJS(wsUrl),
  connectHeaders: {
    userId: userId,
    Authorization: `Bearer ${localStorage.getItem('authToken')}`  // Add this
  },
  // ...
})
```

## Implementation Priority

### Priority 1: Authentication (REQUIRED) 🚨
1. Create `auth.service.js`
2. Update `LoginView.vue` with password field
3. Update `user.js` store to handle backend user data
4. Test registration and login flows

### Priority 2: Test Integration ✅
1. Run backend (`cd ../../backend/ChatAppV2 && ./mvnw spring-boot:run`)
2. Run integration test (`npm run test:integration`)
3. Start frontend (`npm run dev`)
4. Test complete user flow:
   - Register new user
   - Login
   - Create channel
   - Send/receive messages

### Priority 3: Error Handling 🔄
1. Handle 401 Unauthorized (token expired/invalid)
2. Auto-redirect to login on 401
3. Token refresh logic (if backend supports it)
4. WebSocket reconnect with new token

## Backend Integration Checklist

### Prerequisites
- [ ] Backend running on `http://localhost:8080`
- [ ] PostgreSQL database running (port 5432)
- [ ] Redis running (port 6379) - *Optional*
- [ ] Database schema initialized

### Frontend Changes
- [ ] Create `auth.service.js`
- [ ] Update `LoginView.vue` - add password field
- [ ] Update `user.js` store - use backend user data
- [ ] Test WebSocket auth (may need Authorization header)
- [ ] Handle 401 errors and auto-logout

### Testing
- [ ] User registration works
- [ ] User login works
- [ ] JWT token stored in localStorage
- [ ] API requests include `Authorization` header
- [ ] Protected endpoints accessible with token
- [ ] WebSocket connects successfully
- [ ] Messages send/receive in real-time
- [ ] Channel creation/switching works
- [ ] Multi-user scenarios work

## Troubleshooting

### Issue: 403 Forbidden on API calls
**Cause**: No JWT token or invalid token
**Fix**: Ensure user is logged in and token is in localStorage

### Issue: WebSocket connection fails
**Possible causes**:
1. Backend WebSocket requires JWT token in headers
2. CORS issues
3. Backend not running

**Debug**:
```javascript
// Check WebSocket connection in browser console
console.log('WS Status:', wsStore.connectionStatus)
console.log('Token:', localStorage.getItem('authToken'))
```

### Issue: 401 Unauthorized after some time
**Cause**: JWT token expired (default: 24 hours)
**Fix**: Implement token refresh or prompt user to re-login

## Testing Script

Run the integration test script:
```bash
chmod +x scripts/test-backend-integration.sh
./scripts/test-backend-integration.sh
```

This script will:
1. Check backend health
2. Register a test user
3. Login and get JWT token
4. Test protected API endpoints
5. Verify WebSocket endpoint
6. Test channel creation
7. Test message history API

## Next Steps

Once backend integration is complete:
1. **Phase 3 Features** - Implement remaining features:
   - Read receipts
   - Message search
   - File uploads
   - Browser notifications

2. **Production Deployment**:
   - Configure production environment variables
   - Setup HTTPS for WebSocket (wss://)
   - Configure JWT secret in production
   - Setup database migrations

3. **Performance Optimization**:
   - Implement token refresh
   - Add request caching
   - Implement virtual scrolling for large message lists

## API Endpoint Reference

### Authentication
```
POST /api/auth/register
Body: { username, email, password }
Response: { token, user: { id, username, email, ... } }

POST /api/auth/login
Body: { username, password }
Response: { token, user: { id, username, email, ... } }
```

### Users
```
GET /api/users/{id}
GET /api/users/online
PUT /api/users/me
POST /api/users/me/avatar
```

### Channels
```
GET /api/channels (requires JWT)
POST /api/channels/group (requires JWT)
POST /api/channels/direct (requires JWT)
GET /api/channels/{id}/messages (requires JWT)
```

### WebSocket
```
CONNECT /ws
SUBSCRIBE /topic/channel/{channelId}
SEND /app/chat/{channelId}
```

## Current Limitations

1. **Frontend Only Login**: Current implementation uses localStorage only
2. **No Password**: Current login has no password field
3. **Fake User IDs**: Uses `Date.now()` instead of backend IDs
4. **No Token Refresh**: Token expires after 24 hours, no auto-refresh
5. **No 401 Handling**: Doesn't automatically logout on token expiration

All of these will be resolved with proper backend integration!

---

**Status**: Ready for implementation
**Estimated Time**: 2-3 hours for full integration
**Risk Level**: Low - Frontend architecture already supports JWT auth
