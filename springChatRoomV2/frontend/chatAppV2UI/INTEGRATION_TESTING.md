# Frontend-Backend Integration Testing Guide

Complete guide for testing the Chat App V2 frontend with the Spring Boot backend.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Manual Testing Guide](#manual-testing-guide)
- [Automated Testing Scripts](#automated-testing-scripts)
- [Testing Checklist](#testing-checklist)
- [Common Issues](#common-issues)
- [CI/CD Integration](#cicd-integration)

## Prerequisites

### Backend Requirements

1. **Spring Boot Backend Running**
   ```bash
   cd ../../backend/ChatAppV2
   ./mvnw spring-boot:run
   ```
   - Backend should be running on `http://localhost:8080`
   - WebSocket endpoint: `http://localhost:8080/ws`

2. **Database Setup**
   - PostgreSQL running (if configured)
   - Or H2 in-memory database (default)

3. **Verify Backend Health**
   ```bash
   curl http://localhost:8080/actuator/health
   # Expected: {"status":"UP"}
   ```

### Frontend Requirements

1. **Dependencies Installed**
   ```bash
   npm install
   ```

2. **Environment Variables**
   ```bash
   # Create .env.local file
   VITE_API_URL=http://localhost:8080
   VITE_WS_URL=http://localhost:8080/ws
   ```

## Setup

### 1. Start Backend

```bash
# Terminal 1: Start Spring Boot backend
cd backend/ChatAppV2
./mvnw spring-boot:run

# Wait for startup message:
# Started ChatAppV2Application in X.XXX seconds
```

### 2. Start Frontend

```bash
# Terminal 2: Start Vue.js frontend
cd frontend/chatAppV2UI
npm run dev

# Frontend running at: http://localhost:5173
```

### 3. Verify Connection

Open browser to `http://localhost:5173` and check:
- ✅ Login page loads
- ✅ No console errors
- ✅ DevTools Network tab shows no failed requests

## Manual Testing Guide

### Test Session 1: Authentication & Connection

#### 1.1 User Login
**Steps:**
1. Open `http://localhost:5173`
2. Enter username: `alice` (3-20 characters)
3. Click "Login" button

**Expected Results:**
- ✅ Redirected to chat view
- ✅ Header shows "Connected" status (green)
- ✅ Username displayed in header
- ✅ Avatar shown in header
- ✅ Default "general" channel visible

**Verification:**
```javascript
// Open browser console
localStorage.getItem('currentUser')
// Should return: {"id":"...","username":"alice",...}
```

#### 1.2 WebSocket Connection
**Steps:**
1. After login, open Browser DevTools → Network tab
2. Filter by "WS" (WebSocket)
3. Look for connection to `ws://localhost:8080/ws`

**Expected Results:**
- ✅ WebSocket connection established (Status 101)
- ✅ No disconnection errors
- ✅ Header shows "Connected" in green

**Troubleshooting:**
```bash
# If connection fails, check backend logs:
tail -f backend/ChatAppV2/logs/application.log | grep WebSocket
```

### Test Session 2: Messaging

#### 2.1 Send Message
**Steps:**
1. Login as `alice`
2. Type message in input: "Hello from Alice"
3. Press Enter or click Send

**Expected Results:**
- ✅ Message appears in message list
- ✅ Message shows sender name "alice"
- ✅ Message shows timestamp
- ✅ Message bubble has gradient background (own message)
- ✅ Avatar appears next to message

**Backend Verification:**
```bash
# Check backend logs for message
tail -f backend/ChatAppV2/logs/application.log | grep "Message received"
```

#### 2.2 Multi-User Chat
**Steps:**
1. Open 2nd browser window (incognito mode)
2. Login as `bob`
3. In Alice's window, send: "Hi Bob!"
4. Check Bob's window

**Expected Results:**
- ✅ Bob receives Alice's message
- ✅ Message shows "alice" as sender
- ✅ Message has different styling (not own message)
- ✅ Timestamp matches send time

#### 2.3 Message Actions
**Steps:**
1. Hover over your own message
2. Click "Edit" button
3. Change text to "Hello World (edited)"
4. Press Enter

**Expected Results:**
- ✅ Message content updates
- ✅ "(edited)" indicator appears
- ✅ Hover shows edit/copy/delete buttons

**Copy Test:**
1. Hover over message
2. Click "Copy" button
3. Paste in text editor

**Expected:** Message text copied to clipboard

**Delete Test:**
1. Hover over message
2. Click "Delete" button
3. Confirm in dialog

**Expected:** Message removed from list

### Test Session 3: Channels

#### 3.1 Create Channel
**Steps:**
1. Click "+" button in channel list
2. Enter channel name: "tech-talk"
3. Click "Create"

**Expected Results:**
- ✅ New channel appears in list
- ✅ Switches to new channel
- ✅ Empty message list shown
- ✅ Channel name in header

**Backend Verification:**
```bash
# Check backend for channel creation
curl http://localhost:8080/api/channels | jq
```

#### 3.2 Switch Channels
**Steps:**
1. Create 2 channels: "tech" and "random"
2. Send message in "tech"
3. Switch to "random"
4. Switch back to "tech"

**Expected Results:**
- ✅ Messages persist when switching
- ✅ No duplicate messages
- ✅ Message history loads correctly
- ✅ Unread count updates correctly

#### 3.3 Unread Badges
**Steps:**
1. Open 2 browsers (Alice and Bob)
2. Both join "general" channel
3. Alice switches to different channel
4. Bob sends message in "general"
5. Check Alice's channel list

**Expected Results:**
- ✅ "general" channel shows unread badge
- ✅ Badge shows count "1"
- ✅ Badge clears when switching back
- ✅ Badge is red/prominent

### Test Session 4: Direct Messages

#### 4.1 Start DM
**Steps:**
1. Login as `alice`
2. Click on user "bob" in user list
3. Click "Send Direct Message"

**Expected Results:**
- ✅ DM channel created: "Direct: bob"
- ✅ Switches to DM channel
- ✅ Shows under "Direct Messages" section
- ✅ Only Alice and Bob see this channel

#### 4.2 DM Privacy
**Steps:**
1. Login as 3rd user `charlie`
2. Check channel list

**Expected Results:**
- ✅ Charlie does NOT see Alice-Bob DM
- ✅ Only participants see DM channels

### Test Session 5: User Profiles

#### 5.1 View Own Profile
**Steps:**
1. Click avatar in header
2. Profile modal opens

**Expected Results:**
- ✅ Shows username
- ✅ Shows email field (editable)
- ✅ Shows display name field (editable)
- ✅ Shows status message field (editable)
- ✅ Shows avatar upload button

#### 5.2 Edit Profile
**Steps:**
1. Open profile modal
2. Update display name: "Alice Smith"
3. Update email: "alice@example.com"
4. Update status: "Available"
5. Click "Save"

**Expected Results:**
- ✅ Success toast notification
- ✅ Profile saved to localStorage
- ✅ Changes visible on next profile open

#### 5.3 Upload Avatar
**Steps:**
1. Open profile modal
2. Click "Choose File"
3. Select image (< 5MB)
4. Click "Save"

**Expected Results:**
- ✅ Image preview shown
- ✅ Avatar updates in header
- ✅ Avatar persists after refresh

### Test Session 6: Typing Indicators

#### 6.1 Typing Notification
**Steps:**
1. Open 2 browsers (Alice and Bob)
2. Both in same channel
3. Alice starts typing
4. Check Bob's view

**Expected Results:**
- ✅ Bob sees "alice is typing..." below message input
- ✅ Indicator disappears after 3 seconds
- ✅ Multiple users show: "alice, bob are typing..."

### Test Session 7: Online Status

#### 7.1 User List
**Steps:**
1. Login as multiple users
2. Check user list sidebar

**Expected Results:**
- ✅ All logged-in users shown
- ✅ Green dot for online users
- ✅ Gray dot for offline users
- ✅ User count accurate

#### 7.2 Disconnect Handling
**Steps:**
1. Login as `alice`
2. Close browser tab
3. Check user list in other browser

**Expected Results:**
- ✅ Alice marked as offline after timeout
- ✅ No error messages in console

## Automated Testing Scripts

### Test Script 1: Health Check

Create `scripts/health-check.js`:

```javascript
#!/usr/bin/env node

const http = require('http');

const tests = {
  backend: { url: 'http://localhost:8080/actuator/health', name: 'Backend API' },
  frontend: { url: 'http://localhost:5173', name: 'Frontend Server' }
};

async function checkHealth(url) {
  return new Promise((resolve) => {
    http.get(url, (res) => {
      resolve(res.statusCode === 200);
    }).on('error', () => {
      resolve(false);
    });
  });
}

async function runHealthChecks() {
  console.log('🏥 Running Health Checks...\n');

  for (const [key, test] of Object.entries(tests)) {
    const healthy = await checkHealth(test.url);
    const icon = healthy ? '✅' : '❌';
    const status = healthy ? 'UP' : 'DOWN';
    console.log(`${icon} ${test.name}: ${status}`);
  }
}

runHealthChecks();
```

**Usage:**
```bash
node scripts/health-check.js
```

### Test Script 2: WebSocket Connection Test

Create `scripts/test-websocket.js`:

```javascript
#!/usr/bin/env node

const WebSocket = require('ws');
const SockJS = require('sockjs-client');
const Stomp = require('@stomp/stompjs');

async function testWebSocket() {
  console.log('🔌 Testing WebSocket Connection...\n');

  return new Promise((resolve, reject) => {
    // Configure STOMP client
    const client = new Stomp.Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,

      onConnect: () => {
        console.log('✅ WebSocket connected successfully');
        console.log('✅ STOMP protocol established');

        // Subscribe to a channel
        const subscription = client.subscribe('/topic/channel/group:general', (message) => {
          console.log('✅ Subscription working');
          console.log('📩 Received:', message.body);
        });

        // Send test message
        client.publish({
          destination: '/app/chat/group:general',
          body: JSON.stringify({
            content: 'Test message from automation',
            senderId: 'test-user',
            senderName: 'Test User',
            timestamp: new Date().toISOString()
          })
        });

        console.log('📤 Test message sent');

        // Cleanup after 2 seconds
        setTimeout(() => {
          subscription.unsubscribe();
          client.deactivate();
          console.log('\n✅ WebSocket test completed successfully');
          resolve(true);
        }, 2000);
      },

      onStompError: (frame) => {
        console.error('❌ STOMP error:', frame.headers['message']);
        reject(new Error(frame.headers['message']));
      },

      onWebSocketError: (error) => {
        console.error('❌ WebSocket error:', error);
        reject(error);
      }
    });

    client.activate();
  });
}

testWebSocket().catch((err) => {
  console.error('❌ Test failed:', err.message);
  process.exit(1);
});
```

**Usage:**
```bash
npm install ws sockjs-client @stomp/stompjs
node scripts/test-websocket.js
```

### Test Script 3: API Integration Test

Create `scripts/test-api.js`:

```javascript
#!/usr/bin/env node

const axios = require('axios');

const API_BASE = 'http://localhost:8080/api';

const tests = [
  {
    name: 'Create User',
    method: 'POST',
    url: `${API_BASE}/users`,
    data: { username: 'testuser', email: 'test@example.com' }
  },
  {
    name: 'Get Channels',
    method: 'GET',
    url: `${API_BASE}/channels`
  },
  {
    name: 'Create Channel',
    method: 'POST',
    url: `${API_BASE}/channels/group`,
    data: { name: 'test-channel', memberIds: [] }
  },
  {
    name: 'Send Message',
    method: 'POST',
    url: `${API_BASE}/messages`,
    data: {
      content: 'Test message',
      channelId: 'group:general',
      senderId: 'testuser'
    }
  },
  {
    name: 'Get Message History',
    method: 'GET',
    url: `${API_BASE}/channels/group:general/messages?page=0&size=50`
  }
];

async function runApiTests() {
  console.log('🧪 Running API Integration Tests...\n');

  let passed = 0;
  let failed = 0;

  for (const test of tests) {
    try {
      const config = {
        method: test.method,
        url: test.url,
        data: test.data,
        timeout: 5000
      };

      const response = await axios(config);

      if (response.status >= 200 && response.status < 300) {
        console.log(`✅ ${test.name}: PASSED (${response.status})`);
        passed++;
      } else {
        console.log(`⚠️  ${test.name}: UNEXPECTED STATUS (${response.status})`);
        failed++;
      }
    } catch (error) {
      if (error.code === 'ECONNREFUSED') {
        console.log(`❌ ${test.name}: FAILED - Backend not running`);
      } else if (error.response) {
        console.log(`❌ ${test.name}: FAILED (${error.response.status})`);
      } else {
        console.log(`❌ ${test.name}: FAILED - ${error.message}`);
      }
      failed++;
    }
  }

  console.log(`\n📊 Results: ${passed} passed, ${failed} failed`);
  process.exit(failed > 0 ? 1 : 0);
}

runApiTests();
```

**Usage:**
```bash
npm install axios
node scripts/test-api.js
```

### Test Script 4: Complete Integration Test

Create `scripts/integration-test.sh`:

```bash
#!/bin/bash

echo "🚀 Starting Complete Integration Test..."
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test 1: Check if backend is running
echo "📋 Test 1: Backend Health Check"
if curl -s http://localhost:8080/actuator/health | grep -q "UP"; then
    echo -e "${GREEN}✅ Backend is running${NC}"
else
    echo -e "${RED}❌ Backend is not running${NC}"
    echo "Start backend with: cd backend/ChatAppV2 && ./mvnw spring-boot:run"
    exit 1
fi
echo ""

# Test 2: Check if frontend is running
echo "📋 Test 2: Frontend Health Check"
if curl -s http://localhost:5173 > /dev/null; then
    echo -e "${GREEN}✅ Frontend is running${NC}"
else
    echo -e "${RED}❌ Frontend is not running${NC}"
    echo "Start frontend with: npm run dev"
    exit 1
fi
echo ""

# Test 3: Run API tests
echo "📋 Test 3: API Integration Tests"
if node scripts/test-api.js; then
    echo -e "${GREEN}✅ API tests passed${NC}"
else
    echo -e "${RED}❌ API tests failed${NC}"
    exit 1
fi
echo ""

# Test 4: Run WebSocket test
echo "📋 Test 4: WebSocket Connection Test"
if node scripts/test-websocket.js; then
    echo -e "${GREEN}✅ WebSocket test passed${NC}"
else
    echo -e "${RED}❌ WebSocket test failed${NC}"
    exit 1
fi
echo ""

# Test 5: Run frontend unit tests
echo "📋 Test 5: Frontend Unit Tests"
cd frontend/chatAppV2UI
if npm test -- --run > /dev/null 2>&1; then
    echo -e "${GREEN}✅ Frontend tests passed${NC}"
else
    echo -e "${RED}❌ Frontend tests failed${NC}"
    exit 1
fi
cd ../..
echo ""

echo -e "${GREEN}🎉 All integration tests passed!${NC}"
```

**Usage:**
```bash
chmod +x scripts/integration-test.sh
./scripts/integration-test.sh
```

### Test Script 5: E2E Automation with Playwright

Create `tests/e2e/chat.spec.js`:

```javascript
// npm install -D @playwright/test
const { test, expect } = require('@playwright/test');

test.describe('Chat Application E2E', () => {
  test('should login and send message', async ({ page }) => {
    // Navigate to app
    await page.goto('http://localhost:5173');

    // Login
    await page.fill('input[name="username"]', 'alice');
    await page.click('button:has-text("Login")');

    // Wait for chat view
    await expect(page.locator('.chat-view')).toBeVisible();

    // Check connection status
    await expect(page.locator('.connection-status')).toContainText('Connected');

    // Send message
    await page.fill('.message-input input', 'Hello World!');
    await page.press('.message-input input', 'Enter');

    // Verify message appears
    await expect(page.locator('.message-content:has-text("Hello World!")')).toBeVisible();
  });

  test('should create new channel', async ({ page }) => {
    await page.goto('http://localhost:5173');
    await page.fill('input[name="username"]', 'bob');
    await page.click('button:has-text("Login")');

    // Create channel
    await page.click('.channel-list button:has-text("+")');
    await page.fill('input[name="channelName"]', 'test-channel');
    await page.click('button:has-text("Create")');

    // Verify channel appears
    await expect(page.locator('.channel-item:has-text("test-channel")')).toBeVisible();
  });

  test('should show typing indicator', async ({ page, context }) => {
    // User 1: Alice
    await page.goto('http://localhost:5173');
    await page.fill('input[name="username"]', 'alice');
    await page.click('button:has-text("Login")');

    // User 2: Bob in new tab
    const bobPage = await context.newPage();
    await bobPage.goto('http://localhost:5173');
    await bobPage.fill('input[name="username"]', 'bob');
    await bobPage.click('button:has-text("Login")');

    // Alice starts typing
    await page.fill('.message-input input', 'Test');

    // Bob should see typing indicator
    await expect(bobPage.locator('.typing-indicator:has-text("alice is typing")')).toBeVisible();
  });
});
```

**Setup Playwright:**
```bash
npm install -D @playwright/test
npx playwright install
```

**Usage:**
```bash
npx playwright test
npx playwright test --headed  # With browser visible
npx playwright test --debug   # Debug mode
```

## Testing Checklist

### Pre-Test Setup
- [ ] Backend running on port 8080
- [ ] Frontend running on port 5173
- [ ] Database accessible
- [ ] No console errors on page load

### Authentication
- [ ] Login with valid username
- [ ] Login validation (min/max length)
- [ ] Logout clears session
- [ ] Auto-login from localStorage

### Messaging
- [ ] Send text message
- [ ] Receive message from other user
- [ ] Edit own message
- [ ] Delete own message
- [ ] Copy message to clipboard
- [ ] Message timestamps correct
- [ ] Edited indicator shows

### Channels
- [ ] Create new channel
- [ ] Switch between channels
- [ ] Delete channel
- [ ] Unread badge appears
- [ ] Unread badge clears on view
- [ ] Message history persists

### Direct Messages
- [ ] Create DM from user list
- [ ] DM only visible to participants
- [ ] DM persists across sessions

### User Profiles
- [ ] View own profile
- [ ] Edit profile fields
- [ ] Upload avatar image
- [ ] Profile persists on refresh

### Real-time Features
- [ ] WebSocket connects on login
- [ ] Messages arrive in real-time
- [ ] Typing indicators work
- [ ] Online status updates
- [ ] Connection status shown

### Error Handling
- [ ] Backend offline shows error
- [ ] WebSocket disconnect recovers
- [ ] Invalid input validation
- [ ] Toast notifications show

### Performance
- [ ] Page loads in < 3 seconds
- [ ] Messages send in < 500ms
- [ ] No memory leaks
- [ ] Smooth scrolling

## Common Issues

### Issue 1: WebSocket Connection Fails

**Symptoms:**
- "Disconnected" status in header
- Messages don't send
- Console shows WebSocket errors

**Solutions:**
```bash
# Check backend WebSocket endpoint
curl -I http://localhost:8080/ws

# Check CORS configuration in backend
# src/main/resources/application.properties
spring.web.cors.allowed-origins=http://localhost:5173

# Restart backend
cd backend/ChatAppV2
./mvnw spring-boot:run
```

### Issue 2: CORS Errors

**Symptoms:**
- Network tab shows CORS errors
- API calls fail with 403

**Solutions:**
```java
// backend: Add CORS configuration
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:5173")
            .allowedMethods("*")
            .allowCredentials(true);
    }
}
```

### Issue 3: Messages Not Persisting

**Symptoms:**
- Messages disappear on refresh
- Message history doesn't load

**Solutions:**
```bash
# Check database connection
# backend/src/main/resources/application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/chatdb

# Check message persistence in backend logs
tail -f backend/ChatAppV2/logs/application.log | grep "Saving message"
```

### Issue 4: Slow Performance

**Symptoms:**
- Messages take > 1 second to send
- UI feels sluggish

**Solutions:**
```bash
# Check backend performance
curl -w "@curl-format.txt" -o /dev/null -s http://localhost:8080/api/channels

# curl-format.txt:
# time_total: %{time_total}s

# Optimize frontend build
npm run build
npm run preview  # Test production build
```

## CI/CD Integration

### GitHub Actions Example

Create `.github/workflows/integration-test.yml`:

```yaml
name: Integration Tests

on: [push, pull_request]

jobs:
  integration-test:
    runs-on: ubuntu-latest

    services:
      postgres:
        image: postgres:14
        env:
          POSTGRES_PASSWORD: postgres
          POSTGRES_DB: chatdb
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
        ports:
          - 5432:5432

    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Set up Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '20'

      - name: Start Backend
        run: |
          cd backend/ChatAppV2
          ./mvnw spring-boot:run &
          sleep 30  # Wait for backend to start

      - name: Install Frontend Dependencies
        run: |
          cd frontend/chatAppV2UI
          npm install

      - name: Start Frontend
        run: |
          cd frontend/chatAppV2UI
          npm run dev &
          sleep 10  # Wait for frontend to start

      - name: Run Health Checks
        run: node scripts/health-check.js

      - name: Run API Tests
        run: node scripts/test-api.js

      - name: Run WebSocket Tests
        run: node scripts/test-websocket.js

      - name: Run E2E Tests
        run: |
          cd frontend/chatAppV2UI
          npx playwright test

      - name: Upload Test Results
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: test-results
          path: frontend/chatAppV2UI/playwright-report/
```

## Best Practices

1. **Always test with backend running** - Frontend alone has limited functionality
2. **Use multiple browsers** - Test as different users simultaneously
3. **Test error scenarios** - Disconnect backend, invalid input, etc.
4. **Monitor console** - Watch for errors and warnings
5. **Check network tab** - Verify API calls and WebSocket frames
6. **Test on different devices** - Desktop, tablet, mobile
7. **Clear localStorage** - Start fresh to avoid stale data
8. **Document issues** - Take screenshots and save console logs

## Reporting Issues

When reporting integration issues, include:

1. **Steps to reproduce**
2. **Expected vs actual behavior**
3. **Screenshots/screen recordings**
4. **Console logs** (both frontend and backend)
5. **Network tab** (failed requests)
6. **Environment** (OS, browser, versions)

---

**Last Updated:** 2025-11-30
**Status:** Production Ready
**Test Coverage:** Manual + Automated
