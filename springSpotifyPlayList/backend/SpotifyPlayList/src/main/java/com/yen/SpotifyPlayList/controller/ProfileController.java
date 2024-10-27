package com.yen.SpotifyPlayList.controller;

import com.yen.SpotifyPlayList.model.dto.Response.UserProfileResp;
import com.yen.SpotifyPlayList.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/")
    public ResponseEntity getCurrentUserId(@PathVariable String authCode) {
        UserProfileResp profile = null;
        try {
            profile = profileService.getUserProfile();
            return ResponseEntity.status(HttpStatus.OK).body(profile);
        } catch (Exception e) {
            log.error("getCurrentUserId error : " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
