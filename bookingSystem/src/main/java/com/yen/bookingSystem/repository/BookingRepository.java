package com.yen.bookingSystem.repository;

import com.yen.bookingSystem.entity.Booking;
import com.yen.bookingSystem.entity.BookingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String> {

    Page<Booking> findByUserId(String userId, Pageable pageable);

    Page<Booking> findByResourceId(String resourceId, Pageable pageable);

    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);

    /**
     * Check for overlapping bookings with pessimistic lock
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b WHERE b.resourceId = :resourceId " +
           "AND b.status = 'CONFIRMED' " +
           "AND b.startTime < :endTime AND b.endTime > :startTime")
    List<Booking> findOverlappingBookingsForUpdate(
        @Param("resourceId") String resourceId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );

    /**
     * Check for overlapping bookings (read-only)
     */
    @Query("SELECT b FROM Booking b WHERE b.resourceId = :resourceId " +
           "AND b.status = 'CONFIRMED' " +
           "AND b.startTime < :endTime AND b.endTime > :startTime")
    List<Booking> findOverlappingBookings(
        @Param("resourceId") String resourceId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
}
