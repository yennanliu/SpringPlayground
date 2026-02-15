package com.yen.HotelApp.service;

import com.yen.HotelApp.entity.Room;
import com.yen.HotelApp.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class OptimisticLockingTest {

    @Autowired
    private HotelService hotelService;
    
    @Autowired
    private RoomRepository roomRepository;

    @Test
    @Transactional
    public void testOptimisticLockingOnRoomBooking() throws InterruptedException {
        // Create a test room
        Room room = new Room("999", "Single", 100.0, "Test room for concurrency");
        room = roomRepository.save(room);
        final Long roomId = room.getId();

        // Setup for concurrent booking attempts
        int numberOfThreads = 3;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // Simulate concurrent booking attempts
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    hotelService.createBooking(
                        roomId,
                        "Guest " + threadId,
                        "guest" + threadId + "@test.com",
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(3)
                    );
                    successCount.incrementAndGet();
                } catch (RuntimeException | ObjectOptimisticLockingFailureException e) {
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // Wait for all threads to complete
        executor.shutdown();

        // Assertions
        assertEquals(1, successCount.get(), "Only one booking should succeed");
        assertEquals(numberOfThreads - 1, failureCount.get(), "Other attempts should fail");
        
        // Verify room is no longer available
        Room updatedRoom = roomRepository.findById(roomId).orElseThrow();
        assertFalse(updatedRoom.getAvailable(), "Room should be unavailable after successful booking");
        assertNotNull(updatedRoom.getVersion(), "Room should have a version number");
    }

    @Test
    public void testVersionFieldsExist() {
        // Test that version fields are properly added
        Room room = new Room("998", "Double", 150.0, "Version test room");
        room = roomRepository.save(room);
        
        assertNotNull(room.getVersion(), "Room should have a version field after save");
        assertTrue(room.getVersion() >= 0, "Version should be non-negative");
    }

    @Test
    public void testOptimisticLockExceptionOnRoomUpdate() {
        // Create and save a room
        Room room = new Room("997", "Suite", 250.0, "Lock test room");
        room = roomRepository.save(room);
        
        // Get two instances of the same room
        Room room1 = roomRepository.findById(room.getId()).orElseThrow();
        Room room2 = roomRepository.findById(room.getId()).orElseThrow();
        
        // Update and save first instance
        room1.setPrice(300.0);
        roomRepository.save(room1);
        
        // Try to update second instance (should fail due to optimistic locking)
        room2.setPrice(350.0);
        
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            roomRepository.save(room2);
        });
    }
}