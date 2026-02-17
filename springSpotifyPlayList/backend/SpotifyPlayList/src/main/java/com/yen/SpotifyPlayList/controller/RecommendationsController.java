package com.yen.SpotifyPlayList.controller;

import com.yen.SpotifyPlayList.exception.SpotifyApiException;
import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import com.yen.SpotifyPlayList.model.dto.Response.LegacyRecommendationsResponse;
import com.yen.SpotifyPlayList.service.RecommendationsService;
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
    private RecommendationsService recommendationsService;

    @PostMapping("/")
    public ResponseEntity getRecommendation(@RequestBody GetRecommendationsDto getRecommendationsDto) {
        try {
            log.info("(getRecommendation) getRecommendationsDto = " + getRecommendationsDto.toString());
            LegacyRecommendationsResponse recommendations = recommendationsService.getRecommendation(getRecommendationsDto);
            return ResponseEntity.status(HttpStatus.OK).body(recommendations);
        } catch (SpotifyApiException e) {
            log.error("getRecommendation Spotify API error: {}", e.getMessage());
            HttpStatus status = HttpStatus.valueOf(e.getStatusCode());
            return ResponseEntity.status(status).body(e.getMessage());
        } catch (Exception e) {
            log.error("getRecommendation unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/playlist/{playListId}")
    public ResponseEntity getRecommendationWithPlayList(@PathVariable("playListId") String playListId) {
        try {
            log.info("(getRecommendationWithPlayList) playListId = " + playListId);
            LegacyRecommendationsResponse recommendations = recommendationsService.getRecommendationWithPlayList(playListId);
            return ResponseEntity.status(HttpStatus.OK).body(recommendations);
        } catch (SpotifyApiException e) {
            log.error("getRecommendationWithPlayList Spotify API error: {}", e.getMessage());
            HttpStatus status = HttpStatus.valueOf(e.getStatusCode());
            return ResponseEntity.status(status).body(e.getMessage());
        } catch (Exception e) {
            log.error("getRecommendationWithPlayList unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}