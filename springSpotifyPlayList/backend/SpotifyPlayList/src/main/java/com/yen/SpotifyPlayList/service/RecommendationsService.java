package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.AudioFeatureAverages;
import com.yen.SpotifyPlayList.model.SpotifyRecommendationsResponse;
import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import com.yen.SpotifyPlayList.model.dto.GetRecommendationsWithFeatureDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.model_objects.specification.Recommendations;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.browse.GetRecommendationsRequest;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecommendationsService {

    private static final String SPOTIFY_API_URL = "https://api.spotify.com/v1";

    @Autowired
    private AuthService authService;

    @Autowired
    private PlayListService playListService;

    @Autowired
    private TrackService trackService;

    private SpotifyApi spotifyApi;
    private final RestTemplate restTemplate;

    public RecommendationsService() {
        this.restTemplate = new RestTemplate();
    }

    public Recommendations getRecommendation(GetRecommendationsDto getRecommendationsDto)
            throws SpotifyWebApiException {
        try {
            this.spotifyApi = authService.initializeSpotifyApi();
            
            // Validate seeds
            boolean hasSeedArtist = getRecommendationsDto.getSeedArtistId() != null && !getRecommendationsDto.getSeedArtistId().isEmpty();
            boolean hasSeedGenres = getRecommendationsDto.getSeedGenres() != null && !getRecommendationsDto.getSeedGenres().isEmpty();
            boolean hasSeedTrack = getRecommendationsDto.getSeedTrack() != null && !getRecommendationsDto.getSeedTrack().isEmpty();

            if (!hasSeedArtist && !hasSeedGenres && !hasSeedTrack) {
                throw new SpotifyWebApiException("At least one seed (artist, genre, or track) is required");
            }

            // Build request using the library's builder
            GetRecommendationsRequest.Builder requestBuilder = spotifyApi.getRecommendations()
                .limit(getRecommendationsDto.getAmount())
                .market(getRecommendationsDto.getMarket());

            // Add seeds
            if (hasSeedArtist) {
                requestBuilder.seed_artists(getRecommendationsDto.getSeedArtistId());
                log.info("Added seed artist: {}", getRecommendationsDto.getSeedArtistId());
            }
            if (hasSeedGenres) {
                requestBuilder.seed_genres(getRecommendationsDto.getSeedGenres());
                log.info("Added seed genres: {}", getRecommendationsDto.getSeedGenres());
            }
            if (hasSeedTrack) {
                requestBuilder.seed_tracks(getRecommendationsDto.getSeedTrack());
                log.info("Added seed track: {}", getRecommendationsDto.getSeedTrack());
            }

            // Add optional parameters
            if (getRecommendationsDto.getMinPopularity() > 0) {
                requestBuilder.min_popularity(getRecommendationsDto.getMinPopularity());
            }
            if (getRecommendationsDto.getMaxPopularity() < 100) {
                requestBuilder.max_popularity(getRecommendationsDto.getMaxPopularity());
            }
            if (getRecommendationsDto.getTargetPopularity() > 0) {
                requestBuilder.target_popularity(getRecommendationsDto.getTargetPopularity());
            }

            // Build and execute request
            GetRecommendationsRequest request = requestBuilder.build();
            
            log.info("Making request to Spotify API with access token: {}", spotifyApi.getAccessToken());
            log.info("Request URI: {}", request.getUri());

            Recommendations recommendations = request.execute();
            log.info("Successfully fetched recommendations");
            
            return recommendations;
        } catch (IOException | ParseException e) {
            log.error("Error making request to Spotify API: {}", e.getMessage(), e);
            throw new SpotifyWebApiException("Error making request to Spotify API: " + e.getMessage());
        } catch (SpotifyWebApiException e) {
            log.error("Spotify API error: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw new SpotifyWebApiException("Unexpected error: " + e.getMessage());
        }
    }

    public Recommendations getRecommendationWithPlayList(String playListId)
            throws SpotifyWebApiException {
        try {
            this.spotifyApi = authService.initializeSpotifyApi();
            List<AudioFeatures> audioFeaturesList = playListService.getSongFeatureByPlayList(playListId);
            log.debug("Retrieved audio features for playlist {}: {} features", playListId, audioFeaturesList.size());

            // Calculate average features
            AudioFeatureAverages averages = calculateAudioFeatureAverages(audioFeaturesList);
            log.debug("Calculated audio feature averages: {}", averages);

            // Get seed tracks and artists
            List<String> seedTracks = selectTopSeedTracks(audioFeaturesList, 2);
            List<String> seedArtists = selectTopSeedArtists(audioFeaturesList, 2);

            GetRecommendationsWithFeatureDto featureDto = createFeatureDto(averages, seedTracks, seedArtists);
            
            GetRecommendationsRequest request = prepareRecommendationsRequestWithPlayList(featureDto);
            Recommendations recommendations = request.execute();
            log.info("Successfully generated recommendations based on playlist {}", playListId);
            
            return recommendations;
        } catch (Exception e) {
            log.error("Error fetching recommendations with playlist features: {}", e.getMessage(), e);
            throw new SpotifyWebApiException("Failed to get recommendations: " + e.getMessage());
        }
    }

    private AudioFeatureAverages calculateAudioFeatureAverages(List<AudioFeatures> features) {
        if (features == null || features.isEmpty()) {
            throw new IllegalArgumentException("Audio features list cannot be null or empty");
        }

        return AudioFeatureAverages.builder()
                .energy(features.stream().mapToDouble(AudioFeatures::getEnergy).average().orElse(0.5))
                .acousticness(features.stream().mapToDouble(AudioFeatures::getAcousticness).average().orElse(0.5))
                .danceability(features.stream().mapToDouble(AudioFeatures::getDanceability).average().orElse(0.5))
                .instrumentalness(features.stream().mapToDouble(AudioFeatures::getInstrumentalness).average().orElse(0.5))
                .liveness(features.stream().mapToDouble(AudioFeatures::getLiveness).average().orElse(0.5))
                .valence(features.stream().mapToDouble(AudioFeatures::getValence).average().orElse(0.5))
                .build();
    }

    private GetRecommendationsWithFeatureDto createFeatureDto(AudioFeatureAverages averages,
                                                            List<String> seedTracks,
                                                            List<String> seedArtists) {
        return GetRecommendationsWithFeatureDto.builder()
                .amount(10)
                .seedTracks(String.join(",", seedTracks))
                .seedArtistIds(String.join(",", seedArtists))
                .danceability((float) averages.getDanceability())
                .energy((float) averages.getEnergy())
                .instrumentalness((float) averages.getInstrumentalness())
                .liveness((float) averages.getLiveness())
                .acousticness((float) averages.getAcousticness())
                .valence((float) averages.getValence())
                .build();
    }

    private List<String> selectTopSeedTracks(List<AudioFeatures> audioFeaturesList, int count) {
        if (audioFeaturesList == null || audioFeaturesList.isEmpty()) {
            throw new IllegalArgumentException("Audio features list cannot be null or empty");
        }

        return audioFeaturesList.stream()
                .sorted(Comparator.comparingDouble(AudioFeatures::getEnergy).reversed())
                .limit(count)
                .map(feature -> getTrackIdFromTrackUrl(feature.getTrackHref()))
                .collect(Collectors.toList());
    }

    private List<String> selectTopSeedArtists(List<AudioFeatures> audioFeaturesList, int count) {
        if (audioFeaturesList == null || audioFeaturesList.isEmpty()) {
            throw new IllegalArgumentException("Audio features list cannot be null or empty");
        }

        Set<String> uniqueArtists = new HashSet<>();
        return audioFeaturesList.stream()
                .map(feature -> {
                    String trackId = feature.getId();
                    Track track = trackService.getTrackInfo(trackId);
                    return track.getArtists()[0].getId();
                })
                .filter(uniqueArtists::add)  // Only keep unique artists
                .limit(count)
                .collect(Collectors.toList());
    }

    private GetRecommendationsRequest prepareRecommendationsRequestWithPlayList(
            GetRecommendationsWithFeatureDto featureDto) {
        return spotifyApi.getRecommendations()
                .limit(featureDto.getAmount())
                .market(featureDto.getMarket())
                .max_popularity(featureDto.getMaxPopularity())
                .min_popularity(featureDto.getMinPopularity())
                .seed_artists(featureDto.getSeedArtistIds())
                .seed_tracks(featureDto.getSeedTracks())
                .target_danceability(featureDto.getDanceability())
                .target_energy(featureDto.getEnergy())
                .target_instrumentalness(featureDto.getInstrumentalness())
                .target_liveness(featureDto.getLiveness())
                .target_acousticness(featureDto.getAcousticness())
                .target_valence(featureDto.getValence())
                .build();
    }

    private String getTrackIdFromTrackUrl(String trackUrl) {
        if (trackUrl == null) {
            throw new IllegalArgumentException("Track URL cannot be null");
        }
        return trackUrl.split("tracks")[1].replace("/", "");
    }
}