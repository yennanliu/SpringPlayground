package com.yen.SpotifyPlayList.dev;

// https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/data/users_profile/GetCurrentUsersProfileExample.java

import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class GetCurrentUsersProfileTest {

  private static final String accessToken =
      "BQAyYa5EulnKhoyw-MO49fHyhGp5AFb3dQBYtMIj_rcH8CKiQabPEjpHox0AlrXxhUJiQDlVp0DZEewMQ9_0AiPcUBp_JhiSxCJA4nwsXSpgHSh4Cfg";

  @Test
  public void getUserProfile_1() {

    final SpotifyApi spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();
    final GetCurrentUsersProfileRequest getCurrentUsersProfileRequest =
        spotifyApi.getCurrentUsersProfile().build();

    try {
      final User user = getCurrentUsersProfileRequest.execute();

      System.out.println("Display name: " + user.getDisplayName());
    } catch (IOException | SpotifyWebApiException | ParseException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }


}
