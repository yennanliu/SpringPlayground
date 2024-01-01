package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Controller.dto.UploadJarDto;
import com.yen.FlinkRestService.Repository.JobJarRepository;
import com.yen.FlinkRestService.Repository.JobRepository;
import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.JobJar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class JobService {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    JobJarRepository jobJarRepository;

//    @Autowired
    private RestTemplate restTemplate;

    // TODO : read from conf
    private String BASE_URL = "http://localhost:8081/";


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

    public void addJobJar(UploadJarDto uploadJarDto) {

        log.info("(addJobJar) uploadJarDto = " + uploadJarDto.toString());

        restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // prepare request
        HttpEntity<String> request = new HttpEntity<String>(uploadJarDto.toString(), headers);
        log.info("(addJobJar) request = " + request);

        // send request to Flink REST api
        restTemplate.postForObject(BASE_URL+"/jars/upload", request, String.class);

        // save jar info to DB
        JobJar jobjar = new JobJar();
        jobjar.setFileName(uploadJarDto.getJarFile());
        jobjar.setStatus("success"); // TODO : error handling
        jobjar.setUploadTime(new Date());
        jobJarRepository.save(jobjar);
    }

}
