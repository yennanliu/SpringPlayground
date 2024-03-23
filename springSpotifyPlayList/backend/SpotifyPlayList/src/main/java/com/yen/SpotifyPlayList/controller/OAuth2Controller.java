//package com.yen.SpotifyPlayList.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class OAuth2Controller {
//
//    private final ClientRegistrationRepository clientRegistrationRepository;
//
//    @Autowired
//    public OAuth2Controller(ClientRegistrationRepository clientRegistrationRepository) {
//        this.clientRegistrationRepository = clientRegistrationRepository;
//    }
//
//    @GetMapping("/callback")
//    public String callback(OAuth2AuthenticationToken token) {
//        // Handle the OAuth2 callback here
//        return "redirect:/";
//    }
//}
