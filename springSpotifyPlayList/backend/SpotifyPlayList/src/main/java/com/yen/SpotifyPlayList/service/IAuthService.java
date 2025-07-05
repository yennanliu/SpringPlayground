package com.yen.SpotifyPlayList.service;

public interface IAuthService {
    String getAccessToken();
    void setAccessToken(String accessToken);
} 