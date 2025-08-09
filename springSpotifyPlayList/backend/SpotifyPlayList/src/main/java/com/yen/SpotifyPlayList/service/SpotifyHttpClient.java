package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import com.yen.SpotifyPlayList.model.dto.GetRecommendationsWithFeatureDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SpotifyHttpClient {

    private static final String SPOTIFY_API_BASE_URL = "https://api.spotify.com/v1";
    private static final String RECOMMENDATIONS_ENDPOINT = "/recommendations";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthService authService;

    public URI buildRecommendationsUri(GetRecommendationsDto dto) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(SPOTIFY_API_BASE_URL + RECOMMENDATIONS_ENDPOINT);

        // Required: At least one seed parameter
        if (dto.getSeedArtistId() != null && !dto.getSeedArtistId().trim().isEmpty()) {
            builder.queryParam("seed_artists", dto.getSeedArtistId());
        }
        if (dto.getSeedGenres() != null && !dto.getSeedGenres().trim().isEmpty()) {
            builder.queryParam("seed_genres", dto.getSeedGenres());
        }
        if (dto.getSeedTrack() != null && !dto.getSeedTrack().trim().isEmpty()) {
            builder.queryParam("seed_tracks", dto.getSeedTrack());
        }

        // Optional parameters
        builder.queryParam("limit", dto.getAmount());
        builder.queryParam("market", dto.getMarket().getAlpha2());
        builder.queryParam("max_popularity", dto.getMaxPopularity());
        builder.queryParam("min_popularity", dto.getMinPopularity());
        builder.queryParam("target_popularity", dto.getTargetPopularity());

        URI uri = builder.build().toUri();
        log.info("Built recommendations URI: {}", uri);
        return uri;
    }

    public URI buildRecommendationsWithFeatureUri(GetRecommendationsWithFeatureDto dto) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(SPOTIFY_API_BASE_URL + RECOMMENDATIONS_ENDPOINT);

        // Required: At least one seed parameter
        if (dto.getSeedArtistId() != null && !dto.getSeedArtistId().trim().isEmpty()) {
            builder.queryParam("seed_artists", dto.getSeedArtistId());
        }
        if (dto.getSeedGenres() != null && !dto.getSeedGenres().trim().isEmpty()) {
            builder.queryParam("seed_genres", dto.getSeedGenres());
        }
        if (dto.getSeedTrack() != null && !dto.getSeedTrack().trim().isEmpty()) {
            builder.queryParam("seed_tracks", dto.getSeedTrack());
        }

        // Basic parameters
        builder.queryParam("limit", dto.getAmount());
        builder.queryParam("market", dto.getMarket().getAlpha2());
        builder.queryParam("max_popularity", dto.getMaxPopularity());
        builder.queryParam("min_popularity", dto.getMinPopularity());
        builder.queryParam("target_popularity", dto.getTargetPopularity());

        // Audio feature parameters (only add if non-zero)
        if (dto.getDanceability() > 0) {
            builder.queryParam("target_danceability", dto.getDanceability());
        }
        if (dto.getEnergy() > 0) {
            builder.queryParam("target_energy", dto.getEnergy());
        }
        if (dto.getInstrumentalness() > 0) {
            builder.queryParam("target_instrumentalness", dto.getInstrumentalness());
        }
        if (dto.getLiveness() > 0) {
            builder.queryParam("target_liveness", dto.getLiveness());
        }
        if (dto.getLoudness() != 0) {
            builder.queryParam("target_loudness", dto.getLoudness());
        }
        if (dto.getSpeechiness() > 0) {
            builder.queryParam("target_speechiness", dto.getSpeechiness());
        }
        if (dto.getTempo() > 0) {
            builder.queryParam("target_tempo", dto.getTempo());
        }

        URI uri = builder.build().toUri();
        log.info("Built recommendations with features URI: {}", uri);
        return uri;
    }

    public HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authService.getAccessToken());
        
        List<MediaType> acceptableMediaTypes = new ArrayList<>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptableMediaTypes);
        
        log.debug("Created headers with bearer token");
        return headers;
    }

    public <T> HttpEntity<T> createHttpEntity(T body) {
        return new HttpEntity<>(body, createAuthHeaders());
    }

    public HttpEntity<Void> createHttpEntityWithoutBody() {
        return new HttpEntity<>(createAuthHeaders());
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public String getSpotifyApiBaseUrl() {
        return SPOTIFY_API_BASE_URL;
    }
}