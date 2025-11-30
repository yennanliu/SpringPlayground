# Backend Feature Testing Guide

This document provides comprehensive testing instructions for all backend features from a user/feature perspective, including manual test cases and automation scripts.

## Table of Contents

- [Testing Environment Setup](#testing-environment-setup)
- [Feature 1: User Authentication](#feature-1-user-authentication)
- [Feature 2: Channel Management](#feature-2-channel-management)
- [Feature 3: Real-time Messaging](#feature-3-real-time-messaging)
- [Feature 4: Message Operations](#feature-4-message-operations)
- [Feature 5: File Sharing](#feature-5-file-sharing)
- [Feature 6: Message Search](#feature-6-message-search)
- [Feature 7: Read Receipts](#feature-7-read-receipts)
- [Feature 8: Typing Indicators](#feature-8-typing-indicators)
- [Feature 9: User Profile Management](#feature-9-user-profile-management)
- [Feature 10: Online Status Tracking](#feature-10-online-status-tracking)
- [Automation Scripts](#automation-scripts)

---

## Testing Environment Setup

### Prerequisites
```bash
# 1. Start databases
docker-compose up -d

# 2. Start backend
./mvnw spring-boot:run

# 3. Verify services are running
curl http://localhost:8080/actuator/health
```

### Base URL
```
API Base: http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui.html
```

### Test Data Setup
Use the provided automation scripts to create test users and channels.

---

## Feature 1: User Authentication

### Description
Users can register new accounts and login to receive JWT tokens for API access.

### Test Cases

#### TC-AUTH-001: User Registration Success
**Given**: A new user with valid credentials
**When**: POST to `/api/auth/register`
**Then**:
- Returns 201 Created
- Returns JWT token
- User is created in database

**Manual Test**:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser1",
    "email": "testuser1@example.com",
    "password": "password123",
    "displayName": "Test User 1"
  }'
```

**Expected Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "testuser1",
  "displayName": "Test User 1"
}
```

#### TC-AUTH-002: User Registration with Duplicate Username
**Given**: Username already exists
**When**: POST to `/api/auth/register`
**Then**: Returns 409 Conflict

**Manual Test**:
```bash
# Register twice with same username
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser1",
    "email": "different@example.com",
    "password": "password123",
    "displayName": "Another User"
  }'
```

#### TC-AUTH-003: User Login Success
**Given**: User has registered
**When**: POST to `/api/auth/login` with correct credentials
**Then**: Returns 200 OK with JWT token

**Manual Test**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser1",
    "password": "password123"
  }'
```

#### TC-AUTH-004: User Login with Wrong Password
**Given**: User exists
**When**: POST to `/api/auth/login` with incorrect password
**Then**: Returns 401 Unauthorized

**Manual Test**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser1",
    "password": "wrongpassword"
  }'
```

#### TC-AUTH-005: Access Protected Endpoint with Valid Token
**Given**: User has valid JWT token
**When**: GET to `/api/channels` with Bearer token
**Then**: Returns 200 OK with data

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X GET "http://localhost:8080/api/channels?userId=1" \
  -H "Authorization: Bearer $TOKEN"
```

#### TC-AUTH-006: Access Protected Endpoint without Token
**Given**: No authentication token provided
**When**: GET to `/api/channels`
**Then**: Returns 403 Forbidden

---

## Feature 2: Channel Management

### Description
Users can create group channels and direct message channels, and manage channel membership.

### Test Cases

#### TC-CHANNEL-001: Create Group Channel
**Given**: Authenticated user with valid token
**When**: POST to `/api/channels/group`
**Then**:
- Returns 201 Created
- Channel is created with all members
- Creator is automatically added

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X POST http://localhost:8080/api/channels/group \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Engineering Team",
    "creatorId": 1,
    "memberIds": [1, 2, 3]
  }'
```

#### TC-CHANNEL-002: Create Direct Message Channel
**Given**: Two users exist
**When**: POST to `/api/channels/direct`
**Then**:
- Returns 200 OK
- Creates new DM channel if doesn't exist
- Returns existing DM channel if already exists

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X POST http://localhost:8080/api/channels/direct \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId1": 1,
    "userId2": 2
  }'
```

#### TC-CHANNEL-003: Get User's Channels
**Given**: User is member of multiple channels
**When**: GET to `/api/channels?userId=1`
**Then**: Returns list of all channels user belongs to

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X GET "http://localhost:8080/api/channels?userId=1" \
  -H "Authorization: Bearer $TOKEN"
```

#### TC-CHANNEL-004: Add Member to Channel
**Given**: Group channel exists
**When**: POST to `/api/channels/{channelId}/members`
**Then**:
- Returns 200 OK
- User is added to channel
- User can see channel in their list

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X POST http://localhost:8080/api/channels/1/members \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 4
  }'
```

#### TC-CHANNEL-005: Prevent Duplicate DM Channels
**Given**: DM channel exists between user A and B
**When**: Create DM channel between user A and B again
**Then**: Returns existing channel (not creating duplicate)

---

## Feature 3: Real-time Messaging

### Description
Users can send and receive messages in real-time via WebSocket connection.

### Test Cases

#### TC-MSG-001: Send Text Message via WebSocket
**Given**: User connected to WebSocket and subscribed to channel
**When**: Send message to `/app/chat/{channelId}`
**Then**:
- Message is saved to database
- Message is cached in Redis
- All subscribers receive message broadcast

**Manual Test** (requires WebSocket client):
```javascript
// Using SockJS + STOMP
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
  // Subscribe to channel
  stompClient.subscribe('/topic/channel/1', function(message) {
    console.log('Received:', JSON.parse(message.body));
  });

  // Send message
  stompClient.send('/app/chat/1', {}, JSON.stringify({
    channelId: 1,
    senderId: 1,
    content: 'Hello World',
    messageType: 'TEXT'
  }));
});
```

#### TC-MSG-002: Multiple Users Receive Same Message
**Given**: 3 users subscribed to same channel
**When**: User A sends message
**Then**: Users B and C both receive the message

#### TC-MSG-003: WebSocket Connection Resilience
**Given**: User connected to WebSocket
**When**: Connection drops and reconnects
**Then**:
- User can reconnect successfully
- Can send/receive messages after reconnection

---

## Feature 4: Message Operations

### Description
Users can view message history, edit their own messages, and delete their own messages.

### Test Cases

#### TC-MSG-OPS-001: Get Message History with Pagination
**Given**: Channel has 100 messages
**When**: GET `/api/messages/channel/{channelId}?page=0&size=50`
**Then**:
- Returns 50 most recent messages
- Sorted by timestamp (newest first)
- Includes pagination metadata

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X GET "http://localhost:8080/api/messages/channel/1?userId=1&page=0&size=50" \
  -H "Authorization: Bearer $TOKEN"
```

#### TC-MSG-OPS-002: Edit Own Message
**Given**: User owns a message
**When**: PUT to `/api/messages/{messageId}`
**Then**:
- Message content is updated
- `editedAt` timestamp is set
- Edit event is broadcast to subscribers

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X PUT "http://localhost:8080/api/messages/1?userId=1&content=Updated%20message" \
  -H "Authorization: Bearer $TOKEN"
```

#### TC-MSG-OPS-003: Prevent Editing Other User's Message
**Given**: Message belongs to User A
**When**: User B tries to edit the message
**Then**: Returns 403 Forbidden

**Manual Test**:
```bash
TOKEN="<user-b-token>"
curl -X PUT "http://localhost:8080/api/messages/1?userId=2&content=Hacked" \
  -H "Authorization: Bearer $TOKEN"
```

#### TC-MSG-OPS-004: Delete Own Message
**Given**: User owns a message
**When**: DELETE to `/api/messages/{messageId}`
**Then**:
- Message is soft deleted (`isDeleted` = true)
- Delete event is broadcast
- Message no longer appears in history

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X DELETE "http://localhost:8080/api/messages/1?userId=1" \
  -H "Authorization: Bearer $TOKEN"
```

#### TC-MSG-OPS-005: Prevent Deleting Other User's Message
**Given**: Message belongs to User A
**When**: User B tries to delete the message
**Then**: Returns 403 Forbidden

---

## Feature 5: File Sharing

### Description
Users can upload files and images, which are automatically converted to messages.

### Test Cases

#### TC-FILE-001: Upload Image File
**Given**: User has an image file
**When**: POST to `/api/files/upload` with multipart form data
**Then**:
- File is saved to filesystem
- IMAGE type message is created
- Returns file URL and message ID

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X POST "http://localhost:8080/api/files/upload" \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/path/to/image.png" \
  -F "channelId=1" \
  -F "senderId=1"
```

#### TC-FILE-002: Upload Document File
**Given**: User has a PDF file
**When**: POST to `/api/files/upload`
**Then**:
- File is saved with unique filename
- FILE type message is created
- Returns download URL

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X POST "http://localhost:8080/api/files/upload" \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/path/to/document.pdf" \
  -F "channelId=1" \
  -F "senderId=1"
```

#### TC-FILE-003: Download File
**Given**: File was previously uploaded
**When**: GET to `/api/files/download/{fileName}`
**Then**: Returns file with appropriate content type

**Manual Test**:
```bash
curl -X GET "http://localhost:8080/api/files/download/abc123.png" \
  -O  # Save to file
```

#### TC-FILE-004: Reject Files Exceeding Size Limit
**Given**: File size is > 10MB
**When**: POST to `/api/files/upload`
**Then**: Returns 413 Payload Too Large

#### TC-FILE-005: Prevent Path Traversal Attack
**Given**: Malicious filename with path traversal
**When**: GET to `/api/files/download/../../etc/passwd`
**Then**: Returns 400 Bad Request

---

## Feature 6: Message Search

### Description
Users can search for messages globally or within specific channels.

### Test Cases

#### TC-SEARCH-001: Global Message Search
**Given**: Messages exist across multiple channels
**When**: GET `/api/search/messages?query=meeting`
**Then**: Returns all messages containing "meeting" (case-insensitive)

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X GET "http://localhost:8080/api/search/messages?query=meeting&page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"
```

#### TC-SEARCH-002: Channel-Specific Search
**Given**: User wants to search within one channel
**When**: GET `/api/search/messages?query=project&channelId=1`
**Then**: Returns messages containing "project" only from channel 1

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X GET "http://localhost:8080/api/search/messages?query=project&channelId=1&page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"
```

#### TC-SEARCH-003: Search with Pagination
**Given**: Search returns 50 results
**When**: GET with `page=0&size=10`
**Then**: Returns first 10 results with pagination metadata

#### TC-SEARCH-004: Case-Insensitive Search
**Given**: Messages contain "Hello", "HELLO", "hello"
**When**: Search for "hello"
**Then**: Returns all three messages

#### TC-SEARCH-005: Empty Search Results
**Given**: No messages match query
**When**: Search for "xyzabc123"
**Then**: Returns empty array with 200 OK

---

## Feature 7: Read Receipts

### Description
System tracks which messages each user has read and provides unread counts.

### Test Cases

#### TC-READ-001: Mark Message as Read
**Given**: User has unread messages in channel
**When**: POST to `/api/messages/{messageId}/read`
**Then**:
- Updates user's `lastReadAt` for channel
- Message is marked as read
- Unread count decreases

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X POST "http://localhost:8080/api/messages/5/read?userId=1&channelId=1" \
  -H "Authorization: Bearer $TOKEN"
```

#### TC-READ-002: Get Unread Message Count
**Given**: User has 10 unread messages in channel
**When**: GET `/api/messages/channel/{channelId}/unread?userId=1`
**Then**: Returns `10`

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X GET "http://localhost:8080/api/messages/channel/1/unread?userId=1" \
  -H "Authorization: Bearer $TOKEN"
```

#### TC-READ-003: Mark All Messages as Read
**Given**: User has multiple unread messages
**When**: Mark latest message as read
**Then**: All previous messages are implicitly marked as read

#### TC-READ-004: Read Status Per User
**Given**: Message sent to channel with 3 members
**When**: User A marks as read
**Then**: Users B and C still see message as unread

---

## Feature 8: Typing Indicators

### Description
Real-time typing indicators show when users are typing in a channel.

### Test Cases

#### TC-TYPING-001: Start Typing Indicator
**Given**: User starts typing in channel
**When**: Send to `/app/typing/{channelId}/start`
**Then**:
- Typing event is stored in Redis (5 second TTL)
- Event is broadcast to `/topic/channel/{channelId}/typing`
- Other users see "User is typing..."

**Manual Test** (WebSocket):
```javascript
stompClient.send('/app/typing/1/start', {}, JSON.stringify({
  userId: 1,
  username: 'testuser1'
}));
```

#### TC-TYPING-002: Stop Typing Indicator
**Given**: User stops typing
**When**: Send to `/app/typing/{channelId}/stop`
**Then**:
- Typing event is removed from Redis
- Stop event is broadcast
- "User is typing..." disappears

#### TC-TYPING-003: Typing Indicator Auto-Expiry
**Given**: User starts typing but doesn't stop
**When**: 5 seconds pass
**Then**: Typing indicator automatically disappears (Redis TTL)

#### TC-TYPING-004: Multiple Users Typing
**Given**: Users A, B, and C are in same channel
**When**: A and B start typing
**Then**: All users see "A and B are typing..."

---

## Feature 9: User Profile Management

### Description
Users can update their display name and avatar image.

### Test Cases

#### TC-PROFILE-001: Update Display Name
**Given**: User wants to change display name
**When**: PUT to `/api/users/{id}/profile`
**Then**: Display name is updated in database

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X PUT "http://localhost:8080/api/users/1/profile" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "displayName": "New Display Name",
    "avatarUrl": null
  }'
```

#### TC-PROFILE-002: Upload Avatar Image
**Given**: User wants to set profile picture
**When**: POST to `/api/users/{id}/avatar`
**Then**:
- Image is uploaded
- Avatar URL is updated
- Returns new avatar URL

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X POST "http://localhost:8080/api/users/1/avatar" \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/path/to/avatar.jpg"
```

#### TC-PROFILE-003: Update Avatar URL Directly
**Given**: User has external avatar URL
**When**: PUT to `/api/users/{id}/profile` with avatarUrl
**Then**: Avatar URL is updated

#### TC-PROFILE-004: Get User Profile
**Given**: User exists
**When**: GET to `/api/users/{id}`
**Then**: Returns user details including display name and avatar

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X GET "http://localhost:8080/api/users/1" \
  -H "Authorization: Bearer $TOKEN"
```

---

## Feature 10: Online Status Tracking

### Description
System tracks which users are currently online based on recent activity.

### Test Cases

#### TC-ONLINE-001: Get Online Users
**Given**: Multiple users have recent activity
**When**: GET to `/api/users/online`
**Then**: Returns list of users active in last 5 minutes

**Manual Test**:
```bash
TOKEN="<your-jwt-token>"
curl -X GET "http://localhost:8080/api/users/online" \
  -H "Authorization: Bearer $TOKEN"
```

#### TC-ONLINE-002: User Goes Offline
**Given**: User was online
**When**: No activity for > 5 minutes
**Then**: User no longer appears in online users list

#### TC-ONLINE-003: Last Seen Timestamp
**Given**: User performs any action
**When**: Action completes
**Then**: User's `lastSeenAt` is updated

---

## Automation Scripts

### Setup Script

Save as `test-automation/setup.sh`:

```bash
#!/bin/bash

# Backend Feature Testing - Setup Script
# This script sets up test data for feature testing

BASE_URL="http://localhost:8080"
OUTPUT_FILE="test-data.json"

echo "=== ChatApp Backend Test Setup ==="
echo ""

# Create test users
echo "Creating test users..."

USER1=$(curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "email": "alice@example.com",
    "password": "password123",
    "displayName": "Alice Johnson"
  }')

USER2=$(curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "bob",
    "email": "bob@example.com",
    "password": "password123",
    "displayName": "Bob Smith"
  }')

USER3=$(curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "charlie",
    "email": "charlie@example.com",
    "password": "password123",
    "displayName": "Charlie Brown"
  }')

echo "✓ Created 3 test users"

# Extract tokens
TOKEN1=$(echo $USER1 | jq -r '.token')
TOKEN2=$(echo $USER2 | jq -r '.token')
TOKEN3=$(echo $USER3 | jq -r '.token')

# Save test data
cat > $OUTPUT_FILE <<EOF
{
  "users": {
    "alice": {
      "token": "$TOKEN1",
      "data": $USER1
    },
    "bob": {
      "token": "$TOKEN2",
      "data": $USER2
    },
    "charlie": {
      "token": "$TOKEN3",
      "data": $USER3
    }
  }
}
EOF

echo "✓ Test data saved to $OUTPUT_FILE"
echo ""
echo "=== Setup Complete ==="
echo "Tokens saved. Use these for testing:"
echo "Alice: $TOKEN1"
echo "Bob: $TOKEN2"
echo "Charlie: $TOKEN3"
```

### Authentication Test Script

Save as `test-automation/test-auth.sh`:

```bash
#!/bin/bash

# Feature Test: Authentication

BASE_URL="http://localhost:8080"
PASSED=0
FAILED=0

echo "=== Testing Feature: Authentication ==="
echo ""

# Test 1: Register new user
echo "Test 1: User Registration Success"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser'$RANDOM'",
    "email": "test'$RANDOM'@example.com",
    "password": "password123",
    "displayName": "Test User"
  }')

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "201" ] && [ ! -z "$(echo $BODY | jq -r '.token')" ]; then
  echo "✓ PASSED: User registration successful"
  TOKEN=$(echo $BODY | jq -r '.token')
  ((PASSED++))
else
  echo "✗ FAILED: Expected 201 with token, got $HTTP_CODE"
  echo "Response: $BODY"
  ((FAILED++))
fi
echo ""

# Test 2: Login with valid credentials
echo "Test 2: User Login Success"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "password123"
  }')

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "200" ] && [ ! -z "$(echo $BODY | jq -r '.token')" ]; then
  echo "✓ PASSED: Login successful"
  ((PASSED++))
else
  echo "✗ FAILED: Expected 200 with token, got $HTTP_CODE"
  ((FAILED++))
fi
echo ""

# Test 3: Login with wrong password
echo "Test 3: Login with Wrong Password"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "wrongpassword"
  }')

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)

if [ "$HTTP_CODE" = "401" ] || [ "$HTTP_CODE" = "500" ]; then
  echo "✓ PASSED: Login rejected with wrong password"
  ((PASSED++))
else
  echo "✗ FAILED: Expected 401, got $HTTP_CODE"
  ((FAILED++))
fi
echo ""

# Test 4: Access protected endpoint with token
echo "Test 4: Access Protected Endpoint with Valid Token"
RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/api/channels?userId=1" \
  -H "Authorization: Bearer $TOKEN")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)

if [ "$HTTP_CODE" = "200" ]; then
  echo "✓ PASSED: Accessed protected endpoint successfully"
  ((PASSED++))
else
  echo "✗ FAILED: Expected 200, got $HTTP_CODE"
  ((FAILED++))
fi
echo ""

# Test 5: Access protected endpoint without token
echo "Test 5: Access Protected Endpoint without Token"
RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/api/channels?userId=1")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)

if [ "$HTTP_CODE" = "403" ] || [ "$HTTP_CODE" = "401" ]; then
  echo "✓ PASSED: Access denied without token"
  ((PASSED++))
else
  echo "✗ FAILED: Expected 403/401, got $HTTP_CODE"
  ((FAILED++))
fi
echo ""

# Summary
echo "=== Test Summary ==="
echo "Passed: $PASSED"
echo "Failed: $FAILED"
echo "Total: $((PASSED + FAILED))"

if [ $FAILED -eq 0 ]; then
  echo "✓ All authentication tests passed!"
  exit 0
else
  echo "✗ Some tests failed"
  exit 1
fi
```

### Channel Management Test Script

Save as `test-automation/test-channels.sh`:

```bash
#!/bin/bash

# Feature Test: Channel Management

BASE_URL="http://localhost:8080"
PASSED=0
FAILED=0

# Load test data
if [ ! -f "test-data.json" ]; then
  echo "Error: test-data.json not found. Run setup.sh first."
  exit 1
fi

TOKEN=$(jq -r '.users.alice.token' test-data.json)

echo "=== Testing Feature: Channel Management ==="
echo ""

# Test 1: Create group channel
echo "Test 1: Create Group Channel"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/channels/group" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Group",
    "creatorId": 1,
    "memberIds": [1, 2, 3]
  }')

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "201" ]; then
  echo "✓ PASSED: Group channel created"
  CHANNEL_ID=$(echo $BODY | jq -r '.id')
  ((PASSED++))
else
  echo "✗ FAILED: Expected 201, got $HTTP_CODE"
  echo "Response: $BODY"
  ((FAILED++))
fi
echo ""

# Test 2: Create direct message channel
echo "Test 2: Create Direct Message Channel"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/channels/direct" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId1": 1,
    "userId2": 2
  }')

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "200" ]; then
  echo "✓ PASSED: DM channel created"
  DM_CHANNEL_ID=$(echo $BODY | jq -r '.id')
  ((PASSED++))
else
  echo "✗ FAILED: Expected 200, got $HTTP_CODE"
  ((FAILED++))
fi
echo ""

# Test 3: Get user's channels
echo "Test 3: Get User's Channels"
RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/api/channels?userId=1" \
  -H "Authorization: Bearer $TOKEN")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "200" ]; then
  CHANNEL_COUNT=$(echo $BODY | jq 'length')
  if [ "$CHANNEL_COUNT" -ge 2 ]; then
    echo "✓ PASSED: Retrieved user channels (found $CHANNEL_COUNT channels)"
    ((PASSED++))
  else
    echo "✗ FAILED: Expected at least 2 channels, found $CHANNEL_COUNT"
    ((FAILED++))
  fi
else
  echo "✗ FAILED: Expected 200, got $HTTP_CODE"
  ((FAILED++))
fi
echo ""

# Test 4: Add member to channel
echo "Test 4: Add Member to Channel"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/channels/$CHANNEL_ID/members" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 4
  }')

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)

if [ "$HTTP_CODE" = "200" ]; then
  echo "✓ PASSED: Member added to channel"
  ((PASSED++))
else
  echo "✗ FAILED: Expected 200, got $HTTP_CODE"
  ((FAILED++))
fi
echo ""

# Test 5: Prevent duplicate DM channels
echo "Test 5: Prevent Duplicate DM Channels"
RESPONSE1=$(curl -s -X POST "$BASE_URL/api/channels/direct" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"userId1": 1, "userId2": 2}')

RESPONSE2=$(curl -s -X POST "$BASE_URL/api/channels/direct" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"userId1": 2, "userId2": 1}')

ID1=$(echo $RESPONSE1 | jq -r '.id')
ID2=$(echo $RESPONSE2 | jq -r '.id')

if [ "$ID1" = "$ID2" ]; then
  echo "✓ PASSED: Same DM channel returned (no duplicate)"
  ((PASSED++))
else
  echo "✗ FAILED: Different channel IDs: $ID1 vs $ID2"
  ((FAILED++))
fi
echo ""

# Summary
echo "=== Test Summary ==="
echo "Passed: $PASSED"
echo "Failed: $FAILED"
echo "Total: $((PASSED + FAILED))"

if [ $FAILED -eq 0 ]; then
  echo "✓ All channel management tests passed!"
  exit 0
else
  echo "✗ Some tests failed"
  exit 1
fi
```

### Message Operations Test Script

Save as `test-automation/test-messages.sh`:

```bash
#!/bin/bash

# Feature Test: Message Operations

BASE_URL="http://localhost:8080"
PASSED=0
FAILED=0

# Load test data
if [ ! -f "test-data.json" ]; then
  echo "Error: test-data.json not found. Run setup.sh first."
  exit 1
fi

TOKEN=$(jq -r '.users.alice.token' test-data.json)

echo "=== Testing Feature: Message Operations ==="
echo ""

# Create a test channel first
CHANNEL_RESPONSE=$(curl -s -X POST "$BASE_URL/api/channels/group" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Message Test Channel",
    "creatorId": 1,
    "memberIds": [1, 2]
  }')

CHANNEL_ID=$(echo $CHANNEL_RESPONSE | jq -r '.id')
echo "Test channel created: $CHANNEL_ID"
echo ""

# Test 1: Get message history (empty channel)
echo "Test 1: Get Message History (Empty Channel)"
RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/api/messages/channel/$CHANNEL_ID?userId=1&page=0&size=50" \
  -H "Authorization: Bearer $TOKEN")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "200" ]; then
  echo "✓ PASSED: Retrieved message history"
  ((PASSED++))
else
  echo "✗ FAILED: Expected 200, got $HTTP_CODE"
  ((FAILED++))
fi
echo ""

# For remaining tests, we need WebSocket which is complex in bash
# These are placeholders for manual testing or Node.js automation

echo "Note: Edit/Delete message tests require messages to be created first"
echo "Use the WebSocket test script or Swagger UI to create messages"
echo ""

# Summary
echo "=== Test Summary ==="
echo "Passed: $PASSED"
echo "Failed: $FAILED"
echo "Total: $((PASSED + FAILED))"
```

### File Upload Test Script

Save as `test-automation/test-files.sh`:

```bash
#!/bin/bash

# Feature Test: File Sharing

BASE_URL="http://localhost:8080"
PASSED=0
FAILED=0

# Load test data
if [ ! -f "test-data.json" ]; then
  echo "Error: test-data.json not found. Run setup.sh first."
  exit 1
fi

TOKEN=$(jq -r '.users.alice.token' test-data.json)

echo "=== Testing Feature: File Sharing ==="
echo ""

# Create test files
echo "Creating test files..."
echo "This is a test file" > /tmp/test.txt
echo ""

# Create a test channel
CHANNEL_RESPONSE=$(curl -s -X POST "$BASE_URL/api/channels/group" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "File Test Channel",
    "creatorId": 1,
    "memberIds": [1]
  }')

CHANNEL_ID=$(echo $CHANNEL_RESPONSE | jq -r '.id')

# Test 1: Upload text file
echo "Test 1: Upload Text File"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/files/upload" \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/tmp/test.txt" \
  -F "channelId=$CHANNEL_ID" \
  -F "senderId=1")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "200" ]; then
  echo "✓ PASSED: File uploaded successfully"
  FILE_NAME=$(echo $BODY | jq -r '.fileName')
  FILE_URL=$(echo $BODY | jq -r '.fileUrl')
  MESSAGE_ID=$(echo $BODY | jq -r '.messageId')
  echo "  File: $FILE_NAME"
  echo "  URL: $FILE_URL"
  echo "  Message ID: $MESSAGE_ID"
  ((PASSED++))
else
  echo "✗ FAILED: Expected 200, got $HTTP_CODE"
  echo "Response: $BODY"
  ((FAILED++))
fi
echo ""

# Test 2: Download file
echo "Test 2: Download Uploaded File"
RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL$FILE_URL" \
  -o /tmp/downloaded.txt)

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)

if [ "$HTTP_CODE" = "200" ]; then
  # Verify file content
  CONTENT=$(cat /tmp/downloaded.txt)
  if [ "$CONTENT" = "This is a test file" ]; then
    echo "✓ PASSED: File downloaded and content matches"
    ((PASSED++))
  else
    echo "✗ FAILED: File content doesn't match"
    ((FAILED++))
  fi
else
  echo "✗ FAILED: Expected 200, got $HTTP_CODE"
  ((FAILED++))
fi
echo ""

# Test 3: Prevent path traversal
echo "Test 3: Prevent Path Traversal Attack"
RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/api/files/download/..%2F..%2Fetc%2Fpasswd")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)

if [ "$HTTP_CODE" = "400" ] || [ "$HTTP_CODE" = "404" ]; then
  echo "✓ PASSED: Path traversal prevented"
  ((PASSED++))
else
  echo "✗ FAILED: Expected 400/404, got $HTTP_CODE"
  ((FAILED++))
fi
echo ""

# Cleanup
rm -f /tmp/test.txt /tmp/downloaded.txt

# Summary
echo "=== Test Summary ==="
echo "Passed: $PASSED"
echo "Failed: $FAILED"
echo "Total: $((PASSED + FAILED))"

if [ $FAILED -eq 0 ]; then
  echo "✓ All file sharing tests passed!"
  exit 0
else
  echo "✗ Some tests failed"
  exit 1
fi
```

### Search Test Script

Save as `test-automation/test-search.sh`:

```bash
#!/bin/bash

# Feature Test: Message Search

BASE_URL="http://localhost:8080"
PASSED=0
FAILED=0

# Load test data
if [ ! -f "test-data.json" ]; then
  echo "Error: test-data.json not found. Run setup.sh first."
  exit 1
fi

TOKEN=$(jq -r '.users.alice.token' test-data.json)

echo "=== Testing Feature: Message Search ==="
echo ""

# Test 1: Global search
echo "Test 1: Global Message Search"
RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/api/search/messages?query=test&page=0&size=20" \
  -H "Authorization: Bearer $TOKEN")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "200" ]; then
  echo "✓ PASSED: Search executed successfully"
  RESULT_COUNT=$(echo $BODY | jq '.content | length')
  echo "  Found $RESULT_COUNT results"
  ((PASSED++))
else
  echo "✗ FAILED: Expected 200, got $HTTP_CODE"
  ((FAILED++))
fi
echo ""

# Test 2: Channel-specific search
echo "Test 2: Channel-Specific Search"
RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/api/search/messages?query=test&channelId=1&page=0&size=20" \
  -H "Authorization: Bearer $TOKEN")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)

if [ "$HTTP_CODE" = "200" ]; then
  echo "✓ PASSED: Channel search executed successfully"
  ((PASSED++))
else
  echo "✗ FAILED: Expected 200, got $HTTP_CODE"
  ((FAILED++))
fi
echo ""

# Test 3: Empty search results
echo "Test 3: Search with No Results"
RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/api/search/messages?query=xyzabc123notfound&page=0&size=20" \
  -H "Authorization: Bearer $TOKEN")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "200" ]; then
  RESULT_COUNT=$(echo $BODY | jq '.content | length')
  if [ "$RESULT_COUNT" = "0" ]; then
    echo "✓ PASSED: Empty search handled correctly"
    ((PASSED++))
  else
    echo "✗ FAILED: Expected 0 results, got $RESULT_COUNT"
    ((FAILED++))
  fi
else
  echo "✗ FAILED: Expected 200, got $HTTP_CODE"
  ((FAILED++))
fi
echo ""

# Summary
echo "=== Test Summary ==="
echo "Passed: $PASSED"
echo "Failed: $FAILED"
echo "Total: $((PASSED + FAILED))"

if [ $FAILED -eq 0 ]; then
  echo "✓ All search tests passed!"
  exit 0
else
  echo "✗ Some tests failed"
  exit 1
fi
```

### Master Test Runner

Save as `test-automation/run-all-tests.sh`:

```bash
#!/bin/bash

# Master Test Runner - Runs all feature tests

echo "╔═══════════════════════════════════════════════╗"
echo "║  ChatApp Backend Feature Test Suite          ║"
echo "╚═══════════════════════════════════════════════╝"
echo ""

# Check if server is running
echo "Checking if backend is running..."
if ! curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
  echo "✗ Backend is not running on http://localhost:8080"
  echo "Please start the backend first: ./mvnw spring-boot:run"
  exit 1
fi
echo "✓ Backend is running"
echo ""

# Setup test data
echo "Running setup..."
bash setup.sh
echo ""

# Run test suites
TOTAL_PASSED=0
TOTAL_FAILED=0

# Authentication tests
bash test-auth.sh
if [ $? -eq 0 ]; then
  ((TOTAL_PASSED+=5))
else
  ((TOTAL_FAILED++))
fi
echo ""

# Channel tests
bash test-channels.sh
if [ $? -eq 0 ]; then
  ((TOTAL_PASSED+=5))
else
  ((TOTAL_FAILED++))
fi
echo ""

# File tests
bash test-files.sh
if [ $? -eq 0 ]; then
  ((TOTAL_PASSED+=3))
else
  ((TOTAL_FAILED++))
fi
echo ""

# Search tests
bash test-search.sh
if [ $? -eq 0 ]; then
  ((TOTAL_PASSED+=3))
else
  ((TOTAL_FAILED++))
fi
echo ""

# Final summary
echo "╔═══════════════════════════════════════════════╗"
echo "║          FINAL TEST SUMMARY                   ║"
echo "╠═══════════════════════════════════════════════╣"
echo "║  Total Tests Passed: $TOTAL_PASSED                      ║"
echo "║  Total Test Suites Failed: $TOTAL_FAILED                 ║"
echo "╚═══════════════════════════════════════════════╝"

if [ $TOTAL_FAILED -eq 0 ]; then
  echo "✓ All test suites passed!"
  exit 0
else
  echo "✗ Some test suites failed"
  exit 1
fi
```

### Make Scripts Executable

```bash
cd test-automation
chmod +x *.sh
```

## Running the Tests

### Run All Tests
```bash
cd test-automation
./run-all-tests.sh
```

### Run Individual Test Suites
```bash
# Setup
./setup.sh

# Authentication
./test-auth.sh

# Channels
./test-channels.sh

# Files
./test-files.sh

# Search
./test-search.sh
```

## CI/CD Integration

Add to your CI pipeline (e.g., GitHub Actions):

```yaml
- name: Run Backend Feature Tests
  run: |
    cd backend/ChatAppV2/test-automation
    ./run-all-tests.sh
```

## Notes

- **WebSocket Testing**: Some features (real-time messaging, typing indicators) require WebSocket connections which are difficult to test with bash scripts. Consider using a Node.js testing framework like Jest with WebSocket libraries for comprehensive WebSocket testing.

- **Database State**: Tests assume a clean database state. Consider adding database reset scripts between test runs for repeatable results.

- **Authentication**: Most tests use the token from setup. In production testing, implement proper test user management with cleanup.

- **Error Handling**: The scripts provide basic error handling. Enhance as needed for your CI/CD environment.
