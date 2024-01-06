package com.yen.FlinkRestService.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RestTemplateService {

    // attr
    //private String url;

    RestTemplate restTemplate;

    HttpHeaders headers;

    HttpEntity<String> requestEntity;

    // constructor
    RestTemplateService(){

        //this.url = url;
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    public ResponseEntity<String> sendPostRequest(String url, String requestBody, MediaType mediaType){

        ResponseEntity<String> responseEntity = null;
        log.info("url = " + url + "requestBody = " + requestBody);

        try{
            // Create headers
            this.headers.setContentType(mediaType);

            // Create the request entity with headers and request body
            this.requestEntity = new HttpEntity<>(requestBody, headers);

            // Make the HTTP POST request
            responseEntity = this.restTemplate.postForEntity(url, requestEntity, String.class);

            log.info("Response Status Code: " + responseEntity.getStatusCode());
            log.info("Response Entity :  " + responseEntity);
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

            log.info("Response Status Code: " + responseEntity.getStatusCode());
            log.info("Response Entity :  " + responseEntity);
        }catch (Exception e){
            log.warn("sendGETRequest failed : " + url);
            e.printStackTrace();
        }

        return responseEntity;
    }

}
