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

//    @Test
//    public void testGetToken() throws IOException, se.michaelthelin.spotify.exceptions.SpotifyWebApiException, org.apache.hc.core5.http.ParseException {
//        when(spotifyApiMock.clientCredentials()).thenReturn(clientCredentialsRequestMock);
//        when(clientCredentialsRequestMock.execute()).thenReturn(clientCredentialsMock);
//        when(clientCredentialsMock.getAccessToken()).thenReturn("test-access-token");
//
//        // Inject the mocked SpotifyApi instance
//        ReflectionTestUtils.setField(authService, "spotifyApi", spotifyApiMock);
//
//        String token = authService.getToken();
//
//        assertEquals("test-access-token", token, "Token should be 'test-access-token'.");
//        verify(spotifyApiMock).setAccessToken("test-access-token");
//    }

//    @Test
//    public void testAuthClientWithAuthCode() throws Exception {
//        when(spotifyApiMock.authorizationCode("test-auth-code")).thenReturn(mock(AuthorizationCodeRequest.Builder.class));
//        when(authorizationCodeCredentialsMock.getAccessToken()).thenReturn("access-token");
//        when(authorizationCodeCredentialsMock.getRefreshToken()).thenReturn("refresh-token");
//
//        // Inject the mocked SpotifyApi instance
//        ReflectionTestUtils.setField(authService, "spotifyApi", spotifyApiMock);
//
//        SpotifyApi spotifyApi = authService.authClientWithAuthCode(spotifyApiMock, "test-auth-code");
//
//        assertNotNull(spotifyApi, "SpotifyApi should be returned.");
//        assertEquals("access-token", authService.getAccessToken(), "Access token should match.");
//        assertEquals("refresh-token", authService.getRefreshToken(), "Refresh token should match.");
//        verify(spotifyApiMock).setAccessToken("access-token");
//        verify(spotifyApiMock).setRefreshToken("refresh-token");
//    }

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