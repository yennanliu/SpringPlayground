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

        String errorMessage = "Spotify API error";
        String spotifyErrorMessage = null;

        try {
            SpotifyErrorResponse errorResponse = objectMapper.readValue(responseBody, SpotifyErrorResponse.class);
            if (errorResponse.getError() != null) {
                spotifyErrorMessage = errorResponse.getError().getMessage();
                errorMessage = "Spotify API error: " + spotifyErrorMessage;
            }
        } catch (Exception e) {
            log.warn("Failed to parse Spotify error response: {}", e.getMessage());
            errorMessage = "Spotify API error: " + responseBody;
        }

        throw new SpotifyApiException(errorMessage, statusCode.value(), spotifyErrorMessage);
    }

    private String getResponseBody(ClientHttpResponse response) throws IOException {
        try {
            java.io.InputStream inputStream = response.getBody();
            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.warn("Failed to read error response body: {}", e.getMessage());
            return "Unable to read error response";
        }
    }
}