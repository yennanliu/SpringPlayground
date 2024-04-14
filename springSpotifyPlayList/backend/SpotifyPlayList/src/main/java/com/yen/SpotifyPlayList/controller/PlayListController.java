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
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

import java.util.Arrays;

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
            //addSongToPlayListDto.setPlaylistId("2cUyRMtc9AsinCLXFy0gcC");
            String[] trackList = addSongToPlayListDto.getSongUris().split(",");
            log.info("received trackList = " + trackList.toString());
            for (String x : trackList){
                System.out.println(x);
            }
            log.info("updated addSongToPlayListDto = " + addSongToPlayListDto);
            SnapshotResult snapshotResult = playListService.addSongToPlayList(addSongToPlayListDto);
            return ResponseEntity.status(HttpStatus.OK).body(snapshotResult);
        }catch (Exception e){
            log.error("addSongToList error : " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/userPlayList")
    public ResponseEntity getUserPlayList(){

        try{
            Paging<PlaylistSimplified> userPlayList = playListService.getUserPlayList();
            return ResponseEntity.status(HttpStatus.OK).body(userPlayList);
        }catch (Exception e){
            log.error("getUserPlayList error : " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
