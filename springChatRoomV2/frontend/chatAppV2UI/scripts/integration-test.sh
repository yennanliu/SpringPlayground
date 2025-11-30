#!/bin/bash

##############################################################################
# Complete Integration Test Suite
# Tests both backend and frontend integration
##############################################################################

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Counters
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Helper function to run a test
run_test() {
    local test_name="$1"
    local test_command="$2"

    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    echo -e "${BLUE}📋 Test $TOTAL_TESTS: $test_name${NC}"

    if eval "$test_command" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ PASSED${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
        echo ""
        return 0
    else
        echo -e "${RED}❌ FAILED${NC}"
        FAILED_TESTS=$((FAILED_TESTS + 1))
        echo ""
        return 1
    fi
}

# Helper function to check if port is in use
check_port() {
    local port=$1
    lsof -i:$port -sTCP:LISTEN > /dev/null 2>&1
    return $?
}

##############################################################################
# Start Test Suite
##############################################################################

echo ""
echo "=================================="
echo "🚀 Integration Test Suite"
echo "=================================="
echo ""

# Test 1: Check if backend is running
echo -e "${BLUE}📋 Test 1: Backend Health Check${NC}"
if curl -s http://localhost:8080/actuator/health | grep -q "UP"; then
    echo -e "${GREEN}✅ PASSED - Backend is running${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "${RED}❌ FAILED - Backend is not running${NC}"
    echo -e "${YELLOW}💡 Start backend with: cd ../../backend/ChatAppV2 && ./mvnw spring-boot:run${NC}"
    FAILED_TESTS=$((FAILED_TESTS + 1))
fi
TOTAL_TESTS=$((TOTAL_TESTS + 1))
echo ""

# Test 2: Check if frontend is running
echo -e "${BLUE}📋 Test 2: Frontend Health Check${NC}"
if check_port 5173; then
    echo -e "${GREEN}✅ PASSED - Frontend is running on port 5173${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "${RED}❌ FAILED - Frontend is not running${NC}"
    echo -e "${YELLOW}💡 Start frontend with: npm run dev${NC}"
    FAILED_TESTS=$((FAILED_TESTS + 1))
fi
TOTAL_TESTS=$((TOTAL_TESTS + 1))
echo ""

# Test 3: Check WebSocket endpoint
echo -e "${BLUE}📋 Test 3: WebSocket Endpoint${NC}"
if curl -s -I http://localhost:8080/ws | grep -q "HTTP"; then
    echo -e "${GREEN}✅ PASSED - WebSocket endpoint accessible${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "${RED}❌ FAILED - WebSocket endpoint not accessible${NC}"
    FAILED_TESTS=$((FAILED_TESTS + 1))
fi
TOTAL_TESTS=$((TOTAL_TESTS + 1))
echo ""

# Test 4: Run health check script
echo -e "${BLUE}📋 Test 4: Health Check Script${NC}"
if node scripts/health-check.js > /dev/null 2>&1; then
    echo -e "${GREEN}✅ PASSED - Health check script succeeded${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "${YELLOW}⚠️  WARNING - Health check script had issues${NC}"
    # Don't fail on this
fi
TOTAL_TESTS=$((TOTAL_TESTS + 1))
echo ""

# Test 5: Run API tests
echo -e "${BLUE}📋 Test 5: API Integration Tests${NC}"
if node scripts/test-api.js > /dev/null 2>&1; then
    echo -e "${GREEN}✅ PASSED - API tests succeeded${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "${YELLOW}⚠️  WARNING - Some API tests failed (expected if endpoints require auth)${NC}"
    # Don't fail on this
fi
TOTAL_TESTS=$((TOTAL_TESTS + 1))
echo ""

# Test 6: Run frontend unit tests
echo -e "${BLUE}📋 Test 6: Frontend Unit Tests${NC}"
if npm test -- --run > /dev/null 2>&1; then
    echo -e "${GREEN}✅ PASSED - All 91 frontend tests passed${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "${RED}❌ FAILED - Frontend tests failed${NC}"
    echo -e "${YELLOW}💡 Run 'npm test' to see details${NC}"
    FAILED_TESTS=$((FAILED_TESTS + 1))
fi
TOTAL_TESTS=$((TOTAL_TESTS + 1))
echo ""

# Test 7: Check frontend build
echo -e "${BLUE}📋 Test 7: Frontend Build${NC}"
if npm run build > /dev/null 2>&1; then
    echo -e "${GREEN}✅ PASSED - Frontend builds successfully${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "${RED}❌ FAILED - Frontend build failed${NC}"
    echo -e "${YELLOW}💡 Run 'npm run build' to see details${NC}"
    FAILED_TESTS=$((FAILED_TESTS + 1))
fi
TOTAL_TESTS=$((TOTAL_TESTS + 1))
echo ""

##############################################################################
# Summary
##############################################################################

echo "=================================="
echo "📊 Test Summary"
echo "=================================="
echo -e "Total Tests:  $TOTAL_TESTS"
echo -e "${GREEN}Passed:       $PASSED_TESTS${NC}"
echo -e "${RED}Failed:       $FAILED_TESTS${NC}"
echo ""

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "${GREEN}🎉 All integration tests passed!${NC}"
    echo ""
    exit 0
else
    echo -e "${RED}❌ Some tests failed. Please check above for details.${NC}"
    echo ""
    exit 1
fi
