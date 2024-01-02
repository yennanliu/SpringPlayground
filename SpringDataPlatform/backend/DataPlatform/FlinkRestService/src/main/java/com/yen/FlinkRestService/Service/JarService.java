package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Controller.dto.UploadJarDto;
import com.yen.FlinkRestService.Repository.JobJarRepository;
import com.yen.FlinkRestService.model.JobJar;
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

    // https://github.com/thestyleofme/flink-api-spring-boot-starter/blob/master/src/main/java/com/github/codingdebugallday/client/app/service/jars/FlinkJarService.java
    public void addJobJar(UploadJarDto uploadJarDto) {

        log.info("(addJobJar) uploadJarDto = " + uploadJarDto.toString());

        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("jarfile", new FileSystemResource(uploadJarDto.getJarFile()));

        restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Expect", "");

        // create request
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        //HttpEntity<String> request = new HttpEntity<String>(uploadJarDto.toString(), headers);
        log.info("(addJobJar) requestEntity = " + requestEntity);

        // send request to Flink REST api
        //restTemplate.postForObject(BASE_URL+"/jars/upload", request, String.class);
        // Make the HTTP POST request
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_URL+"/jars/upload", HttpMethod.POST, requestEntity, String.class);

        // save jar info to DB
        JobJar jobjar = new JobJar();
        jobjar.setFileName(uploadJarDto.getJarFile());
        jobjar.setStatus("success"); // TODO : error handling
        jobjar.setUploadTime(new Date());
        jobJarRepository.save(jobjar);
    }

}
