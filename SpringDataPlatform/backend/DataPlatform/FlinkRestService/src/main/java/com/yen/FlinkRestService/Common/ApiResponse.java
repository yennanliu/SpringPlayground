package com.yen.FlinkRestService.Common;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponse {

    private final boolean success;
    private final String message;
    private final String timestamp;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
    }

    public static ApiResponse success(String message) {
        return new ApiResponse(true, message);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(false, message);
    }
}
