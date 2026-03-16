package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Common.ApiResponse;
import com.yen.FlinkRestService.Service.JobService;
import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.dto.job.CancelJobDto;
import com.yen.FlinkRestService.model.dto.job.JobSubmitDto;
import com.yen.FlinkRestService.model.dto.job.JobUpdateDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<Job> getJobById(@PathVariable Integer jobId) {
        Job job = jobService.getJobById(jobId);
        return ResponseEntity.ok(job);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> submitJob(@Valid @RequestBody JobSubmitDto jobSubmitDto) {
        jobService.addJob(jobSubmitDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Job has been submitted"));
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<ApiResponse> updateJob(
            @PathVariable Integer jobId,
            @Valid @RequestBody JobUpdateDto jobUpdateDto) {
        jobUpdateDto.setId(jobId);
        jobService.updateJob(jobUpdateDto);
        return ResponseEntity.ok(new ApiResponse(true, "Job has been updated"));
    }

    @PostMapping("/{flinkJobId}/cancel")
    public ResponseEntity<ApiResponse> cancelJob(@PathVariable String flinkJobId) {
        jobService.cancelJob(flinkJobId);
        return ResponseEntity.ok(new ApiResponse(true, "Job has been cancelled"));
    }

    // Legacy endpoints for backward compatibility
    @GetMapping("/")
    public ResponseEntity<List<Job>> getAllJobsLegacy() {
        return getAllJobs();
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addJobLegacy(@Valid @RequestBody JobSubmitDto jobSubmitDto) {
        return submitJob(jobSubmitDto);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateJobLegacy(@Valid @RequestBody JobUpdateDto jobUpdateDto) {
        jobService.updateJob(jobUpdateDto);
        return ResponseEntity.ok(new ApiResponse(true, "Job has been updated"));
    }

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse> cancelJobLegacy(@RequestBody String jobId) {
        jobService.cancelJob(jobId.replace("\"", "").trim());
        return ResponseEntity.ok(new ApiResponse(true, "Job has been cancelled"));
    }
}
