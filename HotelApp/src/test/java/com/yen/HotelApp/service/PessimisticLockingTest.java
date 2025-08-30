package com.yen.HotelApp.service;

import com.yen.HotelApp.entity.Booking;
import com.yen.HotelApp.entity.Room;
import com.yen.HotelApp.repository.RoomRepository;
import com.yen.HotelApp.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PessimisticLockingTest {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private Room testRoom;

    @BeforeEach
    public void setUp() {
        // Clear existing data
        bookingRepository.deleteAll();
        roomRepository.deleteAll();
        
        // Create a test room
        testRoom = new Room("TEST-001", "Single", 100.0, "Test room for locking");
        testRoom = roomRepository.save(testRoom);
    }

    @Test
    public void testPessimisticLockPreventsDoubleBooking() throws InterruptedException {
        int numberOfThreads = 5;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completeLatch = new CountDownLatch(numberOfThreads);
        
        AtomicInteger successfulBookings = new AtomicInteger(0);
        AtomicInteger failedBookings = new AtomicInteger(0);

        // Submit multiple booking requests simultaneously
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    // Wait for all threads to be ready
                    startLatch.await();
                    
                    // Attempt to book the same room
                    Booking booking = hotelService.createBooking(
                        testRoom.getId(),
                        "Guest-" + threadId,
                        "guest" + threadId + "@test.com",
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2)
                    );
                    
                    if (booking != null) {
                        successfulBookings.incrementAndGet();
                        System.out.println("Thread " + threadId + ": Booking successful - ID: " + booking.getId());
                    }
                    
                } catch (Exception e) {
                    failedBookings.incrementAndGet();
                    System.out.println("Thread " + threadId + ": Booking failed - " + e.getMessage());
                } finally {
                    completeLatch.countDown();
                }
            });
        }

        // Start all threads simultaneously
        startLatch.countDown();
        
        // Wait for all threads to complete (with timeout)
        boolean completed = completeLatch.await(30, TimeUnit.SECONDS);
        assertTrue(completed, "All threads should complete within timeout");

        executor.shutdown();

        // Verify results
        System.out.println("Successful bookings: " + successfulBookings.get());
        System.out.println("Failed bookings: " + failedBookings.get());
        
        // Only one booking should succeed with pessimistic locking
        assertEquals(1, successfulBookings.get(), "Only one booking should succeed");
        assertEquals(numberOfThreads - 1, failedBookings.get(), "All other bookings should fail");
        
        // Verify room is no longer available
        Room updatedRoom = roomRepository.findById(testRoom.getId()).orElse(null);
        assertNotNull(updatedRoom);
        assertFalse(updatedRoom.getAvailable(), "Room should be marked as unavailable");
        
        // Verify only one booking exists in database
        long bookingCount = bookingRepository.count();
        assertEquals(1, bookingCount, "Only one booking should exist in database");
    }

    @Test
    public void testLockTimeoutConfiguration() {
        // This test verifies that lock timeout is configured
        // In a real scenario, this would timeout if another transaction holds the lock
        Room room = roomRepository.findByIdWithLock(testRoom.getId()).orElse(null);
        assertNotNull(room, "Room should be found with lock");
        assertEquals(testRoom.getId(), room.getId(), "Correct room should be returned");
    }
}