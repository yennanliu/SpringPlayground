package com.yen.bookingSystem.concurrency;

import com.yen.bookingSystem.entity.Resource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe in-memory cache for resources using ConcurrentHashMap.
 * Demonstrates: compute, computeIfAbsent, merge operations.
 */
@Component
public class ResourceCache {

    private final ConcurrentHashMap<String, Resource> cache = new ConcurrentHashMap<>();

    /**
     * Get resource from cache
     */
    public Resource get(String id) {
        return cache.get(id);
    }

    /**
     * Put resource in cache
     */
    public void put(Resource resource) {
        cache.put(resource.getId(), resource);
    }

    /**
     * Get or load resource (computeIfAbsent pattern)
     */
    public Resource getOrLoad(String id, java.util.function.Function<String, Resource> loader) {
        return cache.computeIfAbsent(id, loader);
    }

    /**
     * Update resource atomically (compute pattern)
     */
    public Resource update(String id, java.util.function.Function<Resource, Resource> updater) {
        return cache.compute(id, (key, existing) -> {
            if (existing == null) return null;
            return updater.apply(existing);
        });
    }

    /**
     * Remove from cache
     */
    public void evict(String id) {
        cache.remove(id);
    }

    /**
     * Clear all cache
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Get all cached resources
     */
    public Collection<Resource> getAll() {
        return cache.values();
    }

    /**
     * Check if resource is cached
     */
    public boolean contains(String id) {
        return cache.containsKey(id);
    }

    /**
     * Get cache size
     */
    public int size() {
        return cache.size();
    }
}
