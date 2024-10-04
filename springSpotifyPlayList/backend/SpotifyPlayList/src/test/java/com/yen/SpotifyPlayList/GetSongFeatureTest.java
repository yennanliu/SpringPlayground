package com.yen.SpotifyPlayList;

// https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/data/tracks/GetAudioFeaturesForTrackExample.java

import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioFeaturesForTrackRequest;

import java.io.IOException;
import java.text.ParseException;

public class GetSongFeatureTest {

    final String accessToken = "BQCVX_9iFkOgTNsyrl2l3Xw7BczyFbmugxUUC5Lm0nhTyw02D9RNKzNixqbiEv7o8YK8N7VYH3MyUutbT_uHY6aFb5cAnsolKzOtjP2hgGbjEaMGGPE";
    String trackId = "6oHUMXEKpnXOrs13VoGsuF";

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
             *
             *  >>> audioFeatures: AudioFeatures(acousticness=0.443, analysisUrl=https://api.spotify.com/v1/audio-analysis/6oHUMXEKpnXOrs13VoGsuF, danceability=0.658, durationMs=225387, energy=0.86, id=6oHUMXEKpnXOrs13VoGsuF, instrumentalness=0.0, key=5, liveness=0.304, loudness=-2.898, mode=MINOR, speechiness=0.0272, tempo=88.032, timeSignature=4, trackHref=https://api.spotify.com/v1/tracks/6oHUMXEKpnXOrs13VoGsuF, type=AUDIO_FEATURES, uri=spotify:track:6oHUMXEKpnXOrs13VoGsuF, valence=0.644)
             *
             *
             */
            System.out.println(">>> audioFeatures: " + audioFeatures);

        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (org.apache.hc.core5.http.ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
