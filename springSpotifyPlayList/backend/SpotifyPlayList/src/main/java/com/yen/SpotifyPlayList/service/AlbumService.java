package com.yen.SpotifyPlayList.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumsTracksRequest;

import java.io.IOException;

@Service
@Slf4j
public class AlbumService {

    @Autowired
    private AuthService authService;

    private SpotifyApi spotifyApi;

    public AlbumService() {
    }

    public Album getAlbum(String albumId) throws SpotifyWebApiException {

        Album album = null;
        try {
            // TODO : move below to controller / config
            this.spotifyApi = authService.getSpotifyClient();
            final GetAlbumRequest getAlbumRequest = this.spotifyApi
                    .getAlbum(albumId)
                    .build();
            album = getAlbumRequest.execute();
            log.info("album = " + album);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new SpotifyWebApiException("getAlbum error: " + e.getMessage());
        }
        return album;
    }

    public Paging<TrackSimplified> getAlbumTrack(String albumId) throws SpotifyWebApiException {

        Paging<TrackSimplified> trackSimplifiedPaging = null;

        try {
            // TODO : move below to controller / config
            this.spotifyApi = authService.getSpotifyClient();
            final GetAlbumsTracksRequest getAlbumsTracksRequest = spotifyApi
                    .getAlbumsTracks(albumId)
                    .build();
            trackSimplifiedPaging = getAlbumsTracksRequest.execute();
            log.info("Track count: " + trackSimplifiedPaging.getTotal());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new SpotifyWebApiException("getAlbumTrack error: " + e.getMessage());
        }
        return trackSimplifiedPaging;
    }

}
