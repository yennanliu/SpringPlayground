#!/bin/bash

# E2E Test Script for Pessimistic Lock with Multiple Instances
# This script tests the pessimistic locking mechanism by:
# 1. Starting multiple app instances
# 2. Sending concurrent booking requests
# 3. Verifying only one booking succeeds

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🚀 Starting Pessimistic Lock E2E Test${NC}"
echo "========================================"

# Configuration
ROOM_ID=1
GUEST_BASE_NAME="TestGuest"
EMAIL_BASE="test"
CHECK_IN="2024-12-25"
CHECK_OUT="2024-12-27"
CONCURRENT_REQUESTS=5

# Function to start app instance
start_instance() {
    local port=$1
    local instance_id=$2
    
    echo -e "${YELLOW}📡 Starting instance ${instance_id} on port ${port}${NC}"
    
    INSTANCE_ID="${instance_id}" PORT="${port}" nohup mvn spring-boot:run \
        > logs/instance-${instance_id}.log 2>&1 &
    
    local pid=$!
    echo "${pid}" > "logs/instance-${instance_id}.pid"
    
    # Wait for instance to start
    echo "⏳ Waiting for instance ${instance_id} to start..."
    for i in {1..30}; do
        if curl -s "http://localhost:${port}/actuator/health" > /dev/null 2>&1; then
            echo -e "${GREEN}✅ Instance ${instance_id} started successfully${NC}"
            return 0
        fi
        sleep 2
    done
    
    echo -e "${RED}❌ Instance ${instance_id} failed to start${NC}"
    return 1
}

# Function to stop instance
stop_instance() {
    local instance_id=$1
    local pid_file="logs/instance-${instance_id}.pid"
    
    if [ -f "${pid_file}" ]; then
        local pid=$(cat "${pid_file}")
        echo -e "${YELLOW}🛑 Stopping instance ${instance_id} (PID: ${pid})${NC}"
        kill "${pid}" 2>/dev/null || true
        rm -f "${pid_file}"
    fi
}

# Function to send booking request
send_booking_request() {
    local port=$1
    local guest_name=$2
    local email=$3
    local request_id=$4
    
    local response=$(curl -s -w "%{http_code}" \
        -H "Content-Type: application/json" \
        -d "{
            \"roomId\": ${ROOM_ID},
            \"guestName\": \"${guest_name}\",
            \"guestEmail\": \"${email}@test.com\",
            \"checkInDate\": \"${CHECK_IN}\",
            \"checkOutDate\": \"${CHECK_OUT}\"
        }" \
        "http://localhost:${port}/api/hotel/bookings" 2>/dev/null)
    
    local http_code="${response: -3}"
    local response_body="${response%???}"
    
    echo "Request ${request_id}: HTTP ${http_code} - ${response_body}"
    
    if [ "${http_code}" = "200" ]; then
        return 0
    else
        return 1
    fi
}

# Function to cleanup
cleanup() {
    echo -e "${YELLOW}🧹 Cleaning up...${NC}"
    stop_instance "A"
    stop_instance "B"
    stop_instance "C"
    
    # Kill any remaining processes
    pkill -f "spring-boot:run" 2>/dev/null || true
    
    echo -e "${GREEN}✅ Cleanup completed${NC}"
}

# Trap cleanup on exit
trap cleanup EXIT

# Create logs directory
mkdir -p logs

# Start MySQL if using Docker Compose
echo -e "${BLUE}🐬 Starting MySQL...${NC}"
docker-compose up -d mysql
sleep 10

echo -e "${BLUE}🏃‍♂️ Starting multiple app instances...${NC}"

# Start 3 instances
start_instance 8081 "A"
start_instance 8082 "B" 
start_instance 8083 "C"

echo -e "${BLUE}⏰ Waiting for all instances to be ready...${NC}"
sleep 5

echo -e "${BLUE}🎯 Starting concurrent booking test...${NC}"
echo "Target: Room ${ROOM_ID}, ${CONCURRENT_REQUESTS} concurrent requests"

# Initialize room data (ensure room exists and is available)
echo -e "${YELLOW}🏠 Ensuring test room exists and is available...${NC}"
curl -s "http://localhost:8081/api/hotel/rooms" > /dev/null

echo -e "${BLUE}🚀 Sending ${CONCURRENT_REQUESTS} concurrent booking requests...${NC}"

# Send concurrent requests to different instances
declare -a pids=()
declare -a results=()

for i in $(seq 1 ${CONCURRENT_REQUESTS}); do
    local port=$((8080 + (i % 3) + 1))  # Distribute across instances 8081, 8082, 8083
    local guest_name="${GUEST_BASE_NAME}${i}"
    local email="${EMAIL_BASE}${i}"
    
    echo "📤 Sending request ${i} to port ${port}..."
    
    # Send request in background
    (
        send_booking_request "${port}" "${guest_name}" "${email}" "${i}"
        echo $? > "logs/result-${i}.tmp"
    ) &
    
    pids+=($!)
done

echo -e "${YELLOW}⏳ Waiting for all requests to complete...${NC}"

# Wait for all requests to complete
for pid in "${pids[@]}"; do
    wait "${pid}"
done

sleep 2

# Analyze results
echo -e "${BLUE}📊 Analyzing results...${NC}"
echo "=========================="

successful_bookings=0
failed_bookings=0

for i in $(seq 1 ${CONCURRENT_REQUESTS}); do
    if [ -f "logs/result-${i}.tmp" ]; then
        result=$(cat "logs/result-${i}.tmp")
        if [ "${result}" = "0" ]; then
            successful_bookings=$((successful_bookings + 1))
            echo -e "Request ${i}: ${GREEN}SUCCESS${NC}"
        else
            failed_bookings=$((failed_bookings + 1))
            echo -e "Request ${i}: ${RED}FAILED${NC}"
        fi
        rm -f "logs/result-${i}.tmp"
    else
        failed_bookings=$((failed_bookings + 1))
        echo -e "Request ${i}: ${RED}NO RESULT${NC}"
    fi
done

echo "=========================="
echo -e "Successful bookings: ${GREEN}${successful_bookings}${NC}"
echo -e "Failed bookings: ${RED}${failed_bookings}${NC}"

# Verify pessimistic lock worked correctly
echo -e "${BLUE}🔍 Verifying pessimistic lock behavior...${NC}"

if [ "${successful_bookings}" = "1" ] && [ "${failed_bookings}" = "$((CONCURRENT_REQUESTS - 1))" ]; then
    echo -e "${GREEN}✅ PESSIMISTIC LOCK TEST PASSED!${NC}"
    echo -e "${GREEN}   Only 1 booking succeeded, ${failed_bookings} failed as expected${NC}"
    exit_code=0
else
    echo -e "${RED}❌ PESSIMISTIC LOCK TEST FAILED!${NC}"
    echo -e "${RED}   Expected: 1 success, $((CONCURRENT_REQUESTS - 1)) failures${NC}"
    echo -e "${RED}   Actual: ${successful_bookings} success, ${failed_bookings} failures${NC}"
    exit_code=1
fi

echo -e "${BLUE}📋 Check application logs for detailed lock behavior:${NC}"
echo "  - logs/instance-A.log"
echo "  - logs/instance-B.log" 
echo "  - logs/instance-C.log"

exit ${exit_code}