package com.yen.SpotifyPlayList.service;

import com.neovisionaries.i18n.CountryCode;
import com.yen.SpotifyPlayList.exception.SpotifyApiException;
import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import com.yen.SpotifyPlayList.model.dto.GetRecommendationsWithFeatureDto;
import com.yen.SpotifyPlayList.model.dto.Response.SpotifyRecommendationsResponse;
import com.yen.SpotifyPlayList.service.AuthService;
import com.yen.SpotifyPlayList.service.PlayListService;
import com.yen.SpotifyPlayList.service.RecommendationsService;
import com.yen.SpotifyPlayList.service.SpotifyHttpClient;
import com.yen.SpotifyPlayList.service.TrackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RecommendationsServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private PlayListService playListService;

    @Mock
    private TrackService trackService;

    @Mock
    private SpotifyHttpClient spotifyHttpClient;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RecommendationsService recommendationsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGetRecommendation() throws Exception {
//        // Arrange
//        GetRecommendationsDto getRecommendationsDto = new GetRecommendationsDto();
//        getRecommendationsDto.setSeedTrack("testSeedTrack");
//        getRecommendationsDto.setAmount(10);  // Set the amount to avoid NullPointerException
//        getRecommendationsDto.setMarket(CountryCode.JP); // Set a sample market value
//
//        Recommendations expectedRecommendations = mock(Recommendations.class);
//
//        // Mocking the SpotifyApi and the builder methods to return a builder instance
//        when(authService.initializeSpotifyApi()).thenReturn(spotifyApi);
//
//        // Mock the builder and its methods
//        GetRecommendationsRequest.Builder requestBuilder = mock(GetRecommendationsRequest.Builder.class);
//        when(spotifyApi.getRecommendations()).thenReturn(requestBuilder);
//        when(requestBuilder.limit(anyInt())).thenReturn(requestBuilder);  // Chainable
//        when(requestBuilder.market(CountryCode.valueOf(anyString()))).thenReturn(requestBuilder); // Chainable
//        when(requestBuilder.seed_tracks(anyString())).thenReturn(requestBuilder); // Chainable
//        //when(requestBuilder.execute()).thenReturn(expectedRecommendations);  // Final execution
//
//        // Act
//        Recommendations result = recommendationsService.getRecommendation(getRecommendationsDto);
//
//        // Assert
//        assertEquals(expectedRecommendations, result);
//        //verify(requestBuilder).execute();  // Ensure execute() is called
//    }

//    @Test
//    void testGetRecommendationWithPlayList() throws Exception {
//        // Arrange
//        String playListId = "testPlaylistId";
//        List<AudioFeatures> audioFeaturesList = Arrays.asList(mock(AudioFeatures.class), mock(AudioFeatures.class));
//        when(authService.initializeSpotifyApi()).thenReturn(spotifyApi);
//        when(playListService.getSongFeatureByPlayList(playListId)).thenReturn(audioFeaturesList);
//
//        GetRecommendationsWithFeatureDto featureDto = new GetRecommendationsWithFeatureDto();
//        featureDto.setEnergy(0.5);
//        featureDto.setDanceability(0.5);
//        featureDto.setSeedArtistId("4sJCsXNYmUMeumUKVz4Abm");
//        featureDto.setSeedTrack("some_track");
//
//        Recommendations expectedRecommendations = mock(Recommendations.class);
//        when(getRecommendationsRequest.execute()).thenReturn(expectedRecommendations);
//
//        // Act
//        Recommendations result = recommendationsService.getRecommendationWithPlayList(playListId);
//
//        // Assert
//        assertEquals(expectedRecommendations, result);
//        verify(getRecommendationsRequest).execute();
//    }

    // TODO: Update tests once we fully migrate away from spotify-web-api-java library
    // The tests are currently disabled due to Java version conflicts with the old library
    
    // @Test
    // void testGetRecommendationWithPlayListThrowsException() {
    //     // Tests disabled temporarily due to Java version conflicts
    // }

    // @Test  
    // void testGetRecommendationSuccess() {
    //     // Tests disabled temporarily due to Java version conflicts
    // }
}