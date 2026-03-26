package com.yen.bookingSystem.service;

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

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Transactional
    public Resource create(ResourceDto dto) {
        Resource resource = new Resource();
        resource.setId(UUID.randomUUID().toString());
        resource.setName(dto.getName());
        resource.setType(dto.getType());
        resource.setCapacity(dto.getCapacity() != null ? dto.getCapacity() : 1);
        resource.setActive(dto.getActive() != null ? dto.getActive() : true);
        return resourceRepository.save(resource);
    }

    public Resource getById(String id) {
        return resourceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));
    }

    public List<Resource> list(String type) {
        if (type != null && !type.isBlank()) {
            return resourceRepository.findByType(type);
        }
        return resourceRepository.findByActiveTrue();
    }

    @Transactional
    public Resource update(String id, ResourceDto dto) {
        Resource resource = getById(id);
        if (dto.getName() != null) resource.setName(dto.getName());
        if (dto.getType() != null) resource.setType(dto.getType());
        if (dto.getCapacity() != null) resource.setCapacity(dto.getCapacity());
        if (dto.getActive() != null) resource.setActive(dto.getActive());
        return resourceRepository.save(resource);
    }

    @Transactional
    public void delete(String id) {
        Resource resource = getById(id);
        resource.setActive(false);
        resourceRepository.save(resource);
    }
}
