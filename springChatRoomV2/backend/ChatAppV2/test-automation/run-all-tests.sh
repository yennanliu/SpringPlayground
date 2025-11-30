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

# Run authentication tests
echo "═══════════════════════════════════════════════"
bash test-auth.sh
AUTH_RESULT=$?
echo ""

# Summary
echo "╔═══════════════════════════════════════════════╗"
echo "║          FINAL TEST SUMMARY                   ║"
echo "╠═══════════════════════════════════════════════╣"

if [ $AUTH_RESULT -eq 0 ]; then
  echo "║  ✓ Authentication Tests: PASSED               ║"
else
  echo "║  ✗ Authentication Tests: FAILED               ║"
fi

echo "╚═══════════════════════════════════════════════╝"

if [ $AUTH_RESULT -eq 0 ]; then
  echo "✓ All test suites passed!"
  exit 0
else
  echo "✗ Some test suites failed"
  exit 1
fi
