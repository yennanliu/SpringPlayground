package com.yen.SpotifyPlayList.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yen.SpotifyPlayList.exception.SpotifyApiException;
import com.yen.SpotifyPlayList.model.dto.Response.SpotifyErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class SpotifyErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = response.getStatusCode();
        return statusCode.is4xxClientError() || statusCode.is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = response.getStatusCode();
        String responseBody = getResponseBody(response);
        
        log.error("Spotify API error - Status: {}, Body: {}", statusCode, responseBody);

        String errorMessage = getDefaultErrorMessage(statusCode);
        String spotifyErrorMessage = null;

        try {
            SpotifyErrorResponse errorResponse = objectMapper.readValue(responseBody, SpotifyErrorResponse.class);
            if (errorResponse.getError() != null) {
                spotifyErrorMessage = errorResponse.getError().getMessage();
                errorMessage = createEnhancedErrorMessage(errorResponse);
            }
        } catch (Exception e) {
            log.warn("Failed to parse Spotify error response: {}", e.getMessage());
            errorMessage = String.format("%s: %s", getDefaultErrorMessage(statusCode), responseBody);
        }

        throw new SpotifyApiException(errorMessage, statusCode.value(), spotifyErrorMessage);
    }
    
    private String getDefaultErrorMessage(HttpStatus statusCode) {
        switch (statusCode.value()) {
            case 400:
                return "Bad Request - The request could not be understood by the server";
            case 401:
                return "Unauthorized - Access token is missing, invalid, or expired";
            case 403:
                return "Forbidden - The server understood the request but refuses to authorize it";
            case 404:
                return "Not Found - The requested resource could not be found";
            case 429:
                return "Rate Limited - The app has exceeded its rate limits";
            case 500:
                return "Internal Server Error - Spotify service is temporarily unavailable";
            case 502:
                return "Bad Gateway - Spotify service is temporarily unavailable";
            case 503:
                return "Service Unavailable - Spotify service is temporarily unavailable";
            default:
                return "Spotify API error";
        }
    }
    
    private String createEnhancedErrorMessage(SpotifyErrorResponse errorResponse) {
        StringBuilder message = new StringBuilder();
        
        // Add context based on error type
        if (errorResponse.isAuthenticationError()) {
            message.append("Authentication Error: ");
        } else if (errorResponse.isRateLimitError()) {
            message.append("Rate Limit Exceeded: ");
        } else if (errorResponse.isNotFoundError()) {
            message.append("Resource Not Found: ");
        } else if (errorResponse.isBadRequestError()) {
            message.append("Invalid Request: ");
        }
        
        message.append(errorResponse.getErrorDescription());
        
        // Add helpful hints for common errors
        if (errorResponse.isAuthenticationError()) {
            message.append(". Please check your Spotify API credentials and ensure the access token is valid.");
        } else if (errorResponse.isRateLimitError()) {
            message.append(". Please wait before making additional requests.");
        }
        
        return message.toString();
    }

    private String getResponseBody(ClientHttpResponse response) throws IOException {
        try {
            java.io.InputStream inputStream = response.getBody();
            
            // Handle empty response body
            if (inputStream == null) {
                return "Empty response body";
            }
            
            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            
            String responseBody = new String(buffer.toByteArray(), StandardCharsets.UTF_8);
            
            // Handle empty string response
            return responseBody.isEmpty() ? "Empty response body" : responseBody;
            
        } catch (IOException e) {
            log.warn("Failed to read error response body: {}", e.getMessage());
            return "Unable to read error response: " + e.getMessage();
        }
    }
}