package com.yen.SpotifyPlayList.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TrackService {

    @Autowired
    private AuthService authService;

    public Track getTrackInfo(String trackId) {

        SpotifyApi spotifyApi = authService.initializeSpotifyApi(); // Get the Spotify API instance
        Track track = null;

        try {
            final GetTrackRequest getTrackRequest = spotifyApi.getTrack(trackId)
                    .build();
            track = getTrackRequest.execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Error occurred during artist search: {}", e.getMessage());
        }
        return track;
    }

}
