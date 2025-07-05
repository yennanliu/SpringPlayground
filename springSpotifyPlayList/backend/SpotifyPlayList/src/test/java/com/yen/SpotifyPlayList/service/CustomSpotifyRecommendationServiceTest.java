package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomSpotifyRecommendationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AuthService authService;

    @InjectMocks
    private CustomSpotifyRecommendationService recommendationService;

    private static final String MOCK_ACCESS_TOKEN = "mock-access-token";
    private static final String MOCK_RESPONSE = "{\"tracks\": [], \"seeds\": []}";

    @BeforeEach
    void setUp() {
        when(authService.getAccessToken()).thenReturn(MOCK_ACCESS_TOKEN);
    }

    @Test
    void getRecommendations_Success() {
        // Arrange
        GetRecommendationsDto dto = new GetRecommendationsDto();
        dto.setSeedArtistId("artist123");
        dto.setSeedTrack("track123");
        dto.setSeedGenres("rock");

        ResponseEntity<String> mockResponse = new ResponseEntity<>(MOCK_RESPONSE, HttpStatus.OK);
        when(restTemplate.exchange(
            anyString(),
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

        // Verify HTTP call was made with correct headers
        verify(restTemplate).exchange(
            anyString(),
            eq(HttpMethod.GET),
            argThat(entity -> {
                HttpHeaders headers = entity.getHeaders();
                return headers.getContentType().equals(MediaType.APPLICATION_JSON) &&
                       headers.getFirst(HttpHeaders.AUTHORIZATION).equals("Bearer " + MOCK_ACCESS_TOKEN);
            }),
            eq(String.class)
        );
    }

    @Test
    void getRecommendations_WithNullSeeds_Success() {
        // Arrange
        GetRecommendationsDto dto = new GetRecommendationsDto();
        // Don't set any seeds - test null handling

        ResponseEntity<String> mockResponse = new ResponseEntity<>(MOCK_RESPONSE, HttpStatus.OK);
        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn(mockResponse);

        // Act
        ResponseEntity<String> response = recommendationService.getRecommendations(dto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
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
    }

    @Test
    void getRecommendations_WhenSpotifyApiFails_ThrowsException() {
        // Arrange
        GetRecommendationsDto dto = new GetRecommendationsDto();
        when(restTemplate.exchange(
            anyString(),
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
        GetRecommendationsDto dto = new GetRecommendationsDto();
        dto.setSeedArtistId("artist123");
        dto.setSeedTrack("track123");
        dto.setSeedGenres("rock");
        dto.setAmount(5);

        ResponseEntity<String> mockResponse = new ResponseEntity<>(MOCK_RESPONSE, HttpStatus.OK);
        when(restTemplate.exchange(
            argThat((String url) -> 
                url.contains("seed_artists=artist123") &&
                url.contains("seed_tracks=track123") &&
                url.contains("seed_genres=rock") &&
                url.contains("limit=5")
            ),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn(mockResponse);

        // Act
        recommendationService.getRecommendations(dto);

        // Verify
        verify(restTemplate).exchange(
            argThat((String url) -> 
                url.contains("recommendations") &&
                url.contains("seed_artists=artist123") &&
                url.contains("seed_tracks=track123") &&
                url.contains("seed_genres=rock") &&
                url.contains("limit=5")
            ),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        );
    }
} 