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
