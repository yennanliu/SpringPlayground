package com.yen.bookingSystem.controller;

import com.yen.bookingSystem.dto.ResourceDto;
import com.yen.bookingSystem.entity.Resource;
import com.yen.bookingSystem.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@Tag(name = "Resource", description = "Resource management APIs")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    @Operation(summary = "Create a new resource")
    public ResponseEntity<ResourceDto> create(@Valid @RequestBody ResourceDto dto) {
        Resource resource = resourceService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResourceDto.from(resource));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get resource by ID")
    public ResponseEntity<ResourceDto> getById(@PathVariable String id) {
        Resource resource = resourceService.getById(id);
        return ResponseEntity.ok(ResourceDto.from(resource));
    }

    @GetMapping
    @Operation(summary = "List resources")
    public ResponseEntity<List<ResourceDto>> list(@RequestParam(required = false) String type) {
        List<Resource> resources = resourceService.list(type);
        return ResponseEntity.ok(resources.stream().map(ResourceDto::from).toList());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update resource")
    public ResponseEntity<ResourceDto> update(@PathVariable String id, @RequestBody ResourceDto dto) {
        Resource resource = resourceService.update(id, dto);
        return ResponseEntity.ok(ResourceDto.from(resource));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete resource (soft delete)")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        resourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
