package com.yen.SpotifyPlayList;

import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;

import java.io.IOException;
import java.text.ParseException;

public class GetListOfUsersPlaylistsTest {

    // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/data/playlists/GetListOfUsersPlaylistsExample.java
    @Test
    public void getUserPlayList(){

        // replace below with yours
        final String accessToken = "BQBwj40F-tTXYd5EgeI7wB2nawCRGIci_SlLiam-Z5FTwOImU2-kIS6hwMs_L2pwpqaoHUbuKPHvq85ZxXIDJOYIblidKo9VnMlr7WpP7YqJqLjLY48";
        final String userId = "62kytpy7jswykfjtnjn9zv3ou";

       final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        final GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest = spotifyApi
                .getListOfUsersPlaylists(userId)
//          .limit(10)
//          .offset(0)
                .build();

        try {
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfUsersPlaylistsRequest.execute();

            PlaylistSimplified[] items = playlistSimplifiedPaging.getItems();
            for (PlaylistSimplified item : items){
                System.out.println("playList =  " + item.getName() + " id = " + item.getId());
            }


            System.out.println("Total: " + playlistSimplifiedPaging.getTotal());
            System.out.println("getUserPlayList OK");
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("getUserPlayList Error: " + e.getMessage());
        }

    }

}
