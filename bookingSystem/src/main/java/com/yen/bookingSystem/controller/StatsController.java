package com.yen.bookingSystem.controller;

import com.yen.bookingSystem.concurrency.BookingStats;
import com.yen.bookingSystem.concurrency.ResourceLockManager;
import com.yen.bookingSystem.service.BookingService;
import com.yen.bookingSystem.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@Tag(name = "Stats", description = "System statistics and monitoring")
public class StatsController {

    private final BookingService bookingService;
    private final ResourceService resourceService;
    private final ResourceLockManager lockManager;

    public StatsController(BookingService bookingService,
                           ResourceService resourceService,
                           ResourceLockManager lockManager) {
        this.bookingService = bookingService;
        this.resourceService = resourceService;
        this.lockManager = lockManager;
    }

    @GetMapping
    @Operation(summary = "Get system statistics")
    public ResponseEntity<Map<String, Object>> getStats() {
        BookingStats.Stats bookingStats = bookingService.getStats();

        Map<String, Object> stats = new HashMap<>();

        // Booking stats
        Map<String, Long> bookings = new HashMap<>();
        bookings.put("total", bookingStats.total());
        bookings.put("active", bookingStats.active());
        bookings.put("cancelled", bookingStats.cancelled());
        bookings.put("conflicts", bookingStats.conflicts());
        stats.put("bookings", bookings);

        // Cache stats
        Map<String, Integer> cache = new HashMap<>();
        cache.put("resourcesCached", resourceService.getCacheSize());
        stats.put("cache", cache);

        // Lock stats
        Map<String, Integer> locks = new HashMap<>();
        locks.put("activeLocks", lockManager.getLockCount());
        stats.put("locks", locks);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "booking-system");
        return ResponseEntity.ok(health);
    }
}
