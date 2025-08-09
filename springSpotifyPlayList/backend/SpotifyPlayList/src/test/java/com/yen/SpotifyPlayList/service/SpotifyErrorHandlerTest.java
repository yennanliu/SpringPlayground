package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.exception.SpotifyApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotifyErrorHandlerTest {

    private SpotifyErrorHandler errorHandler;
    private ClientHttpResponse mockResponse;

    @BeforeEach
    void setUp() {
        errorHandler = new SpotifyErrorHandler();
        mockResponse = mock(ClientHttpResponse.class);
    }

    @Test
    void testHasError_ClientError() throws IOException {
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        assertTrue(errorHandler.hasError(mockResponse));
    }

    @Test
    void testHasError_ServerError() throws IOException {
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        assertTrue(errorHandler.hasError(mockResponse));
    }

    @Test
    void testHasError_SuccessStatus() throws IOException {
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        assertFalse(errorHandler.hasError(mockResponse));
    }

    @Test
    void testHandleError_AuthenticationError() throws IOException {
        String errorJson = "{\n" +
                "  \"error\": {\n" +
                "    \"status\": 401,\n" +
                "    \"message\": \"Invalid access token\",\n" +
                "    \"reason\": \"TOKEN_EXPIRED\"\n" +
                "  }\n" +
                "}";

        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.UNAUTHORIZED);
        when(mockResponse.getBody()).thenReturn(new ByteArrayInputStream(errorJson.getBytes(StandardCharsets.UTF_8)));

        SpotifyApiException exception = assertThrows(SpotifyApiException.class, () -> {
            errorHandler.handleError(mockResponse);
        });

        assertEquals(401, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Authentication Error"));
        assertTrue(exception.getMessage().contains("Invalid access token"));
        assertTrue(exception.getMessage().contains("check your Spotify API credentials"));
    }

    @Test
    void testHandleError_RateLimitError() throws IOException {
        String errorJson = "{\n" +
                "  \"error\": {\n" +
                "    \"status\": 429,\n" +
                "    \"message\": \"Rate limit exceeded\"\n" +
                "  }\n" +
                "}";

        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.TOO_MANY_REQUESTS);
        when(mockResponse.getBody()).thenReturn(new ByteArrayInputStream(errorJson.getBytes(StandardCharsets.UTF_8)));

        SpotifyApiException exception = assertThrows(SpotifyApiException.class, () -> {
            errorHandler.handleError(mockResponse);
        });

        assertEquals(429, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Rate Limit Exceeded"));
        assertTrue(exception.getMessage().contains("wait before making additional requests"));
    }

    @Test
    void testHandleError_BadRequestError() throws IOException {
        String errorJson = "{\n" +
                "  \"error\": {\n" +
                "    \"status\": 400,\n" +
                "    \"message\": \"Invalid seed parameters\"\n" +
                "  }\n" +
                "}";

        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(mockResponse.getBody()).thenReturn(new ByteArrayInputStream(errorJson.getBytes(StandardCharsets.UTF_8)));

        SpotifyApiException exception = assertThrows(SpotifyApiException.class, () -> {
            errorHandler.handleError(mockResponse);
        });

        assertEquals(400, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Invalid Request"));
        assertTrue(exception.getMessage().contains("Invalid seed parameters"));
    }

    @Test
    void testHandleError_MalformedErrorResponse() throws IOException {
        String malformedJson = "{ malformed json }";

        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(mockResponse.getBody()).thenReturn(new ByteArrayInputStream(malformedJson.getBytes(StandardCharsets.UTF_8)));

        SpotifyApiException exception = assertThrows(SpotifyApiException.class, () -> {
            errorHandler.handleError(mockResponse);
        });

        assertEquals(400, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Bad Request"));
        assertTrue(exception.getMessage().contains("malformed json"));
    }

    @Test
    void testHandleError_EmptyResponseBody() throws IOException {
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(mockResponse.getBody()).thenReturn(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));

        SpotifyApiException exception = assertThrows(SpotifyApiException.class, () -> {
            errorHandler.handleError(mockResponse);
        });

        assertEquals(500, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Internal Server Error"));
    }

    @Test
    void testHandleError_NullResponseBody() throws IOException {
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(mockResponse.getBody()).thenReturn(null);

        SpotifyApiException exception = assertThrows(SpotifyApiException.class, () -> {
            errorHandler.handleError(mockResponse);
        });

        assertEquals(500, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Internal Server Error"));
        assertTrue(exception.getMessage().contains("Empty response body"));
    }

    @Test
    void testHandleError_UnknownStatusCode() throws IOException {
        String errorJson = "{\n" +
                "  \"error\": {\n" +
                "    \"status\": 418,\n" +
                "    \"message\": \"I'm a teapot\"\n" +
                "  }\n" +
                "}";

        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.valueOf(418));
        when(mockResponse.getBody()).thenReturn(new ByteArrayInputStream(errorJson.getBytes(StandardCharsets.UTF_8)));

        SpotifyApiException exception = assertThrows(SpotifyApiException.class, () -> {
            errorHandler.handleError(mockResponse);
        });

        assertEquals(418, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("I'm a teapot"));
    }
}