package com.yen.ShoppingCart.e2e;

import com.yen.ShoppingCart.model.Category;
import com.yen.ShoppingCart.model.dto.cart.AddToCartDto;
import com.yen.ShoppingCart.model.dto.product.ProductDto;
import com.yen.ShoppingCart.model.dto.user.SignInDto;
import com.yen.ShoppingCart.model.dto.user.SignInResponseDto;
import com.yen.ShoppingCart.model.dto.user.SignupDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * High-concurrency end-to-end tests for the ShoppingCart backend.
 *
 * Tests the three approaches from the ShoppingCart-dev-008 branch:
 *   Approach 1 — Virtual Threads (JDK 21, verify no HTTP errors under load)
 *   Approach 2 — HikariCP pool (100 concurrent DB-touching requests complete without exhaustion)
 *   Approach 3 — Redis cache (product/category/token lookups served from cache after warmup)
 *
 * All tests use H2 + ConcurrentMapCacheManager — no external services required.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestCacheConfig.class)
class ConcurrencyE2ETest {

    @MockBean
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    CacheManager cacheManager;

    private static final int CONCURRENCY = 50;

    @BeforeEach
    void clearCaches() {
        cacheManager.getCacheNames().forEach(name -> {
            var c = cacheManager.getCache(name);
            if (c != null) c.clear();
        });
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private String uniqueEmail() {
        return "cc-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10) + "@test.com";
    }

    private String signUpAndGetToken(String email, String password) {
        restTemplate.postForEntity("/user/signup",
                new SignupDto("T", "U", email, password), Map.class);
        ResponseEntity<SignInResponseDto> resp =
                restTemplate.postForEntity("/user/signIn",
                        new SignInDto(email, password), SignInResponseDto.class);
        return resp.getBody().getToken();
    }

    private Integer createCategoryAndGetId() {
        String name = "CCat-" + UUID.randomUUID().toString().substring(0, 6);
        restTemplate.postForEntity("/category/create",
                new Category(name, "desc", "img.png"), Map.class);
        List<Map<String, Object>> cats =
                (List<Map<String, Object>>) restTemplate.getForEntity("/category/", List.class).getBody();
        return cats.stream()
                .filter(c -> name.equals(c.get("categoryName")))
                .map(c -> (Integer) c.get("id"))
                .findFirst().orElseThrow();
    }

    private Integer createProductAndGetId(Integer categoryId) {
        String name = "CProd-" + UUID.randomUUID().toString().substring(0, 6);
        restTemplate.postForEntity("/product/add",
                new ProductDto(name, "img.jpg", 9.99, "desc", categoryId), Map.class);
        List<Map<String, Object>> prods =
                (List<Map<String, Object>>) restTemplate.getForEntity("/product/", List.class).getBody();
        return prods.stream()
                .filter(p -> name.equals(p.get("name")))
                .map(p -> (Integer) p.get("id"))
                .findFirst().orElseThrow();
    }

    /**
     * Fires {@code count} requests in parallel from a fixed thread pool.
     * Returns (successCount, errorCount).
     */
    private int[] runConcurrent(int count, Callable<HttpStatusCode> request) throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch  = new CountDownLatch(count);
        AtomicInteger success = new AtomicInteger();
        AtomicInteger errors  = new AtomicInteger();

        ExecutorService executor = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    HttpStatusCode status = request.call();
                    if (!status.isError()) success.incrementAndGet();
                    else                   errors.incrementAndGet();
                } catch (Exception e) {
                    errors.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        assertTrue(doneLatch.await(60, TimeUnit.SECONDS), "Timeout — threads did not finish in 60 s");
        executor.shutdown();
        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }
        return new int[]{success.get(), errors.get()};
    }

    // ── Approach 3: Redis cache (Concurrent reads) ────────────────────────────

    /**
     * After a single warmup read, 50 concurrent GET /product/ requests must all
     * return HTTP 200 with consistent content. Verifies no race condition on the
     * CacheManager under concurrent read load.
     */
    @Test
    void concurrentProductList_allSucceed_noErrorsAfterWarmup() throws InterruptedException {
        Integer catId = createCategoryAndGetId();
        createProductAndGetId(catId);
        createProductAndGetId(catId);

        // Warmup — primes the cache
        ResponseEntity<List> warmup = restTemplate.getForEntity("/product/", List.class);
        assertThat(warmup.getStatusCode()).isEqualTo(HttpStatus.OK);
        int expectedSize = warmup.getBody().size();

        int[] result = runConcurrent(CONCURRENCY, () -> {
            ResponseEntity<List> resp = restTemplate.getForEntity("/product/", List.class);
            // also assert result consistency (all threads see the same data)
            assertThat(resp.getBody().size()).isEqualTo(expectedSize);
            return resp.getStatusCode();
        });

        assertThat(result[0]).as("success count").isEqualTo(CONCURRENCY);
        assertThat(result[1]).as("error count").isZero();
    }

    /**
     * Same as above for GET /category/ — validates category cache under load.
     */
    @Test
    void concurrentCategoryList_allSucceed_noErrorsAfterWarmup() throws InterruptedException {
        createCategoryAndGetId();

        ResponseEntity<List> warmup = restTemplate.getForEntity("/category/", List.class);
        assertThat(warmup.getStatusCode()).isEqualTo(HttpStatus.OK);
        int expectedSize = warmup.getBody().size();

        int[] result = runConcurrent(CONCURRENCY, () -> {
            ResponseEntity<List> resp = restTemplate.getForEntity("/category/", List.class);
            assertThat(resp.getBody().size()).isEqualTo(expectedSize);
            return resp.getStatusCode();
        });

        assertThat(result[0]).isEqualTo(CONCURRENCY);
        assertThat(result[1]).isZero();
    }

    /**
     * Validates that @CacheEvict on addProduct fires correctly under concurrent readers.
     * Steps:
     *  1. Add product A and warm up the cache.
     *  2. Add product B  — this evicts the cache.
     *  3. Next read must return fresh data (both A and B).
     */
    @Test
    void cacheEviction_newProductVisibleAfterEviction() throws InterruptedException {
        Integer catId = createCategoryAndGetId();

        String nameA = "EvictA-" + UUID.randomUUID().toString().substring(0, 6);
        restTemplate.postForEntity("/product/add",
                new ProductDto(nameA, "img.jpg", 10.0, "desc", catId), Map.class);

        // Warm up cache
        ResponseEntity<List> cached = restTemplate.getForEntity("/product/", List.class);
        int countBeforeEviction = cached.getBody().size();

        // Fire concurrent reads while about to evict
        CountDownLatch beforeEvict = new CountDownLatch(1);
        AtomicInteger readsBeforeEvict = new AtomicInteger();
        ExecutorService readers = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            readers.submit(() -> {
                try { beforeEvict.await(); } catch (InterruptedException ignored) {}
                ResponseEntity<List> r = restTemplate.getForEntity("/product/", List.class);
                if (r.getStatusCode() == HttpStatus.OK) readsBeforeEvict.incrementAndGet();
            });
        }
        beforeEvict.countDown();
        readers.shutdown();
        readers.awaitTermination(30, TimeUnit.SECONDS);
        assertThat(readsBeforeEvict.get()).isEqualTo(10);

        // Add product B — evicts cache
        String nameB = "EvictB-" + UUID.randomUUID().toString().substring(0, 6);
        restTemplate.postForEntity("/product/add",
                new ProductDto(nameB, "img.jpg", 20.0, "desc", catId), Map.class);

        // Next read must see fresh data with product B
        List<Map<String, Object>> fresh =
                (List<Map<String, Object>>) restTemplate.getForEntity("/product/", List.class).getBody();
        assertThat(fresh.size()).isEqualTo(countBeforeEviction + 1);
        assertThat(fresh.stream().anyMatch(p -> nameB.equals(p.get("name")))).isTrue();
    }

    // ── Approach 3: Token cache ───────────────────────────────────────────────

    /**
     * 50 threads simultaneously call GET /user/userProfile with the same token.
     * After the first hit populates the token cache, all subsequent calls are
     * served without a DB lookup. All 50 must succeed.
     */
    @Test
    void concurrentTokenAuth_allSucceed() throws InterruptedException {
        String email = uniqueEmail();
        String token = signUpAndGetToken(email, "pass");

        // Warm up token cache
        restTemplate.getForEntity("/user/userProfile?token=" + token, Map.class);

        int[] result = runConcurrent(CONCURRENCY, () ->
                restTemplate.getForEntity("/user/userProfile?token=" + token, Map.class)
                        .getStatusCode());

        assertThat(result[0]).isEqualTo(CONCURRENCY);
        assertThat(result[1]).isZero();
    }

    // ── Approach 2: HikariCP pool stress ─────────────────────────────────────

    /**
     * 100 concurrent sign-in attempts (with invalid credentials) exercise the
     * HikariCP pool. Each request triggers a real DB lookup for the user.
     * No request should fail with a 5xx pool-exhaustion error.
     */
    @Test
    void highConcurrencySignin_poolDoesNotExhaust() throws InterruptedException {
        int load = 100;
        Map<String, String> body = Map.of("email", "nobody@test.com", "password", "wrong");

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch  = new CountDownLatch(load);
        AtomicInteger poolErrors  = new AtomicInteger();

        ExecutorService executor = Executors.newFixedThreadPool(load);
        for (int i = 0; i < load; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    ResponseEntity<Map> resp =
                            restTemplate.postForEntity("/user/signIn", body, Map.class);
                    // Pool exhaustion shows up as 5xx; user-not-found is expected (4xx or 5xx from the app)
                    // We only fail the test if we see a timeout/connection-refused style error (status 0 or 503+)
                    if (resp.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE
                            || resp.getStatusCode() == HttpStatus.GATEWAY_TIMEOUT) {
                        poolErrors.incrementAndGet();
                    }
                } catch (Exception e) {
                    poolErrors.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        assertTrue(doneLatch.await(120, TimeUnit.SECONDS),
                "Pool exhausted — some requests timed out waiting for a DB connection");
        executor.shutdown();

        assertThat(poolErrors.get()).as("HikariCP pool exhaustion errors").isZero();
    }

    // ── Approaches 1 + 2: Concurrent cart writes ─────────────────────────────

    /**
     * 20 users simultaneously add a product to their own carts.
     * Tests concurrent DB INSERT throughput (each user has exactly 1 cart item
     * due to the @OneToOne Cart→User constraint in the data model).
     * All 20 requests must return HTTP 201 with no data loss.
     */
    @Test
    void concurrentAddToCart_allItemsSaved() throws InterruptedException {
        Integer catId   = createCategoryAndGetId();
        Integer prodId  = createProductAndGetId(catId);
        int threads = 20;

        // Pre-create users to isolate user-creation cost from cart-write concurrency
        List<String> tokens = new ArrayList<>(threads);
        for (int i = 0; i < threads; i++) {
            tokens.add(signUpAndGetToken(uniqueEmail(), "pass"));
        }

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch  = new CountDownLatch(threads);
        AtomicInteger success = new AtomicInteger();
        AtomicInteger errors  = new AtomicInteger();

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            final String token = tokens.get(i);
            executor.submit(() -> {
                try {
                    startLatch.await();
                    ResponseEntity<Map> resp = restTemplate.postForEntity(
                            "/cart/add?token=" + token,
                            new AddToCartDto(null, prodId, 1),
                            Map.class);
                    if (resp.getStatusCode() == HttpStatus.CREATED) success.incrementAndGet();
                    else                                              errors.incrementAndGet();
                } catch (Exception e) {
                    errors.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        assertTrue(doneLatch.await(60, TimeUnit.SECONDS));
        executor.shutdown();

        assertThat(success.get()).as("successful cart additions").isEqualTo(threads);
        assertThat(errors.get()).as("cart addition errors").isZero();
    }

    /**
     * 50 users concurrently sign up and sign in. Validates that concurrent user
     * creation (which generates an AuthenticationToken for each user) does not
     * cause duplicate-token or constraint-violation errors.
     */
    @Test
    void concurrentUserSignup_allTokensUnique() throws InterruptedException {
        int threads = CONCURRENCY;
        List<String> emails = new ArrayList<>(threads);
        for (int i = 0; i < threads; i++) {
            emails.add(uniqueEmail());
        }

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch  = new CountDownLatch(threads);
        List<String> tokens = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger errors = new AtomicInteger();

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            final String email = emails.get(i);
            executor.submit(() -> {
                try {
                    startLatch.await();
                    restTemplate.postForEntity("/user/signup",
                            new SignupDto("T", "U", email, "pass"), Map.class);
                    ResponseEntity<SignInResponseDto> resp =
                            restTemplate.postForEntity("/user/signIn",
                                    new SignInDto(email, "pass"), SignInResponseDto.class);
                    if (resp.getStatusCode() == HttpStatus.OK && resp.getBody().getToken() != null) {
                        tokens.add(resp.getBody().getToken());
                    } else {
                        errors.incrementAndGet();
                    }
                } catch (Exception e) {
                    errors.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        assertTrue(doneLatch.await(60, TimeUnit.SECONDS));
        executor.shutdown();

        assertThat(errors.get()).as("signup/signin errors").isZero();
        assertThat(tokens).hasSize(threads);
        // Each user gets a distinct UUID token — no duplicates
        assertThat(new HashSet<>(tokens)).hasSize(threads);
    }
}
