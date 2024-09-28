package com.yen.SpotifyPlayList.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;

@Service
@Slf4j
public class UserDataService {

    @Autowired
    private AuthService authService;

    /**
     * Fetches all playlists for a given user.
     *
     * @param userId The Spotify user ID.
     * @return An array of simplified playlist objects.
     */
    public PlaylistSimplified[] getUserAllPlaylists(String userId) {

        PlaylistSimplified[] playlists = null;

        try {
            // Get SpotifyApi instance from AuthService
            SpotifyApi spotifyApi = authService.initializeSpotifyApi();

            // Build request to get the user's playlists
            GetListOfUsersPlaylistsRequest request = spotifyApi
                    .getListOfUsersPlaylists(userId)
                    .build();

            // Execute the request and fetch the playlist data
            Paging<PlaylistSimplified> playlistPaging = request.execute();
            playlists = playlistPaging.getItems();
            log.info("Retrieved {} playlists for user: {}", playlists.length, userId);

        } catch (Exception e) {
            log.error("Error fetching playlists for user {}: {}", userId, e.getMessage());
        }

        return playlists;
    }

}