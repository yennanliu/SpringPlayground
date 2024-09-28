package com.yen.SpotifyPlayList.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;

@Service
@Slf4j
public class UserDataService {

    @Autowired
    private AuthService authService;

    private SpotifyApi spotifyApi;

    public PlaylistSimplified[] getUserAllPlayList(String userId) {

        PlaylistSimplified[] playlistSimplifieds = null;
        this.spotifyApi = authService.getSpotifyClient();
        final GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest = spotifyApi
                .getListOfUsersPlaylists(userId)
                //.limit(10).offset(0)
                .build();
        try {
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfUsersPlaylistsRequest.execute();
            playlistSimplifieds = playlistSimplifiedPaging.getItems();
//            for (PlaylistSimplified x : playlistSimplifieds) {
//                //System.out.println(">>> name = " + x.getName() + ", id = " + x.getId());
//            }
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            return playlistSimplifieds;
        }
        return playlistSimplifieds;
    }

}
