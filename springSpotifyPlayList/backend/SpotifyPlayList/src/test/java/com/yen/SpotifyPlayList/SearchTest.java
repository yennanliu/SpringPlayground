package com.yen.SpotifyPlayList;

import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchAlbumsRequest;

// https://spotify-web-api-java.github.io/spotify-web-api-java/se/michaelthelin/spotify/requests/data/search/package-summary.html

public class SearchTest {

    private static final String accessToken = "BQC1tc7OGNljPoVLCWvR_QJXk6DRGza3okmZGD4ACSKCaY98ile8D7kY9u-NOSuyUSexCJxhmYTgyAphzaPGtz1-nz3qeWz-BxidPf9coyD8B7C31Hg";

    private static final String q = "mojito";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken(accessToken)
            .build();

    // search album
    // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/data/search/simplified/SearchAlbumsExample.java
    @Test
    public void test_search_album(){

        final SearchAlbumsRequest searchAlbumsRequest = spotifyApi.searchAlbums(q)
                .build();

        try {
            final Paging<AlbumSimplified> albumSimplifiedPaging = searchAlbumsRequest.execute();
            System.out.println("Total: " + albumSimplifiedPaging.getTotal());

            AlbumSimplified[] x = albumSimplifiedPaging.getItems();
            for (AlbumSimplified item : albumSimplifiedPaging.getItems()){
                System.out.println("name = " + item.getName() + ", artist" + item.getArtists().toString() + ", id = " + item.getId() + ", url = " + item.getExternalUrls());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

}
