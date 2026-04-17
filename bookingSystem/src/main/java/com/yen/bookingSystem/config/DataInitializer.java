package com.yen.bookingSystem.config;

import com.yen.bookingSystem.dto.ResourceDto;
import com.yen.bookingSystem.entity.Booking;
import com.yen.bookingSystem.entity.BookingStatus;
import com.yen.bookingSystem.entity.Resource;
import com.yen.bookingSystem.repository.BookingRepository;
import com.yen.bookingSystem.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

/**
 * Seeds mock data on startup in non-prod environments.
 * Uses ResourceService (populates cache) and BookingRepository directly.
 */
@Component
@Profile("!prod & !test")
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final ResourceService resourceService;
    private final BookingRepository bookingRepository;

    public DataInitializer(ResourceService resourceService, BookingRepository bookingRepository) {
        this.resourceService = resourceService;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(String... args) {
        log.info("Seeding mock data...");

        List<Resource> resources = seedResources();
        seedBookings(resources);

        log.info("Mock data seeded: {} resources, bookings inserted.", resources.size());
    }

    private List<Resource> seedResources() {
        ResourceDto[] dtos = {
            resource("Galaxy Conference Room",  "room",      20),
            resource("Aurora Meeting Room",     "room",       8),
            resource("Nebula Boardroom",        "room",      16),
            resource("Comet Podcast Studio",    "room",       4),
            resource("Hot Desk Alpha",          "desk",       1),
            resource("Hot Desk Beta",           "desk",       1),
            resource("Hot Desk Gamma",          "desk",       1),
            resource("4K Projector Pro",        "equipment",  1),
            resource("Laptop Pool Station",     "equipment",  6),
            resource("Video Camera Kit",        "equipment",  1),
        };

        return java.util.Arrays.stream(dtos)
            .map(resourceService::create)
            .toList();
    }

    private void seedBookings(List<Resource> resources) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        // Helper to get resource by index
        String r0 = resources.get(0).getId(); // Galaxy Conference Room
        String r1 = resources.get(1).getId(); // Aurora Meeting Room
        String r2 = resources.get(2).getId(); // Nebula Boardroom
        String r3 = resources.get(3).getId(); // Comet Podcast Studio
        String r4 = resources.get(4).getId(); // Hot Desk Alpha
        String r5 = resources.get(5).getId(); // Hot Desk Beta
        String r7 = resources.get(7).getId(); // 4K Projector Pro
        String r8 = resources.get(8).getId(); // Laptop Pool Station

        List<Booking> bookings = List.of(
            // --- Past bookings (yesterday / last week) ---
            booking(r0, "alice",   now.minusDays(1).withHour(9).withMinute(0),  now.minusDays(1).withHour(11).withMinute(0),  BookingStatus.CONFIRMED),
            booking(r1, "bob",     now.minusDays(1).withHour(13).withMinute(0), now.minusDays(1).withHour(14).withMinute(30), BookingStatus.CONFIRMED),
            booking(r4, "charlie", now.minusDays(3).withHour(8).withMinute(0),  now.minusDays(3).withHour(18).withMinute(0),  BookingStatus.CONFIRMED),
            booking(r2, "diana",   now.minusDays(5).withHour(10).withMinute(0), now.minusDays(5).withHour(12).withMinute(0),  BookingStatus.CANCELLED),
            booking(r7, "eve",     now.minusDays(2).withHour(14).withMinute(0), now.minusDays(2).withHour(16).withMinute(0),  BookingStatus.CONFIRMED),
            booking(r3, "frank",   now.minusDays(7).withHour(9).withMinute(0),  now.minusDays(7).withHour(10).withMinute(0),  BookingStatus.CANCELLED),

            // --- Today's bookings ---
            booking(r0, "alice",   now.withHour(9).withMinute(0),               now.withHour(11).withMinute(0),               BookingStatus.CONFIRMED),
            booking(r5, "grace",   now.withHour(8).withMinute(30),              now.withHour(17).withMinute(30),              BookingStatus.CONFIRMED),
            booking(r8, "henry",   now.withHour(13).withMinute(0),              now.withHour(15).withMinute(0),               BookingStatus.CONFIRMED),

            // --- Future bookings ---
            booking(r1, "alice",   now.plusDays(1).withHour(10).withMinute(0),  now.plusDays(1).withHour(12).withMinute(0),   BookingStatus.CONFIRMED),
            booking(r2, "bob",     now.plusDays(2).withHour(14).withMinute(0),  now.plusDays(2).withHour(16).withMinute(0),   BookingStatus.CONFIRMED),
            booking(r3, "diana",   now.plusDays(3).withHour(9).withMinute(0),   now.plusDays(3).withHour(10).withMinute(0),   BookingStatus.CONFIRMED),
            booking(r4, "eve",     now.plusDays(1).withHour(8).withMinute(0),   now.plusDays(1).withHour(18).withMinute(0),   BookingStatus.CONFIRMED),
            booking(r7, "frank",   now.plusDays(5).withHour(13).withMinute(0),  now.plusDays(5).withHour(17).withMinute(0),   BookingStatus.CONFIRMED),
            booking(r0, "charlie", now.plusDays(7).withHour(9).withMinute(0),   now.plusDays(7).withHour(17).withMinute(0),   BookingStatus.CONFIRMED)
        );

        bookingRepository.saveAll(bookings);
    }

    private ResourceDto resource(String name, String type, int capacity) {
        ResourceDto dto = new ResourceDto();
        dto.setName(name);
        dto.setType(type);
        dto.setCapacity(capacity);
        dto.setActive(true);
        return dto;
    }

    private Booking booking(String resourceId, String userId,
                            LocalDateTime start, LocalDateTime end,
                            BookingStatus status) {
        return new Booking(UUID.randomUUID().toString(), resourceId, userId, start, end, status);
    }
}
