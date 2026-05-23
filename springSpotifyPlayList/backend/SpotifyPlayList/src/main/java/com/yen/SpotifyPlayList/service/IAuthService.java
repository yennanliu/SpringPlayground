package com.yen.SpotifyPlayList.service;

import se.michaelthelin.spotify.SpotifyApi;

public interface IAuthService {
    String getAccessToken();
    void setAccessToken(String accessToken);
    SpotifyApi initializeSpotifyApi();
    String getToken();
} 