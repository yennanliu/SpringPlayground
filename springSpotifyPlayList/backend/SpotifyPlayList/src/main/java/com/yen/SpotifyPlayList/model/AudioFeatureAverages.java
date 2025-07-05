package com.yen.SpotifyPlayList.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AudioFeatureAverages {
    private double energy;
    private double acousticness;
    private double danceability;
    private double instrumentalness;
    private double liveness;
    private double valence;
} 