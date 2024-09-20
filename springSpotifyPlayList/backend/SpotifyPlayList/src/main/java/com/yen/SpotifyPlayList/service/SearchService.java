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

    private SpotifyApi spotifyApi;

    public SearchService() {

    }

    public AlbumSimplified[] searchAlbum(String keyword) {

        AlbumSimplified[] res = null;

        SearchAlbumsRequest searchAlbumsRequest = spotifyApi.searchAlbums(keyword)
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

        Artist[] res = null;

        final SearchArtistsRequest searchArtistsRequest = spotifyApi.searchArtists(keyword)
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
