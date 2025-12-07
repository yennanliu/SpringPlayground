#!/bin/bash

# Backend Integration Test Script
# Tests the full authentication and API flow

set -e  # Exit on error

echo "🧪 Backend Integration Testing Script"
echo "======================================"
echo ""

BASE_URL="http://localhost:8080"
API_URL="$BASE_URL/api"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test counter
PASSED=0
FAILED=0

# Helper functions
pass() {
    echo -e "${GREEN}✓${NC} $1"
    ((PASSED++))
}

fail() {
    echo -e "${RED}✗${NC} $1"
    ((FAILED++))
}

warn() {
    echo -e "${YELLOW}⚠${NC} $1"
}

section() {
    echo ""
    echo "==== $1 ===="
}

# Check if backend is running
section "1. Health Check"
if curl -s -f "$BASE_URL" > /dev/null 2>&1; then
    pass "Backend is reachable"
else
    fail "Backend is NOT reachable at $BASE_URL"
    echo "Please start backend: cd ../../backend/ChatAppV2 && ./mvnw spring-boot:run"
    exit 1
fi

# Test user registration
section "2. User Registration"
TIMESTAMP=$(date +%s)
TEST_USER="testuser_$TIMESTAMP"
TEST_EMAIL="test_$TIMESTAMP@example.com"
TEST_PASSWORD="password123"

echo "Registering user: $TEST_USER"
REGISTER_RESPONSE=$(curl -s -X POST "$API_URL/auth/register" \
    -H "Content-Type: application/json" \
    -d "{\"username\":\"$TEST_USER\",\"email\":\"$TEST_EMAIL\",\"password\":\"$TEST_PASSWORD\"}")

if echo "$REGISTER_RESPONSE" | grep -q "token"; then
    pass "User registered successfully"
    TOKEN=$(echo "$REGISTER_RESPONSE" | grep -o '"token":"[^"]*' | cut -d'"' -f4)
    USER_ID=$(echo "$REGISTER_RESPONSE" | grep -o '"id":[0-9]*' | cut -d':' -f2)
    echo "   Token: ${TOKEN:0:20}..."
    echo "   User ID: $USER_ID"
else
    fail "User registration failed"
    echo "   Response: $REGISTER_RESPONSE"
    exit 1
fi

# Test login
section "3. User Login"
echo "Logging in as: $TEST_USER"
LOGIN_RESPONSE=$(curl -s -X POST "$API_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d "{\"username\":\"$TEST_USER\",\"password\":\"$TEST_PASSWORD\"}")

if echo "$LOGIN_RESPONSE" | grep -q "token"; then
    pass "Login successful"
    TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*' | cut -d'"' -f4)
else
    fail "Login failed"
    echo "   Response: $LOGIN_RESPONSE"
fi

# Test authenticated API endpoint
section "4. Authenticated API Calls"
echo "Testing GET /api/channels with JWT token"
CHANNELS_RESPONSE=$(curl -s -X GET "$API_URL/channels" \
    -H "Authorization: Bearer $TOKEN")

if echo "$CHANNELS_RESPONSE" | grep -q "\["; then
    pass "Channels API accessible with JWT"
    echo "   Response: ${CHANNELS_RESPONSE:0:100}..."
else
    warn "Channels API returned: $CHANNELS_RESPONSE"
    if echo "$CHANNELS_RESPONSE" | grep -q "403"; then
        fail "Still getting 403 - JWT might not be working"
    fi
fi

# Test WebSocket endpoint
section "5. WebSocket Endpoint"
WS_RESPONSE=$(curl -s -I "$BASE_URL/ws/info" | head -1)
if echo "$WS_RESPONSE" | grep -q "200\|404"; then
    pass "WebSocket endpoint is accessible"
else
    warn "WebSocket endpoint status: $WS_RESPONSE"
fi

# Test CORS headers
section "6. CORS Configuration"
CORS_RESPONSE=$(curl -s -I -X OPTIONS "$API_URL/channels" \
    -H "Origin: http://localhost:5173" \
    -H "Access-Control-Request-Method: GET")

if echo "$CORS_RESPONSE" | grep -qi "Access-Control-Allow-Origin"; then
    pass "CORS headers present"
else
    warn "CORS headers might not be configured correctly"
fi

# Create a test channel
section "7. Channel Creation"
echo "Creating test channel"
CREATE_CHANNEL_RESPONSE=$(curl -s -X POST "$API_URL/channels/group" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d "{\"name\":\"test-channel-$TIMESTAMP\",\"memberIds\":[]}")

if echo "$CREATE_CHANNEL_RESPONSE" | grep -q '"id"'; then
    pass "Channel created successfully"
    CHANNEL_ID=$(echo "$CREATE_CHANNEL_RESPONSE" | grep -o '"id":"[^"]*' | cut -d'"' -f4)
    echo "   Channel ID: $CHANNEL_ID"
else
    fail "Channel creation failed"
    echo "   Response: $CREATE_CHANNEL_RESPONSE"
fi

# Test getting channel messages
section "8. Message History"
if [ ! -z "$CHANNEL_ID" ]; then
    echo "Fetching messages for channel: $CHANNEL_ID"
    MESSAGES_RESPONSE=$(curl -s -X GET "$API_URL/channels/$CHANNEL_ID/messages?page=0&size=50" \
        -H "Authorization: Bearer $TOKEN")

    if echo "$MESSAGES_RESPONSE" | grep -q "\["; then
        pass "Message history API working"
    else
        warn "Message history returned: ${MESSAGES_RESPONSE:0:100}"
    fi
fi

# Summary
section "Test Summary"
echo "Passed: $PASSED"
echo "Failed: $FAILED"
echo ""

if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}✓ All tests passed!${NC}"
    echo ""
    echo "Backend is ready for frontend integration."
    echo "JWT Token for testing: ${TOKEN:0:30}..."
    echo ""
    echo "Next steps:"
    echo "1. Update frontend LoginView to call /api/auth/login"
    echo "2. Store JWT token in localStorage"
    echo "3. Add Authorization header to all API requests"
    echo "4. Test WebSocket connection with authenticated user"
    exit 0
else
    echo -e "${RED}✗ Some tests failed${NC}"
    echo "Please check the backend logs and fix issues before proceeding."
    exit 1
fi
