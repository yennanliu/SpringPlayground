package com.yen.bookingSystem.concurrency;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Thread-safe booking statistics using atomic operations.
 * Demonstrates: AtomicLong, LongAdder for high-contention counters.
 */
@Component
public class BookingStats {

    // Total bookings created (AtomicLong for precise count with get)
    private final AtomicLong totalBookings = new AtomicLong(0);

    // Active bookings (can increase/decrease)
    private final AtomicLong activeBookings = new AtomicLong(0);

    // Cancelled bookings (LongAdder for high-throughput increment-only)
    private final LongAdder cancelledBookings = new LongAdder();

    // Conflict rejections
    private final LongAdder conflictCount = new LongAdder();

    // Per-resource booking counts
    private final ConcurrentHashMap<String, LongAdder> resourceBookings = new ConcurrentHashMap<>();

    /**
     * Record a new booking
     */
    public void recordBooking(String resourceId) {
        totalBookings.incrementAndGet();
        activeBookings.incrementAndGet();
        resourceBookings.computeIfAbsent(resourceId, k -> new LongAdder()).increment();
    }

    /**
     * Record a cancellation
     */
    public void recordCancellation(String resourceId) {
        activeBookings.decrementAndGet();
        cancelledBookings.increment();
    }

    /**
     * Record a conflict rejection
     */
    public void recordConflict() {
        conflictCount.increment();
    }

    /**
     * Get total bookings ever created
     */
    public long getTotalBookings() {
        return totalBookings.get();
    }

    /**
     * Get currently active bookings
     */
    public long getActiveBookings() {
        return activeBookings.get();
    }

    /**
     * Get total cancelled bookings
     */
    public long getCancelledBookings() {
        return cancelledBookings.sum();
    }

    /**
     * Get total conflict rejections
     */
    public long getConflictCount() {
        return conflictCount.sum();
    }

    /**
     * Get booking count for a specific resource
     */
    public long getResourceBookings(String resourceId) {
        LongAdder adder = resourceBookings.get(resourceId);
        return adder != null ? adder.sum() : 0;
    }

    /**
     * Compare and set total bookings (for testing/reset)
     */
    public boolean compareAndSetTotal(long expected, long newValue) {
        return totalBookings.compareAndSet(expected, newValue);
    }

    /**
     * Get snapshot of all stats
     */
    public Stats getSnapshot() {
        return new Stats(
            totalBookings.get(),
            activeBookings.get(),
            cancelledBookings.sum(),
            conflictCount.sum()
        );
    }

    public record Stats(long total, long active, long cancelled, long conflicts) {}
}
