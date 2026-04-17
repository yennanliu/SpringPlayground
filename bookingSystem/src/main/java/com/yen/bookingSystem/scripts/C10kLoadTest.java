package com.yen.bookingSystem.scripts; ///usr/bin/env java --source 21 "$0" "$@"; exit $?

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * C10K Load Test for BookingHub
 *
 * Usage:
 *   java scripts/com.yen.bookingSystem.scripts.C10kLoadTest.java                    # default: 10,000 requests
 *   java scripts/com.yen.bookingSystem.scripts.C10kLoadTest.java 5000               # custom request count
 *   java scripts/com.yen.bookingSystem.scripts.C10kLoadTest.java 10000 localhost 8080
 *
 * Requires JDK 21+. No external dependencies.
 * The server must be running before you execute this script.
 */
public class C10kLoadTest {

    // ── Config ────────────────────────────────────────────────────────────────
    static int    TOTAL_REQUESTS = 10_000;
    static String HOST           = "localhost";
    static int    PORT           = 8080;
    static String BASE_URL;

    // Mix: 60% POST bookings, 40% GET requests
    static final double WRITE_RATIO = 0.60;

    // How many virtual threads to use (bounded to avoid overwhelming the JVM)
    static final int MAX_CONCURRENT = 1_000;

    // ── Metrics ───────────────────────────────────────────────────────────────
    static final LongAdder  totalSent      = new LongAdder();
    static final LongAdder  successCount   = new LongAdder();   // 2xx
    static final LongAdder  conflictCount  = new LongAdder();   // 409
    static final LongAdder  clientErrCount = new LongAdder();   // 4xx (not 409)
    static final LongAdder  serverErrCount = new LongAdder();   // 5xx
    static final LongAdder  networkErrors  = new LongAdder();   // IOException / timeout
    static final LongAdder  totalLatencyMs = new LongAdder();

    static final ConcurrentLinkedQueue<Long> latencySamples = new ConcurrentLinkedQueue<>();

    // ── State ─────────────────────────────────────────────────────────────────
    static final List<String> resourceIds = new CopyOnWriteArrayList<>();
    static final Random       rng         = new Random(42);
    static final DateTimeFormatter FMT     = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    static HttpClient httpClient;

    // ─────────────────────────────────────────────────────────────────────────
    public static void main(String[] args) throws Exception {
        // Parse args
        if (args.length >= 1) TOTAL_REQUESTS = Integer.parseInt(args[0]);
        if (args.length >= 2) HOST           = args[1];
        if (args.length >= 3) PORT           = Integer.parseInt(args[2]);
        BASE_URL = "http://" + HOST + ":" + PORT;

        httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

        printBanner();

        // ── Step 1: health check ──────────────────────────────────────────────
        System.out.println("[ 1/4 ] Checking server at " + BASE_URL + " ...");
        if (!ping()) {
            System.err.println("       Server not reachable. Start the app first (mvn spring-boot:run)");
            System.exit(1);
        }
        System.out.println("        OK\n");

        // ── Step 2: fetch resources ───────────────────────────────────────────
        System.out.println("[ 2/4 ] Fetching resources ...");
        fetchResources();
        if (resourceIds.isEmpty()) {
            System.err.println("       No resources found. Run the app with DataInitializer (dev profile).");
            System.exit(1);
        }
        System.out.println("        Found " + resourceIds.size() + " resources\n");

        // ── Step 3: warmup ────────────────────────────────────────────────────
        System.out.println("[ 3/4 ] Warming up (100 requests) ...");
        runLoad(100, true);
        resetMetrics();
        System.out.println("        Done\n");

        // ── Step 4: full load test ────────────────────────────────────────────
        System.out.printf("[ 4/4 ] Firing %,d concurrent requests (virtual threads, max %d in-flight)...%n%n",
            TOTAL_REQUESTS, MAX_CONCURRENT);

        long start = System.currentTimeMillis();
        runLoad(TOTAL_REQUESTS, false);
        long elapsed = System.currentTimeMillis() - start;

        // ── Report ────────────────────────────────────────────────────────────
        printReport(elapsed);
    }

    // ─────────────────────────────────────────────────────────────────────────
    static void runLoad(int count, boolean quiet) throws InterruptedException {
        Semaphore semaphore = new Semaphore(MAX_CONCURRENT);
        CountDownLatch done = new CountDownLatch(count);
        AtomicInteger progress = new AtomicInteger(0);

        for (int i = 0; i < count; i++) {
            semaphore.acquire();
            final int req = i;
            Thread.ofVirtual().start(() -> {
                try {
                    boolean isWrite = (req % 100) < (int)(WRITE_RATIO * 100);
                    if (isWrite) sendBooking();
                    else         sendGet();
                } finally {
                    semaphore.release();
                    done.countDown();
                    if (!quiet) {
                        int done2 = progress.incrementAndGet();
                        if (done2 % 1000 == 0) {
                            System.out.printf("        %,6d / %,d sent  (ok=%,d  conflict=%,d  err=%,d)%n",
                                done2, count, successCount.sum(), conflictCount.sum(),
                                clientErrCount.sum() + serverErrCount.sum() + networkErrors.sum());
                        }
                    }
                }
            });
        }
        done.await();
    }

    // ── Individual request senders ────────────────────────────────────────────
    static void sendBooking() {
        String resourceId = resourceIds.get(rng.nextInt(resourceIds.size()));

        // Random future slot: now + 1..90 days, random hour 0..22, 1-2h duration
        LocalDateTime base  = LocalDateTime.now().plusDays(1 + rng.nextInt(90));
        int startHour       = rng.nextInt(23);
        int durationHours   = 1 + rng.nextInt(2);
        LocalDateTime start = base.withHour(startHour).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end   = start.plusHours(durationHours);

        String[] users = {"alice","bob","charlie","diana","eve","frank","grace","henry"};
        String userId  = users[rng.nextInt(users.length)];

        String body = String.format(
            "{\"resourceId\":\"%s\",\"userId\":\"%s\",\"startTime\":\"%s\",\"endTime\":\"%s\"}",
            resourceId, userId, start.format(FMT), end.format(FMT)
        );

        send("POST", "/api/bookings", body, "Idempotency-Key", "idem-" + UUID.randomUUID());
    }

    static void sendGet() {
        // Randomly pick between listing bookings or resources
        String path = rng.nextBoolean() ? "/api/bookings?size=20" : "/api/resources";
        send("GET", path, null);
    }

    static void send(String method, String path, String body, String... extraHeaders) {
        totalSent.increment();
        long t0 = System.currentTimeMillis();
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(30));

            if ("POST".equals(method)) {
                builder.header("Content-Type", "application/json");
                builder.POST(HttpRequest.BodyPublishers.ofString(body));
            } else {
                builder.GET();
            }

            for (int i = 0; i + 1 < extraHeaders.length; i += 2) {
                builder.header(extraHeaders[i], extraHeaders[i + 1]);
            }

            HttpResponse<String> response = httpClient.send(builder.build(),
                HttpResponse.BodyHandlers.ofString());

            long latency = System.currentTimeMillis() - t0;
            totalLatencyMs.add(latency);
            latencySamples.add(latency);

            int status = response.statusCode();
            if (status >= 200 && status < 300) successCount.increment();
            else if (status == 409)             conflictCount.increment();
            else if (status >= 400 && status < 500) clientErrCount.increment();
            else if (status >= 500)             serverErrCount.increment();

        } catch (Exception e) {
            networkErrors.increment();
            latencySamples.add(System.currentTimeMillis() - t0);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    static boolean ping() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/resources"))
                .timeout(Duration.ofSeconds(5))
                .GET().build();
            int status = httpClient.send(req, HttpResponse.BodyHandlers.discarding()).statusCode();
            return status < 500;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    static void fetchResources() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/resources"))
            .GET().build();
        String json = httpClient.send(req, HttpResponse.BodyHandlers.ofString()).body();

        // Minimal JSON parse: extract "id":"..." values (no external lib needed)
        int pos = 0;
        while ((pos = json.indexOf("\"id\"", pos)) != -1) {
            int colon = json.indexOf(':', pos);
            int q1    = json.indexOf('"', colon + 1);
            int q2    = json.indexOf('"', q1 + 1);
            if (q1 != -1 && q2 != -1) {
                resourceIds.add(json.substring(q1 + 1, q2));
            }
            pos = colon + 1;
        }
    }

    static void resetMetrics() {
        // Reset after warmup (LongAdder has no reset, so use a simple workaround)
        long s = successCount.sumThenReset();
        long c = conflictCount.sumThenReset();
        long ce = clientErrCount.sumThenReset();
        long se = serverErrCount.sumThenReset();
        long n = networkErrors.sumThenReset();
        long tl = totalLatencyMs.sumThenReset();
        long ts = totalSent.sumThenReset();
        latencySamples.clear();
    }

    // ── Report ────────────────────────────────────────────────────────────────
    static void printReport(long elapsedMs) {
        long total    = totalSent.sum();
        long ok       = successCount.sum();
        long conflict = conflictCount.sum();
        long cliErr   = clientErrCount.sum();
        long srvErr   = serverErrCount.sum();
        long netErr   = networkErrors.sum();
        long allOk    = ok + conflict;   // conflict = expected business logic, not a system error

        double elapsedSec  = elapsedMs / 1000.0;
        double throughput  = total / elapsedSec;
        double successRate = total == 0 ? 0 : (allOk * 100.0 / total);

        // Latency percentiles
        long[] sorted = latencySamples.stream().mapToLong(Long::longValue).sorted().toArray();
        long p50  = percentile(sorted, 50);
        long p90  = percentile(sorted, 90);
        long p95  = percentile(sorted, 95);
        long p99  = percentile(sorted, 99);
        long pMax = sorted.length > 0 ? sorted[sorted.length - 1] : 0;
        long avg  = sorted.length > 0 ? (long)(totalLatencyMs.sum() / (double)sorted.length) : 0;

        String line = "─".repeat(54);
        System.out.println("\n" + line);
        System.out.println("  C10K LOAD TEST RESULTS");
        System.out.println(line);
        System.out.printf("  Target          %s%n", BASE_URL);
        System.out.printf("  Total requests  %,d%n", total);
        System.out.printf("  Duration        %.2f s%n", elapsedSec);
        System.out.printf("  Throughput      %.0f req/s%n", throughput);
        System.out.println(line);
        System.out.printf("  ✅ Success (2xx)      %,6d  (%5.1f%%)%n", ok, pct(ok, total));
        System.out.printf("  ⚡ Conflict (409)     %,6d  (%5.1f%%)  [expected]%n", conflict, pct(conflict, total));
        System.out.printf("  ⚠️  Client err (4xx)  %,6d  (%5.1f%%)%n", cliErr, pct(cliErr, total));
        System.out.printf("  ❌ Server err (5xx)   %,6d  (%5.1f%%)%n", srvErr, pct(srvErr, total));
        System.out.printf("  🔌 Network error      %,6d  (%5.1f%%)%n", netErr, pct(netErr, total));
        System.out.println(line);
        System.out.printf("  System success rate   %.1f%%  (2xx + 409)%n", successRate);
        System.out.println(line);
        System.out.println("  LATENCY");
        System.out.printf("  avg   %5d ms%n", avg);
        System.out.printf("  p50   %5d ms%n", p50);
        System.out.printf("  p90   %5d ms%n", p90);
        System.out.printf("  p95   %5d ms%n", p95);
        System.out.printf("  p99   %5d ms%n", p99);
        System.out.printf("  max   %5d ms%n", pMax);
        System.out.println(line);

        // Verdict
        boolean passed = srvErr == 0 && netErr == 0 && successRate >= 99.0;
        if (passed) {
            System.out.println("  VERDICT  ✅  PASSED — system handled C10K load");
        } else {
            System.out.println("  VERDICT  ❌  FAILED — see errors above");
            if (srvErr > 0)  System.out.printf("           → %,d server errors (5xx) — check app logs%n", srvErr);
            if (netErr > 0)  System.out.printf("           → %,d network errors — server may be overloaded%n", netErr);
            if (successRate < 99.0) System.out.printf("           → success rate %.1f%% < 99%% threshold%n", successRate);
        }
        System.out.println(line + "\n");
    }

    static long percentile(long[] sorted, int pct) {
        if (sorted.length == 0) return 0;
        int idx = (int) Math.ceil(pct / 100.0 * sorted.length) - 1;
        return sorted[Math.max(0, Math.min(idx, sorted.length - 1))];
    }

    static double pct(long value, long total) {
        return total == 0 ? 0 : value * 100.0 / total;
    }

    static void printBanner() {
        System.out.println("""
            ╔══════════════════════════════════════════════════════╗
            ║          BookingHub — C10K Load Test                 ║
            ║  Virtual Threads · Java 21 · No external deps        ║
            ╚══════════════════════════════════════════════════════╝
            """);
        System.out.printf("  Requests : %,d%n", TOTAL_REQUESTS);
        System.out.printf("  Mix      : %.0f%% writes (POST /bookings) + %.0f%% reads%n",
            WRITE_RATIO * 100, (1 - WRITE_RATIO) * 100);
        System.out.printf("  Max inflight : %d virtual threads%n%n", MAX_CONCURRENT);
    }
}
