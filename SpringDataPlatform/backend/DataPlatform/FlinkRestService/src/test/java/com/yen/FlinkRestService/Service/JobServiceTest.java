package com.yen.FlinkRestService.Service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yen.FlinkRestService.Repository.JobJarRepository;
import com.yen.FlinkRestService.Repository.JobRepository;
import com.yen.FlinkRestService.Service.JobService;
import com.yen.FlinkRestService.Service.RestTemplateService;
import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.JobJar;
import com.yen.FlinkRestService.model.dto.job.JobSubmitDto;
import com.yen.FlinkRestService.model.dto.job.JobUpdateDto;
import com.yen.FlinkRestService.model.response.JobOverview;
import com.yen.FlinkRestService.model.response.JobOverviewResponse;
import com.yen.FlinkRestService.model.response.JobSubmitResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class JobServiceTest {

    @InjectMocks
    private JobService jobService;

    @Mock
    private JobJarRepository jobJarRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private RestTemplateService restTemplateService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddJob() {

        // Mock job jar repository
        JobJar jobJar = new JobJar();
        jobJar.setId(1);
        jobJar.setSavedJarName("MyProgram.jar");
        when(jobJarRepository.findById(1)).thenReturn(Optional.of(jobJar));

        // Mock restTemplateService
        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"jobid\":\"12345\"}", HttpStatus.OK);
        when(restTemplateService.sendPostRequest(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(responseEntity);

        // Create job submit DTO
        JobSubmitDto jobSubmitDto = new JobSubmitDto();
        jobSubmitDto.setJarId(1);

        // Call addJob method
        jobService.addJob(jobSubmitDto);

        // Verify that job was saved
        Mockito.verify(jobRepository).save(any(Job.class));
    }


    @Test
    public void testUpdateJob() {

        // Mock job repository
        Job job = new Job();
        job.setId(1);
        when(jobRepository.findById(1)).thenReturn(Optional.of(job));

        // Create job update DTO
        JobUpdateDto jobUpdateDto = new JobUpdateDto();
        jobUpdateDto.setId(1);
        jobUpdateDto.setState("updated_state");

        // Call updateJob method
        jobService.updateJob(jobUpdateDto);

        // Verify that job was updated
        Mockito.verify(jobRepository).save(any(Job.class));
    }

//    @Test
//    public void testUpdateAllJobs() {
//
//        // Mock the responseEntity
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"jobs\":[{\"jid\":\"12345\",\"name\":\"job_name\",\"start-time\":1234567890,\"end-time\":1234567890,\"duration\":122,\"state\":\"RUNNING\",\"last-modification\":1234567890,\"tasks\":{}}]}", HttpStatus.OK);
//        when(restTemplateService.sendGetRequest(Mockito.anyString())).thenReturn(responseEntity);
//
//        // Mock the parseJobOverviewResponse method
//        JobOverviewResponse jobOverviewResponse = new JobOverviewResponse();
//        jobOverviewResponse.setJobs(Arrays.asList(new JobOverview("12345", "job_name", 1234567890L, 1234567890L, 122, "RUNNING", 1234567890L, new HashMap<>())));
//        when(jobService.parseJobOverviewResponse(Mockito.anyString())).thenReturn(jobOverviewResponse);
//
//        // Call the method under test
//        jobService.updateAllJobs();
//
//        // Verify that jobs were updated
//        Mockito.verify(jobRepository, Mockito.times(1)).save(any(Job.class));
//    }


    @Test
    public void testGetJobByJid() {

        // Mock job repository
        Job job = new Job();
        job.setJobId("12345");
        List<Job> jobs = new ArrayList<>();
        jobs.add(job);
        when(jobRepository.findAll()).thenReturn(jobs);

        // Call getJobByJid method
        Job result = jobService.getJobByJid("12345");

        // Verify that job was retrieved
        assertEquals(job, result);
    }

//    @Test
//    public void testCancelJob() {
//        // Mock restTemplateService
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"message\":\"Job was cancelled.\"}", HttpStatus.OK);
//        when(restTemplateService.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.eq(String.class))).thenReturn(responseEntity);
//
//        // Call cancelJob method
//        jobService.cancelJob("jobID");
//
//        // Verify that cancelJob was successful
//        Mockito.verify(restTemplateService).postForEntity(Mockito.anyString(), Mockito.any(), Mockito.eq(String.class));
//    }


}
