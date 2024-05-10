package com.yen.FlinkRestService.Service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.yen.FlinkRestService.model.dto.jar.UploadJarDto;
import com.yen.FlinkRestService.Repository.JobJarRepository;
import com.yen.FlinkRestService.model.JobJar;
import com.yen.FlinkRestService.model.response.JarUploadResponse;
import com.yen.FlinkRestService.util.JarUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class JarService {

    @Autowired
    JobJarRepository jobJarRepository;

    //@Autowired
    private final RestTemplate restTemplate;

    @Value("${flink.base_url}")
    private String BASE_URL; // "http://localhost:8081/";

    private final JarUtil jarUtil = new JarUtil();

    // constructor
    JarService(){

        this.restTemplate = new RestTemplate();
    }

    // https://github.com/thestyleofme/flink-api-spring-boot-starter/blob/master/src/main/java/com/github/codingdebugallday/client/app/service/jars/FlinkJarService.java
    public void addJobJar(UploadJarDto uploadJarDto) {

        log.info("(addJobJar) uploadJarDto = " + uploadJarDto.toString());
        // Set the URL
        String url = BASE_URL + "/jars/upload";
        log.info("url = " + url);

        // Set the file path
        // Create a MultiValueMap to hold the file and headers
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
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
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();
        JarUploadResponse jarUploadResponse = gson.fromJson(responseEntity.getBody(), JarUploadResponse.class);

        // Print the response status code and body
        log.info("Response Status Code: " + responseEntity.getStatusCode());
        log.info("Response Body: " + responseEntity.getBody());

        // save jar info to DB
        JobJar jobjar = new JobJar();
        jobjar.setFileName(uploadJarDto.getJarFile());
        jobjar.setStatus("success"); // TODO : error handling
        jobjar.setUploadTime(new Date());
        jobjar.setSavedJarName(jarUtil.getJarNameFromRepsonse(jarUploadResponse));
        jobJarRepository.save(jobjar);
    }

    public List<JobJar> getJars() {

        return jobJarRepository.findAll();
    }

    public JobJar getJarByJobId(Integer jobJarId){

        if (jobJarRepository.findById(jobJarId).isPresent()){
            return jobJarRepository.findById(jobJarId).get();
        }
        log.warn("No Job jar found with jobJarId = " + jobJarId);
        return null;
    }

}
