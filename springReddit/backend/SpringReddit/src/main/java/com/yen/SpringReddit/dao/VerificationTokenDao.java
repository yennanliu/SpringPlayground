package com.yen.SpringReddit.dao;

import com.yen.SpringReddit.po.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenDao extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}