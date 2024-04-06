package com.yen.SpotifyPlayList.controller;

import com.yen.SpotifyPlayList.model.dto.AddSongToPlayListDto;
import com.yen.SpotifyPlayList.model.dto.CreatePlayListDto;
import com.yen.SpotifyPlayList.service.PlayListService;

import com.yen.SpotifyPlayList.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Playlist;

@Slf4j
@RestController
@RequestMapping("/playlist")
public class PlayListController {

    @Autowired
    private PlayListService playListService;

    @Autowired
    private ProfileService profileService;

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
    public ResponseEntity CreatePlayList(@RequestBody CreatePlayListDto createPlayListDto){

        try{
            log.info("received createPlayListDto = " + createPlayListDto);
            Playlist playlist = playListService.createPlayList(createPlayListDto);
            return ResponseEntity.status(HttpStatus.OK).body(playlist);
        }catch (Exception e){
            log.error("CreatePlayList error : " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/addSong")
    public ResponseEntity addSongToList(@RequestBody AddSongToPlayListDto addSongToPlayListDto){

        try{
            log.info("received addSongToPlayListDto = " + addSongToPlayListDto);
            addSongToPlayListDto.setPlaylistId("7E6QGOTX5fQ2kzfDYP2FzS");
            log.info("updated addSongToPlayListDto = " + addSongToPlayListDto);
            SnapshotResult snapshotResult = playListService.addSongToPlayList(addSongToPlayListDto);
            return ResponseEntity.status(HttpStatus.OK).body(snapshotResult);
        }catch (Exception e){
            log.error("addSongToList error : " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
