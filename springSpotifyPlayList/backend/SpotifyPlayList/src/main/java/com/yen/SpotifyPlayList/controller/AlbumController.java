package com.yen.SpotifyPlayList.controller;

import com.yen.SpotifyPlayList.service.AlbumService;
import com.yen.SpotifyPlayList.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

//    @Autowired
//    private AuthService authService;

    @GetMapping("/{albumId}")
    public ResponseEntity getAlbumWithId(@PathVariable("albumId") String albumId){

        try{
            Album album = albumService.getAlbum(albumId);
            return ResponseEntity.status(HttpStatus.OK).body(album);
        }catch (Exception e){
            log.error("getAlbumWithId error : " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/track/{albumId}")
    public ResponseEntity getAlbumTrackWithId(@PathVariable("albumId") String albumId){
        try{

            Paging<TrackSimplified> trackSimplifiedPaging = albumService.getAlbumTrack(albumId);
            return ResponseEntity.status(HttpStatus.OK).body(trackSimplifiedPaging);
        }catch (Exception e){
            log.error("getAlbumTrackWithId error : " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
