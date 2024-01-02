package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.model.dto.UploadJarDto;
import com.yen.FlinkRestService.Repository.JobJarRepository;
import com.yen.FlinkRestService.model.JobJar;
import com.yen.FlinkRestService.util.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Date;

@Slf4j
@Service
public class JarService {

    @Autowired
    JobJarRepository jobJarRepository;

    //@Autowired
    private RestTemplate restTemplate;

    // TODO : read from conf
    private String BASE_URL = "http://localhost:8081/";

    // constructor
    JarService(){
        this.restTemplate = new RestTemplate();
    }

    // https://github.com/thestyleofme/flink-api-spring-boot-starter/blob/master/src/main/java/com/github/codingdebugallday/client/app/service/jars/FlinkJarService.java
    public void addJobJar(UploadJarDto uploadJarDto) {

        System.out.println(">>> uploadJarDto = " + uploadJarDto.toString());

        // Set the URL
        String url = "http://localhost:8081/jars/upload";

        // Set the file path

        // Create a MultiValueMap to hold the file and headers
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        //bodyMap.add("jarfile", new FileSystemResource(filePath));
        bodyMap.add("jarfile", new FileSystemResource(uploadJarDto.getJarFile()));

        // Create headers with "Expect" set to an empty string
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Expect", "");

        // Create the request entity with headers and body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

        // Create a RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Make the HTTP POST request
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // Print the response status code and body
        System.out.println("Response Status Code: " + responseEntity.getStatusCode());
        System.out.println("Response Body: " + responseEntity.getBody());

        // save jar info to DB
        JobJar jobjar = new JobJar();
        jobjar.setFileName(uploadJarDto.getJarFile());
        jobjar.setStatus("success"); // TODO : error handling
        jobjar.setUploadTime(new Date());
        jobJarRepository.save(jobjar);
    }

}
