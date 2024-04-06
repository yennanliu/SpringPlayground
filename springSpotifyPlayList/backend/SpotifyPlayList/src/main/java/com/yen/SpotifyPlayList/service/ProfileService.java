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

    @Autowired
    private AuthService authService;

    private SpotifyApi spotifyApi;

    public ProfileService(){

    }

    public String getCurrentUserId(){
        String userId = null;
        try{
            this.spotifyApi = authService.getSpotifyApi();
            GetCurrentUsersProfileRequest profile = spotifyApi.getCurrentUsersProfile().build();
            String profileString = profile.getJson();
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            UserProfileResp userProfileResp = gson.fromJson(profileString, UserProfileResp.class);
            log.info("userProfileResp = " + userProfileResp);
            log.info("userProfileResp ID = " + userProfileResp.getId());

        }catch (Exception e){
            log.error("getCurrentUserId error,  " + e);
        }

        return userId;
    }

}
