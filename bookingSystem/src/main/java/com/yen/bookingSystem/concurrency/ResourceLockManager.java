package com.yen.bookingSystem.concurrency;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * Resource-level lock manager for fine-grained concurrency control.
 * Demonstrates: ReentrantLock, ReadWriteLock, tryLock with timeout.
 */
@Component
public class ResourceLockManager {

    // Per-resource locks for booking operations
    private final ConcurrentHashMap<String, ReentrantLock> resourceLocks = new ConcurrentHashMap<>();

    // Read-write lock for resource list operations
    private final ReentrantReadWriteLock listLock = new ReentrantReadWriteLock();

    // Default lock timeout
    private static final long DEFAULT_TIMEOUT_MS = 5000;

    /**
     * Get or create lock for a resource
     */
    private ReentrantLock getLock(String resourceId) {
        return resourceLocks.computeIfAbsent(resourceId, k -> new ReentrantLock(true)); // fair lock
    }

    /**
     * Execute action with resource lock (blocking)
     */
    public <T> T withLock(String resourceId, Supplier<T> action) {
        ReentrantLock lock = getLock(resourceId);
        lock.lock();
        try {
            return action.get();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Execute action with resource lock (with timeout)
     * Returns null if lock not acquired
     */
    public <T> T tryWithLock(String resourceId, long timeoutMs, Supplier<T> action) {
        ReentrantLock lock = getLock(resourceId);
        try {
            if (lock.tryLock(timeoutMs, TimeUnit.MILLISECONDS)) {
                try {
                    return action.get();
                } finally {
                    lock.unlock();
                }
            }
            return null; // Lock not acquired
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * Execute action with resource lock (default timeout)
     */
    public <T> T tryWithLock(String resourceId, Supplier<T> action) {
        return tryWithLock(resourceId, DEFAULT_TIMEOUT_MS, action);
    }

    /**
     * Execute read operation (shared lock)
     */
    public <T> T withReadLock(Supplier<T> action) {
        listLock.readLock().lock();
        try {
            return action.get();
        } finally {
            listLock.readLock().unlock();
        }
    }

    /**
     * Execute write operation (exclusive lock)
     */
    public <T> T withWriteLock(Supplier<T> action) {
        listLock.writeLock().lock();
        try {
            return action.get();
        } finally {
            listLock.writeLock().unlock();
        }
    }

    /**
     * Check if resource is currently locked
     */
    public boolean isLocked(String resourceId) {
        ReentrantLock lock = resourceLocks.get(resourceId);
        return lock != null && lock.isLocked();
    }

    /**
     * Get queue length for a resource lock
     */
    public int getQueueLength(String resourceId) {
        ReentrantLock lock = resourceLocks.get(resourceId);
        return lock != null ? lock.getQueueLength() : 0;
    }

    /**
     * Clean up unused locks (call periodically)
     */
    public void cleanup() {
        resourceLocks.entrySet().removeIf(entry ->
            !entry.getValue().isLocked() && entry.getValue().getQueueLength() == 0
        );
    }

    /**
     * Get number of managed locks
     */
    public int getLockCount() {
        return resourceLocks.size();
    }
}
