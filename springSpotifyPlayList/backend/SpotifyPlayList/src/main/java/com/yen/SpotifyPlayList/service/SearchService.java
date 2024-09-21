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

    private SpotifyApi spotifyApi; // = authService.getSpotifyClient(); // TODO : fix this

    public SearchService() {
        // TODO : fix below
        //this.spotifyApi = authService.getSpotifyClient();
    }

    public AlbumSimplified[] searchAlbum(String keyword) {

        this.spotifyApi = authService.getSpotifyClient();

        AlbumSimplified[] res = null;

        SearchAlbumsRequest searchAlbumsRequest = this.spotifyApi.searchAlbums(keyword)
                .build();
        try {
            final Paging<AlbumSimplified> albumSimplifiedPaging = searchAlbumsRequest.execute();
            System.out.println("Total: " + albumSimplifiedPaging.getTotal());
            res = albumSimplifiedPaging.getItems();
            for (AlbumSimplified item : res) {
                System.out.println("name = " + item.getName() + ", artist" + item.getArtists().toString() + ", id = " + item.getId() + ", url = " + item.getExternalUrls());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return res;
    }

    public Artist[] searchArtist(String keyword){

        this.spotifyApi = authService.getSpotifyClient();

        Artist[] res = null;

        final SearchArtistsRequest searchArtistsRequest = this.spotifyApi.searchArtists(keyword)
                .build();
        try {
            final Paging<Artist> artistPaging = searchArtistsRequest.execute();
            System.out.println("Total: " + artistPaging.getTotal());
            res = artistPaging.getItems();
            for (Artist artist : res){
                System.out.println("name = " + artist.getName() + ", id = " + artist.getId() + ", url = " + artist.getExternalUrls());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return res;
    }


}
