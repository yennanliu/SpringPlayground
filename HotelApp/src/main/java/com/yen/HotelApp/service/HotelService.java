package com.yen.HotelApp.service;

import com.yen.HotelApp.entity.Booking;
import com.yen.HotelApp.entity.Room;
import com.yen.HotelApp.repository.BookingRepository;
import com.yen.HotelApp.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HotelService {
    
    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Value("${server.port:8080}")
    private String serverPort;
    
    @Value("${spring.application.instance-id:UNKNOWN}")
    private String instanceId;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findByAvailable(true);
    }

    public List<Room> getRoomsByType(String roomType) {
        return roomRepository.findByRoomType(roomType);
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public Booking createBooking(Long roomId, String guestName, String guestEmail, 
                                LocalDate checkInDate, LocalDate checkOutDate) {

        String requestId = generateRequestId();
        LocalDateTime startTime = LocalDateTime.now();
        
        logger.info("🔐 [{}] Instance:{} Port:{} - BOOKING REQUEST STARTED - Room:{} Guest:{} Thread:{}", 
                   requestId, instanceId, serverPort, roomId, guestName, Thread.currentThread().getName());

        try {
            logger.info("🔒 [{}] Attempting to acquire PESSIMISTIC LOCK on Room:{}", requestId, roomId);
            
            /**
             *  PESSIMISTIC LOCK - This will block until lock is acquired
             */
            Optional<Room> roomOpt = roomRepository.findByIdWithLock(roomId);
            LocalDateTime lockAcquiredTime = LocalDateTime.now();
            long waitTimeMs = ChronoUnit.MILLIS.between(startTime, lockAcquiredTime);
            
            logger.info("✅ [{}] PESSIMISTIC LOCK ACQUIRED on Room:{} - Wait time: {}ms", 
                       requestId, roomId, waitTimeMs);
            
            if (!roomOpt.isPresent()) {
                logger.error("❌ [{}] Room not found with id: {}", requestId, roomId);
                throw new RuntimeException("Room not found with id: " + roomId);
            }
            
            Room room = roomOpt.get();
            logger.info("🏠 [{}] Room:{} current availability: {}", requestId, roomId, room.getAvailable());
            
            if (!room.getAvailable()) {
                logger.warn("⚠️ [{}] Room:{} is not available for booking", requestId, roomId);
                throw new RuntimeException("Room is not available for booking");
            }

            logger.info("📋 [{}] Validating booking dates - CheckIn:{} CheckOut:{}", 
                       requestId, checkInDate, checkOutDate);
                       
            if (checkInDate.isAfter(checkOutDate)) {
                logger.error("❌ [{}] Invalid dates: Check-in {} is after check-out {}", 
                           requestId, checkInDate, checkOutDate);
                throw new RuntimeException("Check-in date must be before check-out date");
            }

            if (checkInDate.isBefore(LocalDate.now())) {
                logger.error("❌ [{}] Invalid check-in date {} is in the past", requestId, checkInDate);
                throw new RuntimeException("Check-in date cannot be in the past");
            }

            long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            if (nights <= 0) {
                logger.error("❌ [{}] Invalid booking duration: {} nights", requestId, nights);
                throw new RuntimeException("Booking must be for at least one night");
            }

            Double totalPrice = room.getPrice() * nights;
            logger.info("💰 [{}] Booking calculation: {}nights × ${} = ${}", 
                       requestId, nights, room.getPrice(), totalPrice);

            logger.info("🔄 [{}] Updating room availability to FALSE and creating booking", requestId);
            room.setAvailable(false);
            roomRepository.save(room);

            Booking booking = new Booking(room, guestName, guestEmail, checkInDate, checkOutDate, totalPrice);
            booking = bookingRepository.save(booking);
            
            LocalDateTime endTime = LocalDateTime.now();
            long totalTimeMs = ChronoUnit.MILLIS.between(startTime, endTime);
            
            logger.info("🎉 [{}] BOOKING COMPLETED SUCCESSFULLY - BookingId:{} TotalTime:{}ms", 
                       requestId, booking.getId(), totalTimeMs);
            logger.info("🔓 [{}] PESSIMISTIC LOCK RELEASED on Room:{}", requestId, roomId);
            
            return booking;
            
        } catch (Exception e) {
            LocalDateTime endTime = LocalDateTime.now();
            long totalTimeMs = ChronoUnit.MILLIS.between(startTime, endTime);
            
            logger.error("💥 [{}] BOOKING FAILED - Error:{} TotalTime:{}ms", 
                        requestId, e.getMessage(), totalTimeMs);
            logger.info("🔓 [{}] PESSIMISTIC LOCK RELEASED on Room:{} (due to error)", requestId, roomId);
            throw e;
        }
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public void cancelBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (!bookingOpt.isPresent()) {
            throw new RuntimeException("Booking not found with id: " + bookingId);
        }

        Booking booking = bookingOpt.get();
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        
        Room room = booking.getRoom();
        room.setAvailable(true);
        roomRepository.save(room);
        
        bookingRepository.save(booking);
    }
    
    private String generateRequestId() {
        return "REQ-" + System.currentTimeMillis() + "-" + Thread.currentThread().getName().hashCode();
    }
}