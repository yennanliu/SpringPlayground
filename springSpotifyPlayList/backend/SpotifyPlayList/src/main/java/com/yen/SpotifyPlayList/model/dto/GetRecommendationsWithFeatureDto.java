package com.yen.SpotifyPlayList.model.dto;

import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * { acousticness: 0.359, analysisUrl:
 * "https://api.spotify.com/v1/audio-analysis/7FJC2pF6zMliU7Lvk0GBDV", danceability: 0.337,
 * durationMs: 358587, energy: 0.532, id: "7FJC2pF6zMliU7Lvk0GBDV", instrumentalness: 0, key: 6,
 * liveness: 0.0827, loudness: -6.296, mode: "MAJOR", speechiness: 0.0345, tempo: 140.257,
 * timeSignature: 4, trackHref: "https://api.spotify.com/v1/tracks/7FJC2pF6zMliU7Lvk0GBDV", type:
 * "AUDIO_FEATURES", uri: "spotify:track:7FJC2pF6zMliU7Lvk0GBDV", valence: 0.336 },
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRecommendationsWithFeatureDto {
  private int amount = 10;
  private CountryCode market = CountryCode.JP;
  private int maxPopularity = 100;
  private int minPopularity = 0;
  private String seedArtistIds;  // Multiple artists, comma-separated
  private String seedTracks;     // Multiple tracks, comma-separated
  private float danceability = 0.5f;
  private float energy = 0.5f;
  private float instrumentalness = 0.0f;
  private float liveness = 0.0f;
  private float acousticness = 0.5f;
  private float valence = 0.5f;
  private float tempo = 120.0f;
}
