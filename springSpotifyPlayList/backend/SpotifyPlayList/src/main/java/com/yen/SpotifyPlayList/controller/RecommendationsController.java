package com.yen.SpotifyPlayList.controller;

import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import com.yen.SpotifyPlayList.service.CustomSpotifyRecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/recommend")
public class RecommendationsController {

    @Autowired
    private CustomSpotifyRecommendationService recommendationsService;

    @PostMapping("/")
    public ResponseEntity<?> getRecommendation(@RequestBody GetRecommendationsDto getRecommendationsDto) {
        try {
            log.info("(getRecommendation) getRecommendationsDto = " + getRecommendationsDto.toString());
            ResponseEntity<String> recommendations = recommendationsService.getRecommendations(getRecommendationsDto);
            return ResponseEntity.status(recommendations.getStatusCode()).body(recommendations.getBody());
        } catch (Exception e) {
            log.error("getRecommendation error : " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // TODO: Implement custom recommendation logic for playlist-based recommendations
    @GetMapping("/playlist/{playListId}")
    public ResponseEntity<?> getRecommendationWithPlayList(@PathVariable("playListId") String playListId) {
        try {
            log.info("(getRecommendationWithPlayList) playListId = " + playListId);
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Feature not yet implemented with custom service");
        } catch (Exception e) {
            log.error("getRecommendationWithPlayList error : " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}