package com.yen.SpotifyPlayList.model.dto.Response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Legacy response format that matches the original spotify-web-api-java library structure
 * This maintains compatibility with the existing frontend expectations
 */
@Data
@ToString
public class LegacyRecommendationsResponse {
    private List<LegacyTrack> tracks;
    private List<LegacySeed> seeds;

    @Data
    @ToString
    public static class LegacyTrack {
        private LegacyAlbum album;
        private List<LegacyArtist> artists;
        private List<String> availableMarkets;
        private Integer discNumber;
        private Integer durationMs;
        private Boolean explicit;
        private LegacyExternalIds externalIds;
        private LegacyExternalUrls externalUrls;  // Frontend expects this nested structure
        private String href;
        private String id;
        private Boolean isPlayable;
        private LegacyLinkedFrom linkedFrom;
        private String name;
        private Integer popularity;
        private String previewUrl;  // Frontend expects camelCase
        private LegacyRestrictions restrictions;
        private Integer trackNumber;
        private String type;
        private String uri;
        private Boolean isLocal;
    }

    @Data
    @ToString
    public static class LegacyAlbum {
        private String albumType;
        private Integer totalTracks;
        private List<String> availableMarkets;
        private LegacyExternalUrls externalUrls;
        private String href;
        private String id;
        private List<LegacyImage> images;
        private String name;
        private String releaseDate;
        private String releaseDatePrecision;
        private LegacyRestrictions restrictions;
        private String type;
        private String uri;
        private List<LegacyArtist> artists;
    }

    @Data
    @ToString
    public static class LegacyArtist {
        private LegacyExternalUrls externalUrls;
        private String href;
        private String id;
        private String name;
        private String type;
        private String uri;
    }

    @Data
    @ToString
    public static class LegacyImage {
        private String url;
        private Integer height;
        private Integer width;
    }

    @Data
    @ToString
    public static class LegacyExternalIds {
        private String isrc;
        private String ean;
        private String upc;
    }

    @Data
    @ToString
    public static class LegacyExternalUrls {
        // Frontend expects: track.externalUrls.externalUrls.spotify
        private LegacySpotifyUrls externalUrls;
        
        @Data
        @ToString
        public static class LegacySpotifyUrls {
            private String spotify;
        }
    }

    @Data
    @ToString
    public static class LegacyRestrictions {
        private String reason;
    }

    @Data
    @ToString
    public static class LegacyLinkedFrom {
        private LegacyExternalUrls externalUrls;
        private String href;
        private String id;
        private String type;
        private String uri;
    }

    @Data
    @ToString
    public static class LegacySeed {
        private Integer afterFilteringSize;
        private Integer afterRelinkingSize;
        private String href;
        private String id;
        private Integer initialPoolSize;
        private String type;
    }
}