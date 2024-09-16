package com.yen.SpotifyPlayList;

// https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/data/playlists/GetListOfUsersPlaylistsExample.java
// https://spotify-web-api-java.github.io/spotify-web-api-java/se/michaelthelin/spotify/requests/data/library/package-summary.html

import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;

public class UserDataTest {

    private static final String accessToken = "BQAT63uNMBH_hvzu5yHet8QIIWxcii2h8lld7aOP6R80ZUBC8OAmnAzE60M5FNrHdrxVDOmaaeaoKpR205MMnqVP_DaSMYV8vlLC2pFUzOquBPE-vmI";
    private static final String userId = "62kytpy7jswykfjtnjn9zv3ou";

    @Test
    public void test1() {

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

            PlaylistSimplified[] playlistSimplifieds = playlistSimplifiedPaging.getItems();
            for(PlaylistSimplified x : playlistSimplifieds){
                System.out.println(">>> name = " + x.getName() + ", id = " + x.getId());
            }

//            while (playlistSimplifiedPaging.getNext() != null) {
//                String playList = playlistSimplifiedPaging.getNext();
//                System.out.println(">>> playList = " + playList);
//            }


            System.out.println("Total: " + playlistSimplifiedPaging.getTotal());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
