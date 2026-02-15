package com.yen.HotelApp.exception;

import com.yen.HotelApp.controller.HotelController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.OptimisticLockException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({OptimisticLockException.class, ObjectOptimisticLockingFailureException.class})
    public ResponseEntity<HotelController.ErrorResponse> handleOptimisticLockException(Exception e) {
        String message = "The room was just booked by someone else. Please refresh and try again.";
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new HotelController.ErrorResponse(message));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<HotelController.ErrorResponse> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest()
                .body(new HotelController.ErrorResponse(e.getMessage()));
    }
}