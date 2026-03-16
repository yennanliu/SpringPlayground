package com.yen.FlinkRestService.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yen.FlinkRestService.Service.JobService;
import com.yen.FlinkRestService.exception.EntityNotFoundException;
import com.yen.FlinkRestService.exception.GlobalExceptionHandler;
import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.dto.job.JobSubmitDto;
import com.yen.FlinkRestService.model.dto.job.JobUpdateDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class JobControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(jobController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllJobs() throws Exception {
        Job job1 = createJob(1, "job-id-1", "Job 1", "RUNNING");
        Job job2 = createJob(2, "job-id-2", "Job 2", "FINISHED");
        List<Job> jobs = Arrays.asList(job1, job2);

        when(jobService.getJobs()).thenReturn(jobs);

        mockMvc.perform(get("/job"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].jobId", is("job-id-1")))
                .andExpect(jsonPath("$[0].state", is("RUNNING")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].state", is("FINISHED")));

        verify(jobService, times(1)).getJobs();
    }

    @Test
    void testGetJobById_Found() throws Exception {
        Job job = createJob(1, "job-id-1", "Test Job", "RUNNING");

        when(jobService.getJobById(1)).thenReturn(job);

        mockMvc.perform(get("/job/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.jobId", is("job-id-1")))
                .andExpect(jsonPath("$.name", is("Test Job")))
                .andExpect(jsonPath("$.state", is("RUNNING")));

        verify(jobService, times(1)).getJobById(1);
    }

    @Test
    void testGetJobById_NotFound() throws Exception {
        when(jobService.getJobById(999))
                .thenThrow(new EntityNotFoundException("Job", 999));

        mockMvc.perform(get("/job/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)));

        verify(jobService, times(1)).getJobById(999);
    }

    @Test
    void testSubmitJob() throws Exception {
        JobSubmitDto dto = new JobSubmitDto();
        dto.setJarId(1);
        dto.setEntryClass("com.example.Main");
        dto.setParallelism(2);

        Job createdJob = createJob(1, "new-job-id", "New Job", "CREATED");
        when(jobService.addJob(any(JobSubmitDto.class))).thenReturn(createdJob);

        mockMvc.perform(post("/job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("submitted")));

        verify(jobService, times(1)).addJob(any(JobSubmitDto.class));
    }

    @Test
    void testSubmitJob_ValidationError() throws Exception {
        JobSubmitDto dto = new JobSubmitDto();
        // Missing required jarId

        mockMvc.perform(post("/job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateJob() throws Exception {
        JobUpdateDto dto = new JobUpdateDto();
        dto.setId(1);
        dto.setState("RUNNING");

        Job updatedJob = createJob(1, "job-id-1", "Updated Job", "RUNNING");
        when(jobService.updateJob(any(JobUpdateDto.class))).thenReturn(updatedJob);

        mockMvc.perform(put("/job/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("updated")));

        verify(jobService, times(1)).updateJob(any(JobUpdateDto.class));
    }

    @Test
    void testCancelJob() throws Exception {
        doNothing().when(jobService).cancelJob("flink-job-id-123");

        mockMvc.perform(post("/job/flink-job-id-123/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("cancelled")));

        verify(jobService, times(1)).cancelJob("flink-job-id-123");
    }

    @Test
    void testGetAllJobsLegacy() throws Exception {
        Job job = createJob(1, "job-id-1", "Job 1", "RUNNING");
        when(jobService.getJobs()).thenReturn(Arrays.asList(job));

        mockMvc.perform(get("/job/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(jobService, times(1)).getJobs();
    }

    private Job createJob(Integer id, String jobId, String name, String state) {
        Job job = new Job();
        job.setId(id);
        job.setJobId(jobId);
        job.setName(name);
        job.setState(state);
        job.setStartTime(System.currentTimeMillis());
        return job;
    }
}
