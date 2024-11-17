package com.yen.SpringReddit.service;

import com.yen.SpringReddit.model.RefreshToken;

public interface RefreshTokenService {

    public RefreshToken generateRefreshToken();

    public void validateRefreshToken(String token);

    public void deleteRefreshToken(String token);
}
