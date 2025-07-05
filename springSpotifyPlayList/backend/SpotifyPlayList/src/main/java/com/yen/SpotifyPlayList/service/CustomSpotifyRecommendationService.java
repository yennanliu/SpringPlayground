package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class CustomSpotifyRecommendationService {

    @Value("${spotify.api.base-url:https://api.spotify.com/v1}")
    private String spotifyApiBaseUrl;

    // List of valid Spotify genres
    private static final Set<String> VALID_GENRES = new HashSet<>(Arrays.asList(
        "acoustic", "afrobeat", "alt-rock", "alternative", "ambient", "anime", "black-metal",
        "bluegrass", "blues", "bossanova", "brazil", "breakbeat", "british", "cantopop", "chicago-house",
        "children", "chill", "classical", "club", "comedy", "country", "dance", "dancehall", "death-metal",
        "deep-house", "detroit-techno", "disco", "disney", "drum-and-bass", "dub", "dubstep", "edm",
        "electro", "electronic", "emo", "folk", "forro", "french", "funk", "garage", "german", "gospel",
        "goth", "grindcore", "groove", "grunge", "guitar", "happy", "hard-rock", "hardcore", "hardstyle",
        "heavy-metal", "hip-hop", "holidays", "honky-tonk", "house", "idm", "indian", "indie", "indie-pop",
        "industrial", "iranian", "j-dance", "j-idol", "j-pop", "j-rock", "jazz", "k-pop", "kids", "latin",
        "latino", "malay", "mandopop", "metal", "metal-misc", "metalcore", "minimal-techno", "movies",
        "mpb", "new-age", "new-release", "opera", "pagode", "party", "philippines-opm", "piano", "pop",
        "pop-film", "post-dubstep", "power-pop", "progressive-house", "psych-rock", "punk", "punk-rock",
        "r-n-b", "rainy-day", "reggae", "reggaeton", "road-trip", "rock", "rock-n-roll", "rockabilly",
        "romance", "sad", "salsa", "samba", "sertanejo", "show-tunes", "singer-songwriter", "ska",
        "sleep", "songwriter", "soul", "soundtracks", "spanish", "study", "summer", "swedish", "synth-pop",
        "tango", "techno", "trance", "trip-hop", "turkish", "work-out", "world-music"
    ));

    @Autowired
    private IAuthService authService;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> getRecommendations(GetRecommendationsDto request) {
        try {
            // Validate request
            validateRequest(request);

            // Ensure we have a valid token
            String accessToken = authService.getAccessToken();
            if (accessToken == null || accessToken.isEmpty()) {
                log.info("Access token not found, getting new token");
                accessToken = authService.getToken();
                if (accessToken == null || accessToken.isEmpty()) {
                    throw new RuntimeException("Failed to obtain access token");
                }
            }

            String url = buildRecommendationUrl(request);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            
            log.info("Making recommendation request to URL: {}", url);
            log.debug("Using access token: {}", accessToken);
            
            try {
                ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
                );
                
                log.info("Received recommendation response: {} {}", response.getStatusCode(), response.getBody());
                return response;
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    String errorMessage = String.format(
                        "Invalid seed parameters. Please check that your artist ID (%s), track ID (%s), and genres (%s) are valid.",
                        request.getSeedArtistId(),
                        request.getSeedTrack(),
                        request.getSeedGenres()
                    );
                    throw new IllegalArgumentException(errorMessage);
                }
                throw e;
            }
            
        } catch (Exception e) {
            log.error("Error getting recommendations: {}", e.getMessage());
            if (e instanceof IllegalArgumentException) {
                throw e;  // Rethrow validation errors as-is
            }
            if (e.getMessage() != null && (e.getMessage().contains("401") || e.getMessage().contains("unauthorized"))) {
                // Token might be expired, try to get a new one
                try {
                    log.info("Token might be expired, getting new token");
                    String newToken = authService.getToken();
                    authService.setAccessToken(newToken);
                    // Retry the request with new token
                    return getRecommendations(request);
                } catch (Exception retryError) {
                    log.error("Failed to refresh token and retry: {}", retryError.getMessage());
                    throw new RuntimeException("Failed to refresh access token", retryError);
                }
            }
            throw new RuntimeException("Failed to get recommendations: " + e.getMessage(), e);
        }
    }

    private void validateRequest(GetRecommendationsDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        boolean hasSeed = false;
        
        // Validate artist ID
        if (request.getSeedArtistId() != null && !request.getSeedArtistId().isEmpty()) {
            if (!request.getSeedArtistId().matches("^[0-9A-Za-z]{22}$")) {
                throw new IllegalArgumentException(
                    "Invalid artist ID format. Spotify IDs are 22 characters long and contain only letters and numbers."
                );
            }
            hasSeed = true;
        }

        // Validate track ID
        if (request.getSeedTrack() != null && !request.getSeedTrack().isEmpty()) {
            if (!request.getSeedTrack().matches("^[0-9A-Za-z]{22}$")) {
                throw new IllegalArgumentException(
                    "Invalid track ID format. Spotify IDs are 22 characters long and contain only letters and numbers."
                );
            }
            hasSeed = true;
        }

        // Validate genres
        if (request.getSeedGenres() != null && !request.getSeedGenres().isEmpty()) {
            String[] genres = request.getSeedGenres().split(",");
            for (String genre : genres) {
                String trimmedGenre = genre.trim();
                if (!VALID_GENRES.contains(trimmedGenre)) {
                    throw new IllegalArgumentException(
                        "Invalid genre: '" + trimmedGenre + "'. Please use one of the supported Spotify genres."
                    );
                }
            }
            hasSeed = true;
        }

        if (!hasSeed) {
            throw new IllegalArgumentException(
                "At least one seed (artist, track, or genre) is required. Please provide at least one valid seed parameter."
            );
        }

        // Validate other parameters
        if (request.getAmount() < 1 || request.getAmount() > 100) {
            throw new IllegalArgumentException("Amount must be between 1 and 100");
        }
    }

    private String buildRecommendationUrl(GetRecommendationsDto request) {
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(spotifyApiBaseUrl + "/recommendations");

        // Add seed parameters (already validated)
        if (request.getSeedArtistId() != null && !request.getSeedArtistId().isEmpty()) {
            builder.queryParam("seed_artists", request.getSeedArtistId());
        }
        if (request.getSeedTrack() != null && !request.getSeedTrack().isEmpty()) {
            builder.queryParam("seed_tracks", request.getSeedTrack());
        }
        if (request.getSeedGenres() != null && !request.getSeedGenres().isEmpty()) {
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