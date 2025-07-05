package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.AudioFeatureAverages;
import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import com.yen.SpotifyPlayList.model.dto.GetRecommendationsWithFeatureDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Autowired
    private AuthService authService;

    @Autowired
    private PlayListService playListService;

    @Autowired
    private TrackService trackService;

    private SpotifyApi spotifyApi;

    public RecommendationsService() {
    }

    public Recommendations getRecommendation(GetRecommendationsDto getRecommendationsDto)
            throws SpotifyWebApiException {
        try {
            this.spotifyApi = authService.initializeSpotifyApi();
            GetRecommendationsRequest getRecommendationsRequest =
                    prepareRecommendationsRequest(getRecommendationsDto);
            Recommendations recommendations = getRecommendationsRequest.execute();
            log.info("Fetched recommendations: {}", recommendations);
            return recommendations;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Error fetching recommendations: {}", e.getMessage());
            throw new SpotifyWebApiException("getRecommendation error: " + e.getMessage());
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

    private GetRecommendationsRequest prepareRecommendationsRequest(GetRecommendationsDto dto) {
        return spotifyApi.getRecommendations()
                .limit(dto.getAmount())
                .market(dto.getMarket())
                .max_popularity(dto.getMaxPopularity())
                .min_popularity(dto.getMinPopularity())
                .seed_artists(dto.getSeedArtistId())
                .seed_genres(dto.getSeedGenres())
                .seed_tracks(dto.getSeedTrack())
                .target_popularity(dto.getTargetPopularity())
                .build();
    }

    private String getTrackIdFromTrackUrl(String trackUrl) {
        if (trackUrl == null) {
            throw new IllegalArgumentException("Track URL cannot be null");
        }
        return trackUrl.split("tracks")[1].replace("/", "");
    }
}