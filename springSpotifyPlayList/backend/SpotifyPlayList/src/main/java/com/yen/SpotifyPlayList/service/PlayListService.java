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
            // TODO : move below to controller / config
            this.spotifyApi = authService.getSpotifyApi();

            // ------------
            // TODO : get Auth code from Spotify Auth redirect resp
            String code = "AQA1dvPZRXinCRzrG3Sec9dz8rkpkPY95NB0gc7ICSGQD-X27IhM-y6hmdWqRwsQ4_nbqWziJRX-eMys66LMcoq2UdQZHjAjgRXZmYpvL0PW9KhKkPY4eQaB9-Iu7UA1NoE8Q1Ik6kX3EZtSRrVF_3Fn-Hl5azrBsk2wt4KXkOcndRKbikZ97YZSzOFE03LGp4Q3n6geLGkv60cAp9HRkw";
            final AuthorizationCodeRequest authorizationCodeRequest = this.spotifyApi
                    .authorizationCode(code)
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
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new SpotifyWebApiException("createPlayList error: " + e.getMessage());
        }
        return playlist;
    }

}
