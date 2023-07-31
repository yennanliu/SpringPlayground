package com.yen.SpringReddit.service.impl;

import com.yen.SpringReddit.dao.UserDao;
import com.yen.SpringReddit.dao.VerificationTokenDao;
import com.yen.SpringReddit.dto.RegisterRequest;
import com.yen.SpringReddit.po.User;
import com.yen.SpringReddit.po.VerificationToken;
import com.yen.SpringReddit.service.AuthService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

public class AuthServiceImpl implements AuthService {


    /**
     *  Autowired injection VS Constructor injection
     *      - https://youtu.be/kpKUMmAmcj0?t=373
     *      - Field Injection is Evil - https://odrotbohm.de/2013/11/why-field-injection-is-evil/
     *      - Field Injection Considered Harmful - https://www.vojtechruzicka.com/field-dependency-injection-considered-harmful/
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private VerificationTokenDao verificationTokenD;

    @Transactional
    @Override
    public void SignUp(RegisterRequest registerRequest) {

        User user = new User();
        // copy attr from registerRequest to user
        BeanUtils.copyProperties(registerRequest, user);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        // save to DB
        userDao.save(user);

        String token = generateVerificationToken(user);
    }

    @Override
    public String generateVerificationToken(User user) {

        String token =  UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        // save to DB
        verificationTokenD.save(verificationToken);
        return token;
    }

}
