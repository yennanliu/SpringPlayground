package com.yen.SpotifyPlayList.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.BadRequestException;
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException;
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

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    private String authCode;

    private String refreshCode;

    private SpotifyApi spotifyApi;

    public AuthService() {
    }

    public SpotifyApi getBasicSpotifyClient() {

        return new SpotifyApi
                .Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
    }

    public SpotifyApi getSpotifyClient() {

        final URI redirectUri = SpotifyHttpManager
                .makeUri(redirectURL);

        log.info("(getSpotifyApi) current accessToken = " + this.accessToken);
        // lazy approach
        if (this.accessToken == null) {
            this.accessToken = this.getToken();
            log.info("(getSpotifyApi) new accessToken = " + accessToken);
            this.spotifyApi = new SpotifyApi.Builder()
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setRedirectUri(redirectUri)
                    .setAccessToken(accessToken) // use accessToken here
                    .build();
        }

        return this.spotifyApi;
    }

    public SpotifyApi getSpotifyClientWithRefreshCode(String refreshCode) {

        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(refreshCode)
                .build();
    }


    public String getToken() {

        log.info("getToken ...");
        String token = "";
        try {
            final SpotifyApi spotifyApi = this.getBasicSpotifyClient();

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

    public SpotifyApi authClientWithAuthCode(SpotifyApi spotifyApi, String authCode) {

        log.info("Authorize spotifyApi with Auth Code = " + authCode);
        this.authCode = authCode;

        try{
            final AuthorizationCodeRequest authorizationCodeRequest = this.spotifyApi
                    // authCode can be retrieved when auth success (by Spotify) , get from spotify response
                    .authorizationCode(authCode)
                    .build();

            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest
                    .execute();

            log.info("---> RefreshToken = " + authorizationCodeCredentials.getRefreshToken());

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            // save refreshCode
            this.refreshCode = authorizationCodeCredentials.getRefreshToken();
        }
//        catch (BadRequestException | UnauthorizedException e) {
//            log.error("Error during authorization. Invalid authorization code or other issue: {}", e.getMessage());
//        }
        catch (Exception e) {
            log.error("Unexpected error during authorization: ", e);
        }

        return spotifyApi;
    }

    /**
     * Authorization flow
     * <p>
     * https://github.com/spotify-web-api-java/spotify-web-api-java?tab=readme-ov-file#authorization
     * <p>
     * Step 1) The authorization code flow requires a code, which is part of the redirectUri's query parameters when the user has opened a custom URL in a browser and authorized the application.
     * Step 2) When the code has been retrieved, it can be used in another request to get an access token as well as a refresh token.
     * Step 3) Now, the refresh token in turn can be used in a loop to retrieve new access and refresh tokens.
     */
    public SpotifyApi refreshSpotifyClient(String authCode) throws SpotifyWebApiException, IOException, ParseException {

        // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/cfd0dae1262bd7f95f90c37b28d27b9c944d471a/examples/authorization/authorization_code/AuthorizationCodeRefreshExample.java#L22
        // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/authorization/authorization_code/AuthorizationCodeExample.java

        log.info("refreshSpotifyApi start ... authCode = {}", authCode);
        this.spotifyApi = this.getSpotifyClient();

        try {
            final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi
                    .authorizationCode(authCode)
                    .build();

            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest
                    .execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            log.info("refreshSpotifyApi OK");
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error(">>> refreshSpotifyApi Error: " + e.getMessage());
        }

        return this.spotifyApi;
    }

}
