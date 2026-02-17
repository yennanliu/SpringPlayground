package com.yen.SpotifyPlayList.model.dto.Response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SpotifyErrorResponse {
    private SpotifyError error;

    @Data
    @ToString
    public static class SpotifyError {
        private int status;
        private String message;
        private String reason;  // Additional reason field for detailed errors
    }
    
    // Helper methods for common error scenarios
    public boolean isAuthenticationError() {
        return error != null && (error.status == 401 || error.status == 403);
    }
    
    public boolean isRateLimitError() {
        return error != null && error.status == 429;
    }
    
    public boolean isNotFoundError() {
        return error != null && error.status == 404;
    }
    
    public boolean isBadRequestError() {
        return error != null && error.status == 400;
    }
    
    public String getErrorDescription() {
        if (error == null) return "Unknown error";
        
        StringBuilder description = new StringBuilder();
        description.append("HTTP ").append(error.status);
        
        if (error.message != null) {
            description.append(": ").append(error.message);
        }
        
        if (error.reason != null) {
            description.append(" (Reason: ").append(error.reason).append(")");
        }
        
        return description.toString();
    }
}