package com.yen.bookingSystem.controller;

import com.yen.bookingSystem.dto.BookingResponse;
import com.yen.bookingSystem.dto.CreateBookingRequest;
import com.yen.bookingSystem.dto.UpdateBookingRequest;
import com.yen.bookingSystem.entity.Booking;
import com.yen.bookingSystem.entity.BookingStatus;
import com.yen.bookingSystem.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Booking", description = "Booking management APIs")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @Operation(summary = "Create a new booking")
    public ResponseEntity<BookingResponse> create(
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @Valid @RequestBody CreateBookingRequest request) {
        BookingResponse response = bookingService.create(request, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID")
    public ResponseEntity<BookingResponse> getById(@PathVariable String id) {
        Booking booking = bookingService.getById(id);
        return ResponseEntity.ok(BookingResponse.from(booking));
    }

    @GetMapping
    @Operation(summary = "List bookings with optional filters")
    public ResponseEntity<Page<BookingResponse>> list(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String resourceId,
            @RequestParam(required = false) BookingStatus status,
            Pageable pageable) {
        Page<Booking> bookings = bookingService.list(userId, resourceId, status, pageable);
        return ResponseEntity.ok(bookings.map(BookingResponse::from));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update booking")
    public ResponseEntity<BookingResponse> update(
            @PathVariable String id,
            @RequestBody UpdateBookingRequest request) {
        Booking booking = bookingService.update(id, request);
        return ResponseEntity.ok(BookingResponse.from(booking));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel booking (soft delete)")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
