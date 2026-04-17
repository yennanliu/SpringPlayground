package com.yen.bookingSystem.service;

import com.yen.bookingSystem.dto.BookingResponse;
import com.yen.bookingSystem.dto.CreateBookingRequest;
import com.yen.bookingSystem.dto.ResourceDto;
import com.yen.bookingSystem.dto.UpdateBookingRequest;
import com.yen.bookingSystem.entity.Booking;
import com.yen.bookingSystem.entity.BookingStatus;
import com.yen.bookingSystem.entity.Resource;
import com.yen.bookingSystem.exception.BookingConflictException;
import com.yen.bookingSystem.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ResourceService resourceService;

    private Resource testResource;

    @BeforeEach
    void setUp() {
        ResourceDto dto = new ResourceDto();
        dto.setName("Test Room");
        dto.setType("room");
        dto.setCapacity(10);
        testResource = resourceService.create(dto);
    }

    @Nested
    @DisplayName("Create Booking")
    class CreateBookingTests {

        @Test
        @DisplayName("should create booking successfully")
        void createBooking_Success() {
            // Given
            CreateBookingRequest request = new CreateBookingRequest();
            request.setResourceId(testResource.getId());
            request.setUserId("user-1");
            request.setStartTime(LocalDateTime.now().plusDays(1));
            request.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

            // When
            BookingResponse response = bookingService.create(request, null);

            // Then
            assertNotNull(response.getId());
            assertEquals(testResource.getId(), response.getResourceId());
            assertEquals("user-1", response.getUserId());
            assertEquals(BookingStatus.CONFIRMED, response.getStatus());
        }

        @Test
        @DisplayName("should reject booking when time slot overlaps")
        void createBooking_Conflict() {
            // Given - create first booking
            CreateBookingRequest first = new CreateBookingRequest();
            first.setResourceId(testResource.getId());
            first.setUserId("user-1");
            first.setStartTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
            first.setEndTime(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0));
            bookingService.create(first, null);

            // When - try overlapping booking
            CreateBookingRequest second = new CreateBookingRequest();
            second.setResourceId(testResource.getId());
            second.setUserId("user-2");
            second.setStartTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(30));
            second.setEndTime(LocalDateTime.now().plusDays(1).withHour(11).withMinute(30));

            // Then
            BookingConflictException ex = assertThrows(
                BookingConflictException.class,
                () -> bookingService.create(second, null)
            );
            assertEquals("Resource already booked for this time slot", ex.getMessage());
        }

        @Test
        @DisplayName("should allow booking for non-overlapping time slot")
        void createBooking_NoConflict() {
            // Given - create first booking
            CreateBookingRequest first = new CreateBookingRequest();
            first.setResourceId(testResource.getId());
            first.setUserId("user-1");
            first.setStartTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
            first.setEndTime(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0));
            bookingService.create(first, null);

            // When - book different time slot
            CreateBookingRequest second = new CreateBookingRequest();
            second.setResourceId(testResource.getId());
            second.setUserId("user-2");
            second.setStartTime(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0));
            second.setEndTime(LocalDateTime.now().plusDays(1).withHour(12).withMinute(0));

            // Then - should succeed
            BookingResponse response = bookingService.create(second, null);
            assertNotNull(response.getId());
        }

        @Test
        @DisplayName("should reject booking when endTime is before startTime")
        void createBooking_InvalidTimeRange() {
            // Given
            CreateBookingRequest request = new CreateBookingRequest();
            request.setResourceId(testResource.getId());
            request.setUserId("user-1");
            request.setStartTime(LocalDateTime.now().plusDays(1).plusHours(1));
            request.setEndTime(LocalDateTime.now().plusDays(1));

            // When & Then
            BookingConflictException ex = assertThrows(
                BookingConflictException.class,
                () -> bookingService.create(request, null)
            );
            assertEquals("endTime must be after startTime", ex.getMessage());
        }

        @Test
        @DisplayName("should reject booking when resource not found")
        void createBooking_ResourceNotFound() {
            // Given
            CreateBookingRequest request = new CreateBookingRequest();
            request.setResourceId("non-existent-resource");
            request.setUserId("user-1");
            request.setStartTime(LocalDateTime.now().plusDays(1));
            request.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

            // When & Then
            assertThrows(ResourceNotFoundException.class, () -> bookingService.create(request, null));
        }
    }

    @Nested
    @DisplayName("Idempotency")
    class IdempotencyTests {

        @Test
        @DisplayName("should return same response for duplicate idempotency key")
        void createBooking_IdempotencyHit() {
            // Given
            String idempotencyKey = "idem-key-123";
            CreateBookingRequest request = new CreateBookingRequest();
            request.setResourceId(testResource.getId());
            request.setUserId("user-1");
            request.setStartTime(LocalDateTime.now().plusDays(1));
            request.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

            // When - first call
            BookingResponse first = bookingService.create(request, idempotencyKey);
            // When - second call with same key
            BookingResponse second = bookingService.create(request, idempotencyKey);

            // Then - should return same booking ID
            assertEquals(first.getId(), second.getId());
        }

        @Test
        @DisplayName("should create different bookings with different idempotency keys")
        void createBooking_DifferentKeys() {
            // Given
            CreateBookingRequest request1 = new CreateBookingRequest();
            request1.setResourceId(testResource.getId());
            request1.setUserId("user-1");
            request1.setStartTime(LocalDateTime.now().plusDays(1));
            request1.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

            CreateBookingRequest request2 = new CreateBookingRequest();
            request2.setResourceId(testResource.getId());
            request2.setUserId("user-1");
            request2.setStartTime(LocalDateTime.now().plusDays(2));
            request2.setEndTime(LocalDateTime.now().plusDays(2).plusHours(1));

            // When
            BookingResponse first = bookingService.create(request1, "key-1");
            BookingResponse second = bookingService.create(request2, "key-2");

            // Then
            assertNotEquals(first.getId(), second.getId());
        }
    }

    @Nested
    @DisplayName("Get Booking")
    class GetBookingTests {

        @Test
        @DisplayName("should return booking when found")
        void getBooking_Success() {
            // Given
            CreateBookingRequest request = new CreateBookingRequest();
            request.setResourceId(testResource.getId());
            request.setUserId("user-1");
            request.setStartTime(LocalDateTime.now().plusDays(1));
            request.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));
            BookingResponse created = bookingService.create(request, null);

            // When
            Booking result = bookingService.getById(created.getId());

            // Then
            assertEquals(created.getId(), result.getId());
        }

        @Test
        @DisplayName("should throw exception when booking not found")
        void getBooking_NotFound() {
            assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.getById("non-existent")
            );
        }
    }

    @Nested
    @DisplayName("Update Booking")
    class UpdateBookingTests {

        @Test
        @DisplayName("should update booking status to CANCELLED")
        void updateBooking_StatusChange() {
            // Given
            CreateBookingRequest request = new CreateBookingRequest();
            request.setResourceId(testResource.getId());
            request.setUserId("user-1");
            request.setStartTime(LocalDateTime.now().plusDays(1));
            request.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));
            BookingResponse created = bookingService.create(request, null);

            UpdateBookingRequest update = new UpdateBookingRequest();
            update.setStatus(BookingStatus.CANCELLED);

            // When
            Booking result = bookingService.update(created.getId(), update);

            // Then
            assertEquals(BookingStatus.CANCELLED, result.getStatus());
        }
    }

    @Nested
    @DisplayName("Delete Booking")
    class DeleteBookingTests {

        @Test
        @DisplayName("should soft delete booking by setting status to CANCELLED")
        void deleteBooking_SoftDelete() {
            // Given
            CreateBookingRequest request = new CreateBookingRequest();
            request.setResourceId(testResource.getId());
            request.setUserId("user-1");
            request.setStartTime(LocalDateTime.now().plusDays(1));
            request.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));
            BookingResponse created = bookingService.create(request, null);

            // When
            bookingService.delete(created.getId());

            // Then
            Booking deleted = bookingService.getById(created.getId());
            assertEquals(BookingStatus.CANCELLED, deleted.getStatus());
        }
    }
}
