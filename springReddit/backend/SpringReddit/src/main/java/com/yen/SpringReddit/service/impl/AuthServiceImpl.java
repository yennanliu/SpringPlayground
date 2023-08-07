package com.yen.SpringReddit.service.impl;

import com.yen.SpringReddit.dao.UserDao;
import com.yen.SpringReddit.dao.VerificationTokenDao;
import com.yen.SpringReddit.dto.RegisterRequest;
import com.yen.SpringReddit.po.NotificationEmail;
import com.yen.SpringReddit.po.User;
import com.yen.SpringReddit.po.VerificationToken;
import com.yen.SpringReddit.service.AuthService;
import com.yen.SpringReddit.service.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
//@AllArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {


    /**
     *  Autowired injection VS Constructor injection
     *      - https://youtu.be/kpKUMmAmcj0?t=373
     *      - Field Injection is Evil - https://odrotbohm.de/2013/11/why-field-injection-is-evil/
     *      - Field Injection Considered Harmful - https://www.vojtechruzicka.com/field-dependency-injection-considered-harmful/
     */
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private VerificationTokenDao verificationTokenD;

    @Autowired
    private MailService mailService;

    @Override
    public void signUp(RegisterRequest registerRequest) {

        log.info("registerRequest = " + registerRequest.toString());

        User user = new User();
        // copy attr from registerRequest to user
        //BeanUtils.copyProperties(registerRequest, user);
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        //user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        log.info("user = " + user.toString());

        // save to DB
        userDao.save(user);

        String token = generateVerificationToken(user);

        NotificationEmail email = new NotificationEmail(
                "plz activate your email",
                user.getEmail(),
                "Thanks for register!, plz click below url to activate your account :" +
               "http://localhost:8080/api/auth/accountVerification/" + token
        );
        log.info("email = " + email.toString());
        mailService.sendMail(email);
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
