package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class CustomSpotifyRecommendationService {

    @Value("${spotify.api.base-url:https://api.spotify.com/v1}")
    private String spotifyApiBaseUrl;

    @Autowired
    private AuthService authService;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> getRecommendations(GetRecommendationsDto request) {
        try {
            String url = buildRecommendationUrl(request);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authService.getAccessToken());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            
            log.info("Making recommendation request to URL: {}", url);
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
            );
            
            log.info("Received recommendation response: {}", response.getStatusCode());
            return response;
            
        } catch (Exception e) {
            log.error("Error getting recommendations: {}", e.getMessage());
            throw new RuntimeException("Failed to get recommendations", e);
        }
    }

    private String buildRecommendationUrl(GetRecommendationsDto request) {
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(spotifyApiBaseUrl + "/recommendations");

        // Add seed parameters
        if (request.getSeedArtistId() != null) {
            builder.queryParam("seed_artists", request.getSeedArtistId());
        }
        if (request.getSeedTrack() != null) {
            builder.queryParam("seed_tracks", request.getSeedTrack());
        }
        if (request.getSeedGenres() != null) {
            builder.queryParam("seed_genres", request.getSeedGenres());
        }

        // Add other parameters
        builder.queryParam("limit", request.getAmount());
        builder.queryParam("market", request.getMarket());
        builder.queryParam("min_popularity", request.getMinPopularity());
        builder.queryParam("max_popularity", request.getMaxPopularity());
        builder.queryParam("target_popularity", request.getTargetPopularity());

        return builder.build().encode().toUriString();
    }
} 