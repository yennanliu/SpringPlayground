package com.yen.SpotifyPlayList.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;

import java.io.IOException;

@Service
@Slf4j
public class AlbumService {

    @Autowired
    private AuthService authService;

    private String accessToken;
    //private String accessToken = "BQDIiCYIHV5g1LI98m4HwuMRZJleQZxVvJ8yzvtk8qMcwjHMGQOXrd7Lq-oMwHwQcJ3s6QOfNlOiZ8OerLUSjr69qVNkSnXE7egzmpbN-YNbrhxym6o";

    private SpotifyApi spotifyApi;

    public AlbumService(){
    }

//    public AlbumService(){
//        //this.accessToken = accessToken;
//        this.accessToken = this.authService.getToken();
//        this.spotifyApi = new SpotifyApi.Builder()
//                .setAccessToken(this.accessToken)
//                .build();
//    }

    public Album getAlbum(String albumId){

        Album album = null;
        // TODO : move below to controller / config
        this.spotifyApi = this.getSpotifyApi();
        final GetAlbumRequest getAlbumRequest = this.spotifyApi
                .getAlbum(albumId)
                .build();
        try {
            album = getAlbumRequest.execute();
            log.info("album = " + album);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("getAlbum error: " + e.getMessage());
        }
        return album;
    }


    public SpotifyApi getSpotifyApi() {
        log.info(">>> (getSpotifyApi) this.accessToken = " + this.accessToken);
        // lazy approach
        if (this.accessToken == null) {
            this.accessToken = this.authService.getToken();
            log.info(">>> (getSpotifyApi) accessToken = " + accessToken);
            this.spotifyApi = new SpotifyApi.Builder()
                    .setAccessToken(this.accessToken)
                    .build();
        }
        return this.spotifyApi;
    }

//    public void setAuthService(AuthService authService) {
//        this.authService = authService;
//    }
//
//    public void setAccessToken(String accessToken) {
//        this.accessToken = accessToken;
//    }
//
//    public void setSpotifyApi(SpotifyApi spotifyApi) {
//        this.spotifyApi = spotifyApi;
//    }

}
