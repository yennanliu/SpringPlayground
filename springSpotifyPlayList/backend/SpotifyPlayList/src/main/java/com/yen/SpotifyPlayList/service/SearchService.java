package com.yen.SpotifyPlayList.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchAlbumsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;

@Service
@Slf4j
public class SearchService {

    @Autowired
    private AuthService authService;

    /**
     * Searches for albums using a keyword.
     *
     * @param keyword The search keyword.
     * @return An array of simplified album objects.
     */
    public AlbumSimplified[] searchAlbum(String keyword) {

        SpotifyApi spotifyApi = authService.initializeSpotifyApi();  // Get the Spotify API instance

        AlbumSimplified[] result = null;

        try {
            SearchAlbumsRequest searchAlbumsRequest = spotifyApi.searchAlbums(keyword).build();
            Paging<AlbumSimplified> albumPaging = searchAlbumsRequest.execute();
            result = albumPaging.getItems();
            log.info("Album search count: {}", result.length);
        } catch (Exception e) {
            log.error("Error occurred during album search: {}", e.getMessage());
        }

        return result;
    }

    /**
     * Searches for artists using a keyword.
     *
     * @param keyword The search keyword.
     * @return An array of artist objects.
     */
    public Artist[] searchArtist(String keyword) {

        SpotifyApi spotifyApi = authService.initializeSpotifyApi();  // Get the Spotify API instance

        Artist[] result = null;

        try {
            SearchArtistsRequest searchArtistsRequest = spotifyApi.searchArtists(keyword).build();
            Paging<Artist> artistPaging = searchArtistsRequest.execute();
            result = artistPaging.getItems();
            log.info("Artist search count: {}", result.length);
        } catch (Exception e) {
            log.error("Error occurred during artist search: {}", e.getMessage());
        }

        return result;
    }

}