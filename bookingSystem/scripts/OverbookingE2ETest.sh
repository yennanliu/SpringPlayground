#!/usr/bin/env bash
# =============================================================================
# OverbookingE2ETest.sh — Proves Redis atomic DECR prevents overbooking
#
# What this script does:
#   1. Creates a resource with capacity=1
#   2. Fires N concurrent booking requests for the SAME time slot
#   3. Asserts exactly 1 succeeded (HTTP 201) and the rest were rejected (HTTP 409)
#   4. Cancels the successful booking and re-fires to prove the slot is restored
#
# Prerequisites:
#   - App running on localhost:8080  (mvn spring-boot:run)
#   - Redis running on localhost:6379 (booking.redis.enabled=true in application.yml)
#   - curl, jq
# =============================================================================

BASE_URL="http://localhost:8080"
CONCURRENT=10          # Number of simultaneous booking attempts
SLOT_START="2030-12-01T10:00:00"
SLOT_END="2030-12-01T11:00:00"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

pass() { echo -e "${GREEN}[PASS]${NC} $*"; }
fail() { echo -e "${RED}[FAIL]${NC} $*"; exit 1; }
info() { echo -e "${YELLOW}[INFO]${NC} $*"; }

# ---------------------------------------------------------------------------
# 0. Verify server is up
# ---------------------------------------------------------------------------
info "Checking server at $BASE_URL ..."
curl -sf "$BASE_URL/api/bookings" -o /dev/null || fail "Server not reachable. Start the app first."

# ---------------------------------------------------------------------------
# 1. Create a resource with capacity=1
# ---------------------------------------------------------------------------
info "Creating resource with capacity=1 ..."
RESOURCE=$(curl -sf -X POST "$BASE_URL/api/resources" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Room E2E","type":"room","capacity":1,"active":true}')

RESOURCE_ID=$(echo "$RESOURCE" | jq -r '.id')
[[ -z "$RESOURCE_ID" || "$RESOURCE_ID" == "null" ]] && fail "Failed to create resource: $RESOURCE"
pass "Resource created: $RESOURCE_ID (capacity=1)"

# ---------------------------------------------------------------------------
# 2. Fire N concurrent booking requests for the same slot
# ---------------------------------------------------------------------------
info "Firing $CONCURRENT concurrent booking requests for the same time slot ..."

TMPDIR_RESULTS=$(mktemp -d)

for i in $(seq 1 $CONCURRENT); do
  (
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/bookings" \
      -H "Content-Type: application/json" \
      -d "{
        \"resourceId\": \"$RESOURCE_ID\",
        \"userId\": \"user-$i\",
        \"startTime\": \"$SLOT_START\",
        \"endTime\": \"$SLOT_END\"
      }")
    echo "$HTTP_CODE" > "$TMPDIR_RESULTS/$i"
  ) &
done
wait

# ---------------------------------------------------------------------------
# 3. Count results
# ---------------------------------------------------------------------------
SUCCESS=0
CONFLICT=0
OTHER=0
BOOKING_ID=""

for i in $(seq 1 $CONCURRENT); do
  CODE=$(cat "$TMPDIR_RESULTS/$i")
  case "$CODE" in
    201) SUCCESS=$((SUCCESS + 1)) ;;
    409) CONFLICT=$((CONFLICT + 1)) ;;
    *)   OTHER=$((OTHER + 1)); info "Unexpected HTTP $CODE from request $i" ;;
  esac
done
rm -rf "$TMPDIR_RESULTS"

echo ""
info "Results: $CONCURRENT concurrent requests →"
echo "  HTTP 201 (booked):    $SUCCESS"
echo "  HTTP 409 (rejected):  $CONFLICT"
echo "  Other:                $OTHER"
echo ""

[[ $SUCCESS -eq 1 ]]                    && pass "Exactly 1 booking succeeded"       || fail "Expected 1 success, got $SUCCESS"
[[ $CONFLICT -eq $((CONCURRENT - 1)) ]] && pass "$CONFLICT requests correctly rejected" || fail "Expected $((CONCURRENT-1)) conflicts, got $CONFLICT"

# ---------------------------------------------------------------------------
# 4. Fetch the booking ID of the successful booking
# ---------------------------------------------------------------------------
BOOKING_PAGE=$(curl -sf "$BASE_URL/api/bookings?resourceId=$RESOURCE_ID&size=10")
BOOKING_ID=$(echo "$BOOKING_PAGE" | jq -r '.content[] | select(.status=="CONFIRMED") | .id' | head -1)
[[ -z "$BOOKING_ID" || "$BOOKING_ID" == "null" ]] && fail "Could not find the confirmed booking"
pass "Confirmed booking ID: $BOOKING_ID"

# ---------------------------------------------------------------------------
# 5. Cancel the booking — Redis slot should be restored (INCR)
# ---------------------------------------------------------------------------
info "Cancelling booking to restore the Redis slot ..."
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "$BASE_URL/api/bookings/$BOOKING_ID")
[[ "$HTTP_CODE" == "204" ]] && pass "Booking cancelled (HTTP 204)" || fail "Cancel failed (HTTP $HTTP_CODE)"

# ---------------------------------------------------------------------------
# 6. Retry a single booking — should succeed now that slot is restored
# ---------------------------------------------------------------------------
info "Retrying a single booking after cancellation ..."
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/bookings" \
  -H "Content-Type: application/json" \
  -d "{
    \"resourceId\": \"$RESOURCE_ID\",
    \"userId\": \"user-retry\",
    \"startTime\": \"$SLOT_START\",
    \"endTime\": \"$SLOT_END\"
  }")
[[ "$HTTP_CODE" == "201" ]] && pass "Slot restored — new booking succeeded (HTTP 201)" || fail "Expected 201 after cancel, got HTTP $HTTP_CODE"

# ---------------------------------------------------------------------------
echo ""
pass "All assertions passed. Redis overbooking prevention is working correctly."
