package com.yen.FlinkRestService.Service;

import com.alibaba.fastjson2.JSON;
import com.yen.FlinkRestService.Repository.JobRepository;
import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.dto.JobSubmitDto;
import com.yen.FlinkRestService.model.dto.JobUpdateDto;
import com.yen.FlinkRestService.model.response.JobOverview;
import com.yen.FlinkRestService.model.response.JobOverviewResponse;
import com.yen.FlinkRestService.model.response.JobSubmitResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

        log.info("jobSubmitDto = " + jobSubmitDto.toString());

        // Set the URL
        String baseUrl = "http://localhost:8081/jars/"; //"http://localhost:8081/jars/{projectId}/run";
        String projectId = jobSubmitDto.getJarId(); //"yourProjectId"; // Replace with the actual project ID

        String url = baseUrl + jobSubmitDto.getJarId() + "/run";
        System.out.println("url = " + url);

        // Set the query parameters
//        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
//        queryParams.add("entry-class", jobSubmitDto.getEntryClass());
//        queryParams.add("program-args", jobSubmitDto.getProgramArgs());

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Set the request body
        String requestBody = ""; //"{ \"programArgsList\": \"parallelism\": 1 }";

        // Create the request entity with headers and request body
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Create a RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Make the HTTP POST request
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

        // Print the response status code and body : https://www.runoob.com/w3cnote/fastjson-intro.html
        JobSubmitResponse jobSubmitResponse = JSON.parseObject(responseEntity.getBody(), JobSubmitResponse.class);
        log.info("Response Status Code: " + responseEntity.getStatusCode());
        System.out.println("jobSubmitResponse : " +jobSubmitResponse.toString());

        // save to DB
        Job job = new Job();
        job.setJobId(jobSubmitResponse.getJobid());
        job.setName(jobSubmitDto.getJarId());
        job.setStartTime( System.currentTimeMillis()); // TODO : double check
        System.out.println("job = " + job);
        jobRepository.save(job);
    }

    public void updateJob(JobUpdateDto jobUpdateDto) {

        log.info("added  jobUpdateDto = " + jobUpdateDto);
        if (!jobRepository.findById(jobUpdateDto.getId()).isPresent()) {
            log.warn("Job NOT existed, id = " + jobUpdateDto.getId());
        }
        Job currentJob = jobRepository.findById(jobUpdateDto.getId()).get();
        currentJob.setState(jobUpdateDto.getState());
        currentJob.setStartTime(jobUpdateDto.getStartTime());
        currentJob.setEndTime(jobUpdateDto.getEndTime());
        currentJob.setDuration(jobUpdateDto.getDuration());
        // TODO : modify with update method
        log.info("updated currentJob = " + currentJob);
        jobRepository.save(currentJob);
    }

    public void updateAllJobs() {

        String baseUrl = "http://localhost:8081/jobs/overview";
        String url = baseUrl;
        log.info("url = " + url);

        // Create a RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Make the HTTP GET request
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        JobOverviewResponse jobOverviewResponse = JSON.parseObject(responseEntity.getBody(), JobOverviewResponse.class);
        List<JobOverview> jobs = jobOverviewResponse.getJobs();
        log.info(">>> jobOverviewResponse = " + jobOverviewResponse);
        log.info(">>> jobOverviewResponse.getJobOverviewList() = " + jobOverviewResponse.getJobs());
        log.info(">>> jobOverviewResponse len = " + jobOverviewResponse.getJobs().size());

        if (jobs == null || jobs.size() == 0) {
            log.warn("NO job to update");
            return;
        }

        // update job and save to DB
        jobs.stream().forEach(job -> {
            Job currentJob = this.getJobByJid(job.getJid());
            log.info("---> (updateAllJobs) currentJob = " + currentJob.toString());
            currentJob.setStartTime(job.getStartTime());
            currentJob.setEndTime(job.getEndTime());
            currentJob.setState(job.getState());
            log.info("---> (updateAllJobs) currentJob = " + currentJob.toString());
            // will update if record already existed (with PK)
            jobRepository.save(currentJob);
        });
    }

    // TODO : optimize below with mapper (SQL)
    private Job getJobByJid(String jid) {

        List<Job> jobs = jobRepository.findAll();
        for (Job job : jobs) {
            if (job.getJobId().equals(jid)) {
                return job;
            }
        }
        log.warn("No Job with jid = " + jid);
        return null;
    }

}
