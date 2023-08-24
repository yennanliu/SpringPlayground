package com.yen.SpringReddit.service.impl;

import com.yen.SpringReddit.repository.RefreshTokenRepository;
import com.yen.SpringReddit.exceptions.SpringRedditException;
import com.yen.SpringReddit.model.RefreshToken;
import com.yen.SpringReddit.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {


    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken generateRefreshToken() {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void validateRefreshToken(String token) {

        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid refresh Token")
                );
    }

    @Override
    public void deleteRefreshToken(String token) {

        refreshTokenRepository.deleteByToken(token);
    }

}
