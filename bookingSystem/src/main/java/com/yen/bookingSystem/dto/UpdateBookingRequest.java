package com.yen.bookingSystem.dto;

import com.yen.bookingSystem.entity.BookingStatus;

import java.time.LocalDateTime;

public class UpdateBookingRequest {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
