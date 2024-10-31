package com.yen.SpotifyPlayList.controller;

import com.yen.SpotifyPlayList.service.UserDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

@Slf4j
@RestController
@RequestMapping("/user_data")
public class UserDataController {

  @Value("${spotify.userId}")
  private String userId;

  @Autowired private UserDataService userDataService;

  @GetMapping("/playlist")
  public PlaylistSimplified[] getUserPlayList() {
    // String userId = "62kytpy7jswykfjtnjn9zv3ou";
    return userDataService.getUserAllPlaylists(userId);
  }
}
