package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.service.AlbumService;
import com.yen.SpotifyPlayList.service.AuthService;
import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AlbumServiceTest {

  @Mock private AuthService authService;

  @Mock private SpotifyApi spotifyApi;

  @Mock private GetAlbumRequest getAlbumRequest;

  @Mock private GetAlbumRequest.Builder getAlbumRequestBuilder;

  @InjectMocks private AlbumService albumService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(authService.initializeSpotifyApi()).thenReturn(spotifyApi);
  }

  @Test
  void testGetAlbum_Success() throws IOException, SpotifyWebApiException, ParseException {
    // Arrange
    String albumId = "albumId123";
    Album expectedAlbum = mock(Album.class);

    /**
     * NOTE !!!
     *
     * <p>we mock builder via @Mock private GetAlbumRequest.Builder getAlbumRequestBuilder;
     */
    // Mock the getAlbum builder and the build() method
    when(spotifyApi.getAlbum(albumId)).thenReturn(getAlbumRequestBuilder);
    when(getAlbumRequestBuilder.build()).thenReturn(getAlbumRequest);
    when(getAlbumRequest.execute()).thenReturn(expectedAlbum);

    // Act
    Album result = albumService.getAlbum(albumId);

    // Assert
    assertEquals(expectedAlbum, result);
    verify(spotifyApi).getAlbum(albumId);
    verify(getAlbumRequestBuilder).build();
    verify(getAlbumRequest).execute();
  }

  @Test
  void testGetAlbum_Failure() throws IOException, SpotifyWebApiException, ParseException {
    // Arrange
    String albumId = "albumId123";

    // Mock the getAlbum builder and make it throw an exception on execute()
    when(spotifyApi.getAlbum(albumId)).thenReturn(getAlbumRequestBuilder);
    when(getAlbumRequestBuilder.build()).thenReturn(getAlbumRequest);
    when(getAlbumRequest.execute()).thenThrow(new IOException("Error fetching album"));

    // Act & Assert
    assertThrows(SpotifyWebApiException.class, () -> albumService.getAlbum(albumId));
    verify(spotifyApi).getAlbum(albumId);
    verify(getAlbumRequestBuilder).build();
    verify(getAlbumRequest).execute();
  }
}
