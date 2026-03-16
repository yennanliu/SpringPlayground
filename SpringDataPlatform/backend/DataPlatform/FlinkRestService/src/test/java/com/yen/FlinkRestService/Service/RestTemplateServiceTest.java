package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.exception.ExternalServiceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestTemplateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private RestTemplateService restTemplateService;

    @BeforeEach
    void setUp() {
        // Mock the builder chain
        when(restTemplateBuilder.setConnectTimeout(any(Duration.class))).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.setReadTimeout(any(Duration.class))).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        restTemplateService = new RestTemplateService(restTemplateBuilder);
    }

    @Test
    void testSendPostRequest_Success() {
        String url = "http://example.com/api";
        String requestBody = "{\"key\":\"value\"}";
        MediaType mediaType = MediaType.APPLICATION_JSON;

        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"response\":\"success\"}", HttpStatus.OK);
        when(restTemplate.postForEntity(eq(url), any(HttpEntity.class), eq(String.class))).thenReturn(responseEntity);

        ResponseEntity<String> result = restTemplateService.sendPostRequest(url, requestBody, mediaType);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("{\"response\":\"success\"}", result.getBody());
        verify(restTemplate, times(1)).postForEntity(eq(url), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testSendPostRequest_ConnectionFailure() {
        String url = "http://example.com/api";
        String requestBody = "{\"key\":\"value\"}";

        when(restTemplate.postForEntity(eq(url), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new ResourceAccessException("Connection refused"));

        assertThrows(ExternalServiceException.class, () -> {
            restTemplateService.sendPostRequest(url, requestBody, MediaType.APPLICATION_JSON);
        });

        verify(restTemplate, times(1)).postForEntity(eq(url), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testSendGetRequest_Success() {
        String url = "http://example.com/api";

        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"response\":\"success\"}", HttpStatus.OK);
        when(restTemplate.getForEntity(eq(url), eq(String.class))).thenReturn(responseEntity);

        ResponseEntity<String> result = restTemplateService.sendGetRequest(url);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("{\"response\":\"success\"}", result.getBody());
        verify(restTemplate, times(1)).getForEntity(eq(url), eq(String.class));
    }

    @Test
    void testSendGetRequest_ConnectionFailure() {
        String url = "http://example.com/api";

        when(restTemplate.getForEntity(eq(url), eq(String.class)))
                .thenThrow(new ResourceAccessException("Connection refused"));

        assertThrows(ExternalServiceException.class, () -> {
            restTemplateService.sendGetRequest(url);
        });

        verify(restTemplate, times(1)).getForEntity(eq(url), eq(String.class));
    }

    @Test
    void testPingServer_Success() {
        String url = "http://example.com";
        int port = 8080;
        String pingUrl = url + ":" + port;

        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"response\":\"success\"}", HttpStatus.OK);
        when(restTemplate.getForEntity(eq(pingUrl), eq(String.class))).thenReturn(responseEntity);

        ResponseEntity<String> result = restTemplateService.pingServer(url, port);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("{\"response\":\"success\"}", result.getBody());
        verify(restTemplate, times(1)).getForEntity(eq(pingUrl), eq(String.class));
    }

    @Test
    void testPingServer_ConnectionFailure() {
        String url = "http://example.com";
        int port = 8080;
        String pingUrl = url + ":" + port;

        when(restTemplate.getForEntity(eq(pingUrl), eq(String.class)))
                .thenThrow(new ResourceAccessException("Connection refused"));

        ResponseEntity<String> result = restTemplateService.pingServer(url, port);

        // pingServer returns null on failure instead of throwing exception
        assertNull(result);
        verify(restTemplate, times(1)).getForEntity(eq(pingUrl), eq(String.class));
    }
}
