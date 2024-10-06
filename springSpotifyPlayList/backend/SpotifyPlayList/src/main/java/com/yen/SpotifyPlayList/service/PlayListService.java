package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.dto.AddSongToPlayListDto;
import com.yen.SpotifyPlayList.model.dto.CreatePlayListDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioFeaturesForTrackRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class PlayListService {

    @Autowired
    private AuthService authService;

    @Autowired
    private ProfileService profileService;

    /**
     * Fetches a playlist by its ID.
     *
     * @param playlistId The Spotify playlist ID.
     * @return Playlist object
     * @throws SpotifyWebApiException In case of errors while fetching the playlist.
     */
    public Playlist getPlaylistById(String playlistId) throws SpotifyWebApiException {

        Playlist playlist = null;

        try {
            // Get SpotifyApi instance from AuthService
            SpotifyApi spotifyApi = authService.initializeSpotifyApi();

            // Build and execute request to get the playlist
            GetPlaylistRequest getPlaylistRequest = spotifyApi
                    .getPlaylist(playlistId)
                    .build();
            playlist = getPlaylistRequest.execute();

            log.info("Retrieved playlist: {}", playlist);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new SpotifyWebApiException("Error retrieving playlist: " + e.getMessage(), e);
        }
        return playlist;
    }

    /**
     * Creates a new playlist for the authenticated user.
     *
     * @param createPlayListDto Data Transfer Object (DTO) containing playlist details.
     * @return Playlist object
     * @throws SpotifyWebApiException In case of errors while creating the playlist.
     */
    public Playlist createPlayList(CreatePlayListDto createPlayListDto) throws SpotifyWebApiException {

        Playlist playlist = null;

        try {
            // Initialize SpotifyApi with the provided auth code
            SpotifyApi spotifyApi = authService.authClientWithAuthCode(
                    authService.getSpotifyClient(),
                    createPlayListDto.getAuthCode()
            );

            // Fetch current user ID
            String userId = profileService.getCurrentUserId(spotifyApi);

            /**
             * NOTE !!! via refresh token,
             *   we CAN REUSE same Auth code make multiple requests
             *   that need "user redirect auth"
             *
             *   Flow:
             *    step 1) auth with redirect (with defined scope)
             *    step 2) make a first request (e.g. get current user ID)
             *    step 3) update client with refresh token
             *    step 4) make the other request (e.g. create playlist)
             *
             *    Code ref :
             *     - https://github.com/spotify-web-api-java/spotify-web-api-java/blob/cfd0dae1262bd7f95f90c37b28d27b9c944d471a/examples/authorization/authorization_code/AuthorizationCodeRefreshExample.java#L22
             */
            // Refresh token to handle multiple requests
//            String refreshToken = spotifyApi.getRefreshToken();
//            spotifyApi.setRefreshToken(refreshToken);
            authService.refreshClient(spotifyApi);

            // Build and execute request to create playlist
            CreatePlaylistRequest createPlaylistRequest = spotifyApi
                    .createPlaylist(userId, createPlayListDto.getName())
                    .build();

            playlist = createPlaylistRequest.execute();
            log.info("Playlist created: {} (Name: {})", playlist.getId(), playlist.getName());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new SpotifyWebApiException("Error creating playlist: " + e.getMessage(), e);
        }
        return playlist;
    }

    public List<AudioFeatures> getSongFeatureByPlayList(String playListId) {

        List<AudioFeatures> audioFeaturesList = new ArrayList<>();

        try {
            SpotifyApi spotifyApi = authService.authClientWithAuthCode(
                    authService.getSpotifyClient(),
                    authService.getAuthCode()
            );
            final GetPlaylistsItemsRequest getPlaylistsItemsRequest = spotifyApi
                    .getPlaylistsItems(playListId)
                    .build();
            final CompletableFuture<Paging<PlaylistTrack>> pagingFuture = getPlaylistsItemsRequest.executeAsync();
            final Paging<PlaylistTrack> playlistTrackPaging = pagingFuture.join();
            for (PlaylistTrack track : playlistTrackPaging.getItems()) {
                final GetAudioFeaturesForTrackRequest getAudioFeaturesForTrackRequest = spotifyApi
                        .getAudioFeaturesForTrack(track.getTrack().getId())
                        .build();
                audioFeaturesList.add(getAudioFeaturesForTrackRequest.execute());
                //System.out.println(">>> name = " + track.getTrack().getName() + ", id = " + songId + ", audioFeatures = " + audioFeatures);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return audioFeaturesList;
    }

    /**
     * Adds a song to a playlist.
     *
     * @param addSongToPlayListDto Data Transfer Object (DTO) containing song and playlist details.
     * @return SnapshotResult object showing the status of the update.
     */
    public SnapshotResult addSongToPlayList(AddSongToPlayListDto addSongToPlayListDto) {

        SnapshotResult snapshotResult = null;
        log.info("(addSongToPlayList) DTO: {}", addSongToPlayListDto);

        try {
            // Authenticate and initialize SpotifyApi with the provided auth code
            SpotifyApi spotifyApi = authService.authClientWithAuthCode(
                    authService.getSpotifyClient(),
                    authService.getAuthCode()
            );

            // Refresh token to handle multiple requests
            String refreshToken = spotifyApi.getRefreshToken();
            spotifyApi.setRefreshToken(refreshToken);

            // Build and execute request to add songs to the playlist
            AddItemsToPlaylistRequest addItemsToPlaylistRequest = spotifyApi
                    .addItemsToPlaylist(addSongToPlayListDto.getPlaylistId(), addSongToPlayListDto.getSongUris().split(","))
                    .build();

            snapshotResult = addItemsToPlaylistRequest.execute();
            log.info("Snapshot ID: {}", snapshotResult.getSnapshotId());
        } catch (Exception e) {
            log.error("Error adding songs to playlist: {}", e.getMessage(), e);
        }
        return snapshotResult;
    }

}