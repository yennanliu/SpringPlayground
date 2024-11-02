package com.yen.SpotifyPlayList.dev;

// https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/data/users_profile/GetUsersProfileExample.java

import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.users_profile.GetUsersProfileRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class GetUsersProfileExampleTest {


  private static final String accessToken =
          "BQAyYa5EulnKhoyw-MO49fHyhGp5AFb3dQBYtMIj_rcH8CKiQabPEjpHox0AlrXxhUJiQDlVp0DZEewMQ9_0AiPcUBp_JhiSxCJA4nwsXSpgHSh4Cfg";


  private static final String userId = "62kytpy7jswykfjtnjn9zv3ou";

  @Test
  public void getUserProfileTest_1() {

    final SpotifyApi spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();
    final GetUsersProfileRequest getUsersProfileRequest =
        spotifyApi.getUsersProfile(userId).build();

    try {
      final User user = getUsersProfileRequest.execute();

      System.out.println("=====================");
      System.out.println("Display name: " + user.getDisplayName());
      System.out.println("Display id: " + user.getId());
      System.out.println("Display external url: " + user.getExternalUrls());
      System.out.println("Display external country: " + user.getCountry());


    } catch (IOException | SpotifyWebApiException | ParseException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

}
