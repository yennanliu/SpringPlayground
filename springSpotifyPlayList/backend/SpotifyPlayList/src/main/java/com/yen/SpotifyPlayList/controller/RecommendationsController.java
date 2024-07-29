package com.yen.SpotifyPlayList.controller;

import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import com.yen.SpotifyPlayList.service.RecommendationsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.Recommendations;

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
            Recommendations recommendations = recommendationsService.getRecommendation(getRecommendationsDto);
            return ResponseEntity.status(HttpStatus.OK).body(recommendations);
        } catch (Exception e) {
            log.error("getRecommendation error : " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
