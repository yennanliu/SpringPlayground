package com.yen.SpotifyPlayList.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;

import java.io.IOException;

@Service
@Slf4j
public class PersonalizationService {

    @Autowired
    private AuthService authService;

    private SpotifyApi spotifyApi;

    public PersonalizationService() {

    }

    public Paging<Track> getUserTopTracks() throws SpotifyWebApiException {

        Paging<Track> trackPaging = null;
        try {
            // TODO : move below to controller / config
            this.spotifyApi = authService.getSpotifyClient();
            final GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi
                    .getUsersTopTracks()
                    .build();
            trackPaging = getUsersTopTracksRequest.execute();
            log.info("trackPaging = " + trackPaging);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new SpotifyWebApiException("getUserTopTracks error: " + e.getMessage());
        }
        return trackPaging;
    }

}
