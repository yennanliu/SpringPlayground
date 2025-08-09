package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.dto.Response.LegacyRecommendationsResponse;
import com.yen.SpotifyPlayList.model.dto.Response.SpotifyRecommendationsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationsResponseMapperTest {

    private RecommendationsResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RecommendationsResponseMapper();
    }

    @Test
    void testMapToLegacyFormat_NullInput() {
        LegacyRecommendationsResponse result = mapper.mapToLegacyFormat(null);
        assertNull(result);
    }

    @Test
    void testMapToLegacyFormat_ValidInput() {
        // Create mock Spotify API response
        SpotifyRecommendationsResponse spotifyResponse = new SpotifyRecommendationsResponse();
        
        // Create a mock track
        SpotifyRecommendationsResponse.SpotifyTrack track = new SpotifyRecommendationsResponse.SpotifyTrack();
        track.setId("track123");
        track.setName("Test Track");
        track.setUri("spotify:track:track123");
        track.setPreviewUrl("https://example.com/preview.mp3");
        
        // Create mock external URLs
        SpotifyRecommendationsResponse.SpotifyExternalUrls externalUrls = new SpotifyRecommendationsResponse.SpotifyExternalUrls();
        externalUrls.setSpotify("https://open.spotify.com/track/track123");
        track.setExternalUrls(externalUrls);
        
        // Create mock artist
        SpotifyRecommendationsResponse.SpotifyArtist artist = new SpotifyRecommendationsResponse.SpotifyArtist();
        artist.setId("artist123");
        artist.setName("Test Artist");
        artist.setExternalUrls(externalUrls);
        track.setArtists(Arrays.asList(artist));
        
        // Create mock album
        SpotifyRecommendationsResponse.SpotifyAlbum album = new SpotifyRecommendationsResponse.SpotifyAlbum();
        album.setId("album123");
        album.setName("Test Album");
        
        // Create mock image
        SpotifyRecommendationsResponse.SpotifyImage image = new SpotifyRecommendationsResponse.SpotifyImage();
        image.setUrl("https://example.com/image.jpg");
        image.setHeight(300);
        image.setWidth(300);
        album.setImages(Arrays.asList(image));
        
        track.setAlbum(album);
        
        spotifyResponse.setTracks(Arrays.asList(track));
        
        // Map to legacy format
        LegacyRecommendationsResponse result = mapper.mapToLegacyFormat(spotifyResponse);
        
        // Verify mapping
        assertNotNull(result);
        assertNotNull(result.getTracks());
        assertEquals(1, result.getTracks().size());
        
        LegacyRecommendationsResponse.LegacyTrack legacyTrack = result.getTracks().get(0);
        assertEquals("track123", legacyTrack.getId());
        assertEquals("Test Track", legacyTrack.getName());
        assertEquals("spotify:track:track123", legacyTrack.getUri());
        assertEquals("https://example.com/preview.mp3", legacyTrack.getPreviewUrl());
        
        // Verify nested external URLs structure for frontend compatibility
        assertNotNull(legacyTrack.getExternalUrls());
        assertNotNull(legacyTrack.getExternalUrls().getExternalUrls());
        assertEquals("https://open.spotify.com/track/track123", 
                legacyTrack.getExternalUrls().getExternalUrls().getSpotify());
        
        // Verify artist mapping
        assertNotNull(legacyTrack.getArtists());
        assertEquals(1, legacyTrack.getArtists().size());
        assertEquals("Test Artist", legacyTrack.getArtists().get(0).getName());
        
        // Verify album and image mapping
        assertNotNull(legacyTrack.getAlbum());
        assertEquals("Test Album", legacyTrack.getAlbum().getName());
        assertNotNull(legacyTrack.getAlbum().getImages());
        assertEquals(1, legacyTrack.getAlbum().getImages().size());
        assertEquals("https://example.com/image.jpg", legacyTrack.getAlbum().getImages().get(0).getUrl());
    }

    @Test
    void testMapToLegacyFormat_EmptyTracks() {
        SpotifyRecommendationsResponse spotifyResponse = new SpotifyRecommendationsResponse();
        spotifyResponse.setTracks(Collections.emptyList());
        
        LegacyRecommendationsResponse result = mapper.mapToLegacyFormat(spotifyResponse);
        
        assertNotNull(result);
        assertNotNull(result.getTracks());
        assertTrue(result.getTracks().isEmpty());
    }
}