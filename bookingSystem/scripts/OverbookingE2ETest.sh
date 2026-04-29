#!/usr/bin/env bash
# =============================================================================
# OverbookingE2ETest.sh — Proves Redis atomic DECR prevents overbooking
#
# Scenarios:
#   1. Capacity=1  — N concurrent requests, exactly 1 wins
#   2. Capacity=3  — N concurrent requests, exactly 3 win
#   3. Non-overlapping slots — same resource, different windows, both succeed
#   4. Partial overlap — slot that partially overlaps is rejected
#   5. Cancel restores slot — cancelled booking frees capacity for a new one
#   6. Resource isolation — overbooking on resource A does not affect resource B
#   7. Idempotency — same Idempotency-Key replayed returns 201 without double-booking
#
# Prerequisites:
#   - App running on localhost:8080  (mvn spring-boot:run)
#   - Redis running on localhost:6379 (booking.redis.enabled=true in application.yml)
#   - curl, jq
# =============================================================================

BASE_URL="http://localhost:8080"
CONCURRENT=10

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

PASS_COUNT=0
FAIL_COUNT=0

pass()    { echo -e "  ${GREEN}[PASS]${NC} $*"; PASS_COUNT=$((PASS_COUNT+1)); }
fail()    { echo -e "  ${RED}[FAIL]${NC} $*"; FAIL_COUNT=$((FAIL_COUNT+1)); }
info()    { echo -e "  ${YELLOW}[INFO]${NC} $*"; }
section() { echo -e "\n${CYAN}━━━ $* ━━━${NC}"; }

assert_eq() {
  local label="$1" actual="$2" expected="$3"
  if [[ "$actual" == "$expected" ]]; then
    pass "$label: $actual"
  else
    fail "$label: expected $expected, got $actual"
  fi
}

# Fire $1 concurrent POST requests to the same slot on resource $2.
# Writes HTTP codes to $TMPDIR/<n>. Returns after all finish.
fire_concurrent() {
  local count=$1 resource_id=$2 start=$3 end=$4
  local tmp
  tmp=$(mktemp -d)

  for i in $(seq 1 "$count"); do
    (
      code=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/bookings" \
        -H "Content-Type: application/json" \
        -d "{\"resourceId\":\"$resource_id\",\"userId\":\"user-$i\",\"startTime\":\"$start\",\"endTime\":\"$end\"}")
      echo "$code" > "$tmp/$i"
    ) &
  done
  wait

  local ok=0 conflict=0 other=0
  for i in $(seq 1 "$count"); do
    code=$(cat "$tmp/$i")
    case "$code" in
      201) ok=$((ok+1)) ;;
      409) conflict=$((conflict+1)) ;;
      *)   other=$((other+1)); info "Unexpected HTTP $code from request $i" ;;
    esac
  done
  rm -rf "$tmp"

  # Return via globals
  FIRE_OK=$ok
  FIRE_CONFLICT=$conflict
  FIRE_OTHER=$other
}

create_resource() {
  local name=$1 capacity=$2
  local body
  body=$(curl -sf -X POST "$BASE_URL/api/resources" \
    -H "Content-Type: application/json" \
    -d "{\"name\":\"$name\",\"type\":\"room\",\"capacity\":$capacity,\"active\":true}")
  echo "$body" | jq -r '.id'
}

confirmed_booking_id() {
  local resource_id=$1
  curl -sf "$BASE_URL/api/bookings?resourceId=$resource_id&size=20" \
    | jq -r '.content[] | select(.status=="CONFIRMED") | .id' | head -1
}

cancel_booking() {
  local booking_id=$1
  curl -s -o /dev/null -w "%{http_code}" -X DELETE "$BASE_URL/api/bookings/$booking_id"
}

# =============================================================================
# Preflight
# =============================================================================
section "Preflight"
info "Checking server at $BASE_URL ..."
curl -sf "$BASE_URL/api/bookings" -o /dev/null || { echo -e "${RED}[FATAL]${NC} Server not reachable."; exit 1; }
pass "Server is up"

# =============================================================================
# Scenario 1 — Capacity=1: only 1 of N concurrent requests succeeds
# =============================================================================
section "Scenario 1: Capacity=1 — $CONCURRENT concurrent requests, exactly 1 wins"

RES1=$(create_resource "E2E-Room-Cap1" 1)
[[ -z "$RES1" || "$RES1" == "null" ]] && { fail "Failed to create resource"; exit 1; }
info "Resource: $RES1 (capacity=1)"

fire_concurrent $CONCURRENT "$RES1" "2030-01-01T10:00:00" "2030-01-01T11:00:00"
info "$CONCURRENT requests → 201:$FIRE_OK  409:$FIRE_CONFLICT  other:$FIRE_OTHER"
assert_eq "Successful bookings" "$FIRE_OK" "1"
assert_eq "Rejected bookings"   "$FIRE_CONFLICT" "$((CONCURRENT-1))"

# =============================================================================
# Scenario 2 — Capacity=3: exactly 3 of N concurrent requests succeed
# =============================================================================
section "Scenario 2: Capacity=3 — $CONCURRENT concurrent requests, exactly 3 win"

RES2=$(create_resource "E2E-Room-Cap3" 3)
info "Resource: $RES2 (capacity=3)"

fire_concurrent $CONCURRENT "$RES2" "2030-02-01T10:00:00" "2030-02-01T11:00:00"
info "$CONCURRENT requests → 201:$FIRE_OK  409:$FIRE_CONFLICT  other:$FIRE_OTHER"
assert_eq "Successful bookings" "$FIRE_OK" "3"
assert_eq "Rejected bookings"   "$FIRE_CONFLICT" "$((CONCURRENT-3))"

# =============================================================================
# Scenario 3 — Non-overlapping slots on the same resource both succeed
# =============================================================================
section "Scenario 3: Non-overlapping slots — both should succeed"

RES3=$(create_resource "E2E-Room-Slots" 1)
info "Resource: $RES3 (capacity=1)"

CODE_A=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/bookings" \
  -H "Content-Type: application/json" \
  -d "{\"resourceId\":\"$RES3\",\"userId\":\"alice\",\"startTime\":\"2030-03-01T09:00:00\",\"endTime\":\"2030-03-01T10:00:00\"}")

CODE_B=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/bookings" \
  -H "Content-Type: application/json" \
  -d "{\"resourceId\":\"$RES3\",\"userId\":\"bob\",\"startTime\":\"2030-03-01T11:00:00\",\"endTime\":\"2030-03-01T12:00:00\"}")

info "Slot A (09-10): HTTP $CODE_A"
info "Slot B (11-12): HTTP $CODE_B"
assert_eq "Slot A (09-10)" "$CODE_A" "201"
assert_eq "Slot B (11-12)" "$CODE_B" "201"

# =============================================================================
# Scenario 4 — Partial overlap is rejected
# =============================================================================
section "Scenario 4: Partial time overlap — second booking must be rejected"

RES4=$(create_resource "E2E-Room-Overlap" 1)
info "Resource: $RES4 (capacity=1)"

CODE_FIRST=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/bookings" \
  -H "Content-Type: application/json" \
  -d "{\"resourceId\":\"$RES4\",\"userId\":\"alice\",\"startTime\":\"2030-04-01T10:00:00\",\"endTime\":\"2030-04-01T12:00:00\"}")

# Overlaps the tail of the first booking (10-12)
CODE_OVERLAP=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/bookings" \
  -H "Content-Type: application/json" \
  -d "{\"resourceId\":\"$RES4\",\"userId\":\"bob\",\"startTime\":\"2030-04-01T11:00:00\",\"endTime\":\"2030-04-01T13:00:00\"}")

info "First booking (10-12):         HTTP $CODE_FIRST"
info "Overlapping booking (11-13):   HTTP $CODE_OVERLAP"
assert_eq "First booking"       "$CODE_FIRST"   "201"
assert_eq "Overlapping booking" "$CODE_OVERLAP" "409"

# =============================================================================
# Scenario 5 — Cancel restores the Redis slot for a new booking
# =============================================================================
section "Scenario 5: Cancel restores slot — next booking should succeed"

RES5=$(create_resource "E2E-Room-Cancel" 1)
info "Resource: $RES5 (capacity=1)"

CODE_ORIG=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/bookings" \
  -H "Content-Type: application/json" \
  -d "{\"resourceId\":\"$RES5\",\"userId\":\"alice\",\"startTime\":\"2030-05-01T10:00:00\",\"endTime\":\"2030-05-01T11:00:00\"}")
assert_eq "Original booking" "$CODE_ORIG" "201"

# Capacity exhausted — another user should be rejected
CODE_REJECTED=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/bookings" \
  -H "Content-Type: application/json" \
  -d "{\"resourceId\":\"$RES5\",\"userId\":\"bob\",\"startTime\":\"2030-05-01T10:00:00\",\"endTime\":\"2030-05-01T11:00:00\"}")
assert_eq "Second attempt (should fail)" "$CODE_REJECTED" "409"

BID5=$(confirmed_booking_id "$RES5")
[[ -z "$BID5" || "$BID5" == "null" ]] && fail "No confirmed booking found for $RES5" || info "Cancelling booking $BID5 ..."
CANCEL_CODE=$(cancel_booking "$BID5")
assert_eq "Cancel" "$CANCEL_CODE" "204"

# Slot restored — next booking should succeed
CODE_AFTER=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/bookings" \
  -H "Content-Type: application/json" \
  -d "{\"resourceId\":\"$RES5\",\"userId\":\"charlie\",\"startTime\":\"2030-05-01T10:00:00\",\"endTime\":\"2030-05-01T11:00:00\"}")
assert_eq "Booking after cancel (slot restored)" "$CODE_AFTER" "201"

# =============================================================================
# Scenario 6 — Resource isolation: overbooking A does not affect B
# =============================================================================
section "Scenario 6: Resource isolation — exhausting A leaves B unaffected"

RES6A=$(create_resource "E2E-Room-IsoA" 1)
RES6B=$(create_resource "E2E-Room-IsoB" 1)
info "Resource A: $RES6A  |  Resource B: $RES6B"

# Exhaust resource A
fire_concurrent $CONCURRENT "$RES6A" "2030-06-01T10:00:00" "2030-06-01T11:00:00"
assert_eq "Resource A: only 1 wins" "$FIRE_OK" "1"

# Resource B should still accept a booking independently
CODE_B=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/bookings" \
  -H "Content-Type: application/json" \
  -d "{\"resourceId\":\"$RES6B\",\"userId\":\"dave\",\"startTime\":\"2030-06-01T10:00:00\",\"endTime\":\"2030-06-01T11:00:00\"}")
assert_eq "Resource B unaffected" "$CODE_B" "201"

# =============================================================================
# Scenario 7 — Idempotency: replaying the same key returns 201 without double-booking
# =============================================================================
section "Scenario 7: Idempotency key — replay must not consume an extra slot"

RES7=$(create_resource "E2E-Room-Idem" 1)
info "Resource: $RES7 (capacity=1)"
IDEM_KEY="test-idem-key-$(date +%s)"

BODY="{\"resourceId\":\"$RES7\",\"userId\":\"alice\",\"startTime\":\"2030-07-01T10:00:00\",\"endTime\":\"2030-07-01T11:00:00\"}"

CODE_FIRST=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/bookings" \
  -H "Content-Type: application/json" \
  -H "Idempotency-Key: $IDEM_KEY" \
  -d "$BODY")
assert_eq "First request" "$CODE_FIRST" "201"

CODE_REPLAY=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/bookings" \
  -H "Content-Type: application/json" \
  -H "Idempotency-Key: $IDEM_KEY" \
  -d "$BODY")
assert_eq "Replayed request (same key)" "$CODE_REPLAY" "201"

# After replay, capacity should still be 0 — a new user with no idempotency key must be rejected
CODE_NEW=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/bookings" \
  -H "Content-Type: application/json" \
  -d "{\"resourceId\":\"$RES7\",\"userId\":\"bob\",\"startTime\":\"2030-07-01T10:00:00\",\"endTime\":\"2030-07-01T11:00:00\"}")
assert_eq "New request after replay (must fail)" "$CODE_NEW" "409"

# =============================================================================
# Summary
# =============================================================================
echo ""
echo -e "${CYAN}━━━ Summary ━━━${NC}"
echo -e "  ${GREEN}Passed: $PASS_COUNT${NC}"
if [[ $FAIL_COUNT -gt 0 ]]; then
  echo -e "  ${RED}Failed: $FAIL_COUNT${NC}"
  exit 1
else
  echo -e "  ${GREEN}All scenarios passed. Redis overbooking prevention is working correctly.${NC}"
fi
