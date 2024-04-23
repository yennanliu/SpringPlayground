package com.yen.FlinkRestService.Service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.yen.FlinkRestService.Repository.JobJarRepository;
import com.yen.FlinkRestService.Repository.JobRepository;
import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.JobJar;
import com.yen.FlinkRestService.model.dto.job.JobSubmitDto;
import com.yen.FlinkRestService.model.dto.job.JobUpdateDto;
import com.yen.FlinkRestService.model.response.JobOverview;
import com.yen.FlinkRestService.model.response.JobOverviewResponse;
import com.yen.FlinkRestService.model.response.JobSubmitResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    JobJarRepository jobJarRepository;

    @Autowired
    private RestTemplateService restTemplateService;

    @Value("${flink.base_url}")
    private String BASE_URL; // "http://localhost:8081/";

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
        /**
         *  example :
         *
         *  1) POST: /jars/MyProgram.jar/run?savepointPath=/my-savepoints/savepoint-1bae02a80464&allowNonRestoredState=true
         *
         *  2) "http://localhost:8081/jars/{projectId}/run";
         */
        String baseUrl = BASE_URL + "/jars/";
        // TODO : fix below to send entry-class, parallelism to flink
        if (!jobJarRepository.findById(jobSubmitDto.getJarId()).isPresent()){
            throw new RuntimeException("Job jar NOT exists, Jar ID = " + jobSubmitDto.getJarId());
        }
        JobJar jobJar = jobJarRepository.findById(jobSubmitDto.getJarId()).get();
        String url = baseUrl + jobJar.getSavedJarName() + "/run"; // + "entry-class=" + jobSubmitDto.getEntryClass();
        System.out.println("url = " + url);

        // Set request body
        String requestBody = "";

        // Make HTTP POST request
        ResponseEntity<String> responseEntity = restTemplateService.sendPostRequest(url, requestBody, MediaType.APPLICATION_JSON);

        // Print the response status code and body : https://www.runoob.com/w3cnote/fastjson-intro.html
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();
        JobSubmitResponse jobSubmitResponse  = gson.fromJson(responseEntity.getBody(), JobSubmitResponse.class);
        log.info("Response Status Code: " + responseEntity.getStatusCode());
        log.info("jobSubmitResponse : " + jobSubmitResponse.toString());

        // save to DB
        Job job = new Job();
        job.setJobId(jobSubmitResponse.getJobid());
        job.setName(jobJar.getId() + "-" + jobJar.getSavedJarName());
        job.setStartTime( System.currentTimeMillis()); // TODO : double check
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

        String url = BASE_URL + "/jobs/overview"; //"http://localhost:8081/jobs/overview";;
        log.info("url = " + url);

        // Make HTTP GET request
        ResponseEntity<String> responseEntity = restTemplateService.sendGetRequest(url);
        // gson transform with json string name with dash (e.g. start-time) to java object
        // https://github.com/google/gson/blob/main/UserGuide.md#json-field-naming-support
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();
        JobOverviewResponse jobOverviewResponse = gson.fromJson(responseEntity.getBody(), JobOverviewResponse.class);
        List<JobOverview> jobs = jobOverviewResponse.getJobs();

        log.info(">>> jobOverviewResponse = " + jobOverviewResponse);
        log.info(">>> jobOverviewResponse.getJobOverviewList() = " + jobOverviewResponse.getJobs());

        if (jobs == null || jobs.size() == 0) {
            log.warn("NO job to update");
            return;
        }

        // update job and save to DB
        jobs.stream().forEach(job -> {
            Job currentJob = this.getJobByJid(job.getJid());
            if (currentJob == null){
                log.warn("Can't find job in DB, jid = " +  job.getJid(), " save new job to DB");
                Job newJob = new Job();
                newJob.setJobId(job.getJid());
                jobRepository.save(newJob);
                currentJob = newJob;
            }
            currentJob.setStartTime(job.getStartTime());
            currentJob.setEndTime(job.getEndTime());
            currentJob.setState(job.getState());
            if (currentJob.getName() == null || currentJob.getName().length() == 0){
                currentJob.setName(job.getName());
            }
            // will update if record already existed (primary key)
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

    public void cancelJob(String jobID) {

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

        // curl http://localhost:8081/jobs/6e80fe182c310a484bf7e9d4f25ac18d/cancel
        String url = BASE_URL + "/jobs/" + jobID + "/stop";
        log.info("url = " + url);

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
        String resp = gson.fromJson(responseEntity.getBody(), String.class);

        log.info("Response Status Code: " + responseEntity.getStatusCode());
        System.out.println("resp : " + resp);
    }

}
