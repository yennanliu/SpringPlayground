package com.yen.HotelApp.service;

import com.yen.HotelApp.entity.Booking;
import com.yen.HotelApp.entity.Room;
import com.yen.HotelApp.repository.BookingRepository;
import com.yen.HotelApp.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HotelService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private BookingRepository bookingRepository;

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
        
        Optional<Room> roomOpt = roomRepository.findByIdWithLock(roomId);
        if (!roomOpt.isPresent()) {
            throw new RuntimeException("Room not found with id: " + roomId);
        }
        
        Room room = roomOpt.get();
        if (!room.getAvailable()) {
            throw new RuntimeException("Room is not available for booking");
        }

        if (checkInDate.isAfter(checkOutDate)) {
            throw new RuntimeException("Check-in date must be before check-out date");
        }

        if (checkInDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Check-in date cannot be in the past");
        }

        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (nights <= 0) {
            throw new RuntimeException("Booking must be for at least one night");
        }

        Double totalPrice = room.getPrice() * nights;

        room.setAvailable(false);
        roomRepository.save(room);

        Booking booking = new Booking(room, guestName, guestEmail, checkInDate, checkOutDate, totalPrice);
        return bookingRepository.save(booking);
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
}