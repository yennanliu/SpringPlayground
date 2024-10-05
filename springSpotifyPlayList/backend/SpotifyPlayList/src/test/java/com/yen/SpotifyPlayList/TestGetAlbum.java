package com.yen.SpotifyPlayList;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumsTracksRequest;

public class TestGetAlbum {

    // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/data/albums/GetAlbumExample.java
    @Test
    public void testGetAlbum_1(){

        final String albumId = "5zT1JLIj9E57p3e1rFm9Uq";

        // get token from testAuthGetToken_1
        String accessToken = "BQDIiCYIHV5g1LI98m4HwuMRZJleQZxVvJ8yzvtk8qMcwjHMGQOXrd7Lq-oMwHwQcJ3s6QOfNlOiZ8OerLUSjr69qVNkSnXE7egzmpbN-YNbrhxym6o";

        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
        final GetAlbumRequest getAlbumRequest = spotifyApi.getAlbum(albumId)
//          .market(CountryCode.SE)
                .build();

        try {
            final Album album = getAlbumRequest.execute();
            System.out.println(">>> album Name: " + album.getName());
            System.out.println(">>> Name uri: " + album.getUri());
            System.out.println(">>> Name detail: " + album);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/data/albums/GetAlbumsTracksExample.java
    @Test
    public void testGetAlbumTrack(){

        final String albumId = "5zT1JLIj9E57p3e1rFm9Uq";

        // get token from testAuthGetToken_1
        String accessToken = "BQCVX_9iFkOgTNsyrl2l3Xw7BczyFbmugxUUC5Lm0nhTyw02D9RNKzNixqbiEv7o8YK8N7VYH3MyUutbT_uHY6aFb5cAnsolKzOtjP2hgGbjEaMGGPE";

        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        final GetAlbumsTracksRequest getAlbumsTracksRequest = spotifyApi.getAlbumsTracks(albumId)
//          .limit(10)
//          .offset(0)
//          .market(CountryCode.SE)
                .build();

        try {
            final Paging<TrackSimplified> trackSimplifiedPaging = getAlbumsTracksRequest.execute();
            System.out.println("Total: " + trackSimplifiedPaging.getTotal());
            TrackSimplified[] items = trackSimplifiedPaging.getItems();
            for (TrackSimplified item: items){
                System.out.println("item = " + item);
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
