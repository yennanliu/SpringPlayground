package com.yen.bookingSystem.service;

import com.yen.bookingSystem.concurrency.ResourceCache;
import com.yen.bookingSystem.concurrency.ResourceLockManager;
import com.yen.bookingSystem.dto.ResourceDto;
import com.yen.bookingSystem.entity.Resource;
import com.yen.bookingSystem.exception.ResourceNotFoundException;
import com.yen.bookingSystem.repository.ResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceCache cache;
    private final ResourceLockManager lockManager;

    public ResourceService(ResourceRepository resourceRepository,
                           ResourceCache cache,
                           ResourceLockManager lockManager) {
        this.resourceRepository = resourceRepository;
        this.cache = cache;
        this.lockManager = lockManager;
    }

    @Transactional
    public Resource create(ResourceDto dto) {
        return lockManager.withWriteLock(() -> {
            Resource resource = new Resource();
            resource.setId(UUID.randomUUID().toString());
            resource.setName(dto.getName());
            resource.setType(dto.getType());
            resource.setCapacity(dto.getCapacity() != null ? dto.getCapacity() : 1);
            resource.setActive(dto.getActive() != null ? dto.getActive() : true);

            Resource saved = resourceRepository.save(resource);
            cache.put(saved); // Add to cache
            return saved;
        });
    }

    public Resource getById(String id) {
        // Try cache first, then load from DB
        return cache.getOrLoad(id, key ->
            resourceRepository.findById(key)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + key))
        );
    }

    public List<Resource> list(String type) {
        return lockManager.withReadLock(() -> {
            if (type != null && !type.isBlank()) {
                return resourceRepository.findByType(type);
            }
            return resourceRepository.findByActiveTrue();
        });
    }

    @Transactional
    public Resource update(String id, ResourceDto dto) {
        return lockManager.withWriteLock(() -> {
            Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));

            if (dto.getName() != null) resource.setName(dto.getName());
            if (dto.getType() != null) resource.setType(dto.getType());
            if (dto.getCapacity() != null) resource.setCapacity(dto.getCapacity());
            if (dto.getActive() != null) resource.setActive(dto.getActive());

            Resource saved = resourceRepository.save(resource);
            cache.put(saved); // Update cache
            return saved;
        });
    }

    @Transactional
    public void delete(String id) {
        lockManager.withWriteLock(() -> {
            Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));
            resource.setActive(false);
            resourceRepository.save(resource);
            cache.evict(id); // Remove from cache
            return null;
        });
    }

    /**
     * Get cache statistics
     */
    public int getCacheSize() {
        return cache.size();
    }

    /**
     * Clear cache (for admin/testing)
     */
    public void clearCache() {
        cache.clear();
    }
}
