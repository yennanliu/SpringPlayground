package com.yen.SpotifyPlayList.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yen.SpotifyPlayList.model.dto.Response.UserProfileResp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileServiceTest {

    @Test
    public void testGetUserId(){

        String profileString = "{\n" +
                "  \"display_name\" : \"xxx\",\n" +
                "  \"external_urls\" : {\n" +
                "    \"spotify\" : \"https://open.spotify.com/user/some_id\"\n" +
                "  },\n" +
                "  \"href\" : \"https://api.spotify.com/v1/users/some_id\",\n" +
                "  \"id\" : \"some_id\",\n" +
                "  \"images\" : [ ],\n" +
                "  \"type\" : \"user\",\n" +
                "  \"uri\" : \"spotify:user:some_id\",\n" +
                "  \"followers\" : {\n" +
                "    \"href\" : null,\n" +
                "    \"total\" : 2\n" +
                "  }\n" +
                "}";

        System.out.println("profileString = " + profileString);

        // string -> object
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        UserProfileResp userProfileResp = gson.fromJson(profileString, UserProfileResp.class);

        System.out.println("userProfileResp = " + userProfileResp);
        System.out.println("userProfileResp ID = " + userProfileResp.getId());
        System.out.println("userProfileResp images = " + userProfileResp.getImages());

    }

}