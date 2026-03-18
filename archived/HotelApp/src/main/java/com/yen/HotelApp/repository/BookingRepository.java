package com.yen.HotelApp.repository;

import com.yen.HotelApp.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByGuestEmail(String guestEmail);
    List<Booking> findByStatus(Booking.BookingStatus status);
    List<Booking> findByRoomId(Long roomId);
}