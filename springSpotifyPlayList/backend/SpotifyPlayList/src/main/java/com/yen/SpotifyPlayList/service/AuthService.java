package com.yen.SpotifyPlayList.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
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
                    // TODO : implement redirect method with scope, ref : testAuthRedirect_1
//                    /**
//                     * scope ?
//                     *
//                     *  https://developer.spotify.com/documentation/web-api/concepts/scopes
//                     *  https://bret-gibson.medium.com/spotify-api-authentication-with-spring-boot-and-react-23a68f4e79bb
//                     */
//                    .setScheme("user-read-private, user-read-email")
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

    public SpotifyApi getSpotifyApi() {

        final String redirectURL = "http://localhost:8080/playlist"; //"http://localhost:8888/authorized-url"; //"http://localhost:8888/authorized-url"; //"http://localhost:8080/playlist"; // "http://localhost:8888/authorized-url";

        final URI redirectUri = SpotifyHttpManager
                .makeUri(redirectURL);

        log.info(">>> (getSpotifyApi) accessToken = " + this.accessToken);
        // lazy approach
        if (this.accessToken == null) {
            this.accessToken = this.getToken();
            log.info(">>> (getSpotifyApi) new accessToken = " + accessToken);
//            this.spotifyApi = new SpotifyApi.Builder()
//                    .setAccessToken(this.accessToken)
//                    .build();
            this.spotifyApi  = new SpotifyApi.Builder()
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setRedirectUri(redirectUri)
                    .build();
        }
        return this.spotifyApi;
    }

}
