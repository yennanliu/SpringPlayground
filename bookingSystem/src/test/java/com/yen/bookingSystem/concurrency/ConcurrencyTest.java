package com.yen.bookingSystem.concurrency;

import com.yen.bookingSystem.entity.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrencyTest {

    @Nested
    @DisplayName("ResourceCache")
    class ResourceCacheTests {

        private ResourceCache cache;

        @BeforeEach
        void setUp() {
            cache = new ResourceCache();
        }

        @Test
        @DisplayName("should store and retrieve resource")
        void putAndGet() {
            Resource r = new Resource();
            r.setId("r1");
            r.setName("Test");

            cache.put(r);

            assertEquals("Test", cache.get("r1").getName());
        }

        @Test
        @DisplayName("should use computeIfAbsent correctly")
        void computeIfAbsent() {
            AtomicInteger loadCount = new AtomicInteger(0);

            Resource r1 = cache.getOrLoad("r1", id -> {
                loadCount.incrementAndGet();
                Resource r = new Resource();
                r.setId(id);
                r.setName("Loaded");
                return r;
            });

            Resource r2 = cache.getOrLoad("r1", id -> {
                loadCount.incrementAndGet();
                return null;
            });

            assertEquals("Loaded", r1.getName());
            assertSame(r1, r2); // Same instance
            assertEquals(1, loadCount.get()); // Only loaded once
        }

        @Test
        @DisplayName("should handle concurrent access")
        void concurrentAccess() throws InterruptedException {
            int threads = 10;
            int iterations = 100;
            CountDownLatch latch = new CountDownLatch(threads);
            ExecutorService executor = Executors.newFixedThreadPool(threads);

            for (int t = 0; t < threads; t++) {
                executor.submit(() -> {
                    for (int i = 0; i < iterations; i++) {
                        Resource r = new Resource();
                        r.setId("r" + i);
                        r.setName("Resource " + i);
                        cache.put(r);
                        cache.get("r" + i);
                    }
                    latch.countDown();
                });
            }

            latch.await();
            executor.shutdown();

            assertTrue(cache.size() <= iterations);
        }
    }

    @Nested
    @DisplayName("BookingStats")
    class BookingStatsTests {

        private BookingStats stats;

        @BeforeEach
        void setUp() {
            stats = new BookingStats();
        }

        @Test
        @DisplayName("should track booking counts correctly")
        void trackBookings() {
            stats.recordBooking("r1");
            stats.recordBooking("r1");
            stats.recordBooking("r2");

            assertEquals(3, stats.getTotalBookings());
            assertEquals(3, stats.getActiveBookings());
            assertEquals(2, stats.getResourceBookings("r1"));
            assertEquals(1, stats.getResourceBookings("r2"));
        }

        @Test
        @DisplayName("should track cancellations")
        void trackCancellations() {
            stats.recordBooking("r1");
            stats.recordBooking("r1");
            stats.recordCancellation("r1");

            assertEquals(2, stats.getTotalBookings());
            assertEquals(1, stats.getActiveBookings());
            assertEquals(1, stats.getCancelledBookings());
        }

        @Test
        @DisplayName("should track conflicts")
        void trackConflicts() {
            stats.recordConflict();
            stats.recordConflict();
            stats.recordConflict();

            assertEquals(3, stats.getConflictCount());
        }

        @Test
        @DisplayName("should handle concurrent updates")
        void concurrentUpdates() throws InterruptedException {
            int threads = 10;
            int iterations = 1000;
            CountDownLatch latch = new CountDownLatch(threads);
            ExecutorService executor = Executors.newFixedThreadPool(threads);

            for (int t = 0; t < threads; t++) {
                executor.submit(() -> {
                    for (int i = 0; i < iterations; i++) {
                        stats.recordBooking("r1");
                    }
                    latch.countDown();
                });
            }

            latch.await();
            executor.shutdown();

            assertEquals(threads * iterations, stats.getTotalBookings());
        }

        @Test
        @DisplayName("should provide accurate snapshot")
        void snapshot() {
            stats.recordBooking("r1");
            stats.recordBooking("r2");
            stats.recordCancellation("r1");
            stats.recordConflict();

            BookingStats.Stats snapshot = stats.getSnapshot();

            assertEquals(2, snapshot.total());
            assertEquals(1, snapshot.active());
            assertEquals(1, snapshot.cancelled());
            assertEquals(1, snapshot.conflicts());
        }
    }

    @Nested
    @DisplayName("ResourceLockManager")
    class ResourceLockManagerTests {

        private ResourceLockManager lockManager;

        @BeforeEach
        void setUp() {
            lockManager = new ResourceLockManager();
        }

        @Test
        @DisplayName("should execute action with lock")
        void withLock() {
            String result = lockManager.withLock("r1", () -> "success");
            assertEquals("success", result);
        }

        @Test
        @DisplayName("should serialize access to same resource")
        void serializeAccess() throws InterruptedException {
            AtomicInteger counter = new AtomicInteger(0);
            AtomicInteger maxConcurrent = new AtomicInteger(0);
            AtomicInteger currentConcurrent = new AtomicInteger(0);

            int threads = 10;
            CountDownLatch latch = new CountDownLatch(threads);
            ExecutorService executor = Executors.newFixedThreadPool(threads);

            for (int t = 0; t < threads; t++) {
                executor.submit(() -> {
                    lockManager.withLock("r1", () -> {
                        int c = currentConcurrent.incrementAndGet();
                        maxConcurrent.updateAndGet(max -> Math.max(max, c));
                        counter.incrementAndGet();
                        try { Thread.sleep(10); } catch (InterruptedException e) {}
                        currentConcurrent.decrementAndGet();
                        return null;
                    });
                    latch.countDown();
                });
            }

            latch.await();
            executor.shutdown();

            assertEquals(threads, counter.get());
            assertEquals(1, maxConcurrent.get()); // Only 1 concurrent execution
        }

        @Test
        @DisplayName("should allow parallel access to different resources")
        void parallelDifferentResources() throws InterruptedException {
            AtomicInteger maxConcurrent = new AtomicInteger(0);
            AtomicInteger currentConcurrent = new AtomicInteger(0);

            int threads = 5;
            CountDownLatch latch = new CountDownLatch(threads);
            ExecutorService executor = Executors.newFixedThreadPool(threads);

            for (int t = 0; t < threads; t++) {
                final int resourceNum = t;
                executor.submit(() -> {
                    lockManager.withLock("r" + resourceNum, () -> {
                        int c = currentConcurrent.incrementAndGet();
                        maxConcurrent.updateAndGet(max -> Math.max(max, c));
                        try { Thread.sleep(50); } catch (InterruptedException e) {}
                        currentConcurrent.decrementAndGet();
                        return null;
                    });
                    latch.countDown();
                });
            }

            latch.await();
            executor.shutdown();

            assertTrue(maxConcurrent.get() > 1); // Multiple concurrent executions
        }

        @Test
        @DisplayName("should support tryLock with timeout")
        void tryLockTimeout() {
            // Acquire lock in another thread
            new Thread(() -> {
                lockManager.withLock("r1", () -> {
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                    return null;
                });
            }).start();

            try { Thread.sleep(50); } catch (InterruptedException e) {}

            // Try to acquire with short timeout
            String result = lockManager.tryWithLock("r1", 100, () -> "success");
            assertNull(result); // Should fail to acquire
        }

        @Test
        @DisplayName("should support read-write locks")
        void readWriteLock() throws InterruptedException {
            AtomicInteger readCount = new AtomicInteger(0);
            AtomicInteger maxReaders = new AtomicInteger(0);
            AtomicInteger currentReaders = new AtomicInteger(0);

            int readers = 5;
            CountDownLatch latch = new CountDownLatch(readers);
            ExecutorService executor = Executors.newFixedThreadPool(readers);

            for (int t = 0; t < readers; t++) {
                executor.submit(() -> {
                    lockManager.withReadLock(() -> {
                        int c = currentReaders.incrementAndGet();
                        maxReaders.updateAndGet(max -> Math.max(max, c));
                        readCount.incrementAndGet();
                        try { Thread.sleep(50); } catch (InterruptedException e) {}
                        currentReaders.decrementAndGet();
                        return null;
                    });
                    latch.countDown();
                });
            }

            latch.await();
            executor.shutdown();

            assertEquals(readers, readCount.get());
            assertTrue(maxReaders.get() > 1); // Multiple readers allowed
        }
    }
}
