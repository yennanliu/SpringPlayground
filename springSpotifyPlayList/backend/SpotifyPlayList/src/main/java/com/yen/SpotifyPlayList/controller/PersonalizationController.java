package com.yen.SpotifyPlayList.controller;

import com.yen.SpotifyPlayList.service.PersonalizationService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;

@Slf4j
@RestController
@RequestMapping("/personalize")
public class PersonalizationController {

    @Autowired
    private PersonalizationService personalizationService;

    @GetMapping("/")
    public ResponseEntity getTopTrack(){

        try{
            Paging<Track> trackPaging = personalizationService.getUserTopTracks();
            return ResponseEntity.status(HttpStatus.OK).body(trackPaging);
        }catch (Exception e){
            log.error("getTopTrack error : " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
