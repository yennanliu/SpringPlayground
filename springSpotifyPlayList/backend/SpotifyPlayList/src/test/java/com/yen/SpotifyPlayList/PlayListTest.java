package com.yen.SpotifyPlayList;

import org.junit.jupiter.api.Test;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class PlayListTest {

    // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/data/playlists/AddItemsToPlaylistExample.java
    @Test
    public void testAddItemToPlayList() {

        final String accessToken = "BQBZi1FrY15l2dgIzlFw1EiLEIka9wHwG0vWFHCrULeeOZujlk982wwW0-DOxyu9x7BBsgKH6Vtaklut095LxOW3DanaY-CvCwEGXw94V1ayHJum-tU";
        // playList ID can be received from create playList resp
        final String playlistId = "3eJtMC1cB1qStcsN5GXFSK";
        final String[] uris = new String[]{"spotify:track:01iyCAUm8EvOFqVWYJ3dVX", "spotify:episode:4GI3dxEafwap1sFiTGPKd1"};

        try {

            // TODO : check whether need to get "accessToken" after redirect auth
            final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setAccessToken(accessToken)
                    .build();

            final AddItemsToPlaylistRequest addItemsToPlaylistRequest = spotifyApi
                    .addItemsToPlaylist(playlistId, uris)
//          .position(0)
                    .build();

            final SnapshotResult snapshotResult = addItemsToPlaylistRequest.execute();
            System.out.println("Snapshot ID: " + snapshotResult.getSnapshotId());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
