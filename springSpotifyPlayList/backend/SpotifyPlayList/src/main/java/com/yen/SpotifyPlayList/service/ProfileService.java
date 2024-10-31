package com.yen.SpotifyPlayList.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yen.SpotifyPlayList.model.dto.Response.UserProfileResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

@Service
@Slf4j
public class ProfileService {

  private final Gson gson;

  @Autowired private AuthService authService;

  private SpotifyApi spotifyApi;

  public ProfileService() {
    GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();
    this.gson = builder.create();
  }

  public String getCurrentUserId(SpotifyApi spotifyApi) {

    String userId = null;

    try {
      GetCurrentUsersProfileRequest profile = spotifyApi.getCurrentUsersProfile().build();
      UserProfileResp userProfileResp =
          this.gson.fromJson(profile.getJson(), UserProfileResp.class);
      log.info("userProfileResp = " + userProfileResp);
      userId = userProfileResp.getId();
    } catch (Exception e) {
      log.error("getCurrentUserId error,  " + e);
    }

    return userId;
  }

  public UserProfileResp getUserProfile() {

    UserProfileResp resp = null;
    try {
      GetCurrentUsersProfileRequest profile = spotifyApi.getCurrentUsersProfile().build();
      UserProfileResp userProfileResp =
          this.gson.fromJson(profile.getJson(), UserProfileResp.class);
      log.info("getUserProfile = " + userProfileResp);
      resp = userProfileResp;
    } catch (Exception e) {
      log.error("getUserProfile error,  " + e);
    }
    return resp;
  }
}
