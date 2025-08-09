package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.dto.Response.LegacyRecommendationsResponse;
import com.yen.SpotifyPlayList.model.dto.Response.SpotifyRecommendationsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecommendationsResponseMapper {

    public LegacyRecommendationsResponse mapToLegacyFormat(SpotifyRecommendationsResponse spotifyResponse) {
        if (spotifyResponse == null) {
            return null;
        }

        LegacyRecommendationsResponse legacyResponse = new LegacyRecommendationsResponse();
        
        // Map tracks
        if (spotifyResponse.getTracks() != null) {
            legacyResponse.setTracks(spotifyResponse.getTracks().stream()
                    .map(this::mapTrack)
                    .collect(Collectors.toList()));
        }

        // Map seeds
        if (spotifyResponse.getSeeds() != null) {
            legacyResponse.setSeeds(spotifyResponse.getSeeds().stream()
                    .map(this::mapSeed)
                    .collect(Collectors.toList()));
        }

        log.debug("Mapped {} tracks to legacy format", 
                legacyResponse.getTracks() != null ? legacyResponse.getTracks().size() : 0);
        
        return legacyResponse;
    }

    private LegacyRecommendationsResponse.LegacyTrack mapTrack(SpotifyRecommendationsResponse.SpotifyTrack spotifyTrack) {
        LegacyRecommendationsResponse.LegacyTrack legacyTrack = new LegacyRecommendationsResponse.LegacyTrack();
        
        legacyTrack.setId(spotifyTrack.getId());
        legacyTrack.setName(spotifyTrack.getName());
        legacyTrack.setUri(spotifyTrack.getUri());
        legacyTrack.setHref(spotifyTrack.getHref());
        legacyTrack.setType(spotifyTrack.getType());
        legacyTrack.setPopularity(spotifyTrack.getPopularity());
        legacyTrack.setExplicit(spotifyTrack.getExplicit());
        legacyTrack.setIsLocal(spotifyTrack.getIsLocal());
        legacyTrack.setIsPlayable(spotifyTrack.getIsPlayable());
        legacyTrack.setDiscNumber(spotifyTrack.getDiscNumber());
        legacyTrack.setTrackNumber(spotifyTrack.getTrackNumber());
        legacyTrack.setDurationMs(spotifyTrack.getDurationMs());
        legacyTrack.setAvailableMarkets(spotifyTrack.getAvailableMarkets());
        
        // Convert snake_case to camelCase for preview URL
        legacyTrack.setPreviewUrl(spotifyTrack.getPreviewUrl());
        
        // Map external URLs with special nested structure
        if (spotifyTrack.getExternalUrls() != null) {
            legacyTrack.setExternalUrls(mapExternalUrls(spotifyTrack.getExternalUrls()));
        }
        
        // Map external IDs
        if (spotifyTrack.getExternalIds() != null) {
            legacyTrack.setExternalIds(mapExternalIds(spotifyTrack.getExternalIds()));
        }
        
        // Map album
        if (spotifyTrack.getAlbum() != null) {
            legacyTrack.setAlbum(mapAlbum(spotifyTrack.getAlbum()));
        }
        
        // Map artists
        if (spotifyTrack.getArtists() != null) {
            legacyTrack.setArtists(spotifyTrack.getArtists().stream()
                    .map(this::mapArtist)
                    .collect(Collectors.toList()));
        }
        
        return legacyTrack;
    }

    private LegacyRecommendationsResponse.LegacyExternalUrls mapExternalUrls(SpotifyRecommendationsResponse.SpotifyExternalUrls spotifyUrls) {
        LegacyRecommendationsResponse.LegacyExternalUrls legacyUrls = new LegacyRecommendationsResponse.LegacyExternalUrls();
        
        // Create the nested structure that the frontend expects: externalUrls.externalUrls.spotify
        LegacyRecommendationsResponse.LegacyExternalUrls.LegacySpotifyUrls spotifyUrlsInner = 
                new LegacyRecommendationsResponse.LegacyExternalUrls.LegacySpotifyUrls();
        spotifyUrlsInner.setSpotify(spotifyUrls.getSpotify());
        
        legacyUrls.setExternalUrls(spotifyUrlsInner);
        
        return legacyUrls;
    }

    private LegacyRecommendationsResponse.LegacyExternalIds mapExternalIds(SpotifyRecommendationsResponse.SpotifyExternalIds spotifyIds) {
        LegacyRecommendationsResponse.LegacyExternalIds legacyIds = new LegacyRecommendationsResponse.LegacyExternalIds();
        legacyIds.setIsrc(spotifyIds.getIsrc());
        legacyIds.setEan(spotifyIds.getEan());
        legacyIds.setUpc(spotifyIds.getUpc());
        return legacyIds;
    }

    private LegacyRecommendationsResponse.LegacyAlbum mapAlbum(SpotifyRecommendationsResponse.SpotifyAlbum spotifyAlbum) {
        LegacyRecommendationsResponse.LegacyAlbum legacyAlbum = new LegacyRecommendationsResponse.LegacyAlbum();
        
        legacyAlbum.setId(spotifyAlbum.getId());
        legacyAlbum.setName(spotifyAlbum.getName());
        legacyAlbum.setUri(spotifyAlbum.getUri());
        legacyAlbum.setHref(spotifyAlbum.getHref());
        legacyAlbum.setType(spotifyAlbum.getType());
        legacyAlbum.setAvailableMarkets(spotifyAlbum.getAvailableMarkets());
        
        // Convert snake_case to camelCase
        legacyAlbum.setAlbumType(spotifyAlbum.getAlbumType());
        legacyAlbum.setTotalTracks(spotifyAlbum.getTotalTracks());
        legacyAlbum.setReleaseDate(spotifyAlbum.getReleaseDate());
        legacyAlbum.setReleaseDatePrecision(spotifyAlbum.getReleaseDatePrecision());
        
        // Map external URLs
        if (spotifyAlbum.getExternalUrls() != null) {
            legacyAlbum.setExternalUrls(mapExternalUrls(spotifyAlbum.getExternalUrls()));
        }
        
        // Map images
        if (spotifyAlbum.getImages() != null) {
            legacyAlbum.setImages(spotifyAlbum.getImages().stream()
                    .map(this::mapImage)
                    .collect(Collectors.toList()));
        }
        
        // Map artists
        if (spotifyAlbum.getArtists() != null) {
            legacyAlbum.setArtists(spotifyAlbum.getArtists().stream()
                    .map(this::mapArtist)
                    .collect(Collectors.toList()));
        }
        
        return legacyAlbum;
    }

    private LegacyRecommendationsResponse.LegacyArtist mapArtist(SpotifyRecommendationsResponse.SpotifyArtist spotifyArtist) {
        LegacyRecommendationsResponse.LegacyArtist legacyArtist = new LegacyRecommendationsResponse.LegacyArtist();
        
        legacyArtist.setId(spotifyArtist.getId());
        legacyArtist.setName(spotifyArtist.getName());
        legacyArtist.setUri(spotifyArtist.getUri());
        legacyArtist.setHref(spotifyArtist.getHref());
        legacyArtist.setType(spotifyArtist.getType());
        
        if (spotifyArtist.getExternalUrls() != null) {
            legacyArtist.setExternalUrls(mapExternalUrls(spotifyArtist.getExternalUrls()));
        }
        
        return legacyArtist;
    }

    private LegacyRecommendationsResponse.LegacyImage mapImage(SpotifyRecommendationsResponse.SpotifyImage spotifyImage) {
        LegacyRecommendationsResponse.LegacyImage legacyImage = new LegacyRecommendationsResponse.LegacyImage();
        legacyImage.setUrl(spotifyImage.getUrl());
        legacyImage.setHeight(spotifyImage.getHeight());
        legacyImage.setWidth(spotifyImage.getWidth());
        return legacyImage;
    }

    private LegacyRecommendationsResponse.LegacySeed mapSeed(SpotifyRecommendationsResponse.SpotifySeed spotifySeed) {
        LegacyRecommendationsResponse.LegacySeed legacySeed = new LegacyRecommendationsResponse.LegacySeed();
        
        legacySeed.setId(spotifySeed.getId());
        legacySeed.setType(spotifySeed.getType());
        legacySeed.setHref(spotifySeed.getHref());
        legacySeed.setAfterFilteringSize(spotifySeed.getAfterFilteringSize());
        legacySeed.setAfterRelinkingSize(spotifySeed.getAfterRelinkingSize());
        legacySeed.setInitialPoolSize(spotifySeed.getInitialPoolSize());
        
        return legacySeed;
    }
}