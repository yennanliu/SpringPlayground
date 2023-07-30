package com.yen.SpringReddit.dao;

import com.yen.SpringReddit.po.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/master/src/main/java/com/programming/techie/springredditclone/repository/RefreshTokenRepository.java

public interface RefreshTokenDao extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);
}