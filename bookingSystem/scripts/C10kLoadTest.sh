#!/usr/bin/env bash
# =============================================================================
# C10kLoadTest.sh — C10K Load Test for BookingHub
#
# Usage:
#   ./scripts/C10kLoadTest.sh                        # default: 10,000 requests
#   ./scripts/C10kLoadTest.sh 5000                   # custom request count
#   ./scripts/C10kLoadTest.sh 10000 localhost 8080   # custom host/port
#
# Prerequisites:
#   - App running (mvn spring-boot:run)
#   - curl, jq, bc
# =============================================================================

TOTAL_REQUESTS=${1:-10000}
HOST=${2:-localhost}
PORT=${3:-8080}
BASE_URL="http://$HOST:$PORT"

# Mix: 60% POST bookings, 40% GET
WRITE_RATIO=60   # percent

# Max parallel curl processes
MAX_CONCURRENT=200

USERS=("alice" "bob" "charlie" "diana" "eve" "frank" "grace" "henry")

# ── Colors ────────────────────────────────────────────────────────────────────
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

# ── Temp dir for per-request results ─────────────────────────────────────────
TMPDIR_RUN=$(mktemp -d)
trap 'rm -rf "$TMPDIR_RUN"' EXIT

# ── Metrics (files used for cross-process accumulation) ───────────────────────
METRICS_DIR="$TMPDIR_RUN/metrics"
mkdir -p "$METRICS_DIR"

inc_metric() { echo 1 >> "$METRICS_DIR/$1"; }
sum_metric()  { wc -l < "$METRICS_DIR/$1" 2>/dev/null | tr -d ' ' || echo 0; }

LATENCY_FILE="$TMPDIR_RUN/latencies"
touch "$LATENCY_FILE"

# ── Banner ────────────────────────────────────────────────────────────────────
print_banner() {
  echo -e "${CYAN}"
  echo "╔══════════════════════════════════════════════════════╗"
  echo "║          BookingHub — C10K Load Test                 ║"
  echo "║  Parallel curl · Bash · No external deps beyond jq  ║"
  echo "╚══════════════════════════════════════════════════════╝"
  echo -e "${NC}"
  printf "  Requests     : %'d\n" "$TOTAL_REQUESTS"
  printf "  Mix          : %d%% writes (POST /bookings) + %d%% reads\n" "$WRITE_RATIO" "$((100-WRITE_RATIO))"
  printf "  Max inflight : %d parallel processes\n\n" "$MAX_CONCURRENT"
}

# ── Health check ──────────────────────────────────────────────────────────────
ping_server() {
  local code
  code=$(curl -s -o /dev/null -w "%{http_code}" --max-time 5 "$BASE_URL/api/resources")
  [[ "$code" -lt 500 ]] 2>/dev/null
}

# ── Fetch resource IDs ────────────────────────────────────────────────────────
RESOURCE_IDS=()

fetch_resources() {
  local json
  json=$(curl -sf --max-time 10 "$BASE_URL/api/resources") || return 1
  mapfile -t RESOURCE_IDS < <(echo "$json" | jq -r '.[].id // .content[].id' 2>/dev/null)
  [[ ${#RESOURCE_IDS[@]} -gt 0 ]]
}

# ── Single request worker (runs in subshell) ──────────────────────────────────
send_request() {
  local req_idx=$1
  local t_start t_end latency code

  t_start=$(date +%s%3N)

  if (( req_idx % 100 < WRITE_RATIO )); then
    # POST booking
    local res_count=${#RESOURCE_IDS[@]}
    local res_idx=$(( RANDOM % res_count ))
    local resource_id="${RESOURCE_IDS[$res_idx]}"

    local days_ahead=$(( 1 + RANDOM % 90 ))
    local hour=$(( RANDOM % 23 ))
    local dur=$(( 1 + RANDOM % 2 ))
    local start_dt end_dt
    start_dt=$(date -v "+${days_ahead}d" -v "${hour}H" -v 0M -v 0S "+%Y-%m-%dT%H:%M:%S" 2>/dev/null \
             || date -d "+${days_ahead} days" "+%Y-%m-%dT${hour}:00:00")
    end_dt=$(date -v "+${days_ahead}d" -v "$(( hour + dur ))H" -v 0M -v 0S "+%Y-%m-%dT%H:%M:%S" 2>/dev/null \
           || date -d "+${days_ahead} days" "+%Y-%m-%dT$(( hour + dur )):00:00")

    local user="${USERS[$(( RANDOM % ${#USERS[@]} ))]}"
    local idem_key="idem-$$-${req_idx}-${RANDOM}"
    local body="{\"resourceId\":\"${resource_id}\",\"userId\":\"${user}\",\"startTime\":\"${start_dt}\",\"endTime\":\"${end_dt}\"}"

    code=$(curl -s -o /dev/null -w "%{http_code}" --max-time 30 \
      -X POST "$BASE_URL/api/bookings" \
      -H "Content-Type: application/json" \
      -H "Idempotency-Key: $idem_key" \
      -d "$body")
  else
    # GET
    if (( RANDOM % 2 == 0 )); then
      code=$(curl -s -o /dev/null -w "%{http_code}" --max-time 30 "$BASE_URL/api/bookings?size=20")
    else
      code=$(curl -s -o /dev/null -w "%{http_code}" --max-time 30 "$BASE_URL/api/resources")
    fi
  fi

  t_end=$(date +%s%3N)
  latency=$(( t_end - t_start ))
  echo "$latency" >> "$LATENCY_FILE"

  case "$code" in
    2??) inc_metric success ;;
    409) inc_metric conflict ;;
    4??) inc_metric client_err ;;
    5??) inc_metric server_err ;;
    *)   inc_metric network_err ;;
  esac
}

export -f send_request inc_metric
export TMPDIR_RUN METRICS_DIR LATENCY_FILE BASE_URL WRITE_RATIO
export USERS

# ── Run load (with bounded concurrency via xargs) ─────────────────────────────
run_load() {
  local count=$1 quiet=${2:-false}

  mkdir -p "$METRICS_DIR"

  # Export resource IDs as env var for subshells
  export RESOURCE_IDS_STR
  RESOURCE_IDS_STR=$(IFS=','; echo "${RESOURCE_IDS[*]}")

  # Wrapper that re-imports RESOURCE_IDS from env
  _worker() {
    local idx=$1
    IFS=',' read -ra RESOURCE_IDS <<< "$RESOURCE_IDS_STR"
    send_request "$idx"
  }
  export -f _worker send_request inc_metric

  seq 0 $(( count - 1 )) | xargs -P "$MAX_CONCURRENT" -I{} bash -c '_worker "$@"' _ {}

  if [[ "$quiet" == false ]]; then
    echo ""
  fi
}

# ── Percentile calculation ────────────────────────────────────────────────────
percentile() {
  local sorted_file=$1 pct=$2
  local total
  total=$(wc -l < "$sorted_file" | tr -d ' ')
  [[ "$total" -eq 0 ]] && { echo 0; return; }
  local idx=$(( (pct * total + 99) / 100 ))
  [[ $idx -lt 1 ]] && idx=1
  [[ $idx -gt $total ]] && idx=$total
  sed -n "${idx}p" "$sorted_file"
}

# ── Print report ──────────────────────────────────────────────────────────────
print_report() {
  local elapsed_ms=$1

  local ok conflict cli_err srv_err net_err
  ok=$(sum_metric success)
  conflict=$(sum_metric conflict)
  cli_err=$(sum_metric client_err)
  srv_err=$(sum_metric server_err)
  net_err=$(sum_metric network_err)
  local total=$(( ok + conflict + cli_err + srv_err + net_err ))

  local elapsed_sec
  elapsed_sec=$(echo "scale=2; $elapsed_ms / 1000" | bc)
  local throughput
  throughput=$(echo "scale=0; $total / $elapsed_sec" | bc 2>/dev/null || echo "N/A")

  local all_ok=$(( ok + conflict ))
  local success_rate=0
  [[ $total -gt 0 ]] && success_rate=$(echo "scale=1; $all_ok * 100 / $total" | bc)

  # Latency percentiles
  local sorted_file="$TMPDIR_RUN/sorted_latencies"
  sort -n "$LATENCY_FILE" > "$sorted_file"
  local lat_count
  lat_count=$(wc -l < "$sorted_file" | tr -d ' ')
  local avg=0
  [[ $lat_count -gt 0 ]] && avg=$(awk '{s+=$1} END{printf "%d", s/NR}' "$sorted_file")
  local p50 p90 p95 p99 pmax
  p50=$(percentile "$sorted_file" 50)
  p90=$(percentile "$sorted_file" 90)
  p95=$(percentile "$sorted_file" 95)
  p99=$(percentile "$sorted_file" 99)
  pmax=$(tail -1 "$sorted_file")

  pct_of() {
    local v=$1
    [[ $total -eq 0 ]] && echo "  0.0" && return
    echo "scale=1; $v * 100 / $total" | bc | awk '{printf "%5.1f", $1}'
  }

  local line="──────────────────────────────────────────────────────"
  echo ""
  echo "$line"
  echo "  C10K LOAD TEST RESULTS"
  echo "$line"
  printf "  Target          %s\n" "$BASE_URL"
  printf "  Total requests  %'d\n" "$total"
  printf "  Duration        %s s\n" "$elapsed_sec"
  printf "  Throughput      %s req/s\n" "$throughput"
  echo "$line"
  printf "  ✅ Success (2xx)      %6d  (%s%%)\n"  "$ok"      "$(pct_of $ok)"
  printf "  ⚡ Conflict (409)     %6d  (%s%%)  [expected]\n" "$conflict" "$(pct_of $conflict)"
  printf "  ⚠️  Client err (4xx)  %6d  (%s%%)\n"  "$cli_err" "$(pct_of $cli_err)"
  printf "  ❌ Server err (5xx)   %6d  (%s%%)\n"  "$srv_err" "$(pct_of $srv_err)"
  printf "  🔌 Network error      %6d  (%s%%)\n"  "$net_err" "$(pct_of $net_err)"
  echo "$line"
  printf "  System success rate   %s%%  (2xx + 409)\n" "$success_rate"
  echo "$line"
  echo "  LATENCY"
  printf "  avg   %5d ms\n" "$avg"
  printf "  p50   %5d ms\n" "$p50"
  printf "  p90   %5d ms\n" "$p90"
  printf "  p95   %5d ms\n" "$p95"
  printf "  p99   %5d ms\n" "$p99"
  printf "  max   %5d ms\n" "$pmax"
  echo "$line"

  local verdict_ok=true
  [[ $srv_err -gt 0 ]] && verdict_ok=false
  [[ $net_err -gt 0 ]] && verdict_ok=false
  local sr_int
  sr_int=$(echo "$success_rate" | cut -d. -f1)
  [[ $sr_int -lt 99 ]] && verdict_ok=false

  if $verdict_ok; then
    echo -e "  VERDICT  ${GREEN}✅  PASSED — system handled C10K load${NC}"
  else
    echo -e "  VERDICT  ${RED}❌  FAILED — see errors above${NC}"
    [[ $srv_err -gt 0 ]] && printf "           → %d server errors (5xx) — check app logs\n" "$srv_err"
    [[ $net_err -gt 0 ]] && printf "           → %d network errors — server may be overloaded\n" "$net_err"
    [[ $sr_int -lt 99 ]] && printf "           → success rate %s%% < 99%% threshold\n" "$success_rate"
  fi
  echo "$line"
  echo ""
}

# ── Main ──────────────────────────────────────────────────────────────────────
print_banner

echo "[ 1/4 ] Checking server at $BASE_URL ..."
if ! ping_server; then
  echo -e "${RED}        Server not reachable. Start the app first (mvn spring-boot:run)${NC}"
  exit 1
fi
echo "        OK"
echo ""

echo "[ 2/4 ] Fetching resources ..."
if ! fetch_resources; then
  echo -e "${RED}        No resources found. Run with DataInitializer (dev profile).${NC}"
  exit 1
fi
echo "        Found ${#RESOURCE_IDS[@]} resources"
echo ""

echo "[ 3/4 ] Warming up (100 requests) ..."
run_load 100 true
rm -f "$METRICS_DIR"/* "$LATENCY_FILE"
touch "$LATENCY_FILE"
echo "        Done"
echo ""

printf "[ 4/4 ] Firing %'d requests (max %d in-flight)...\n\n" "$TOTAL_REQUESTS" "$MAX_CONCURRENT"

T_START=$(date +%s%3N)
run_load "$TOTAL_REQUESTS" false
T_END=$(date +%s%3N)
ELAPSED=$(( T_END - T_START ))

print_report "$ELAPSED"
