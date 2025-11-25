package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.exception.SpotifyApiException;
import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import com.yen.SpotifyPlayList.model.dto.GetRecommendationsWithFeatureDto;
import com.yen.SpotifyPlayList.model.dto.Response.LegacyRecommendationsResponse;
import com.yen.SpotifyPlayList.model.dto.Response.SpotifyRecommendationsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.net.URI;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecommendationsService {

  @Autowired private AuthService authService;

  @Autowired private PlayListService playListService;

  @Autowired private TrackService trackService;

  @Autowired private SpotifyHttpClient spotifyHttpClient;

  @Autowired private RecommendationsResponseMapper responseMapper;

  public RecommendationsService() {}

  public LegacyRecommendationsResponse getRecommendation(GetRecommendationsDto getRecommendationsDto) {
    try {
      // Ensure we have a valid access token
      authService.initializeSpotifyApi();
      
      // Build the request URI
      URI requestUri = spotifyHttpClient.buildRecommendationsUri(getRecommendationsDto);
      
      // Create HTTP entity with auth headers
      HttpEntity<Void> entity = spotifyHttpClient.createHttpEntityWithoutBody();
      
      // Make the HTTP call
      ResponseEntity<SpotifyRecommendationsResponse> response = spotifyHttpClient.getRestTemplate()
          .exchange(requestUri, HttpMethod.GET, entity, SpotifyRecommendationsResponse.class);
      
      SpotifyRecommendationsResponse spotifyResponse = response.getBody();
      log.info("Fetched recommendations: {} tracks", spotifyResponse != null && spotifyResponse.getTracks() != null ? spotifyResponse.getTracks().size() : 0);
      
      // Map to legacy format for frontend compatibility
      LegacyRecommendationsResponse legacyResponse = responseMapper.mapToLegacyFormat(spotifyResponse);
      log.debug("Mapped to legacy format: {} tracks", legacyResponse != null && legacyResponse.getTracks() != null ? legacyResponse.getTracks().size() : 0);
      
      return legacyResponse;
      
    } catch (SpotifyApiException e) {
      log.error("Spotify API error fetching recommendations: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error fetching recommendations: {}", e.getMessage());
      throw new SpotifyApiException("getRecommendation error: " + e.getMessage(), 500);
    }
  }

  public LegacyRecommendationsResponse getRecommendationWithPlayList(String playListId) {
    try {
      // Ensure we have a valid access token
      authService.initializeSpotifyApi();
      
      List<AudioFeatures> audioFeaturesList = playListService.getSongFeatureByPlayList(playListId);
      log.debug(">>> audioFeaturesList = " + audioFeaturesList);

      // Use functional programming to calculate the averages and cast Double to float
      // TODO : modify GetRecommendationsWithFeatureDto with attr as "float" type, and modify below
      // code (e.g. : averagingDouble)
      double energy =
          audioFeaturesList.stream().collect(Collectors.averagingDouble(AudioFeatures::getEnergy));
      double acousticness =
          audioFeaturesList.stream()
              .collect(Collectors.averagingDouble(AudioFeatures::getAcousticness));
      double danceability =
          audioFeaturesList.stream()
              .collect(Collectors.averagingDouble(AudioFeatures::getDanceability));
      double liveness =
          audioFeaturesList.stream()
              .collect(Collectors.averagingDouble(AudioFeatures::getLiveness));
      double loudness =
          audioFeaturesList.stream()
              .collect(Collectors.averagingDouble(AudioFeatures::getLoudness));
      double speechiness =
          audioFeaturesList.stream()
              .collect(Collectors.averagingDouble(AudioFeatures::getSpeechiness));

      GetRecommendationsWithFeatureDto featureDto = new GetRecommendationsWithFeatureDto();
      featureDto.setEnergy(energy);
      // featureDto.setAcousticness(acousticness);
      featureDto.setDanceability(danceability);
      featureDto.setLiveness(liveness);
      featureDto.setLoudness(loudness);
      featureDto.setSpeechiness(speechiness);

      // TODO : get seed features from playList
      //featureDto.setSeedArtistId("4sJCsXNYmUMeumUKVz4Abm");
      featureDto.setSeedArtistId(getRandomSeedArtistId(audioFeaturesList));
      featureDto.setSeedTrack(getRandomSeedTrackId(audioFeaturesList));

      // Build the request URI with features
      URI requestUri = spotifyHttpClient.buildRecommendationsWithFeatureUri(featureDto);
      
      // Create HTTP entity with auth headers
      HttpEntity<Void> entity = spotifyHttpClient.createHttpEntityWithoutBody();
      
      // Make the HTTP call
      ResponseEntity<SpotifyRecommendationsResponse> response = spotifyHttpClient.getRestTemplate()
          .exchange(requestUri, HttpMethod.GET, entity, SpotifyRecommendationsResponse.class);
      
      SpotifyRecommendationsResponse spotifyResponse = response.getBody();
      log.info("Fetched playlist-based recommendations: {} tracks", spotifyResponse != null && spotifyResponse.getTracks() != null ? spotifyResponse.getTracks().size() : 0);
      
      // Map to legacy format for frontend compatibility
      LegacyRecommendationsResponse legacyResponse = responseMapper.mapToLegacyFormat(spotifyResponse);
      log.debug("Mapped playlist-based recommendations to legacy format: {} tracks", legacyResponse != null && legacyResponse.getTracks() != null ? legacyResponse.getTracks().size() : 0);
      
      return legacyResponse;
      
    } catch (SpotifyApiException e) {
      log.error("Spotify API error fetching recommendations with playlist features: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error fetching recommendations with playlist features: {}", e.getMessage());
      throw new SpotifyApiException("getRecommendationWithPlayList error: " + e.getMessage(), 500);
    }
  }


  private String getRandomSeedArtistId(List<AudioFeatures> audioFeaturesList) {
    if (audioFeaturesList == null || audioFeaturesList.size() == 0) {
      throw new RuntimeException("getRandomSeedArtistId can not be null");
    }
    Random random = new Random();
    int randomInt = random.nextInt(audioFeaturesList.size());
    String trackId = audioFeaturesList.get(randomInt).getId();
    Track track = trackService.getTrackInfo(trackId);
    log.info(">>> track = " + track);
    return track.getArtists()[0].getId();
  }

  private String getRandomSeedTrackId(List<AudioFeatures> audioFeaturesList) {
    if (audioFeaturesList == null || audioFeaturesList.size() == 0) {
      throw new RuntimeException("audioFeaturesList can not be null");
    }
    Random random = new Random();
    int randomInt = random.nextInt(audioFeaturesList.size());
    String trackHref = audioFeaturesList.get(randomInt).getTrackHref();
    return getTrackIdFromTrackUrl(trackHref);
  }

  private String getTrackIdFromTrackUrl(String trackUrl) {
    if (trackUrl == null) {
      throw new RuntimeException("trackUrl can not be null");
    }
    return trackUrl.split("tracks")[1].replace("/", "");
  }

}