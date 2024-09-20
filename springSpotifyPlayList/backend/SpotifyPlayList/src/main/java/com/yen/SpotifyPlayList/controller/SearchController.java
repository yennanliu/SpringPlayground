package com.yen.SpotifyPlayList.controller;

import com.yen.SpotifyPlayList.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;

@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/album")
    public AlbumSimplified[] searchAlbum(String keyword){
        return searchService.searchAlbum(keyword);
    }

    @GetMapping("/artist")
    public Artist[] searchArtist(String keyword){
        return searchService.searchArtist(keyword);
    }

}
