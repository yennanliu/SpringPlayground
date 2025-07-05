package com.yen.SpotifyPlayList.controller;

import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import com.yen.SpotifyPlayList.service.RecommendationsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.specification.Recommendations;

@Slf4j
@RestController
@RequestMapping("/recommend")
@CrossOrigin(origins = "*")
public class RecommendationsController {

    @Autowired
    private RecommendationsService recommendationsService;

    @PostMapping
    public ResponseEntity<Recommendations> getRecommendation(@RequestBody(required = false) GetRecommendationsDto getRecommendationsDto) {
        try {
            log.info("Getting recommendations with DTO: {}", getRecommendationsDto);
            
            // If no DTO provided or no seeds set, use default values
            if (getRecommendationsDto == null || 
                (getRecommendationsDto.getSeedArtistId() == null && 
                 getRecommendationsDto.getSeedTrack() == null && 
                 getRecommendationsDto.getSeedGenres() == null)) {
                
                getRecommendationsDto = new GetRecommendationsDto();
                getRecommendationsDto.setAmount(10);
                // Using Tame Impala as default seed artist
                getRecommendationsDto.setSeedArtistId("4NHQUGzhtTLFvgF5SZesLK");
                // Adding a default genre as well for better recommendations
                getRecommendationsDto.setSeedGenres("alternative,indie");
            }
            
            Recommendations recommendations = recommendationsService.getRecommendation(getRecommendationsDto);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("Error getting recommendations: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/playlist/{playListId}")
    public ResponseEntity<Recommendations> getRecommendationWithPlayList(@PathVariable("playListId") String playListId) {
        try {
            log.info("Getting recommendations for playlist: {}", playListId);
            Recommendations recommendations = recommendationsService.getRecommendationWithPlayList(playListId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("Error getting recommendations for playlist: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}