package com.yen.SpotifyPlayList.controller;

import com.yen.SpotifyPlayList.service.AlbumService;
import com.yen.SpotifyPlayList.service.AuthService;
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

@RestController
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

//    @Autowired
//    private AuthService authService;

    @GetMapping("/{albumId}")
    public ResponseEntity<Album> getAlbumWithId(@PathVariable("albumId") String albumId){

        Album album = albumService.getAlbum(albumId);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @GetMapping("/track/{albumId}")
    public ResponseEntity<Paging<TrackSimplified>> getAlbumTrackWithId(@PathVariable("albumId") String albumId){

        Paging<TrackSimplified> trackSimplifiedPaging = albumService.getAlbumTrack(albumId);
        return new ResponseEntity<>(trackSimplifiedPaging, HttpStatus.OK);
    }


}
