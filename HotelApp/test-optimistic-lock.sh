#!/bin/bash

# E2E Test Script for Optimistic Locking in Hotel Booking System
# This script simulates concurrent booking attempts to test optimistic locking

echo "============================================="
echo "Hotel App Optimistic Locking E2E Test"
echo "============================================="

# Configuration
BASE_URL="http://localhost:8080"
API_URL="$BASE_URL/api/hotel"
ROOM_ID=1
NUM_CONCURRENT_REQUESTS=5
GUEST_NAME_PREFIX="TestGuest"
EMAIL_PREFIX="testguest"
CHECK_IN_DATE=$(date -d "+1 day" '+%Y-%m-%d')
CHECK_OUT_DATE=$(date -d "+3 days" '+%Y-%m-%d')

echo "Configuration:"
echo "- Base URL: $BASE_URL"
echo "- Room ID: $ROOM_ID" 
echo "- Concurrent requests: $NUM_CONCURRENT_REQUESTS"
echo "- Check-in: $CHECK_IN_DATE"
echo "- Check-out: $CHECK_OUT_DATE"
echo ""

# Function to check if server is running
check_server() {
    echo "Checking if server is running..."
    if ! curl -s --fail "$BASE_URL/actuator/health" > /dev/null; then
        echo "❌ Server is not running at $BASE_URL"
        echo "Please start the application first: mvn spring-boot:run"
        exit 1
    fi
    echo "✅ Server is running"
}

# Function to get room details
get_room_details() {
    echo "Getting room details..."
    ROOM_RESPONSE=$(curl -s "$API_URL/rooms/$ROOM_ID")
    if echo "$ROOM_RESPONSE" | grep -q '"available":true'; then
        echo "✅ Room $ROOM_ID is available"
        echo "Room details: $ROOM_RESPONSE"
    else
        echo "❌ Room $ROOM_ID is not available or doesn't exist"
        echo "Room response: $ROOM_RESPONSE"
        echo "Please ensure the room exists and is available"
        exit 1
    fi
}

# Function to make a booking request
make_booking_request() {
    local request_id=$1
    local output_file="/tmp/booking_result_$request_id.json"
    local headers_file="/tmp/booking_headers_$request_id.txt"
    
    local booking_data=$(cat <<EOF
{
    "roomId": $ROOM_ID,
    "guestName": "${GUEST_NAME_PREFIX}${request_id}",
    "guestEmail": "${EMAIL_PREFIX}${request_id}@test.com",
    "checkInDate": "$CHECK_IN_DATE",
    "checkOutDate": "$CHECK_OUT_DATE"
}
EOF
)

    echo "Request $request_id: Attempting to book room $ROOM_ID..."
    
    # Make the booking request and capture both response and HTTP status
    curl -s -w "HTTP_STATUS:%{http_code}\nTIME_TOTAL:%{time_total}" \
         -H "Content-Type: application/json" \
         -d "$booking_data" \
         -D "$headers_file" \
         "$API_URL/bookings" > "$output_file" 2>&1
    
    # Extract HTTP status code
    local http_status=$(grep "HTTP_STATUS:" "$output_file" | cut -d: -f2)
    local response_body=$(grep -v "HTTP_STATUS:\|TIME_TOTAL:" "$output_file")
    
    echo "Request $request_id: HTTP Status $http_status"
    
    if [ "$http_status" = "200" ]; then
        echo "✅ Request $request_id: Booking successful!"
        echo "   Response: $response_body"
        echo "$request_id:SUCCESS:$response_body" >> /tmp/booking_results.txt
    elif [ "$http_status" = "409" ]; then
        echo "⚠️  Request $request_id: Conflict (Expected for optimistic locking)"
        echo "   Response: $response_body"
        echo "$request_id:CONFLICT:$response_body" >> /tmp/booking_results.txt
    elif [ "$http_status" = "400" ]; then
        echo "❌ Request $request_id: Bad request"
        echo "   Response: $response_body"
        echo "$request_id:BAD_REQUEST:$response_body" >> /tmp/booking_results.txt
    else
        echo "❌ Request $request_id: Unexpected status $http_status"
        echo "   Response: $response_body"
        echo "$request_id:ERROR:$response_body" >> /tmp/booking_results.txt
    fi
    
    # Cleanup temp files
    rm -f "$output_file" "$headers_file"
}

# Function to analyze results
analyze_results() {
    echo ""
    echo "============================================="
    echo "Test Results Analysis"
    echo "============================================="
    
    if [ ! -f /tmp/booking_results.txt ]; then
        echo "❌ No results file found"
        return 1
    fi
    
    local success_count=$(grep -c ":SUCCESS:" /tmp/booking_results.txt)
    local conflict_count=$(grep -c ":CONFLICT:" /tmp/booking_results.txt)
    local error_count=$(grep -c ":BAD_REQUEST:\|:ERROR:" /tmp/booking_results.txt)
    
    echo "Summary:"
    echo "- Successful bookings: $success_count"
    echo "- Conflicts (409): $conflict_count"
    echo "- Errors: $error_count"
    echo "- Total requests: $NUM_CONCURRENT_REQUESTS"
    echo ""
    
    if [ "$success_count" -eq 1 ] && [ "$conflict_count" -eq $((NUM_CONCURRENT_REQUESTS - 1)) ]; then
        echo "🎉 OPTIMISTIC LOCKING TEST PASSED!"
        echo "   - Exactly 1 booking succeeded (as expected)"
        echo "   - All other requests received 409 Conflict (as expected)"
        echo "   - This confirms optimistic locking is working correctly"
    elif [ "$success_count" -gt 1 ]; then
        echo "❌ OPTIMISTIC LOCKING TEST FAILED!"
        echo "   - Multiple bookings succeeded: $success_count"
        echo "   - This indicates a concurrency issue (race condition)"
        echo "   - Optimistic locking may not be working properly"
    elif [ "$success_count" -eq 0 ]; then
        echo "⚠️  NO SUCCESSFUL BOOKINGS"
        echo "   - This might indicate a configuration issue"
        echo "   - Check room availability and booking logic"
    else
        echo "⚠️  UNEXPECTED RESULTS"
        echo "   - Success: $success_count, Conflicts: $conflict_count, Errors: $error_count"
        echo "   - Please review the individual responses below"
    fi
    
    echo ""
    echo "Detailed Results:"
    cat /tmp/booking_results.txt | while IFS=':' read -r request_id status response; do
        echo "Request $request_id: $status"
        echo "  Response: $response"
        echo ""
    done
}

# Function to verify final room state
verify_room_state() {
    echo "============================================="
    echo "Final Room State Verification"
    echo "============================================="
    
    FINAL_ROOM_RESPONSE=$(curl -s "$API_URL/rooms/$ROOM_ID")
    if echo "$FINAL_ROOM_RESPONSE" | grep -q '"available":false'; then
        echo "✅ Room $ROOM_ID is now marked as unavailable (correct)"
    else
        echo "❌ Room $ROOM_ID is still available (unexpected)"
    fi
    
    echo "Final room state: $FINAL_ROOM_RESPONSE"
    
    # Get all bookings for this room
    echo ""
    echo "Bookings for room $ROOM_ID:"
    BOOKINGS_RESPONSE=$(curl -s "$API_URL/bookings")
    echo "$BOOKINGS_RESPONSE" | jq --arg room_id "$ROOM_ID" '.[] | select(.room.id == ($room_id | tonumber))' 2>/dev/null || echo "$BOOKINGS_RESPONSE"
}

# Function to cleanup
cleanup() {
    echo ""
    echo "Cleaning up temporary files..."
    rm -f /tmp/booking_results.txt
    rm -f /tmp/booking_result_*.json
    rm -f /tmp/booking_headers_*.txt
}

# Main execution
main() {
    echo "Starting optimistic locking test..."
    
    # Cleanup any previous test results
    cleanup
    
    # Pre-test checks
    check_server
    get_room_details
    
    echo ""
    echo "============================================="
    echo "Executing Concurrent Booking Requests"
    echo "============================================="
    
    # Create results file
    touch /tmp/booking_results.txt
    
    # Start concurrent booking requests
    pids=()
    for i in $(seq 1 $NUM_CONCURRENT_REQUESTS); do
        make_booking_request $i &
        pids+=($!)
        # Small delay to increase chance of true concurrency
        sleep 0.1
    done
    
    # Wait for all requests to complete
    echo ""
    echo "Waiting for all requests to complete..."
    for pid in "${pids[@]}"; do
        wait $pid
    done
    
    # Allow time for database transactions to complete
    sleep 2
    
    # Analyze results
    analyze_results
    
    # Verify final state
    verify_room_state
    
    # Cleanup
    cleanup
    
    echo ""
    echo "Test completed!"
}

# Run the test
main