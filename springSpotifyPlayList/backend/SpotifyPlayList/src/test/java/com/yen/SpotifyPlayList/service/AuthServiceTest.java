package com.yen.SpotifyPlayList.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private SpotifyApi spotifyApiMock;

    @Mock
    private ClientCredentialsRequest clientCredentialsRequestMock;

    @Mock
    private ClientCredentials clientCredentialsMock;

    @Mock
    private AuthorizationCodeCredentials authorizationCodeCredentialsMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inject values for the @Value fields
        ReflectionTestUtils.setField(authService, "clientId", "your-client-id");
        ReflectionTestUtils.setField(authService, "clientSecret", "your-client-secret");
        ReflectionTestUtils.setField(authService, "redirectURL", "your-redirect-url");
    }

    @Test
    public void testInitializeSpotifyApi() {
        // Act
        SpotifyApi spotifyApi = authService.initializeSpotifyApi();

        // Assert
        assertNotNull(spotifyApi, "Spotify API should be initialized.");
    }

    @Test
    public void testGetSpotifyClientWithIdAndSecret() {
        SpotifyApi spotifyApi = authService.getSpotifyClientWithIdAndSecret();

        assertNotNull(spotifyApi, "SpotifyApi instance should be returned.");
        assertEquals("your-client-id", spotifyApi.getClientId());
        assertEquals("your-client-secret", spotifyApi.getClientSecret());
    }


    @Test
    public void testGetToken() {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId("test-client-id")
                .setClientSecret("test-client-secret")
                .build();

        // Call the method being tested
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        System.out.println(">>> clientCredentialsRequest = " + clientCredentialsRequest);
        assertNotNull(clientCredentialsRequest);
    }

    @Test
    public void testRefreshClient() {
        // Set refresh token for the SpotifyApi
        when(spotifyApiMock.getRefreshToken()).thenReturn("refresh-token");

        // Call refreshClient method
        authService.refreshClient(spotifyApiMock);

        // Verify that the refresh token is set correctly
        verify(spotifyApiMock).setRefreshToken("refresh-token");
    }
}