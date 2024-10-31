package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.service.AuthService;
import com.yen.SpotifyPlayList.service.UserDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

class UserDataServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private GetListOfUsersPlaylistsRequest.Builder getListOfUsersPlaylistsRequestBuilder;

    @Mock
    private GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest;

    @Mock
    private Paging<PlaylistSimplified> playlistPaging;

    @InjectMocks
    private UserDataService userDataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserAllPlaylists() throws Exception {
        // Arrange
        String userId = "testUserId";
        PlaylistSimplified[] expectedPlaylists = new PlaylistSimplified[]{
                mock(PlaylistSimplified.class),
                mock(PlaylistSimplified.class)
        };

        // Mock the authService to return a SpotifyApi instance
        when(authService.initializeSpotifyApi()).thenReturn(spotifyApi);

        // Mock the Spotify API request building and execution
        when(spotifyApi.getListOfUsersPlaylists(userId)).thenReturn(getListOfUsersPlaylistsRequestBuilder);
        when(getListOfUsersPlaylistsRequestBuilder.build()).thenReturn(getListOfUsersPlaylistsRequest);
        when(getListOfUsersPlaylistsRequest.execute()).thenReturn(playlistPaging);
        when(playlistPaging.getItems()).thenReturn(expectedPlaylists);

        // Act
        PlaylistSimplified[] result = userDataService.getUserAllPlaylists(userId);

        // Assert
        assertArrayEquals(expectedPlaylists, result);
        verify(getListOfUsersPlaylistsRequest).execute();
    }

}