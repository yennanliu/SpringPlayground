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
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
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
            log.info("Auth OK !!");
            log.info(">>> Expires in: " + clientCredentials.getExpiresIn());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error(">>> Error: " + e.getMessage());
        }
        return token;
    }

    // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/authorization/authorization_code/AuthorizationCodeExample.java
    public SpotifyApi authRedirect(){

        final String code = "";

        try{
            //final URI redirectUri = SpotifyHttpManager.makeUri(this.redirectURL);
            log.info("authRedirect start ...");
            this.spotifyApi = this.getRedirectSpotifyApi();

            final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi
                    .authorizationCode(code)
                    .build();

            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                    /**
                     *  SCOPE !!!
                     *
                     *  https://developer.spotify.com/documentation/web-api/concepts/scopes
                     */
                     .scope("playlist-modify-public")
            //          .show_dialog(true)
            //          .state("x4xkmn9pu3j6ukrs8n")
                    .build();

            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Auth OK !!");

            final URI uri = authorizationCodeUriRequest.execute();
            /**
             *
             *  Open browser with below uri,
             *  click "agree" to proceed spotify auth
             *  and should be redirected to the redirectURI sent above
             */
            System.out.println("uri = " + uri);

        }catch (Exception e){
            System.out.println("(authRedirect) Auth failed : " + e);
            e.printStackTrace();
        }
        return this.spotifyApi;
    }

    public SpotifyApi getSpotifyApi() {

        log.info(">>> (getSpotifyApi) accessToken = " + this.accessToken);
        // lazy approach
        if (this.accessToken == null) {
            this.accessToken = this.getToken();
            log.info(">>> (getSpotifyApi) new accessToken = " + accessToken);
            this.spotifyApi = new SpotifyApi.Builder()
                    .setAccessToken(this.accessToken)
                    .build();
        }
        return this.spotifyApi;
    }

    public SpotifyApi getRedirectSpotifyApi() {

        //log.info(">>> (getRedirectSpotifyApi) accessToken = " + this.accessToken);
        final URI redirectUri = SpotifyHttpManager.makeUri(this.redirectURL);

        this.spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(this.accessToken)
                .setRedirectUri(redirectUri)
                .build();
        return this.spotifyApi;
    }

}
