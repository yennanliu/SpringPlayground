#!/usr/bin/env bash
# =============================================================================
# e2e_fullstack.sh — Full-stack end-to-end tests for ShoppingCart
#
# Simulates every user flow that the Vue.js frontend performs, calling the
# Spring Boot backend API at http://localhost:9999 directly (same calls the
# browser makes via Axios).
#
# Usage:
#   ./scripts/e2e_fullstack.sh                      # test against already-running services
#   ./scripts/e2e_fullstack.sh --start-docker       # start stack with docker-compose first
#   ./scripts/e2e_fullstack.sh --start-local        # start BE+FE locally (Maven + npm)
#   ./scripts/e2e_fullstack.sh --stop               # stop docker-compose after tests
#   BE_URL=http://host:9999 ./scripts/e2e_fullstack.sh   # custom backend URL
#
# Prerequisites (default — services already running):
#   Backend  → localhost:9999  (Spring Boot)
#   MySQL    → localhost:3306  (database: shopping_cart)
#   Redis    → localhost:6379  (cache)
#   Frontend → localhost:8080  (Vue.js)  ← checked but not required for API tests
#
# Prerequisites (--start-docker):
#   Docker + docker compose must be installed
#
# Prerequisites (--start-local):
#   Maven  in PATH (or ~/.local/maven4/bin/mvn)
#   MySQL + Redis running locally
#   npm   in PATH
# =============================================================================

set -uo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

# ── Configuration ─────────────────────────────────────────────────────────────
BE_URL="${BE_URL:-http://localhost:9999}"
FE_URL="${FE_URL:-http://localhost:8080}"
TIMEOUT=10          # curl timeout per request (seconds)
STARTUP_WAIT=120    # max seconds to wait for backend to become ready

# ── Flag parsing ──────────────────────────────────────────────────────────────
START_DOCKER=false
START_LOCAL=false
STOP_AFTER=false
SKIP_FE_CHECK=false

for arg in "$@"; do
    case "$arg" in
        --start-docker)  START_DOCKER=true  ;;
        --start-local)   START_LOCAL=true   ;;
        --stop)          STOP_AFTER=true    ;;
        --no-fe)         SKIP_FE_CHECK=true ;;
        --help|-h)
            sed -n '3,25p' "$0" | sed 's/^# \{0,2\}//'
            exit 0
            ;;
    esac
done

# ── Colors ────────────────────────────────────────────────────────────────────
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'
CYAN='\033[0;36m'; BOLD='\033[1m'; DIM='\033[2m'; NC='\033[0m'

# ── Test counters ─────────────────────────────────────────────────────────────
PASS=0; FAIL=0; SKIP=0
FAILED_TESTS=()

# ── Logging helpers ───────────────────────────────────────────────────────────
log()     { echo -e "${CYAN}[$(date '+%H:%M:%S')]${NC} $*"; }
section() { echo ""; echo -e "${BOLD}${CYAN}━━ $* ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"; }
pass()    { echo -e "  ${GREEN}✓${NC} $*"; ((PASS++)); }
fail()    { echo -e "  ${RED}✗${NC} $*"; ((FAIL++)); FAILED_TESTS+=("$*"); }
skip()    { echo -e "  ${YELLOW}○${NC} $* ${DIM}[skipped]${NC}"; ((SKIP++)); }

# ── HTTP helpers ──────────────────────────────────────────────────────────────

# api_call METHOD URL [BODY] → sets $HTTP_CODE and $BODY
api_call() {
    local method="$1" url="$2" body="${3:-}"
    local raw args
    args=(-s --max-time "$TIMEOUT" -w '\n%{http_code}' -X "$method")
    [[ -n "$body" ]] && args+=(-H "Content-Type: application/json" -d "$body")
    raw=$(curl "${args[@]}" "$url" 2>/dev/null) || raw=$'\n000'
    HTTP_CODE=$(echo "$raw" | tail -1)
    BODY=$(echo "$raw" | head -n -1)
}

GET()    { api_call GET    "$@"; }
POST()   { api_call POST   "$@"; }
PUT()    { api_call PUT    "$@"; }
DELETE() { api_call DELETE "$@"; }

# JSON field extractor — json_get '{"a":{"b":1}}' 'a.b'  → "1"
json_get() {
    local json="$1" path="$2"
    echo "$json" | python3 -c "
import json, sys
try:
    d = json.load(sys.stdin)
    for k in '$path'.split('.'):
        d = d[k] if isinstance(d, dict) else d[int(k)]
    print('' if d is None else d)
except:
    print('')
" 2>/dev/null
}

# json_array_len '{"items":[1,2,3]}' 'items'  → "3"
json_array_len() {
    local json="$1" path="$2"
    echo "$json" | python3 -c "
import json, sys
try:
    d = json.load(sys.stdin)
    for k in '$path'.split('.'):
        d = d[k] if isinstance(d, dict) else d[int(k)]
    print(len(d))
except:
    print(0)
" 2>/dev/null
}

# ── Assertion helpers ─────────────────────────────────────────────────────────

# assert_status LABEL EXPECTED_CODE
assert_status() {
    local label="$1" expected="$2"
    if [[ "$HTTP_CODE" == "$expected" ]]; then
        pass "$label  [HTTP $HTTP_CODE]"
    else
        fail "$label  [expected $expected, got $HTTP_CODE] ${BODY:0:100}"
    fi
}

# assert_field LABEL JSON KEY EXPECTED_VALUE
assert_field() {
    local label="$1" json="$2" key="$3" expected="$4"
    local actual; actual=$(json_get "$json" "$key")
    if [[ "$actual" == "$expected" ]]; then
        pass "$label  [$key=$actual]"
    else
        fail "$label  [expected $key='$expected', got '$actual']"
    fi
}

# assert_nonempty LABEL VALUE
assert_nonempty() {
    local label="$1" val="$2"
    if [[ -n "$val" && "$val" != "None" && "$val" != "null" ]]; then
        pass "$label  [non-empty]"
    else
        fail "$label  [got empty/null value]"
    fi
}

# assert_contains LABEL NEEDLE
assert_contains() {
    local label="$1" needle="$2"
    if echo "$BODY" | grep -q "$needle" 2>/dev/null; then
        pass "$label  [contains '$needle']"
    else
        fail "$label  ['$needle' not in: ${BODY:0:100}]"
    fi
}

# ── Unique test data generator ────────────────────────────────────────────────
uid() { python3 -c "import uuid; print(str(uuid.uuid4())[:8])"; }

# ── Service management ────────────────────────────────────────────────────────

wait_for_backend() {
    log "Waiting for backend at $BE_URL/product/ (up to ${STARTUP_WAIT}s) ..."
    local deadline=$(( $(date +%s) + STARTUP_WAIT ))
    while true; do
        local code
        code=$(curl -s -o /dev/null -w "%{http_code}" --max-time 5 "$BE_URL/product/" 2>/dev/null)
        if [[ "$code" =~ ^[2-4] ]]; then
            echo -e "  ${GREEN}Backend ready${NC} (HTTP $code)"
            return 0
        fi
        if (( $(date +%s) >= deadline )); then
            echo -e "  ${RED}Timed out waiting for backend${NC}"
            return 1
        fi
        sleep 3
    done
}

start_docker() {
    if ! command -v docker &>/dev/null; then
        echo -e "${RED}Docker not found. Install Docker Desktop first.${NC}"
        exit 1
    fi
    log "Starting full stack via docker compose ..."
    cd "$REPO_ROOT"
    docker compose up -d --build
    wait_for_backend
}

start_local() {
    # Find Maven
    local mvn_cmd
    if command -v mvn &>/dev/null; then
        mvn_cmd=mvn
    elif [[ -x "$HOME/.local/maven4/bin/mvn" ]]; then
        mvn_cmd="$HOME/.local/maven4/bin/mvn"
    else
        echo -e "${RED}Maven not found. Install with: brew install maven${NC}"; exit 1
    fi

    # Start backend in background if not running
    local be_code
    be_code=$(curl -s -o /dev/null -w "%{http_code}" --max-time 3 "$BE_URL/product/" 2>/dev/null)
    if [[ "$be_code" =~ ^[2-4] ]]; then
        log "Backend already running."
    else
        log "Starting backend (Spring Boot) in background ..."
        export JAVA_HOME; JAVA_HOME=$(/usr/libexec/java_home 2>/dev/null || echo "")
        export PATH="$HOME/.local/maven4/bin:$PATH"
        cd "$REPO_ROOT/Backend"
        nohup "$mvn_cmd" spring-boot:run -DskipTests \
            > /tmp/shoppingcart-be.log 2>&1 &
        echo "  Backend PID=$! → logs: /tmp/shoppingcart-be.log"
        wait_for_backend
        cd "$REPO_ROOT"
    fi

    # Start frontend in background if not running
    if [[ "$SKIP_FE_CHECK" == "false" ]]; then
        local fe_code
        fe_code=$(curl -s -o /dev/null -w "%{http_code}" --max-time 3 "$FE_URL/" 2>/dev/null)
        if [[ "$fe_code" =~ ^[2-4] ]]; then
            log "Frontend already running."
        else
            log "Starting frontend (Vue.js) in background ..."
            cd "$REPO_ROOT/Frondend/ecommerce-ui"
            nohup npm run serve > /tmp/shoppingcart-fe.log 2>&1 &
            echo "  Frontend PID=$! → logs: /tmp/shoppingcart-fe.log"
            cd "$REPO_ROOT"
        fi
    fi
}

stop_docker() {
    log "Stopping docker compose services ..."
    cd "$REPO_ROOT" && docker compose down
}

# ── Startup ───────────────────────────────────────────────────────────────────
echo ""
echo -e "${BOLD}${CYAN}╔══════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BOLD}${CYAN}║   ShoppingCart — Full-Stack E2E Test Suite                  ║${NC}"
echo -e "${BOLD}${CYAN}║   BE: $BE_URL   FE: $FE_URL          ║${NC}"
echo -e "${BOLD}${CYAN}╚══════════════════════════════════════════════════════════════╝${NC}"
echo ""

if   [[ "$START_DOCKER" == "true" ]]; then start_docker
elif [[ "$START_LOCAL"  == "true" ]]; then start_local
else
    # Just verify the backend is reachable
    be_code=$(curl -s -o /dev/null -w "%{http_code}" --max-time 5 "$BE_URL/product/" 2>/dev/null)
    if [[ ! "$be_code" =~ ^[2-4] ]]; then
        echo -e "${RED}Backend not reachable at $BE_URL/product/ (got: '$be_code')${NC}"
        echo "  Start services first, then re-run. Options:"
        echo "    docker compose up -d   (requires Docker)"
        echo "    $0 --start-local       (starts BE+FE directly)"
        exit 1
    fi
    log "Backend reachable (HTTP $be_code)"
fi

# ════════════════════════════════════════════════════════════════════════════
# HEALTH CHECKS
# ════════════════════════════════════════════════════════════════════════════
section "Health Checks"

GET "$BE_URL/product/"
assert_status "GET /product/  (backend up, public endpoint)" "200"

GET "$BE_URL/category/"
assert_status "GET /category/ (backend up, public endpoint)" "200"

if [[ "$SKIP_FE_CHECK" == "false" ]]; then
    fe_code=$(curl -s -o /dev/null -w "%{http_code}" --max-time 5 "$FE_URL/" 2>/dev/null)
    if [[ "$fe_code" =~ ^[2-4] ]]; then
        pass "GET $FE_URL/  (frontend up, HTTP $fe_code)"
    else
        skip "GET $FE_URL/  (frontend not running — API tests continue)"
    fi
fi

# ════════════════════════════════════════════════════════════════════════════
# FLOW 1 — User Registration & Authentication
# Vue pages: /signup  /signin  /user/userProfile
# ════════════════════════════════════════════════════════════════════════════
section "Flow 1 — User Registration & Authentication"

ID=$(uid)
EMAIL="e2e-${ID}@test.com"
PASS_WORD="pass${ID}"

# 1-1 Sign up new user
POST "$BE_URL/user/signup" \
  "{\"firstName\":\"E2E\",\"lastName\":\"User\",\"email\":\"$EMAIL\",\"password\":\"$PASS_WORD\"}"
assert_status "POST /user/signup (new user)" "200"
assert_field   "  status=SUCCESS" "$BODY" "status" "SUCCESS"

# 1-2 Duplicate email must fail
POST "$BE_URL/user/signup" \
  "{\"firstName\":\"E2E\",\"lastName\":\"User\",\"email\":\"$EMAIL\",\"password\":\"$PASS_WORD\"}"
if [[ "$HTTP_CODE" != "200" ]]; then
    pass "POST /user/signup (duplicate email) → non-200 [HTTP $HTTP_CODE]"
else
    fail "POST /user/signup (duplicate email) → expected error, got 200"
fi

# 1-3 Sign in — get token
POST "$BE_URL/user/signIn" \
  "{\"email\":\"$EMAIL\",\"password\":\"$PASS_WORD\"}"
assert_status "POST /user/signIn" "200"
TOKEN=$(json_get "$BODY" "token")
assert_nonempty "  token returned" "$TOKEN"
log "  token=${TOKEN:0:8}..."

# 1-4 Get user profile with valid token
GET "$BE_URL/user/userProfile?token=$TOKEN"
assert_status  "GET /user/userProfile (valid token)" "200"
assert_field   "  email matches" "$BODY" "email" "$EMAIL"

# 1-5 Invalid token must fail
GET "$BE_URL/cart/?token=INVALID-TOKEN-XYZ"
if [[ "$HTTP_CODE" != "200" ]]; then
    pass "GET /cart/ (invalid token) → non-200 [HTTP $HTTP_CODE]"
else
    fail "GET /cart/ (invalid token) → expected error, got 200"
fi

# 1-6 Wrong password sign-in must fail
POST "$BE_URL/user/signIn" "{\"email\":\"$EMAIL\",\"password\":\"wrongpassword\"}"
if [[ "$HTTP_CODE" != "200" ]]; then
    pass "POST /user/signIn (wrong password) → non-200 [HTTP $HTTP_CODE]"
else
    fail "POST /user/signIn (wrong password) → expected error, got 200"
fi

# ════════════════════════════════════════════════════════════════════════════
# FLOW 2 — Category Management
# Vue pages: /admin/category  /admin/category/add  /admin/category/:id
# ════════════════════════════════════════════════════════════════════════════
section "Flow 2 — Category Management"

CAT_NAME="E2E-Cat-$(uid)"

# 2-1 Create category
POST "$BE_URL/category/create" \
  "{\"categoryName\":\"$CAT_NAME\",\"description\":\"e2e test category\",\"imageUrl\":\"img.png\"}"
assert_status "POST /category/create" "201"
assert_field  "  success=true" "$BODY" "success" "True"

# 2-2 Duplicate category → 409
POST "$BE_URL/category/create" \
  "{\"categoryName\":\"$CAT_NAME\",\"description\":\"dup\",\"imageUrl\":\"img.png\"}"
assert_status "POST /category/create (duplicate) → 409" "409"
assert_field  "  success=false" "$BODY" "success" "False"

# 2-3 List categories — find our new one
GET "$BE_URL/category/"
assert_status "GET /category/" "200"
CAT_ID=$(echo "$BODY" | python3 -c "
import json, sys
cats = json.load(sys.stdin)
found = [c['id'] for c in cats if c.get('categoryName') == '$CAT_NAME']
print(found[0] if found else '')
" 2>/dev/null)
assert_nonempty "  category present in list" "$CAT_ID"
log "  categoryId=$CAT_ID"

# 2-4 Update category
POST "$BE_URL/category/update/$CAT_ID" \
  "{\"categoryName\":\"Updated-$CAT_NAME\",\"description\":\"updated\",\"imageUrl\":\"new.png\"}"
assert_status "POST /category/update/$CAT_ID" "200"

# 2-5 Updated name visible
GET "$BE_URL/category/"
UPDATED_CAT=$(echo "$BODY" | python3 -c "
import json, sys
cats = json.load(sys.stdin)
found = [c for c in cats if c.get('categoryName') == 'Updated-$CAT_NAME']
print('found' if found else '')
" 2>/dev/null)
if [[ "$UPDATED_CAT" == "found" ]]; then
    pass "Category updated name visible in list"
else
    fail "Category updated name NOT visible in list"
fi

# ════════════════════════════════════════════════════════════════════════════
# FLOW 3 — Product Management + Cache
# Vue pages: /product  /admin/product/add  /admin/product/:id
# ════════════════════════════════════════════════════════════════════════════
section "Flow 3 — Product Management + Cache"

PROD_NAME="E2E-Prod-$(uid)"

# 3-1 Add product
POST "$BE_URL/product/add" \
  "{\"name\":\"$PROD_NAME\",\"imageURL\":\"img.jpg\",\"price\":99.99,\"description\":\"e2e product\",\"categoryId\":$CAT_ID}"
assert_status "POST /product/add" "201"

# 3-2 List products (cache primed)
GET "$BE_URL/product/"
assert_status "GET /product/ (first call — primes cache)" "200"
PROD_COUNT1=$(json_array_len "$BODY" "")
assert_nonempty "  product list non-empty" "$PROD_COUNT1"

PROD_ID=$(echo "$BODY" | python3 -c "
import json, sys
prods = json.load(sys.stdin)
found = [p['id'] for p in prods if p.get('name') == '$PROD_NAME']
print(found[0] if found else '')
" 2>/dev/null)
assert_nonempty "  new product in list" "$PROD_ID"
log "  productId=$PROD_ID  listSize=$PROD_COUNT1"

# 3-3 List products again — should return same count (cache hit)
GET "$BE_URL/product/"
PROD_COUNT2=$(json_array_len "$BODY" "")
if [[ "$PROD_COUNT1" == "$PROD_COUNT2" ]]; then
    pass "GET /product/ (second call — cache hit, consistent count=$PROD_COUNT2)"
else
    fail "GET /product/ cache inconsistency: first=$PROD_COUNT1 second=$PROD_COUNT2"
fi

# 3-4 Update product (should evict cache)
POST "$BE_URL/product/update/$PROD_ID" \
  "{\"name\":\"Updated-$PROD_NAME\",\"imageURL\":\"new.jpg\",\"price\":149.99,\"description\":\"updated\",\"categoryId\":$CAT_ID}"
assert_status "POST /product/update/$PROD_ID (evicts cache)" "200"

# 3-5 List after eviction — updated name must appear
GET "$BE_URL/product/"
UPDATED_PROD=$(echo "$BODY" | python3 -c "
import json, sys
prods = json.load(sys.stdin)
found = [p for p in prods if p.get('name') == 'Updated-$PROD_NAME']
print('found' if found else '')
" 2>/dev/null)
if [[ "$UPDATED_PROD" == "found" ]]; then
    pass "GET /product/ after update — cache evicted, fresh data visible"
else
    fail "GET /product/ after update — updated product NOT visible"
fi

# 3-6 Add product with invalid category → 409
POST "$BE_URL/product/add" \
  "{\"name\":\"BadProd\",\"imageURL\":\"img.jpg\",\"price\":1.0,\"description\":\"bad\",\"categoryId\":99999}"
assert_status "POST /product/add (invalid categoryId) → 409" "409"

# ════════════════════════════════════════════════════════════════════════════
# FLOW 4 — Cart (add / list / update / delete)
# Vue pages: /cart
# ════════════════════════════════════════════════════════════════════════════
section "Flow 4 — Cart Operations"

# Create a fresh user for cart tests
CART_EMAIL="cart-$(uid)@test.com"
POST "$BE_URL/user/signup" \
  "{\"firstName\":\"Cart\",\"lastName\":\"User\",\"email\":\"$CART_EMAIL\",\"password\":\"cart123\"}"
POST "$BE_URL/user/signIn" "{\"email\":\"$CART_EMAIL\",\"password\":\"cart123\"}"
CART_TOKEN=$(json_get "$BODY" "token")
log "  cart user token=${CART_TOKEN:0:8}..."

# 4-1 Add to cart
POST "$BE_URL/cart/add?token=$CART_TOKEN" \
  "{\"productId\":$PROD_ID,\"quantity\":2}"
assert_status "POST /cart/add (qty=2)" "201"

# 4-2 List cart — verify item
GET "$BE_URL/cart/?token=$CART_TOKEN"
assert_status "GET /cart/" "200"
CART_ITEM_COUNT=$(json_array_len "$BODY" "cartItems")
if [[ "$CART_ITEM_COUNT" == "1" ]]; then
    pass "  cart has 1 item"
else
    fail "  cart item count: expected 1, got $CART_ITEM_COUNT"
fi
CART_ITEM_QTY=$(json_get "$BODY" "cartItems.0.quantity")
if [[ "$CART_ITEM_QTY" == "2" ]]; then
    pass "  cart item quantity=2"
else
    fail "  cart item quantity: expected 2, got $CART_ITEM_QTY"
fi
CART_ITEM_ID=$(json_get "$BODY" "cartItems.0.id")
TOTAL_COST=$(json_get "$BODY" "totalCost")
log "  cartItemId=$CART_ITEM_ID  totalCost=$TOTAL_COST"

# 4-3 Update cart quantity
PUT "$BE_URL/cart/update/$CART_ITEM_ID?token=$CART_TOKEN" \
  "{\"id\":$CART_ITEM_ID,\"productId\":$PROD_ID,\"quantity\":5}"
assert_status "PUT /cart/update/$CART_ITEM_ID (qty=5)" "200"

GET "$BE_URL/cart/?token=$CART_TOKEN"
NEW_QTY=$(json_get "$BODY" "cartItems.0.quantity")
if [[ "$NEW_QTY" == "5" ]]; then
    pass "  cart quantity updated to 5"
else
    fail "  cart quantity update: expected 5, got $NEW_QTY"
fi

# 4-4 Delete cart item
DELETE "$BE_URL/cart/delete/$CART_ITEM_ID?token=$CART_TOKEN"
assert_status "DELETE /cart/delete/$CART_ITEM_ID" "200"

GET "$BE_URL/cart/?token=$CART_TOKEN"
AFTER_DELETE=$(json_array_len "$BODY" "cartItems")
if [[ "$AFTER_DELETE" == "0" ]]; then
    pass "  cart empty after delete"
else
    fail "  cart not empty after delete: $AFTER_DELETE items remain"
fi

# 4-5 Delete non-existent item — must fail
DELETE "$BE_URL/cart/delete/999999?token=$CART_TOKEN"
if [[ "$HTTP_CODE" != "200" ]]; then
    pass "DELETE /cart/delete/999999 (not found) → non-200 [HTTP $HTTP_CODE]"
else
    fail "DELETE /cart/delete/999999 (not found) → expected error, got 200"
fi

# ════════════════════════════════════════════════════════════════════════════
# FLOW 5 — WishList
# Vue pages: /wishlist
# ════════════════════════════════════════════════════════════════════════════
section "Flow 5 — WishList"

WL_EMAIL="wl-$(uid)@test.com"
POST "$BE_URL/user/signup" \
  "{\"firstName\":\"WL\",\"lastName\":\"User\",\"email\":\"$WL_EMAIL\",\"password\":\"wl123\"}"
POST "$BE_URL/user/signIn" "{\"email\":\"$WL_EMAIL\",\"password\":\"wl123\"}"
WL_TOKEN=$(json_get "$BODY" "token")

# 5-1 Add product to wishlist
POST "$BE_URL/wishlist/add?token=$WL_TOKEN" "{\"id\":$PROD_ID}"
assert_status "POST /wishlist/add" "201"

# 5-2 Get wishlist — product should be there
GET "$BE_URL/wishlist/$WL_TOKEN"
assert_status "GET /wishlist/$WL_TOKEN" "200"
WL_LEN=$(json_array_len "$BODY" "")
if [[ "$WL_LEN" == "1" ]]; then
    pass "  wishlist has 1 item"
else
    fail "  wishlist item count: expected 1, got $WL_LEN"
fi

# ════════════════════════════════════════════════════════════════════════════
# FLOW 6 — Product Search
# Vue component: Navbar.vue search bar
# ════════════════════════════════════════════════════════════════════════════
section "Flow 6 — Product Search"

SEARCH_NAME="SearchItem-$(uid)"
POST "$BE_URL/product/add" \
  "{\"name\":\"$SEARCH_NAME\",\"imageURL\":\"img.jpg\",\"price\":5.0,\"description\":\"searchable\",\"categoryId\":$CAT_ID}"

GET "$BE_URL/search/api?query=$SEARCH_NAME"
assert_status "GET /search/api?query=$SEARCH_NAME" "200"
SEARCH_HITS=$(json_array_len "$BODY" "")
if (( SEARCH_HITS >= 1 )); then
    pass "  search returned $SEARCH_HITS result(s)"
else
    fail "  search returned 0 results for '$SEARCH_NAME'"
fi

# Partial match
PARTIAL="${SEARCH_NAME:0:8}"
GET "$BE_URL/search/api?query=$PARTIAL"
assert_status "GET /search/api?query=$PARTIAL (partial match)" "200"
PARTIAL_HITS=$(json_array_len "$BODY" "")
if (( PARTIAL_HITS >= 1 )); then
    pass "  partial search returned $PARTIAL_HITS result(s)"
else
    fail "  partial search returned 0 results for '$PARTIAL'"
fi

# ════════════════════════════════════════════════════════════════════════════
# FLOW 7 — Orders
# Vue pages: /order  /order/:id  (after adding to cart and checking out)
# ════════════════════════════════════════════════════════════════════════════
section "Flow 7 — Orders"

ORD_EMAIL="ord-$(uid)@test.com"
POST "$BE_URL/user/signup" \
  "{\"firstName\":\"Ord\",\"lastName\":\"User\",\"email\":\"$ORD_EMAIL\",\"password\":\"ord123\"}"
POST "$BE_URL/user/signIn" "{\"email\":\"$ORD_EMAIL\",\"password\":\"ord123\"}"
ORD_TOKEN=$(json_get "$BODY" "token")

# Add a product to cart first
POST "$BE_URL/cart/add?token=$ORD_TOKEN" "{\"productId\":$PROD_ID,\"quantity\":1}"

# 7-1 Place order (uses cart contents; sessionId simulates Stripe session)
POST "$BE_URL/order/add?token=$ORD_TOKEN&sessionId=e2e-session-$(uid)"
assert_status "POST /order/add (place order)" "201"

# 7-2 Cart must be cleared after order placement
GET "$BE_URL/cart/?token=$ORD_TOKEN"
CART_AFTER=$(json_array_len "$BODY" "cartItems")
if [[ "$CART_AFTER" == "0" ]]; then
    pass "  cart cleared after order (0 items)"
else
    fail "  cart not cleared: $CART_AFTER items remain"
fi

# 7-3 List orders
GET "$BE_URL/order/?token=$ORD_TOKEN"
assert_status "GET /order/" "200"
ORD_COUNT=$(json_array_len "$BODY" "")
if (( ORD_COUNT >= 1 )); then
    pass "  order list has $ORD_COUNT order(s)"
else
    fail "  order list is empty"
fi
ORD_ID=$(json_get "$BODY" "0.id")
assert_nonempty "  orderId returned" "$ORD_ID"
log "  orderId=$ORD_ID"

# 7-4 Total price > 0 (product has price 149.99)
ORD_TOTAL=$(json_get "$BODY" "0.totalPrice")
if python3 -c "exit(0 if float('$ORD_TOTAL') > 0 else 1)" 2>/dev/null; then
    pass "  order totalPrice=$ORD_TOTAL (> 0)"
else
    fail "  order totalPrice=$ORD_TOTAL (expected > 0)"
fi

# 7-5 Get order by ID
GET "$BE_URL/order/$ORD_ID?token=$ORD_TOKEN"
assert_status "GET /order/$ORD_ID" "200"
assert_field  "  id matches" "$BODY" "id" "$ORD_ID"

# 7-6 Get non-existent order → 404
GET "$BE_URL/order/999999?token=$ORD_TOKEN"
assert_status "GET /order/999999 (not found) → 404" "404"

# ════════════════════════════════════════════════════════════════════════════
# FLOW 8 — Concurrent Cache Reads (Approach 3 validation)
# Verifies the Redis/ConcurrentMap cache handles concurrent reads correctly
# ════════════════════════════════════════════════════════════════════════════
section "Flow 8 — Concurrent Reads (cache / HikariCP stress)"

CONC=30   # simultaneous requests
log "Firing $CONC concurrent GET /product/ ..."
TMPDIR_CC=$(mktemp -d); trap "rm -rf $TMPDIR_CC" EXIT

for i in $(seq 1 $CONC); do
    (
        code=$(curl -s -o /dev/null -w "%{http_code}" --max-time "$TIMEOUT" "$BE_URL/product/" 2>/dev/null)
        echo "$code" > "$TMPDIR_CC/$i"
    ) &
done
wait

OK_CC=0; FAIL_CC=0
for i in $(seq 1 $CONC); do
    code=$(cat "$TMPDIR_CC/$i" 2>/dev/null)
    [[ "$code" == "200" ]] && ((OK_CC++)) || ((FAIL_CC++))
done
if [[ "$FAIL_CC" -eq 0 ]]; then
    pass "$CONC concurrent GET /product/ — all 200 (OK=$OK_CC errors=$FAIL_CC)"
else
    fail "$CONC concurrent GET /product/ — $FAIL_CC errors out of $CONC"
fi

log "Firing $CONC concurrent POST /user/signIn (HikariCP pool test) ..."
for i in $(seq 1 $CONC); do
    (
        code=$(curl -s -o /dev/null -w "%{http_code}" --max-time "$TIMEOUT" \
            -X POST -H "Content-Type: application/json" \
            -d '{"email":"pool@test.com","password":"wrong"}' \
            "$BE_URL/user/signIn" 2>/dev/null)
        echo "$code" > "$TMPDIR_CC/signin_$i"
    ) &
done
wait

POOL_ERRS=0
for i in $(seq 1 $CONC); do
    code=$(cat "$TMPDIR_CC/signin_$i" 2>/dev/null)
    [[ "$code" == "503" || "$code" == "504" || "$code" == "000" ]] && ((POOL_ERRS++))
done
if [[ "$POOL_ERRS" -eq 0 ]]; then
    pass "$CONC concurrent sign-in requests — HikariCP pool OK (no 503/504/timeout)"
else
    fail "$CONC concurrent sign-in requests — $POOL_ERRS pool exhaustion errors"
fi

# ════════════════════════════════════════════════════════════════════════════
# SUMMARY
# ════════════════════════════════════════════════════════════════════════════
echo ""
echo -e "${BOLD}${CYAN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BOLD}  Test Summary${NC}"
echo -e "${BOLD}${CYAN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "  ${GREEN}PASS: $PASS${NC}   ${RED}FAIL: $FAIL${NC}   ${YELLOW}SKIP: $SKIP${NC}"

if (( FAIL > 0 )); then
    echo ""
    echo -e "${RED}  Failed tests:${NC}"
    for t in "${FAILED_TESTS[@]}"; do echo -e "    ${RED}✗${NC} $t"; done
fi

echo ""

if [[ "$STOP_AFTER" == "true" ]]; then stop_docker; fi

# Exit code reflects failures
[[ "$FAIL" -eq 0 ]]
