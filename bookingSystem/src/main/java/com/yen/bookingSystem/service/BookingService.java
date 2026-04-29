package com.yen.bookingSystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yen.bookingSystem.concurrency.BookingStats;
import com.yen.bookingSystem.concurrency.ResourceLockManager;
import com.yen.bookingSystem.dto.BookingResponse;
import com.yen.bookingSystem.dto.CreateBookingRequest;
import com.yen.bookingSystem.dto.UpdateBookingRequest;
import com.yen.bookingSystem.entity.Booking;
import com.yen.bookingSystem.entity.BookingStatus;
import com.yen.bookingSystem.entity.IdempotencyKey;
import com.yen.bookingSystem.exception.BookingConflictException;
import com.yen.bookingSystem.exception.ResourceNotFoundException;
import com.yen.bookingSystem.redis.RedisOverbookingGuard;
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
    private final ResourceLockManager lockManager;
    private final BookingStats stats;
    private final Optional<RedisOverbookingGuard> redisGuard;

    public BookingService(BookingRepository bookingRepository,
                          IdempotencyKeyRepository idempotencyKeyRepository,
                          ResourceService resourceService,
                          ObjectMapper objectMapper,
                          ResourceLockManager lockManager,
                          BookingStats stats,
                          Optional<RedisOverbookingGuard> redisGuard) {
        this.bookingRepository = bookingRepository;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
        this.resourceService = resourceService;
        this.objectMapper = objectMapper;
        this.lockManager = lockManager;
        this.stats = stats;
        this.redisGuard = redisGuard;
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

        // 4. Redis fast-path: atomic DECR to pre-check capacity (if enabled)
        String resourceId = request.getResourceId();
        if (redisGuard.isPresent() && !redisGuard.get().tryAcquire(resourceId)) {
            stats.recordConflict();
            throw new BookingConflictException("No available capacity for resource (overbooking prevented)");
        }

        // 5. Acquire resource lock and check for conflicts in DB
        try {
        return lockManager.withLock(resourceId, () -> {
            // Check for overlapping bookings (with DB lock)
            List<Booking> overlapping = bookingRepository.findOverlappingBookingsForUpdate(
                resourceId,
                request.getStartTime(),
                request.getEndTime()
            );

            if (!overlapping.isEmpty()) {
                stats.recordConflict();
                throw new BookingConflictException("Resource already booked for this time slot");
            }

            // Create booking
            Booking booking = new Booking();
            booking.setId(UUID.randomUUID().toString());
            booking.setResourceId(resourceId);
            booking.setUserId(request.getUserId());
            booking.setStartTime(request.getStartTime());
            booking.setEndTime(request.getEndTime());
            booking.setStatus(BookingStatus.CONFIRMED);

            booking = bookingRepository.save(booking);
            BookingResponse response = BookingResponse.from(booking);

            // Record stats
            stats.recordBooking(resourceId);

            // Store idempotency key
            if (idempotencyKey != null) {
                IdempotencyKey key = new IdempotencyKey(idempotencyKey, toJson(response));
                idempotencyKeyRepository.save(key);
            }

            log.info("Booking created: {} for resource: {}", booking.getId(), resourceId);
            return response;
        });
        } catch (BookingConflictException e) {
            // DB conflict check failed — roll back the Redis DECR
            redisGuard.ifPresent(g -> g.release(resourceId));
            throw e;
        }
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
            // Use lock for time change
            return lockManager.withLock(booking.getResourceId(), () -> {
                List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                    booking.getResourceId(),
                    request.getStartTime(),
                    request.getEndTime()
                );
                overlapping = overlapping.stream()
                    .filter(b -> !b.getId().equals(id))
                    .toList();

                if (!overlapping.isEmpty()) {
                    stats.recordConflict();
                    throw new BookingConflictException("Resource already booked for this time slot");
                }

                booking.setStartTime(request.getStartTime());
                booking.setEndTime(request.getEndTime());
                return bookingRepository.save(booking);
            });
        }

        return bookingRepository.save(booking);
    }

    @Transactional
    public void delete(String id) {
        Booking booking = getById(id);
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        stats.recordCancellation(booking.getResourceId());
        redisGuard.ifPresent(g -> g.release(booking.getResourceId()));
        log.info("Booking cancelled: {}", id);
    }

    /**
     * Get booking statistics
     */
    public BookingStats.Stats getStats() {
        return stats.getSnapshot();
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
