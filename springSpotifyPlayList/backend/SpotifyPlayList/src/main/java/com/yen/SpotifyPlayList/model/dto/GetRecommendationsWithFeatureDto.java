package com.yen.SpotifyPlayList.model.dto;

import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.ToString;

/**
 *
 * {
 * acousticness: 0.359,
 * analysisUrl: "https://api.spotify.com/v1/audio-analysis/7FJC2pF6zMliU7Lvk0GBDV",
 * danceability: 0.337,
 * durationMs: 358587,
 * energy: 0.532,
 * id: "7FJC2pF6zMliU7Lvk0GBDV",
 * instrumentalness: 0,
 * key: 6,
 * liveness: 0.0827,
 * loudness: -6.296,
 * mode: "MAJOR",
 * speechiness: 0.0345,
 * tempo: 140.257,
 * timeSignature: 4,
 * trackHref: "https://api.spotify.com/v1/tracks/7FJC2pF6zMliU7Lvk0GBDV",
 * type: "AUDIO_FEATURES",
 * uri: "spotify:track:7FJC2pF6zMliU7Lvk0GBDV",
 * valence: 0.336
 * },
 *
 */

@ToString
@Data
public class GetRecommendationsWithFeatureDto {

    private int amount = 10;
    private CountryCode market = CountryCode.JP;
    private int maxPopularity = 100;
    private int minPopularity = 0;
    private String seedArtistId; // e.g. : 0LcJLqbBmaGUft1e9Mm8HV
    private String seedGenres;
    private String seedTrack; // e.g. 01iyCAUm8EvOFqVWYJ3dVX
    private int targetPopularity = 50;
    private double danceability = 0;
    private double energy = 0;
    private double instrumentalness = 0;
    private double liveness = 0;
    private double loudness = 0;
    private double speechiness = 0;
    private double tempo = 0;
}
