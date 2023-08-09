package com.yen.SpringReddit.service;

import com.yen.SpringReddit.dao.RefreshTokenDao;
import com.yen.SpringReddit.po.RefreshToken;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

public interface RefreshTokenService {

    public RefreshToken generateRefreshToken();

    public void validateRefreshToken(String token);

    public void deleteRefreshToken(String token);
}
