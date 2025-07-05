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
            
            // If no DTO provided, use default values
            if (getRecommendationsDto == null) {
                getRecommendationsDto = new GetRecommendationsDto();
                getRecommendationsDto.setSeedArtistId("4NHQUGzhtTLFvgF5SZesLK");  // Tame Impala as default
                getRecommendationsDto.setAmount(10);
            }
            
            Recommendations recommendations = recommendationsService.getRecommendation(getRecommendationsDto);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("Error getting recommendations: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}