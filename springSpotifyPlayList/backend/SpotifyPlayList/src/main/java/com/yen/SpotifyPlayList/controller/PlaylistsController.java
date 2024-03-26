package com.yen.SpotifyPlayList.controller;

import com.yen.SpotifyPlayList.service.AlbumService;
import com.yen.SpotifyPlayList.service.PlaylistsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.Playlist;

@Slf4j
@RestController
@RequestMapping("/playlist")
public class PlaylistsController {

    @Autowired
    private PlaylistsService playlistsService;

    @GetMapping("/{playListId}")
    public ResponseEntity getPlayListWithId(@PathVariable("playListId") String playListId){

        try{
            Playlist playlist = playlistsService.getPlaylistById(playListId);
            return ResponseEntity.status(HttpStatus.OK).body(playlist);
        }catch (Exception e){
            log.error("getPlayListWithId error : " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
