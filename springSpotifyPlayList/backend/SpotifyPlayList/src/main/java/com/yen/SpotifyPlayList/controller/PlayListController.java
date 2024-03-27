package com.yen.SpotifyPlayList.controller;

import com.yen.SpotifyPlayList.model.dto.CreatePlayListDto;
import com.yen.SpotifyPlayList.service.PlayListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.specification.Playlist;

@Slf4j
@RestController
@RequestMapping("/playlist")
public class PlayListController {

    @Autowired
    private PlayListService playListService;

    @GetMapping("/{playListId}")
    public ResponseEntity getPlayListWithId(@PathVariable("playListId") String playListId){

        try{
            Playlist playlist = playListService.getPlaylistById(playListId);
            return ResponseEntity.status(HttpStatus.OK).body(playlist);
        }catch (Exception e){
            log.error("getPlayListWithId error : " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity CreatePlayList(CreatePlayListDto createPlayListDto){

        try{
            Playlist playlist = playListService.createPlayList(createPlayListDto);
            return ResponseEntity.status(HttpStatus.OK).body(playlist);
        }catch (Exception e){
            log.error("CreatePlayList error : " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
