package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.JobRepository;
import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.dto.JobSubmitDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class JobService {

    @Autowired
    JobRepository jobRepository;


    public List<Job> getJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Integer jobId) {

        if (jobRepository.findById(jobId).isPresent()){
            return jobRepository.findById(jobId).get();
        }
        log.warn("No Job with JobId = " + jobId);
        return null;
    }

    public void addJob(JobSubmitDto jobSubmitDto) {

        System.out.println("jobSubmitDto = " + jobSubmitDto.toString());

        // call Flink REST endpoint
        // Set the URL
        String baseUrl = "http://localhost:8081/jars/"; //"http://localhost:8081/jars/{projectId}/run";
        String projectId = jobSubmitDto.getJarId(); //"yourProjectId"; // Replace with the actual project ID

        String url = baseUrl + jobSubmitDto.getJarId() + "/run";
        // Create the request URL with path variables
        //String url = baseUrl.replace("{projectId}", projectId);

        System.out.println("url = " + url);

        // Set the query parameters
//        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
//        queryParams.add("entry-class", jobSubmitDto.getEntryClass());
//        queryParams.add("program-args", jobSubmitDto.getProgramArgs());

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //JarRunRequestBody requestBody = new JarRunRequestBody();

        // Set the request body
        String requestBody = ""; //"{ \"programArgsList\": \"parallelism\": 1 }";

        // Create the request entity with headers and request body
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Create a RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Make the HTTP POST request
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        
        // Print the response status code and body
        System.out.println("Response Status Code: " + responseEntity.getStatusCode());
        System.out.println("Response Body: " + responseEntity.getBody());

        // save to DB

    }
}
