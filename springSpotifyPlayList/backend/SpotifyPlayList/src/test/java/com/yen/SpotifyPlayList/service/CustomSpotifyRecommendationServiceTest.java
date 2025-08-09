package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CustomSpotifyRecommendationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private IAuthService authService;

    @InjectMocks
    private CustomSpotifyRecommendationService recommendationService;

    private static final String MOCK_ACCESS_TOKEN = "mock-access-token";
    private static final String MOCK_RESPONSE = "{\"tracks\": [], \"seeds\": []}";
    private static final String BASE_URL = "https://api.spotify.com/v1";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(recommendationService, "spotifyApiBaseUrl", BASE_URL);
    }

    @Test
    void getRecommendations_Success() {
        // Arrange
        when(authService.getAccessToken()).thenReturn(MOCK_ACCESS_TOKEN);
        
        GetRecommendationsDto dto = new GetRecommendationsDto();
        dto.setSeedArtistId("artist123");
        dto.setSeedTrack("track123");
        dto.setSeedGenres("rock");

        ResponseEntity<String> mockResponse = new ResponseEntity<>(MOCK_RESPONSE, HttpStatus.OK);
        when(restTemplate.exchange(
            any(String.class),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn(mockResponse);

        // Act
        ResponseEntity<String> response = recommendationService.getRecommendations(dto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MOCK_RESPONSE, response.getBody());

        // Verify HTTP call was made with correct URL and headers
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HttpEntity<?>> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        
        verify(restTemplate).exchange(
            urlCaptor.capture(),
            eq(HttpMethod.GET),
            entityCaptor.capture(),
            eq(String.class)
        );

        String capturedUrl = urlCaptor.getValue();
        assertTrue(capturedUrl.startsWith(BASE_URL + "/recommendations"));
        assertTrue(capturedUrl.contains("seed_artists=artist123"));
        assertTrue(capturedUrl.contains("seed_tracks=track123"));
        assertTrue(capturedUrl.contains("seed_genres=rock"));

        HttpHeaders headers = entityCaptor.getValue().getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals("Bearer " + MOCK_ACCESS_TOKEN, headers.getFirst(HttpHeaders.AUTHORIZATION));
    }

    @Test
    void getRecommendations_WithNullSeeds_Success() {
        // Arrange
        when(authService.getAccessToken()).thenReturn(MOCK_ACCESS_TOKEN);
        
        GetRecommendationsDto dto = new GetRecommendationsDto();
        // Don't set any seeds - test null handling

        ResponseEntity<String> mockResponse = new ResponseEntity<>(MOCK_RESPONSE, HttpStatus.OK);
        when(restTemplate.exchange(
            any(String.class),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn(mockResponse);

        // Act
        ResponseEntity<String> response = recommendationService.getRecommendations(dto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify URL doesn't contain seed parameters
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplate).exchange(
            urlCaptor.capture(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        );

        String capturedUrl = urlCaptor.getValue();
        assertTrue(capturedUrl.startsWith(BASE_URL + "/recommendations"));
        assertFalse(capturedUrl.contains("seed_artists="));
        assertFalse(capturedUrl.contains("seed_tracks="));
        assertFalse(capturedUrl.contains("seed_genres="));
    }

    @Test
    void getRecommendations_WhenAuthServiceFails_ThrowsException() {
        // Arrange
        when(authService.getAccessToken()).thenThrow(new RuntimeException("Auth failed"));
        GetRecommendationsDto dto = new GetRecommendationsDto();

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            recommendationService.getRecommendations(dto);
        });
        assertTrue(exception.getMessage().contains("Failed to get recommendations"));
        
        // Verify no HTTP call was made
        verify(restTemplate, never()).exchange(
            any(String.class),
            any(HttpMethod.class),
            any(HttpEntity.class),
            eq(String.class)
        );
    }

    @Test
    void getRecommendations_WhenSpotifyApiFails_ThrowsException() {
        // Arrange
        when(authService.getAccessToken()).thenReturn(MOCK_ACCESS_TOKEN);
        
        GetRecommendationsDto dto = new GetRecommendationsDto();
        when(restTemplate.exchange(
            any(String.class),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        )).thenThrow(new RuntimeException("API call failed"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            recommendationService.getRecommendations(dto);
        });
        assertTrue(exception.getMessage().contains("Failed to get recommendations"));
    }

    @Test
    void getRecommendations_VerifyUrlConstruction() {
        // Arrange
        when(authService.getAccessToken()).thenReturn(MOCK_ACCESS_TOKEN);
        
        GetRecommendationsDto dto = new GetRecommendationsDto();
        dto.setSeedArtistId("artist123");
        dto.setSeedTrack("track123");
        dto.setSeedGenres("rock");
        dto.setAmount(5);

        ResponseEntity<String> mockResponse = new ResponseEntity<>(MOCK_RESPONSE, HttpStatus.OK);
        when(restTemplate.exchange(
            any(String.class),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn(mockResponse);

        // Act
        recommendationService.getRecommendations(dto);

        // Verify URL construction
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplate).exchange(
            urlCaptor.capture(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        );

        String capturedUrl = urlCaptor.getValue();
        assertTrue(capturedUrl.startsWith(BASE_URL + "/recommendations"));
        assertTrue(capturedUrl.contains("seed_artists=artist123"));
        assertTrue(capturedUrl.contains("seed_tracks=track123"));
        assertTrue(capturedUrl.contains("seed_genres=rock"));
        assertTrue(capturedUrl.contains("limit=5"));
    }
} 