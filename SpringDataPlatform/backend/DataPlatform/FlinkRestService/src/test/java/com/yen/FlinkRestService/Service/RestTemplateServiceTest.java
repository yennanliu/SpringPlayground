package com.yen.FlinkRestService.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RestTemplateServiceTest {

    @InjectMocks
    private RestTemplateService restTemplateService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendPostRequest() {
        String url = "http://example.com/api";
        String requestBody = "{\"key\":\"value\"}";
        MediaType mediaType = MediaType.APPLICATION_JSON;

        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"response\":\"success\"}", HttpStatus.OK);
        // mock
        when(restTemplate.postForEntity(eq(url), any(HttpEntity.class), eq(String.class))).thenReturn(responseEntity);

        ResponseEntity<String> result = restTemplateService.sendPostRequest(url, requestBody, mediaType);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("{\"response\":\"success\"}", result.getBody());
        verify(restTemplate, times(1)).postForEntity(eq(url), any(HttpEntity.class), eq(String.class));
    }

    @Test
    public void testSendGetRequest() {

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
    public void testPingServer() {

        String url = "http://example.com";
        int port = 8080;
        String pingUrl = url + ":" + port;

        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"response\":\"success\"}", HttpStatus.OK);
        // mock
        when(restTemplate.getForEntity(eq(pingUrl), eq(String.class))).thenReturn(responseEntity);

        ResponseEntity<String> result = restTemplateService.pingServer(url, port);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("{\"response\":\"success\"}", result.getBody());
        verify(restTemplate, times(1)).getForEntity(eq(pingUrl), eq(String.class));
    }

}