//package com.yen.SpotifyPlayList.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import se.michaelthelin.spotify.SpotifyApi;
//import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
//import se.michaelthelin.spotify.model_objects.specification.Album;
//import se.michaelthelin.spotify.model_objects.specification.Paging;
//import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
//import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
//import se.michaelthelin.spotify.requests.data.albums.GetAlbumsTracksRequest;
//
//import java.io.IOException;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import se.michaelthelin.spotify.SpotifyApi;
//import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
//import se.michaelthelin.spotify.model_objects.specification.Album;
//import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class AlbumServiceTest {
//
//    @Mock
//    private AuthService authService;
//
//    @Mock
//    private SpotifyApi spotifyApi;
//
//    @Mock
//    private GetAlbumRequest.Builder getAlbumRequestBuilder;
//
//    @Mock
//    private GetAlbumRequest getAlbumRequest;
//
//    @InjectMocks
//    private AlbumService albumService;
//
//    @BeforeEach
//    public void setUp() {
//        //MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testGetAlbum_Success() throws Exception {
//        // Arrange
//        String albumId = "testAlbumId";
//        Album mockAlbum = mock(Album.class);
//
//        when(authService.initializeSpotifyApi()).thenReturn(spotifyApi);
//        when(spotifyApi.getAlbum(albumId)).thenReturn(getAlbumRequestBuilder);  // Mocking builder
//        when(getAlbumRequestBuilder.build()).thenReturn(getAlbumRequest);       // Mocking build() method
//        when(getAlbumRequest.execute()).thenReturn(mockAlbum);                  // Mocking execute()
//
//        // Act
//        Album album = albumService.getAlbum(albumId);
//
//        // Assert
//        assertNotNull(album);
//        verify(authService, times(1)).initializeSpotifyApi();
//        verify(spotifyApi, times(1)).getAlbum(albumId);
//        verify(getAlbumRequestBuilder, times(1)).build();
//        verify(getAlbumRequest, times(1)).execute();
//    }
//
//    @Test
//    public void testGetAlbum_Exception() throws Exception {
//        // Arrange
//        String albumId = "testAlbumId";
//
//        when(authService.initializeSpotifyApi()).thenReturn(spotifyApi);
//        when(spotifyApi.getAlbum(albumId)).thenReturn(getAlbumRequestBuilder);  // Mocking builder
//        when(getAlbumRequestBuilder.build()).thenReturn(getAlbumRequest);       // Mocking build() method
//        when(getAlbumRequest.execute()).thenThrow(new IOException("API error")); // Mocking exception
//
//        // Act & Assert
//        SpotifyWebApiException thrown = assertThrows(SpotifyWebApiException.class, () -> {
//            albumService.getAlbum(albumId);
//        });
//
//        assertTrue(thrown.getMessage().contains("Error fetching album"));
//        verify(authService, times(1)).initializeSpotifyApi();
//        verify(spotifyApi, times(1)).getAlbum(albumId);
//        verify(getAlbumRequestBuilder, times(1)).build();
//        verify(getAlbumRequest, times(1)).execute();
//    }
//}