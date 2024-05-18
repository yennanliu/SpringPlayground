package com.yen.FlinkRestService.Service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Slf4j
@Service
public class RestTemplateService {

    RestTemplate restTemplate;

    HttpHeaders headers;

    HttpEntity<String> requestEntity;

    // constructor
    public RestTemplateService(){

        /**
         *  customize restTemplate timeout
         *      https://stackoverflow.com/questions/13837012/spring-resttemplate-timeout
         */
        this.restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(20)) // 20 sec conn timeout
                .setReadTimeout(Duration.ofSeconds(500)) // 500 sec read timeout
                .build();

        this.headers = new HttpHeaders();
    }

    public ResponseEntity<String> sendPostRequest(String url, String requestBody, MediaType mediaType){

        ResponseEntity<String> responseEntity = null;
        log.info("url = " + url + " requestBody = " + requestBody);

        try{
            // Create headers
            this.headers.setContentType(mediaType);

            // Create the request entity with headers and request body
            this.requestEntity = new HttpEntity<>(requestBody, headers);

            // Make the HTTP POST request
            responseEntity = this.restTemplate.postForEntity(url, requestEntity, String.class);
            log.info("sendPostRequest failed  OK, Response Status Code: " + responseEntity.getStatusCode() + "\n" + "Response Entity :  " + responseEntity);
        }catch (Exception e){
            log.warn("sendPostRequest failed : " + url);
            e.printStackTrace();
        }

        return responseEntity;
    }

    public ResponseEntity<String> sendGetRequest(String url){

        ResponseEntity<String> responseEntity = null;
        log.info("url = " + url);
        try{
            // Make the HTTP GET request
            responseEntity = this.restTemplate.getForEntity(url, String.class);
            log.info("Response Status Code: " + responseEntity.getStatusCode() + "\n" + "Response Entity :  " + responseEntity);
        }catch (Exception e){
            log.warn("sendGETRequest failed : " + url);
            e.printStackTrace();
        }

        return responseEntity;
    }

    // ping/test if remote server is accessible
    //@Async
    public ResponseEntity<String> pingServer(String url, Integer port){

        ResponseEntity<String> resp = null;
        try{
            String pingUrl = url + ":" + port;
            log.info("pingUrl = " + pingUrl);
            resp = this.restTemplate.getForEntity(pingUrl, String.class);
        } catch (Exception e){
            log.warn("pingServer fail : " + e.getMessage());
            e.printStackTrace();
        }

        return resp;
    }

}
