package com.yen.SpringReddit.service.impl;

import com.yen.SpringReddit.dao.RefreshTokenDao;
import com.yen.SpringReddit.exceptions.SpringRedditException;
import com.yen.SpringReddit.po.RefreshToken;
import com.yen.SpringReddit.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {


    @Autowired
    RefreshTokenDao refreshTokenDao;

    @Override
    public RefreshToken generateRefreshToken() {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenDao.save(refreshToken);
    }

    @Override
    public void validateRefreshToken(String token) {

        refreshTokenDao.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid refresh Token")
                );
    }

    @Override
    public void deleteRefreshToken(String token) {

        refreshTokenDao.deleteByToken(token);
    }

}
