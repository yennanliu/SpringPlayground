package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.service.AuthService;
import com.yen.SpotifyPlayList.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchAlbumsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private SearchAlbumsRequest.Builder searchAlbumsRequestBuilder;

    @Mock
    private SearchAlbumsRequest searchAlbumsRequest;

    @Mock
    private Paging<AlbumSimplified> albumPaging;

    @Mock
    private SearchArtistsRequest.Builder searchArtistsRequestBuilder;

    @Mock
    private SearchArtistsRequest searchArtistsRequest;

    @Mock
    private Paging<Artist> artistPaging;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchAlbum() throws Exception {
        // Arrange
        String keyword = "testAlbum";
        AlbumSimplified[] expectedAlbums = new AlbumSimplified[]{mock(AlbumSimplified.class), mock(AlbumSimplified.class)};

        when(authService.initializeSpotifyApi()).thenReturn(spotifyApi);
        when(spotifyApi.searchAlbums(keyword)).thenReturn(searchAlbumsRequestBuilder);
        when(searchAlbumsRequestBuilder.build()).thenReturn(searchAlbumsRequest);
        when(searchAlbumsRequest.execute()).thenReturn(albumPaging);
        when(albumPaging.getItems()).thenReturn(expectedAlbums);

        // Act
        AlbumSimplified[] result = searchService.searchAlbum(keyword);

        // Assert
        assertArrayEquals(expectedAlbums, result);
        verify(searchAlbumsRequest).execute();
    }

    @Test
    void testSearchArtist() throws Exception {
        // Arrange
        String keyword = "testArtist";
        Artist[] expectedArtists = new Artist[]{mock(Artist.class), mock(Artist.class)};

        when(authService.initializeSpotifyApi()).thenReturn(spotifyApi);
        when(spotifyApi.searchArtists(keyword)).thenReturn(searchArtistsRequestBuilder);
        when(searchArtistsRequestBuilder.build()).thenReturn(searchArtistsRequest);
        when(searchArtistsRequest.execute()).thenReturn(artistPaging);
        when(artistPaging.getItems()).thenReturn(expectedArtists);

        // Act
        Artist[] result = searchService.searchArtist(keyword);

        // Assert
        assertArrayEquals(expectedArtists, result);
        verify(searchArtistsRequest).execute();
    }
}