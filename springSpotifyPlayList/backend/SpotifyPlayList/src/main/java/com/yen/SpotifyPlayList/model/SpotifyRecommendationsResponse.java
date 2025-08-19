package com.yen.SpotifyPlayList.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyRecommendationsResponse {
    private List<Track> tracks;
    private List<RecommendationSeed> seeds;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecommendationSeed {
        private Integer afterFilteringSize;
        private Integer afterRelinkingSize;
        private String href;
        private String id;
        private Integer initialPoolSize;
        private String type;
    }
} 