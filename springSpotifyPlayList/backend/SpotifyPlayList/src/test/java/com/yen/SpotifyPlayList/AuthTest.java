package com.yen.SpotifyPlayList;

import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;


import java.net.URI;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import java.io.IOException;

public class AuthTest {

    // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/authorization/client_credentials/ClientCredentialsExample.java
    @Test
    public void testAuthGetToken_1() {

        final String clientId = "";
        final String clientSecret = "";

        final SpotifyApi spotifyApi = new SpotifyApi
                .Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();

        final ClientCredentialsRequest clientCredentialsRequest = spotifyApi
                .clientCredentials()
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

    // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/authorization/authorization_code/AuthorizationCodeUriExample.java
    @Test
    public void testAuthRedirect_1(){

        final String clientId = "";
        final String clientSecret = "";
        /**
         *  NOTE !!!
         *   make sure the redirect url you use below
         *   is AS SAME AS the one set in Spotify api app
         *      - https://developer.spotify.com/dashboard/833e496a818d4dac926a00970ba4d725/settings
         *
         *   so, you can redirect to whatever url after you pass Auth handled by Spotify
         *   e.g. can redirect to
         *      - https://google.com
         *      - or even a local url http://localhost:8080/hello
         *
         */
        final String redirectURI = "http://localhost:8080/hello"; //"https://google.com/";
        final URI redirectUri = SpotifyHttpManager.makeUri(redirectURI);

        try{
            final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setRedirectUri(redirectUri)
                    .build();

            final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
//          .state("x4xkmn9pu3j6ukrs8n")
//          .scope("user-read-birthdate,user-read-email")
//          .show_dialog(true)
                    .build();
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
            System.out.println("(AuthorizationCodeUriRequest) Auth failed : " + e);
            e.printStackTrace();
        }
    }

    // https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/authorization/authorization_code/AuthorizationCodeExample.java
    @Test
    public void testAuthRedirectGetRefreshToken(){

        final String clientId = "";
        final String clientSecret = "";
        final String redirectURI = "http://localhost:8080/hello"; //"https://google.com/";
        final URI redirectUri = SpotifyHttpManager.makeUri(redirectURI);

        // TODO : validate below
        //final String code = "BQBDlJ4IqBE3aYowwZkKNExMR2_KUblZ2EDPqzW4luZIWy3rx7kFj2THwe-x-hyS814fW4q_DrVBCR7MBPcODszAnZHeF1sJE4MWC9osqcPnDDTmTkE";
        final String code = "AQBDNjnsAc6sytg1cXAA4JVF9Dezt_ArBAJ-Biez8HyEJWx0SdsC8EOFGCMvTw85XXrwkcRX1dWhRitZ6jbPGPoahvYaSVo9uFcQW6MKh55ly8cPokDnbwyMlWuXuwMFvTjjzmq7-0y14pfzYW9dL3jWVIAuQLc3LnWj6SR";

        try{

            final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setRedirectUri(redirectUri)
                    .build();

            final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
                    .build();

            System.out.println("Auth OK !!");

            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            System.out.println("set token with token and refresh token");
            System.out.println("token = " + authorizationCodeCredentials.getAccessToken());
            System.out.println("refresh token = " + authorizationCodeCredentials.getRefreshToken());

            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());

        }catch (IOException | SpotifyWebApiException | ParseException e){
            System.out.println("Auth failed : " + e);
            e.printStackTrace();
        }
    }

}
