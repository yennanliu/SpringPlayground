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

    @Test
    void testMapToLegacyFormat_WithRestrictions() {
        SpotifyRecommendationsResponse spotifyResponse = new SpotifyRecommendationsResponse();
        
        SpotifyRecommendationsResponse.SpotifyTrack track = new SpotifyRecommendationsResponse.SpotifyTrack();
        track.setId("track123");
        track.setName("Restricted Track");
        
        // Add restrictions
        SpotifyRecommendationsResponse.SpotifyRestrictions restrictions = new SpotifyRecommendationsResponse.SpotifyRestrictions();
        restrictions.setReason("market");
        track.setRestrictions(restrictions);
        
        spotifyResponse.setTracks(Arrays.asList(track));
        
        LegacyRecommendationsResponse result = mapper.mapToLegacyFormat(spotifyResponse);
        
        assertNotNull(result);
        assertNotNull(result.getTracks());
        assertEquals(1, result.getTracks().size());
        
        LegacyRecommendationsResponse.LegacyTrack legacyTrack = result.getTracks().get(0);
        assertNotNull(legacyTrack.getRestrictions());
        assertEquals("market", legacyTrack.getRestrictions().getReason());
    }

    @Test
    void testMapToLegacyFormat_WithLinkedFrom() {
        SpotifyRecommendationsResponse spotifyResponse = new SpotifyRecommendationsResponse();
        
        SpotifyRecommendationsResponse.SpotifyTrack track = new SpotifyRecommendationsResponse.SpotifyTrack();
        track.setId("track123");
        track.setName("Linked Track");
        
        // Add linked_from
        SpotifyRecommendationsResponse.SpotifyLinkedFrom linkedFrom = new SpotifyRecommendationsResponse.SpotifyLinkedFrom();
        linkedFrom.setId("original123");
        linkedFrom.setHref("https://api.spotify.com/v1/tracks/original123");
        linkedFrom.setType("track");
        linkedFrom.setUri("spotify:track:original123");
        
        SpotifyRecommendationsResponse.SpotifyExternalUrls externalUrls = new SpotifyRecommendationsResponse.SpotifyExternalUrls();
        externalUrls.setSpotify("https://open.spotify.com/track/original123");
        linkedFrom.setExternalUrls(externalUrls);
        
        track.setLinkedFrom(linkedFrom);
        spotifyResponse.setTracks(Arrays.asList(track));
        
        LegacyRecommendationsResponse result = mapper.mapToLegacyFormat(spotifyResponse);
        
        assertNotNull(result);
        assertNotNull(result.getTracks());
        assertEquals(1, result.getTracks().size());
        
        LegacyRecommendationsResponse.LegacyTrack legacyTrack = result.getTracks().get(0);
        assertNotNull(legacyTrack.getLinkedFrom());
        assertEquals("original123", legacyTrack.getLinkedFrom().getId());
        assertEquals("track", legacyTrack.getLinkedFrom().getType());
        
        // Verify nested external URLs structure
        assertNotNull(legacyTrack.getLinkedFrom().getExternalUrls());
        assertNotNull(legacyTrack.getLinkedFrom().getExternalUrls().getExternalUrls());
        assertEquals("https://open.spotify.com/track/original123", 
                legacyTrack.getLinkedFrom().getExternalUrls().getExternalUrls().getSpotify());
    }

    @Test
    void testMapToLegacyFormat_WithSeeds() {
        SpotifyRecommendationsResponse spotifyResponse = new SpotifyRecommendationsResponse();
        
        // Create seeds
        SpotifyRecommendationsResponse.SpotifySeed seed = new SpotifyRecommendationsResponse.SpotifySeed();
        seed.setId("4NHQUGzhtTLFvgF5SZesLK");
        seed.setType("artist");
        seed.setHref("https://api.spotify.com/v1/artists/4NHQUGzhtTLFvgF5SZesLK");
        seed.setInitialPoolSize(500);
        seed.setAfterFilteringSize(250);
        seed.setAfterRelinkingSize(240);
        
        spotifyResponse.setSeeds(Arrays.asList(seed));
        spotifyResponse.setTracks(Collections.emptyList());
        
        LegacyRecommendationsResponse result = mapper.mapToLegacyFormat(spotifyResponse);
        
        assertNotNull(result);
        assertNotNull(result.getSeeds());
        assertEquals(1, result.getSeeds().size());
        
        LegacyRecommendationsResponse.LegacySeed legacySeed = result.getSeeds().get(0);
        assertEquals("4NHQUGzhtTLFvgF5SZesLK", legacySeed.getId());
        assertEquals("artist", legacySeed.getType());
        assertEquals(Integer.valueOf(500), legacySeed.getInitialPoolSize());
        assertEquals(Integer.valueOf(250), legacySeed.getAfterFilteringSize());
        assertEquals(Integer.valueOf(240), legacySeed.getAfterRelinkingSize());
    }

    @Test
    void testMapToLegacyFormat_NullOptionalFields() {
        // Test that null optional fields don't cause issues
        SpotifyRecommendationsResponse spotifyResponse = new SpotifyRecommendationsResponse();
        
        SpotifyRecommendationsResponse.SpotifyTrack track = new SpotifyRecommendationsResponse.SpotifyTrack();
        track.setId("track123");
        track.setName("Basic Track");
        track.setUri("spotify:track:track123");
        
        // Explicitly set optional fields to null
        track.setRestrictions(null);
        track.setLinkedFrom(null);
        track.setPreviewUrl(null);
        track.setExternalUrls(null);
        track.setExternalIds(null);
        track.setAlbum(null);
        track.setArtists(null);
        
        spotifyResponse.setTracks(Arrays.asList(track));
        
        LegacyRecommendationsResponse result = mapper.mapToLegacyFormat(spotifyResponse);
        
        assertNotNull(result);
        assertNotNull(result.getTracks());
        assertEquals(1, result.getTracks().size());
        
        LegacyRecommendationsResponse.LegacyTrack legacyTrack = result.getTracks().get(0);
        assertEquals("track123", legacyTrack.getId());
        assertEquals("Basic Track", legacyTrack.getName());
        assertEquals("spotify:track:track123", legacyTrack.getUri());
        
        // Verify null fields remain null
        assertNull(legacyTrack.getRestrictions());
        assertNull(legacyTrack.getLinkedFrom());
        assertNull(legacyTrack.getPreviewUrl());
        assertNull(legacyTrack.getExternalUrls());
        assertNull(legacyTrack.getExternalIds());
        assertNull(legacyTrack.getAlbum());
        assertNull(legacyTrack.getArtists());
    }
}