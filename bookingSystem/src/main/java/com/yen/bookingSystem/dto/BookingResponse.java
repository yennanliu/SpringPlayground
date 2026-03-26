package com.yen.bookingSystem.dto;

import com.yen.bookingSystem.entity.Booking;
import com.yen.bookingSystem.entity.BookingStatus;

import java.time.LocalDateTime;

public class BookingResponse {

    private String id;
    private String resourceId;
    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private LocalDateTime createdAt;

    public BookingResponse() {}

    public static BookingResponse from(Booking booking) {
        BookingResponse r = new BookingResponse();
        r.setId(booking.getId());
        r.setResourceId(booking.getResourceId());
        r.setUserId(booking.getUserId());
        r.setStartTime(booking.getStartTime());
        r.setEndTime(booking.getEndTime());
        r.setStatus(booking.getStatus());
        r.setCreatedAt(booking.getCreatedAt());
        return r;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
