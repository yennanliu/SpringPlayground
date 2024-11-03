package com.yen.SpotifyPlayList.dev;

// https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/data/tracks/GetTrackExample.java

import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.io.IOException;

public class GetTrackExampleTest {

    @Test
    public void test_1() {

        final String accessToken = "BQDWDKSDXI_91il6M_GRMxBzQlipbx4xjVd1kze_7jQFpUHJrK66iF7sA5wSSK7mriP2JkZnrK7iMvKZPfpLxoBM_kQxnDp29o5qj6NcfNnjepLEvKM";
        final String id = "01iyCAUm8EvOFqVWYJ3dVX";

        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
        final GetTrackRequest getTrackRequest = spotifyApi.getTrack(id)
//          .market(CountryCode.SE)
                .build();
        try {
            final Track track = getTrackRequest.execute();

            System.out.println(">>> Name: " + track.getName());
            System.out.println(">>> artists: " + track.getArtists());
            System.out.println(">>> artist id: " + track.getArtists()[0].getId());
            System.out.println(">>> all: " + track.toString());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
