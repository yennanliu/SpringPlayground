package com.yen.bookingSystem.service;

import com.yen.bookingSystem.dto.ResourceDto;
import com.yen.bookingSystem.entity.Resource;
import com.yen.bookingSystem.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ResourceServiceTest {

    @Autowired
    private ResourceService resourceService;

    @Nested
    @DisplayName("Create Resource")
    class CreateResourceTests {

        @Test
        @DisplayName("should create resource with all fields")
        void createResource_AllFields() {
            // Given
            ResourceDto dto = new ResourceDto();
            dto.setName("Conference Room A");
            dto.setType("room");
            dto.setCapacity(10);
            dto.setActive(true);

            // When
            Resource result = resourceService.create(dto);

            // Then
            assertNotNull(result.getId());
            assertEquals("Conference Room A", result.getName());
            assertEquals("room", result.getType());
            assertEquals(10, result.getCapacity());
            assertTrue(result.getActive());
        }

        @Test
        @DisplayName("should create resource with default values")
        void createResource_DefaultValues() {
            // Given
            ResourceDto dto = new ResourceDto();
            dto.setName("Simple Resource");

            // When
            Resource result = resourceService.create(dto);

            // Then
            assertEquals(1, result.getCapacity());
            assertTrue(result.getActive());
        }
    }

    @Nested
    @DisplayName("Get Resource")
    class GetResourceTests {

        @Test
        @DisplayName("should return resource when found")
        void getResource_Success() {
            // Given
            ResourceDto dto = new ResourceDto();
            dto.setName("Test Resource");
            Resource created = resourceService.create(dto);

            // When
            Resource result = resourceService.getById(created.getId());

            // Then
            assertEquals(created.getId(), result.getId());
            assertEquals("Test Resource", result.getName());
        }

        @Test
        @DisplayName("should throw exception when resource not found")
        void getResource_NotFound() {
            assertThrows(
                ResourceNotFoundException.class,
                () -> resourceService.getById("non-existent")
            );
        }
    }

    @Nested
    @DisplayName("List Resources")
    class ListResourcesTests {

        @Test
        @DisplayName("should list resources by type")
        void listResources_ByType() {
            // Given
            ResourceDto room = new ResourceDto();
            room.setName("Room 1");
            room.setType("room");
            resourceService.create(room);

            ResourceDto desk = new ResourceDto();
            desk.setName("Desk 1");
            desk.setType("desk");
            resourceService.create(desk);

            // When
            List<Resource> rooms = resourceService.list("room");

            // Then
            assertEquals(1, rooms.size());
            assertEquals("room", rooms.get(0).getType());
        }

        @Test
        @DisplayName("should list all active resources when no type specified")
        void listResources_AllActive() {
            // Given
            ResourceDto r1 = new ResourceDto();
            r1.setName("Resource 1");
            resourceService.create(r1);

            ResourceDto r2 = new ResourceDto();
            r2.setName("Resource 2");
            resourceService.create(r2);

            // When
            List<Resource> result = resourceService.list(null);

            // Then
            assertTrue(result.size() >= 2);
        }
    }

    @Nested
    @DisplayName("Update Resource")
    class UpdateResourceTests {

        @Test
        @DisplayName("should update only provided fields")
        void updateResource_PartialUpdate() {
            // Given
            ResourceDto create = new ResourceDto();
            create.setName("Old Name");
            create.setType("room");
            create.setCapacity(5);
            Resource existing = resourceService.create(create);

            ResourceDto update = new ResourceDto();
            update.setName("New Name");

            // When
            Resource result = resourceService.update(existing.getId(), update);

            // Then
            assertEquals("New Name", result.getName());
            assertEquals("room", result.getType());
            assertEquals(5, result.getCapacity());
        }

        @Test
        @DisplayName("should throw exception when updating non-existent resource")
        void updateResource_NotFound() {
            ResourceDto dto = new ResourceDto();
            dto.setName("New Name");

            assertThrows(
                ResourceNotFoundException.class,
                () -> resourceService.update("non-existent", dto)
            );
        }
    }

    @Nested
    @DisplayName("Delete Resource")
    class DeleteResourceTests {

        @Test
        @DisplayName("should soft delete resource by setting active to false")
        void deleteResource_SoftDelete() {
            // Given
            ResourceDto dto = new ResourceDto();
            dto.setName("To Delete");
            Resource created = resourceService.create(dto);
            assertTrue(created.getActive());

            // When
            resourceService.delete(created.getId());

            // Then
            Resource deleted = resourceService.getById(created.getId());
            assertFalse(deleted.getActive());
        }
    }
}
