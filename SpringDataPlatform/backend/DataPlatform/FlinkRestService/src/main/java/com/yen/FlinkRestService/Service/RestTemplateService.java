//package com.yen.FlinkRestService.Service;
//
//import com.alibaba.fastjson2.JSON;
//import com.yen.FlinkRestService.Common.RestTemplateResponse;
//import com.yen.FlinkRestService.model.response.JarUploadResponse;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class RestTemplateService<T> {
//
//    // attr
//    private String baseURL;
//
//    public RestTemplateResponse<T> sendPostRequst(String url, Object requestBody){
//
//
//        // Create a MultiValueMap to hold the file and headers
//        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
//        bodyMap.add("jarfile", new FileSystemResource(uploadJarDto.getJarFile()));
//
//        // Create headers with "Expect" set to an empty string
//        HttpHeaders headers = new HttpHeaders();
//
//        // Create the request entity with headers and body
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
//
//        // Create a RestTemplate
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Make the HTTP POST request
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//
//        JarUploadResponse jarUploadResponse = JSON.parseObject(responseEntity.getBody(), JarUploadResponse.class);
//
//        return null;
//    }
//
//    public void sendGETRequst(){
//
//        //return null;
//    }
//
//}
