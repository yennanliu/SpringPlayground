package com.yen.SpotifyPlayList.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;
import java.net.URI;

@Service
@Slf4j
public class AuthService {

    @Value("${spotify.clientId}")
    private String clientId;

    @Value("${spotify.clientSecret}")
    private String clientSecret;

    @Value("${spotify.redirectURL}")
    private String redirectURL;

    private String accessToken;

    private String refreshCode;

    private SpotifyApi spotifyApi;

    public AuthService(){
    }

    public String getToken(){

        log.info(">>> getToken ...");
        String token = "";

        try {
            final SpotifyApi spotifyApi = new SpotifyApi
                    .Builder()
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .build();

            final ClientCredentialsRequest clientCredentialsRequest = spotifyApi
                    .clientCredentials()
                    .build();

            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            token = clientCredentials.getAccessToken();
            log.info(">>> token = " + token);

            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            log.info("get token OK !!");
            log.info(">>> Expires in: " + clientCredentials.getExpiresIn());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error(">>> get token Error: " + e.getMessage());
        }
        return token;
    }

    public SpotifyApi getSpotifyApi() {

        final URI redirectUri = SpotifyHttpManager
                .makeUri(redirectURL);

        log.info("(getSpotifyApi) current accessToken = " + this.accessToken);
        // lazy approach
        if (this.accessToken == null) {
            this.accessToken = this.getToken();
            log.info("(getSpotifyApi) new accessToken = " + accessToken);
            this.spotifyApi  = new SpotifyApi.Builder()
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setRedirectUri(redirectUri)
                    .setAccessToken(accessToken) // use accessToken here
                    .build();
        }

        return this.spotifyApi;
    }

    public SpotifyApi authClientWithAuthCode(SpotifyApi spotifyApi, String authCode) throws SpotifyWebApiException, IOException, ParseException {

        log.info("Authorize spotifyApi with Auth Code = " + authCode);

        final AuthorizationCodeRequest authorizationCodeRequest = this.spotifyApi
                /**
                 *  authCode can be retrieved when auth success (by Spotify)
                 *  , get from spotify response
                 */
                .authorizationCode(authCode)
                .build();

        final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest
                .execute();

        log.info("RefreshToken = " + authorizationCodeCredentials.getRefreshToken());

        // Set access and refresh token for further "spotifyApi" object usage
        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
        spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

        // save refreshCode
        this.refreshCode = authorizationCodeCredentials.getRefreshToken();

        return spotifyApi;
    }

    public SpotifyApi refreshSpotifyApi(SpotifyApi spotifyApi) throws SpotifyWebApiException, IOException, ParseException{

        // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/cfd0dae1262bd7f95f90c37b28d27b9c944d471a/examples/authorization/authorization_code/AuthorizationCodeRefreshExample.java#L22
        spotifyApi.setRefreshToken(this.refreshCode);
        return spotifyApi;
    }

}
