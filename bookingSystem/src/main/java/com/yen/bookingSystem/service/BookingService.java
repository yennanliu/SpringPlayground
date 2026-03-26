package com.yen.bookingSystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yen.bookingSystem.dto.BookingResponse;
import com.yen.bookingSystem.dto.CreateBookingRequest;
import com.yen.bookingSystem.dto.UpdateBookingRequest;
import com.yen.bookingSystem.entity.Booking;
import com.yen.bookingSystem.entity.BookingStatus;
import com.yen.bookingSystem.entity.IdempotencyKey;
import com.yen.bookingSystem.exception.BookingConflictException;
import com.yen.bookingSystem.exception.ResourceNotFoundException;
import com.yen.bookingSystem.repository.BookingRepository;
import com.yen.bookingSystem.repository.IdempotencyKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final ResourceService resourceService;
    private final ObjectMapper objectMapper;

    public BookingService(BookingRepository bookingRepository,
                          IdempotencyKeyRepository idempotencyKeyRepository,
                          ResourceService resourceService,
                          ObjectMapper objectMapper) {
        this.bookingRepository = bookingRepository;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
        this.resourceService = resourceService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public BookingResponse create(CreateBookingRequest request, String idempotencyKey) {
        // 1. Check idempotency
        if (idempotencyKey != null) {
            Optional<IdempotencyKey> existing = idempotencyKeyRepository.findById(idempotencyKey);
            if (existing.isPresent()) {
                log.info("Idempotency key found, returning cached response: {}", idempotencyKey);
                return parseResponse(existing.get().getResponse());
            }
        }

        // 2. Validate resource exists
        resourceService.getById(request.getResourceId());

        // 3. Validate time range
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new BookingConflictException("endTime must be after startTime");
        }

        // 4. Check for overlapping bookings (with lock)
        List<Booking> overlapping = bookingRepository.findOverlappingBookingsForUpdate(
            request.getResourceId(),
            request.getStartTime(),
            request.getEndTime()
        );

        if (!overlapping.isEmpty()) {
            throw new BookingConflictException("Resource already booked for this time slot");
        }

        // 5. Create booking
        Booking booking = new Booking();
        booking.setId(UUID.randomUUID().toString());
        booking.setResourceId(request.getResourceId());
        booking.setUserId(request.getUserId());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setStatus(BookingStatus.CONFIRMED);

        booking = bookingRepository.save(booking);
        BookingResponse response = BookingResponse.from(booking);

        // 6. Store idempotency key
        if (idempotencyKey != null) {
            IdempotencyKey key = new IdempotencyKey(idempotencyKey, toJson(response));
            idempotencyKeyRepository.save(key);
        }

        return response;
    }

    public Booking getById(String id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));
    }

    public Page<Booking> list(String userId, String resourceId, BookingStatus status, Pageable pageable) {
        if (userId != null) {
            return bookingRepository.findByUserId(userId, pageable);
        }
        if (resourceId != null) {
            return bookingRepository.findByResourceId(resourceId, pageable);
        }
        if (status != null) {
            return bookingRepository.findByStatus(status, pageable);
        }
        return bookingRepository.findAll(pageable);
    }

    @Transactional
    public Booking update(String id, UpdateBookingRequest request) {
        Booking booking = getById(id);

        if (request.getStatus() != null) {
            booking.setStatus(request.getStatus());
        }

        if (request.getStartTime() != null && request.getEndTime() != null) {
            // Check for conflicts if time is changing
            List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                booking.getResourceId(),
                request.getStartTime(),
                request.getEndTime()
            );
            // Exclude current booking
            overlapping = overlapping.stream()
                .filter(b -> !b.getId().equals(id))
                .toList();

            if (!overlapping.isEmpty()) {
                throw new BookingConflictException("Resource already booked for this time slot");
            }

            booking.setStartTime(request.getStartTime());
            booking.setEndTime(request.getEndTime());
        }

        return bookingRepository.save(booking);
    }

    @Transactional
    public void delete(String id) {
        Booking booking = getById(id);
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize response", e);
        }
    }

    private BookingResponse parseResponse(String json) {
        try {
            return objectMapper.readValue(json, BookingResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse cached response", e);
        }
    }
}
