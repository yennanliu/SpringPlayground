package com.yen.SpotifyPlayList.service;

import java.io.IOException;
import java.net.URI;
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

@Service
@Slf4j
public class AuthService implements IAuthService {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.redirect.url}")
    private String redirectURL;
    private String accessToken;
    private String refreshToken;
    private String authCode;
    private SpotifyApi spotifyApi;

    public AuthService() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    /**
     * Initializes the SpotifyApi instance if not already initialized.
     * @return the SpotifyApi instance.
     */
    public SpotifyApi initializeSpotifyApi() {
        if (this.spotifyApi == null) {
            this.spotifyApi = getSpotifyClient();
            log.info("SpotifyApi initialized with access token: {}", this.spotifyApi.getAccessToken());
        }
        return this.spotifyApi;
    }

    public SpotifyApi getSpotifyClientWithIdAndSecret() {

        return new SpotifyApi
                .Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
    }

    public SpotifyApi getSpotifyClientWithIdAndSecretAndRedirectUrlAndToken() {

        return new SpotifyApi
                .Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(getRedirectURL(redirectURL))
                .setAccessToken(accessToken) // use accessToken here
                .build();
    }


    public SpotifyApi getSpotifyClient() {

        log.info("(getSpotifyApi) current accessToken = " + this.accessToken);
        // lazy approach
        if (this.accessToken == null) {
            this.accessToken = this.getToken();
            log.info("(getSpotifyApi) new accessToken = " + accessToken);
            this.spotifyApi = this.getSpotifyClientWithIdAndSecretAndRedirectUrlAndToken();
        }

        return this.spotifyApi;
    }

    public String getToken() {

        log.info("getToken ...");
        String token = "";
        try {
            final SpotifyApi spotifyApi = this.getSpotifyClientWithIdAndSecret();

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

        log.info("Authorize spotifyApi with Auth Code = {}", authCode);
        this.authCode = authCode;

        try {
            // Build the authorization code request
            final AuthorizationCodeRequest authorizationCodeRequest = this.spotifyApi
                    .authorizationCode(authCode)
                    .build();

            // Execute the request to get the credentials
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Retrieve tokens
            this.accessToken = authorizationCodeCredentials.getAccessToken();
            this.refreshToken = authorizationCodeCredentials.getRefreshToken();

            log.info("Access Token = {}", this.accessToken);
            log.info("Refresh Token = {}", this.refreshToken);

            // Set tokens to Spotify API instance
            spotifyApi.setAccessToken(this.accessToken);
            spotifyApi.setRefreshToken(this.refreshToken);

        } catch (BadRequestException e) {
            log.error("Bad Request during authorization. Check the authorization code and parameters: {}", e.getMessage());
        } catch (UnauthorizedException e) {
            log.error("Unauthorized access. Check if the authorization code is valid and not expired: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during authorization: ", e);
        }

        return spotifyApi;
    }

    public void refreshClient(SpotifyApi spotifyApi){
        spotifyApi.setRefreshToken(spotifyApi.getRefreshToken());
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

    private URI getRedirectURL(String redirectURL){
        return SpotifyHttpManager
                .makeUri(redirectURL);
    }

}
