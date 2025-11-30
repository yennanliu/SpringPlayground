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
