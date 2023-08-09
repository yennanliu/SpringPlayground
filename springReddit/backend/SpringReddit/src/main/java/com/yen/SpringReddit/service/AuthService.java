package com.yen.SpringReddit.service;

// https://youtu.be/kpKUMmAmcj0?t=280
// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/master/src/main/java/com/programming/techie/springredditclone/service/AuthService.java

import com.yen.SpringReddit.dto.AuthenticationResponse;
import com.yen.SpringReddit.dto.LoginRequest;
import com.yen.SpringReddit.dto.RegisterRequest;
import com.yen.SpringReddit.po.User;

public interface AuthService {

    void signUp(RegisterRequest registerRequest);

    String generateVerificationToken(User user);

    void verifyAccount(String token);

    AuthenticationResponse login(LoginRequest loginRequest);
}
