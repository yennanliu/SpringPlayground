package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.JobJarRepository;
import com.yen.FlinkRestService.Repository.JobRepository;
import com.yen.FlinkRestService.exception.EntityNotFoundException;
import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.JobJar;
import com.yen.FlinkRestService.model.dto.job.JobSubmitDto;
import com.yen.FlinkRestService.model.dto.job.JobUpdateDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private JobJarRepository jobJarRepository;

    @Mock
    private RestTemplateService restTemplateService;

    private JobService jobService;

    @BeforeEach
    void setUp() {
        jobService = new JobService(jobRepository, jobJarRepository, restTemplateService);
        ReflectionTestUtils.setField(jobService, "flinkBaseUrl", "http://localhost:8081");
    }

    @Test
    void testGetJobs() {
        Job job1 = new Job();
        job1.setId(1);
        job1.setJobId("job-1");

        Job job2 = new Job();
        job2.setId(2);
        job2.setJobId("job-2");

        when(jobRepository.findAll()).thenReturn(Arrays.asList(job1, job2));

        List<Job> result = jobService.getJobs();

        assertEquals(2, result.size());
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void testGetJobById_Found() {
        Job job = new Job();
        job.setId(1);
        job.setJobId("job-1");

        when(jobRepository.findById(1)).thenReturn(Optional.of(job));

        Job result = jobService.getJobById(1);

        assertEquals(job, result);
        verify(jobRepository, times(1)).findById(1);
    }

    @Test
    void testGetJobById_NotFound() {
        when(jobRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            jobService.getJobById(999);
        });

        verify(jobRepository, times(1)).findById(999);
    }

    @Test
    void testGetJobByJid() {
        Job job = new Job();
        job.setId(1);
        job.setJobId("flink-job-12345");

        when(jobRepository.findByJobId("flink-job-12345")).thenReturn(Optional.of(job));

        Optional<Job> result = jobService.getJobByJid("flink-job-12345");

        assertTrue(result.isPresent());
        assertEquals("flink-job-12345", result.get().getJobId());
        verify(jobRepository, times(1)).findByJobId("flink-job-12345");
    }

    @Test
    void testAddJob() {
        JobJar jobJar = new JobJar();
        jobJar.setId(1);
        jobJar.setSavedJarName("MyProgram.jar");

        when(jobJarRepository.findById(1)).thenReturn(Optional.of(jobJar));

        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"jobid\":\"flink-12345\"}", HttpStatus.OK);
        when(restTemplateService.sendPostRequest(anyString(), anyString(), any(MediaType.class))).thenReturn(responseEntity);

        Job savedJob = new Job();
        savedJob.setId(1);
        savedJob.setJobId("flink-12345");
        when(jobRepository.save(any(Job.class))).thenReturn(savedJob);

        JobSubmitDto jobSubmitDto = new JobSubmitDto();
        jobSubmitDto.setJarId(1);

        Job result = jobService.addJob(jobSubmitDto);

        assertNotNull(result);
        assertEquals("flink-12345", result.getJobId());
        verify(jobJarRepository, times(1)).findById(1);
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void testAddJob_JarNotFound() {
        when(jobJarRepository.findById(999)).thenReturn(Optional.empty());

        JobSubmitDto jobSubmitDto = new JobSubmitDto();
        jobSubmitDto.setJarId(999);

        assertThrows(EntityNotFoundException.class, () -> {
            jobService.addJob(jobSubmitDto);
        });

        verify(jobJarRepository, times(1)).findById(999);
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    void testUpdateJob() {
        Job existingJob = new Job();
        existingJob.setId(1);
        existingJob.setJobId("job-1");

        when(jobRepository.findById(1)).thenReturn(Optional.of(existingJob));
        when(jobRepository.save(any(Job.class))).thenReturn(existingJob);

        JobUpdateDto jobUpdateDto = new JobUpdateDto();
        jobUpdateDto.setId(1);
        jobUpdateDto.setState("RUNNING");
        jobUpdateDto.setStartTime(System.currentTimeMillis());

        Job result = jobService.updateJob(jobUpdateDto);

        assertEquals("RUNNING", result.getState());
        verify(jobRepository, times(1)).findById(1);
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void testUpdateJob_NotFound() {
        when(jobRepository.findById(999)).thenReturn(Optional.empty());

        JobUpdateDto jobUpdateDto = new JobUpdateDto();
        jobUpdateDto.setId(999);

        assertThrows(EntityNotFoundException.class, () -> {
            jobService.updateJob(jobUpdateDto);
        });

        verify(jobRepository, times(1)).findById(999);
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    void testCancelJob() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("{}", HttpStatus.OK);
        when(restTemplateService.sendPostRequest(anyString(), anyString(), any(MediaType.class))).thenReturn(responseEntity);

        assertDoesNotThrow(() -> jobService.cancelJob("flink-job-12345"));

        verify(restTemplateService, times(1)).sendPostRequest(anyString(), anyString(), any(MediaType.class));
    }

    @Test
    void testParseJobOverviewResponse() {
        String json = "{\"jobs\":[{\"jid\":\"12345\",\"name\":\"test-job\",\"start-time\":1000,\"end-time\":2000,\"state\":\"RUNNING\"}]}";

        var result = jobService.parseJobOverviewResponse(json);

        assertNotNull(result);
        assertNotNull(result.getJobs());
    }
}
