package com.yen.bookingSystem.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Overbooking prevention via atomic Redis DECR.
 *
 * Key: booking:capacity:{resourceId}  — stores remaining available slots.
 *
 * tryAcquire: DECR the counter atomically.
 *   If result >= 0  → slot secured, proceed.
 *   If result < 0   → no capacity left; INCR to roll back and reject.
 *
 * release: INCR the counter (called on booking cancellation).
 *
 * initCapacity: SET key = capacity (called on resource create/update).
 *
 * Enabled only when booking.redis.enabled=true.
 */
@Component
@ConditionalOnProperty(name = "booking.redis.enabled", havingValue = "true")
public class RedisOverbookingGuard {

    private static final Logger log = LoggerFactory.getLogger(RedisOverbookingGuard.class);
    private static final String KEY_PREFIX = "booking:capacity:";

    private final StringRedisTemplate redis;

    public RedisOverbookingGuard(StringRedisTemplate redis) {
        this.redis = redis;
    }

    /**
     * Atomically decrement available capacity.
     * @return true if a slot was successfully reserved, false if overbooked
     */
    public boolean tryAcquire(String resourceId) {
        String key = KEY_PREFIX + resourceId;
        Long remaining = redis.opsForValue().decrement(key);
        if (remaining == null || remaining < 0) {
            // Roll back — no capacity left
            redis.opsForValue().increment(key);
            log.warn("Overbooking rejected for resource {} (remaining={})", resourceId, remaining);
            return false;
        }
        log.debug("Slot acquired for resource {} (remaining={})", resourceId, remaining);
        return true;
    }

    /**
     * Restore one slot (call on booking cancellation).
     */
    public void release(String resourceId) {
        String key = KEY_PREFIX + resourceId;
        redis.opsForValue().increment(key);
        log.debug("Slot released for resource {}", resourceId);
    }

    /**
     * Initialize the capacity counter for a resource.
     * Call on resource create or capacity update.
     */
    public void initCapacity(String resourceId, int capacity) {
        String key = KEY_PREFIX + resourceId;
        redis.opsForValue().set(key, String.valueOf(capacity));
        log.info("Redis capacity initialized for resource {}: {}", resourceId, capacity);
    }
}
