package com.yen.SpotifyPlayList.model.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SpotifyRecommendationsResponse {
    private List<SpotifyTrack> tracks;
    private List<SpotifySeed> seeds;

    @Data
    @ToString
    public static class SpotifyTrack {
        private SpotifyAlbum album;
        private List<SpotifyArtist> artists;
        @JsonProperty("available_markets")
        private List<String> availableMarkets;
        @JsonProperty("disc_number")
        private Integer discNumber;
        @JsonProperty("duration_ms")
        private Integer durationMs;
        private Boolean explicit;
        @JsonProperty("external_ids")
        private SpotifyExternalIds externalIds;
        @JsonProperty("external_urls")
        private SpotifyExternalUrls externalUrls;
        private String href;
        private String id;
        @JsonProperty("is_playable")
        private Boolean isPlayable;
        @JsonProperty("linked_from")
        private SpotifyLinkedFrom linkedFrom;
        private String name;
        private Integer popularity;
        @JsonProperty("preview_url")
        private String previewUrl;
        private SpotifyRestrictions restrictions;
        @JsonProperty("track_number")
        private Integer trackNumber;
        private String type;
        private String uri;
        @JsonProperty("is_local")
        private Boolean isLocal;
    }

    @Data
    @ToString
    public static class SpotifyAlbum {
        @JsonProperty("album_type")
        private String albumType;
        @JsonProperty("total_tracks")
        private Integer totalTracks;
        @JsonProperty("available_markets")
        private List<String> availableMarkets;
        @JsonProperty("external_urls")
        private SpotifyExternalUrls externalUrls;
        private String href;
        private String id;
        private List<SpotifyImage> images;
        private String name;
        @JsonProperty("release_date")
        private String releaseDate;
        @JsonProperty("release_date_precision")
        private String releaseDatePrecision;
        private SpotifyRestrictions restrictions;
        private String type;
        private String uri;
        private List<SpotifyArtist> artists;
    }

    @Data
    @ToString
    public static class SpotifyArtist {
        @JsonProperty("external_urls")
        private SpotifyExternalUrls externalUrls;
        private String href;
        private String id;
        private String name;
        private String type;
        private String uri;
    }

    @Data
    @ToString
    public static class SpotifyImage {
        private String url;
        private Integer height;
        private Integer width;
    }

    @Data
    @ToString
    public static class SpotifyExternalIds {
        private String isrc;
        private String ean;
        private String upc;
    }

    @Data
    @ToString
    public static class SpotifyExternalUrls {
        private String spotify;
    }

    @Data
    @ToString
    public static class SpotifyRestrictions {
        private String reason;
    }

    @Data
    @ToString
    public static class SpotifyLinkedFrom {
        @JsonProperty("external_urls")
        private SpotifyExternalUrls externalUrls;
        private String href;
        private String id;
        private String type;
        private String uri;
    }

    @Data
    @ToString
    public static class SpotifySeed {
        @JsonProperty("afterFilteringSize")
        private Integer afterFilteringSize;
        @JsonProperty("afterRelinkingSize")
        private Integer afterRelinkingSize;
        private String href;
        private String id;
        @JsonProperty("initialPoolSize")
        private Integer initialPoolSize;
        private String type;
    }
}