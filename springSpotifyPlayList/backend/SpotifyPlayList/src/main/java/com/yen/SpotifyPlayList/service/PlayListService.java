package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.dto.CreatePlayListDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;

import java.io.IOException;

@Service
@Slf4j
public class PlayListService {

    @Autowired
    private AuthService authService;

    private SpotifyApi spotifyApi;

    public PlayListService(){

    }

    public Playlist getPlaylistById(String playlistId) throws SpotifyWebApiException {

        Playlist playlist = null;
        try {
            // TODO : move below to controller / config
            this.spotifyApi = authService.getSpotifyApi();
            final GetPlaylistRequest getPlaylistRequest = spotifyApi
                    .getPlaylist(playlistId)
                    .build();
            playlist = getPlaylistRequest.execute();
            log.info("playlist = " + playlist);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new SpotifyWebApiException("getPlaylistById error: " + e.getMessage());
        }
        return playlist;
    }

    public Playlist createPlayList(CreatePlayListDto createPlayListDto) throws SpotifyWebApiException {

        Playlist playlist = null;
        try {
            log.info("(PlayListService) createPlayList start ... ");
            // TODO : move below to controller / config
            this.spotifyApi = authService.getSpotifyApi();
            log.info("(PlayListService) redirect URI " + spotifyApi.getRedirectURI());

            // ------------
            // TODO : get Auth code from Spotify Auth redirect resp
            //String code = "AQC9S5RJDdy5DnkQEeCe10TXxQokM6pHLbEVMXihbXobGyLLAR54tj_ppouURX4L6647uP7u_yR-g9LdtRD_07JDs1gbKpvwy-qKOKUymsZ55cI2JCJVRzMhFEs6OYzMf9t0uD6tMjXMCKL4f70Dh0jbvK63ZJByLber4VDzQX6j9-66BS8LTFLrxKO_-1-7guyyM2De9tjDE4KmIORadw";
            final AuthorizationCodeRequest authorizationCodeRequest = this.spotifyApi
                    .authorizationCode(createPlayListDto.getAuthCode())
                    .build();

            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest
                    .execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            // ------------

            // TODO : get userId from auth ?
            final CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(createPlayListDto.getUserId(), createPlayListDto.getName())
//          .collaborative(false)
//          .public_(false)
//          .description("Amazing music.")
                    .build();

            playlist = createPlaylistRequest.execute();
            log.info("playlist is created !  " + playlist + " name = " + playlist.getName());

            log.info("(PlayListService) createPlayList end ... ");
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new SpotifyWebApiException("createPlayList error: " + e.getMessage());
        }
        return playlist;
    }

}
