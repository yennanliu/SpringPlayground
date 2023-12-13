package com.yen.mdblog.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface CustomOAuth2UserService {

    public OAuth2User loadUser(OAuth2UserRequest userRequest);
}
