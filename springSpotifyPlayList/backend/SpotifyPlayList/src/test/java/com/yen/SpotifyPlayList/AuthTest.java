package com.yen.SpotifyPlayList;

import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;

public class AuthTest {

    // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/authorization/client_credentials/ClientCredentialsExample.java
    @Test
    public void testGetToken1() {

        final String clientId = "";
        final String clientSecret = "";

        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
        final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
                .build();
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            String token = clientCredentials.getAccessToken();
            System.out.println(">>> token = " + token);
            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            System.out.println("Auth OK !!");
            System.out.println(">>> Expires in: " + clientCredentials.getExpiresIn());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println(">>> Error: " + e.getMessage());
        }

    }

}
