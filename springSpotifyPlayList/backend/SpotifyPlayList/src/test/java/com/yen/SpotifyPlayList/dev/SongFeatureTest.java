package com.yen.SpotifyPlayList;

import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioFeaturesForTrackRequest;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class SongFeatureTest {

    final String accessToken = "BQCVX_9iFkOgTNsyrl2l3Xw7BczyFbmugxUUC5Lm0nhTyw02D9RNKzNixqbiEv7o8YK8N7VYH3MyUutbT_uHY6aFb5cAnsolKzOtjP2hgGbjEaMGGPE";
    String trackId = "6oHUMXEKpnXOrs13VoGsuF";

    String playListId = "6fbBvdE95blBGc3ekFMzh1";

    // // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/data/tracks/GetAudioFeaturesForTrackExample.java
    @Test
    public void test_getSong_feature() {

        try {
            final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setAccessToken(accessToken)
                    .build();
            final GetAudioFeaturesForTrackRequest getAudioFeaturesForTrackRequest = spotifyApi
                    .getAudioFeaturesForTrack(trackId)
                    .build();

            final AudioFeatures audioFeatures = getAudioFeaturesForTrackRequest.execute();

            System.out.println(">>> ID: " + audioFeatures.getId());

            /**
             *  example output:
             *
             *  >>> audioFeatures: AudioFeatures(acousticness=0.443, analysisUrl=https://api.spotify.com/v1/audio-analysis/6oHUMXEKpnXOrs13VoGsuF, danceability=0.658, durationMs=225387, energy=0.86, id=6oHUMXEKpnXOrs13VoGsuF, instrumentalness=0.0, key=5, liveness=0.304, loudness=-2.898, mode=MINOR, speechiness=0.0272, tempo=88.032, timeSignature=4, trackHref=https://api.spotify.com/v1/tracks/6oHUMXEKpnXOrs13VoGsuF, type=AUDIO_FEATURES, uri=spotify:track:6oHUMXEKpnXOrs13VoGsuF, valence=0.644)
             */
            System.out.println(">>> audioFeatures: " + audioFeatures);

        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (org.apache.hc.core5.http.ParseException e) {
            throw new RuntimeException(e);
        }

    }

    // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/data/playlists/GetPlaylistsItemsExample.java

    @Test
    public void test_getSongFeatureList_from_playList(){

        try {
            final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setAccessToken(accessToken)
                    .build();

            final GetPlaylistsItemsRequest getPlaylistsItemsRequest = spotifyApi
                    .getPlaylistsItems(playListId)
//          .fields("description")
//          .limit(10)
//          .offset(0)
//          .market(CountryCode.SE)
//          .additionalTypes("track,episode")
                    .build();


            final CompletableFuture<Paging<PlaylistTrack>> pagingFuture = getPlaylistsItemsRequest.executeAsync();

            // Thread free to do other tasks...

            // Example Only. Never block in production code.
            final Paging<PlaylistTrack> playlistTrackPaging = pagingFuture.join();

            System.out.println("Total: " + playlistTrackPaging.getTotal());
            //System.out.println("Track's first artist: " + ((Track) playlistTrackPaging.getItems()[0].getTrack()).getArtists()[0]);
            //System.out.println("Episode's show: " + ((Episode) playlistTrackPaging.getItems()[0].getTrack()).getShow());


            PlaylistTrack[] playlistTracks = playlistTrackPaging.getItems();
            for (PlaylistTrack track: playlistTracks){

                String songId = track.getTrack().getId();
                //System.out.println(">>> name = " + track.getTrack().getName() + ", id = " + songId);
                // get song feature
                final GetAudioFeaturesForTrackRequest getAudioFeaturesForTrackRequest = spotifyApi
                        .getAudioFeaturesForTrack(songId)
                        .build();

                final AudioFeatures audioFeatures = getAudioFeaturesForTrackRequest.execute();

                System.out.println(">>> name = " + track.getTrack().getName() + ", id = " + songId + ", audioFeatures = " + audioFeatures);

            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

}
