package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Controller.dto.UploadJarDto;
import com.yen.FlinkRestService.Repository.JobJarRepository;
import com.yen.FlinkRestService.Repository.JobRepository;
import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.JobJar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
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

}
