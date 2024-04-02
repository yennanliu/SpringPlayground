package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.dto.AddSongToPlayListDto;
import com.yen.SpotifyPlayList.model.dto.CreatePlayListDto;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
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
            this.spotifyApi  = authService.authClientWithAuthCode(
                    authService.getSpotifyApi(),
                    createPlayListDto.getAuthCode()
            );

            // TODO : get userId from auth ?
            final CreatePlaylistRequest createPlaylistRequest = spotifyApi
                    .createPlaylist(createPlayListDto.getUserId(), createPlayListDto.getName())
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

    public SnapshotResult addSongToPlayList(AddSongToPlayListDto addSongToPlayListDto){

        SnapshotResult snapshotResult = null;

        //final String accessToken = "BQBZi1FrY15l2dgIzlFw1EiLEIka9wHwG0vWFHCrULeeOZujlk982wwW0-DOxyu9x7BBsgKH6Vtaklut095LxOW3DanaY-CvCwEGXw94V1ayHJum-tU";
        // playList ID can be received from create playList resp
        final String playlistId = "7KP94mNZB4goH7bwjplrsH";
        final String[] uris = new String[]{"spotify:track:0Sxq05leQaZXCktX05Kr7b"};

        addSongToPlayListDto.setPlaylistId(playlistId);
        addSongToPlayListDto.setSongUris(uris);
        log.info("(addSongToPlayList) addSongToPlayListDto = " + addSongToPlayListDto);

        try {
            this.spotifyApi  = authService.authClientWithAuthCode(
                    authService.getSpotifyApi(),
                    addSongToPlayListDto.getAuthCode()
            );

            final AddItemsToPlaylistRequest addItemsToPlaylistRequest = this.spotifyApi
                    .addItemsToPlaylist(addSongToPlayListDto.getPlaylistId(), addSongToPlayListDto.getSongUris())
                    //.position(0)
                    .build();

            snapshotResult = addItemsToPlaylistRequest.execute();
            log.info("Snapshot ID: " + snapshotResult.getSnapshotId());
            log.info("addSongToPlayList OK");

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("addSongToPlayList Error: " + e.getMessage());
        }
        return snapshotResult;
    }

}
