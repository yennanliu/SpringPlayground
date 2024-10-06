//package com.yen.SpotifyPlayList.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import se.michaelthelin.spotify.SpotifyApi;
//import se.michaelthelin.spotify.model_objects.specification.Paging;
//import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
//import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class UserDataServiceTest {
//
//    @Mock
//    private AuthService authService;
//
//    @Mock
//    private SpotifyApi spotifyApi;
//
//    @Mock
//    private GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest;
//
//    @Mock
//    private Paging<PlaylistSimplified> playlistPaging;
//
//    @InjectMocks
//    private UserDataService userDataService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testGetUserAllPlaylistsSuccess() throws Exception {
//        // Given
//        String userId = "testUserId";
//        PlaylistSimplified[] mockPlaylists = new PlaylistSimplified[2]; // Mock data
//
//        // Set up mocks
//        when(authService.initializeSpotifyApi()).thenReturn(spotifyApi);
//        //getListOfUsersPlaylistsRequest = new GetListOfUsersPlaylistsRequest.Builder();
//
//
////        GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest = spotifyApi
////                .getListOfUsersPlaylists(userId)
////                .build();
//
//        when(spotifyApi.getListOfUsersPlaylists(userId)).thenReturn(any());
//        //when(getListOfUsersPlaylistsRequest.build()).thenReturn(getListOfUsersPlaylistsRequest);
//        when(getListOfUsersPlaylistsRequest.execute()).thenReturn(playlistPaging);
//        when(playlistPaging.getItems()).thenReturn(mockPlaylists);
//
//        // When
//        PlaylistSimplified[] result = userDataService.getUserAllPlaylists(userId);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(2, result.length); // Ensure correct length of playlists
//        verify(authService).initializeSpotifyApi();
//        verify(spotifyApi).getListOfUsersPlaylists(userId);
//        verify(getListOfUsersPlaylistsRequest).execute();
//    }
//
//    @Test
//    public void testGetUserAllPlaylistsFailure() throws Exception {
//        // Given
//        String userId = "testUserId";
//
//        // Set up mocks to throw an exception
//        when(authService.initializeSpotifyApi()).thenReturn(spotifyApi);
//        when(spotifyApi.getListOfUsersPlaylists(userId)).thenReturn(getListOfUsersPlaylistsRequest);
//        when(getListOfUsersPlaylistsRequest.build()).thenReturn(getListOfUsersPlaylistsRequest);
//        when(getListOfUsersPlaylistsRequest.execute()).thenThrow(new RuntimeException("Spotify API error"));
//
//        // When
//        PlaylistSimplified[] result = userDataService.getUserAllPlaylists(userId);
//
//        // Then
//        assertNull(result); // Result should be null when an exception occurs
//        verify(authService).initializeSpotifyApi();
//        verify(spotifyApi).getListOfUsersPlaylists(userId);
//        verify(getListOfUsersPlaylistsRequest).execute();
//    }
//}