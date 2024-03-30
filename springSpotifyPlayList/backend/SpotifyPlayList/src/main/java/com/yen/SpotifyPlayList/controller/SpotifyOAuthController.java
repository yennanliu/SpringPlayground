package com.yen.SpotifyPlayList.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;
import java.security.Principal;

@Controller
public class SpotifyOAuthController {

    @Value("${spotify.clientId}")
    private String clientId;

    @Value("${spotify.clientSecret}")
    private String clientSecret;

//    @Value("${spotify.redirectURL}")
//    private String redirectURL;

    private String redirectURL = "http://localhost:8888/callback";

    private final URI redirectUri = SpotifyHttpManager
            .makeUri(redirectURL);

    private final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .build();
    private final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi
            .authorizationCodeUri()
            .scope("playlist-modify-public")
            .show_dialog(true)
            .build();

    @GetMapping("/authorize")
    public String authorize() {
        final URI uri = authorizationCodeUriRequest.execute();
        return "redirect:" + uri.toString();
    }

    // This is your callback URL where Spotify will redirect the user after authorization
    @GetMapping("/callback")
    public String callback() {
        // Handle the callback logic here (e.g., exchange authorization code for access token)
        return "callback-page"; // Redirect to a page or do further processing
    }

    // get authorized result
    @GetMapping("/authorized-endpoint")
    public String authorizedEndpoint(Principal principal) {
        // Principal contains information about the authenticated user
        String username = principal.getName();
        return "Hello, " + username + "! You have access to this authorized endpoint.";
    }

}

