package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.exception.ExternalServiceException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Slf4j
@Service
public class RestTemplateService {

    private final RestTemplate restTemplate;

    public RestTemplateService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(20))
                .setReadTimeout(Duration.ofSeconds(500))
                .build();
    }

    public ResponseEntity<String> sendPostRequest(String url, String requestBody, MediaType mediaType) {
        log.info("Sending POST request to url={}, body={}", url, requestBody);

        try {
            HttpHeaders headers = new HttpHeaders();
            if (mediaType != null) {
                headers.setContentType(mediaType);
            }

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

            log.info("POST request succeeded, status={}", responseEntity.getStatusCode());
            return responseEntity;

        } catch (ResourceAccessException e) {
            log.error("POST request failed - cannot connect to url={}: {}", url, e.getMessage());
            throw new ExternalServiceException("RestTemplate", "Cannot connect to " + url, e);
        } catch (RestClientException e) {
            log.error("POST request failed to url={}: {}", url, e.getMessage());
            throw new ExternalServiceException("RestTemplate", "Request failed: " + e.getMessage(), e);
        }
    }

    public ResponseEntity<String> sendGetRequest(String url) {
        log.info("Sending GET request to url={}", url);

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            log.info("GET request succeeded, status={}", responseEntity.getStatusCode());
            return responseEntity;

        } catch (ResourceAccessException e) {
            log.error("GET request failed - cannot connect to url={}: {}", url, e.getMessage());
            throw new ExternalServiceException("RestTemplate", "Cannot connect to " + url, e);
        } catch (RestClientException e) {
            log.error("GET request failed to url={}: {}", url, e.getMessage());
            throw new ExternalServiceException("RestTemplate", "Request failed: " + e.getMessage(), e);
        }
    }

    public ResponseEntity<String> pingServer(String url, Integer port) {
        String pingUrl = url + ":" + port;
        log.info("Pinging server at url={}", pingUrl);

        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(pingUrl, String.class);
            log.info("Ping succeeded, status={}", resp.getStatusCode());
            return resp;
        } catch (ResourceAccessException e) {
            log.warn("Ping failed - cannot connect to {}: {}", pingUrl, e.getMessage());
            return null;
        } catch (RestClientException e) {
            log.warn("Ping failed to {}: {}", pingUrl, e.getMessage());
            return null;
        }
    }

    public <T> ResponseEntity<T> sendPostRequestWithType(String url, Object requestBody, Class<T> responseType) {
        log.info("Sending POST request to url={}", url);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, requestEntity, responseType);

            log.info("POST request succeeded, status={}", responseEntity.getStatusCode());
            return responseEntity;

        } catch (ResourceAccessException e) {
            log.error("POST request failed - cannot connect to url={}: {}", url, e.getMessage());
            throw new ExternalServiceException("RestTemplate", "Cannot connect to " + url, e);
        } catch (RestClientException e) {
            log.error("POST request failed to url={}: {}", url, e.getMessage());
            throw new ExternalServiceException("RestTemplate", "Request failed: " + e.getMessage(), e);
        }
    }
}
