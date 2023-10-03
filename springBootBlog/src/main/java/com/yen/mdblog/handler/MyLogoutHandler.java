package com.yen.mdblog.handler;

// https://matthung0807.blogspot.com/2019/11/spring-boot-security-custom-logout.html

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO : implement this custom LogoutHandler to security config
@Component
public class MyLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        // 登出時在console印出的訊息
        System.out.println(">>> going to logoug ... MyLogoutHandler.logout()");
    }

}