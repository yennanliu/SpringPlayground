#!/usr/bin/env bash
# =============================================================================
# pressure_test.sh — High-concurrency pressure test for ShoppingCart backend
#
# Usage:
#   ./scripts/pressure_test.sh [BASE_URL] [CONCURRENCY] [TOTAL_REQUESTS]
#
# Examples:
#   ./scripts/pressure_test.sh                         # defaults below
#   ./scripts/pressure_test.sh http://localhost:9999   # custom URL
#   ./scripts/pressure_test.sh http://localhost:9999 50 500
#
# What it tests:
#   1. Warmup  — small burst to prime any caches / JIT
#   2. Product listing (GET /product/) — benefits from Redis cache + HikariCP tuning
#   3. Category listing (GET /category/) — benefits from Redis cache
#   4. Concurrent auth attempts (POST /user/signin) — benefits from pool tuning
#
# Reports:
#   - Total requests, success (2xx), errors (non-2xx / timeouts)
#   - Total time, requests/sec, min/avg/max latency
# =============================================================================

BASE_URL="${1:-http://localhost:9999}"
CONCURRENCY="${2:-50}"
TOTAL_REQUESTS="${3:-300}"
TIMEOUT_SEC=10

# Colors
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; NC='\033[0m'

TMPDIR_RESULTS=$(mktemp -d)
trap "rm -rf $TMPDIR_RESULTS" EXIT

log()  { echo -e "${CYAN}[$(date '+%H:%M:%S')]${NC} $*"; }
ok()   { echo -e "${GREEN}[OK]${NC} $*"; }
warn() { echo -e "${YELLOW}[WARN]${NC} $*"; }
fail() { echo -e "${RED}[FAIL]${NC} $*"; }

# ── helpers ──────────────────────────────────────────────────────────────────

check_server() {
    log "Checking server at $BASE_URL ..."
    local http_code
    http_code=$(curl -s -o /dev/null -w "%{http_code}" --max-time 5 "$BASE_URL/product/" 2>/dev/null)
    if [[ "$http_code" == "200" || "$http_code" == "401" || "$http_code" == "403" ]]; then
        ok "Server is up (HTTP $http_code)"
        return 0
    else
        fail "Server unreachable at $BASE_URL (got: '$http_code'). Start the backend first."
        exit 1
    fi
}

# Send one curl request, write timing+status to a tmp file
send_request() {
    local url="$1"
    local method="${2:-GET}"
    local body="${3:-}"
    local out_file="$TMPDIR_RESULTS/r_$$_${RANDOM}"

    local curl_args=(
        -s -o /dev/null
        -w "%{http_code} %{time_total}"
        --max-time "$TIMEOUT_SEC"
        -X "$method"
    )
    [[ -n "$body" ]] && curl_args+=(-H "Content-Type: application/json" -d "$body")

    local result
    result=$(curl "${curl_args[@]}" "$url" 2>/dev/null)
    if [[ $? -ne 0 ]]; then
        echo "000 ${TIMEOUT_SEC}.000" >> "$out_file"
    else
        echo "$result" >> "$out_file"
    fi
}

# Run TOTAL_REQUESTS requests with at most CONCURRENCY in parallel
run_load() {
    local label="$1"; local url="$2"; local method="${3:-GET}"; local body="${4:-}"
    local total="$TOTAL_REQUESTS"; local concurrency="$CONCURRENCY"

    log "[$label] $method $url  (concurrency=$concurrency, total=$total)"

    local batch_file="$TMPDIR_RESULTS/${label}_results"
    > "$batch_file"

    local running=0
    local pids=()

    for ((i=1; i<=total; i++)); do
        (
            send_request "$url" "$method" "$body"
            # Each subprocess writes to the batch file
            echo "$result" >> "$batch_file"
        ) &
        pids+=($!)
        ((running++))

        if ((running >= concurrency)); then
            wait "${pids[@]}"
            pids=()
            running=0
        fi
    done
    [[ ${#pids[@]} -gt 0 ]] && wait "${pids[@]}"

    # collect all tmp result files for this run
    cat "$TMPDIR_RESULTS"/r_* 2>/dev/null >> "$batch_file" || true
    rm -f "$TMPDIR_RESULTS"/r_* 2>/dev/null || true

    print_stats "$label" "$batch_file"
}

print_stats() {
    local label="$1"; local file="$2"

    local total=0 success=0 errors=0
    local min_t=9999 max_t=0 sum_t=0
    local latencies=()

    while IFS=' ' read -r code time_s; do
        [[ -z "$code" ]] && continue
        ((total++))
        local code_int="${code//[^0-9]/}"
        if [[ "$code_int" =~ ^2 ]]; then ((success++)); else ((errors++)); fi

        # strip leading zero issues on macOS bc
        local t_ms
        t_ms=$(echo "$time_s * 1000" | bc 2>/dev/null | cut -d'.' -f1)
        t_ms="${t_ms:-0}"
        latencies+=("$t_ms")
        sum_t=$((sum_t + t_ms))
        (( t_ms < min_t )) && min_t=$t_ms
        (( t_ms > max_t )) && max_t=$t_ms
    done < "$file"

    [[ $total -eq 0 ]] && { warn "[$label] No results collected."; return; }

    local avg_t=$(( sum_t / total ))

    # p95 — sort latencies and pick 95th percentile
    IFS=$'\n' sorted=($(sort -n <<<"${latencies[*]}")); unset IFS
    local p95_idx=$(( total * 95 / 100 ))
    local p95_t="${sorted[$p95_idx]:-$max_t}"

    local error_rate
    error_rate=$(echo "scale=1; $errors * 100 / $total" | bc 2>/dev/null || echo "?")

    echo ""
    echo -e "  ${CYAN}── $label ─────────────────────────────────${NC}"
    echo -e "  Requests   : total=$total  success=${GREEN}$success${NC}  errors=${RED}$errors${NC}  error_rate=${error_rate}%"
    echo -e "  Latency    : min=${min_t}ms  avg=${avg_t}ms  p95=${p95_t}ms  max=${max_t}ms"
    echo ""
}

run_load_timed() {
    local label="$1"; local url="$2"; local method="${3:-GET}"; local body="${4:-}"
    local start_ts end_ts elapsed rps

    start_ts=$(date +%s%3N)
    run_load "$label" "$url" "$method" "$body"
    end_ts=$(date +%s%3N)
    elapsed=$(( end_ts - start_ts ))
    rps=$(echo "scale=1; $TOTAL_REQUESTS * 1000 / $elapsed" | bc 2>/dev/null || echo "?")
    echo -e "  Wall time  : ${elapsed}ms  →  ~${rps} req/sec"
    echo ""
}

# ── main ─────────────────────────────────────────────────────────────────────

echo ""
echo -e "${CYAN}════════════════════════════════════════════════════════${NC}"
echo -e "${CYAN}  ShoppingCart Pressure Test${NC}"
echo -e "${CYAN}  base_url=$BASE_URL  concurrency=$CONCURRENCY  total=$TOTAL_REQUESTS${NC}"
echo -e "${CYAN}════════════════════════════════════════════════════════${NC}"
echo ""

check_server

# ── 1. Warmup ────────────────────────────────────────────────────────────────
log "Phase 1/4 — Warmup (10 requests, primes Redis cache + JIT)"
ORIG_TOTAL=$TOTAL_REQUESTS; ORIG_CONC=$CONCURRENCY
TOTAL_REQUESTS=10; CONCURRENCY=5
run_load "warmup-products"   "$BASE_URL/product/"
run_load "warmup-categories" "$BASE_URL/category/"
TOTAL_REQUESTS=$ORIG_TOTAL; CONCURRENCY=$ORIG_CONC

# ── 2. Product list (GET /product/) ─────────────────────────────────────────
log "Phase 2/4 — Product listing (Redis-cached after warmup)"
run_load_timed "GET /product/" "$BASE_URL/product/"

# ── 3. Category list (GET /category/) ───────────────────────────────────────
log "Phase 3/4 — Category listing (Redis-cached after warmup)"
run_load_timed "GET /category/" "$BASE_URL/category/"

# ── 4. Auth endpoint (POST /user/signin) ────────────────────────────────────
log "Phase 4/4 — Auth sign-in (HikariCP pool stress)"
SIGNIN_BODY='{"email":"nonexistent@test.com","password":"wrongpassword"}'
run_load_timed "POST /user/signin" "$BASE_URL/user/signin" POST "$SIGNIN_BODY"

echo -e "${CYAN}════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}  Pressure test complete.${NC}"
echo -e "${CYAN}════════════════════════════════════════════════════════${NC}"
echo ""
echo "  Tips for interpreting results:"
echo "  • error_rate=0%  and avg latency drops after warmup → Redis cache is working"
echo "  • Throughput scales with CONCURRENCY up to HikariCP pool size (50)"
echo "  • Re-run with CONCURRENCY=200 to test beyond pool limits"
echo "  • To test with a real token: export TOKEN=<value> and hit /cart/ or /order/"
echo ""
