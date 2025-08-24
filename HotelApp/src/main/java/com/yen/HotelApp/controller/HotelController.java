package com.yen.HotelApp.controller;

import com.yen.HotelApp.entity.Booking;
import com.yen.HotelApp.entity.Room;
import com.yen.HotelApp.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hotel")
@CrossOrigin(origins = "*")
@Tag(name = "Hotel Management", description = "APIs for managing hotel rooms and bookings")
public class HotelController {
    
    @Autowired
    private HotelService hotelService;

    @Operation(summary = "Get all rooms", description = "Retrieve a list of all hotel rooms")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all rooms",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Room.class)) })
    })
    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = hotelService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "Get available rooms", description = "Retrieve a list of all available hotel rooms")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved available rooms",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Room.class)) })
    })
    @GetMapping("/rooms/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        List<Room> availableRooms = hotelService.getAvailableRooms();
        return ResponseEntity.ok(availableRooms);
    }

    @Operation(summary = "Get rooms by type", description = "Retrieve rooms filtered by room type (Single, Double, Suite, Family)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved rooms by type",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Room.class)) })
    })
    @GetMapping("/rooms/type/{roomType}")
    public ResponseEntity<List<Room>> getRoomsByType(
            @Parameter(description = "Type of room (Single, Double, Suite, Family)", example = "Double")
            @PathVariable String roomType) {
        List<Room> rooms = hotelService.getRoomsByType(roomType);
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "Get room by ID", description = "Retrieve a specific room by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room found",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Room.class)) }),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    @GetMapping("/rooms/{id}")
    public ResponseEntity<Room> getRoomById(
            @Parameter(description = "ID of the room to retrieve", example = "1")
            @PathVariable Long id) {
        Optional<Room> room = hotelService.getRoomById(id);
        return room.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a booking", description = "Create a new room booking")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Booking created successfully",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Booking.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid booking request",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(
            @Parameter(description = "Booking request details")
            @RequestBody BookingRequest request) {
        try {
            Booking booking = hotelService.createBooking(
                request.getRoomId(),
                request.getGuestName(),
                request.getGuestEmail(),
                request.getCheckInDate(),
                request.getCheckOutDate()
            );
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Get all bookings", description = "Retrieve a list of all bookings")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all bookings",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Booking.class)) })
    })
    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = hotelService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @Operation(summary = "Get bookings by email", description = "Retrieve bookings for a specific guest email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved bookings by email",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Booking.class)) })
    })
    @GetMapping("/bookings/email/{email}")
    public ResponseEntity<List<Booking>> getBookingsByEmail(
            @Parameter(description = "Guest email address", example = "john.doe@example.com")
            @PathVariable String email) {
        List<Booking> bookings = hotelService.getBookingsByEmail(email);
        return ResponseEntity.ok(bookings);
    }

    @Operation(summary = "Get booking by ID", description = "Retrieve a specific booking by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Booking found",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Booking.class)) }),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/bookings/{id}")
    public ResponseEntity<Booking> getBookingById(
            @Parameter(description = "ID of the booking to retrieve", example = "1")
            @PathVariable Long id) {
        Optional<Booking> booking = hotelService.getBookingById(id);
        return booking.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cancel booking", description = "Cancel an existing booking and make the room available again")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Booking cancelled successfully",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = SuccessResponse.class)) }),
        @ApiResponse(responseCode = "400", description = "Error cancelling booking",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<?> cancelBooking(
            @Parameter(description = "ID of the booking to cancel", example = "1")
            @PathVariable Long id) {
        try {
            hotelService.cancelBooking(id);
            return ResponseEntity.ok(new SuccessResponse("Booking cancelled successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @Schema(description = "Booking request object")
    public static class BookingRequest {
        @Schema(description = "ID of the room to book", example = "1", required = true)
        private Long roomId;
        
        @Schema(description = "Name of the guest", example = "John Doe", required = true)
        private String guestName;
        
        @Schema(description = "Email of the guest", example = "john.doe@example.com", required = true)
        private String guestEmail;
        
        @Schema(description = "Check-in date", example = "2024-12-25", required = true)
        private LocalDate checkInDate;
        
        @Schema(description = "Check-out date", example = "2024-12-28", required = true)
        private LocalDate checkOutDate;

        public Long getRoomId() {
            return roomId;
        }

        public void setRoomId(Long roomId) {
            this.roomId = roomId;
        }

        public String getGuestName() {
            return guestName;
        }

        public void setGuestName(String guestName) {
            this.guestName = guestName;
        }

        public String getGuestEmail() {
            return guestEmail;
        }

        public void setGuestEmail(String guestEmail) {
            this.guestEmail = guestEmail;
        }

        public LocalDate getCheckInDate() {
            return checkInDate;
        }

        public void setCheckInDate(LocalDate checkInDate) {
            this.checkInDate = checkInDate;
        }

        public LocalDate getCheckOutDate() {
            return checkOutDate;
        }

        public void setCheckOutDate(LocalDate checkOutDate) {
            this.checkOutDate = checkOutDate;
        }
    }

    @Schema(description = "Error response object")
    public static class ErrorResponse {
        @Schema(description = "Error message", example = "Room not found")
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @Schema(description = "Success response object")
    public static class SuccessResponse {
        @Schema(description = "Success message", example = "Operation completed successfully")
        private String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}