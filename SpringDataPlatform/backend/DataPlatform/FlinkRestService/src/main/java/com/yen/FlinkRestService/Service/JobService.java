package com.yen.FlinkRestService.Service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.yen.FlinkRestService.Repository.JobJarRepository;
import com.yen.FlinkRestService.Repository.JobRepository;
import com.yen.FlinkRestService.exception.EntityNotFoundException;
import com.yen.FlinkRestService.exception.ExternalServiceException;
import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.JobJar;
import com.yen.FlinkRestService.model.dto.job.JobSubmitDto;
import com.yen.FlinkRestService.model.dto.job.JobUpdateDto;
import com.yen.FlinkRestService.model.response.JobOverview;
import com.yen.FlinkRestService.model.response.JobOverviewResponse;
import com.yen.FlinkRestService.model.response.JobSubmitResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final JobJarRepository jobJarRepository;
    private final RestTemplateService restTemplateService;

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
            .create();

    @Value("${flink.base_url}")
    private String flinkBaseUrl;

    @Transactional(readOnly = true)
    public List<Job> getJobs() {
        return jobRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Job getJobById(Integer jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job", jobId));
    }

    @Transactional(readOnly = true)
    public Optional<Job> getJobByJid(String jid) {
        return jobRepository.findByJobId(jid);
    }

    @Transactional
    public Job addJob(JobSubmitDto jobSubmitDto) {
        log.info("Submitting job with jarId={}", jobSubmitDto.getJarId());

        JobJar jobJar = jobJarRepository.findById(jobSubmitDto.getJarId())
                .orElseThrow(() -> new EntityNotFoundException("JobJar", jobSubmitDto.getJarId()));

        String url = UriComponentsBuilder.fromHttpUrl(flinkBaseUrl)
                .pathSegment("jars", jobJar.getSavedJarName(), "run")
                .build()
                .toUriString();

        log.info("Submitting job to url={}", url);

        ResponseEntity<String> responseEntity = restTemplateService.sendPostRequest(url, "", MediaType.APPLICATION_JSON);

        JobSubmitResponse jobSubmitResponse = GSON.fromJson(responseEntity.getBody(), JobSubmitResponse.class);
        if (jobSubmitResponse == null || jobSubmitResponse.getJobid() == null) {
            throw new ExternalServiceException("Flink", "Invalid or empty response from job submit endpoint");
        }
        log.info("Job submitted successfully, flinkJobId={}", jobSubmitResponse.getJobid());

        Job job = new Job();
        job.setJobId(jobSubmitResponse.getJobid());
        job.setName(jobJar.getId() + "-" + jobJar.getSavedJarName());
        job.setStartTime(System.currentTimeMillis());

        Job savedJob = jobRepository.save(job);
        log.info("Job saved to database, id={}", savedJob.getId());
        return savedJob;
    }

    @Transactional
    public Job updateJob(JobUpdateDto jobUpdateDto) {
        log.info("Updating job id={}", jobUpdateDto.getId());

        Job job = jobRepository.findById(jobUpdateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Job", jobUpdateDto.getId()));

        job.setState(jobUpdateDto.getState());
        job.setStartTime(jobUpdateDto.getStartTime());
        job.setEndTime(jobUpdateDto.getEndTime());
        job.setDuration(jobUpdateDto.getDuration());

        Job updatedJob = jobRepository.save(job);
        log.info("Job updated successfully, id={}", updatedJob.getId());
        return updatedJob;
    }

    @Transactional
    public void updateAllJobs() {
        String url = UriComponentsBuilder.fromHttpUrl(flinkBaseUrl)
                .pathSegment("jobs", "overview")
                .build()
                .toUriString();

        log.info("Fetching job overview from url={}", url);

        try {
            ResponseEntity<String> responseEntity = restTemplateService.sendGetRequest(url);
            JobOverviewResponse jobOverviewResponse = parseJobOverviewResponse(responseEntity.getBody());

            if (jobOverviewResponse == null || jobOverviewResponse.getJobs() == null) {
                log.warn("Empty or unparseable response from Flink job overview");
                return;
            }

            List<JobOverview> jobs = jobOverviewResponse.getJobs();
            if (jobs.isEmpty()) {
                log.info("No jobs to update");
                return;
            }

            log.info("Updating {} jobs from Flink cluster", jobs.size());

            for (JobOverview jobOverview : jobs) {
                updateOrCreateJob(jobOverview);
            }

        } catch (ExternalServiceException e) {
            log.warn("Failed to fetch job overview from Flink cluster: {}", e.getMessage());
        }
    }

    private void updateOrCreateJob(JobOverview jobOverview) {
        Optional<Job> existingJob = jobRepository.findByJobId(jobOverview.getJid());

        Job job;
        if (existingJob.isPresent()) {
            job = existingJob.get();
        } else {
            log.info("Creating new job record for jid={}", jobOverview.getJid());
            job = new Job();
            job.setJobId(jobOverview.getJid());
        }

        job.setStartTime(jobOverview.getStartTime());
        job.setEndTime(jobOverview.getEndTime());
        job.setState(jobOverview.getState());

        if (job.getName() == null || job.getName().isEmpty()) {
            job.setName(jobOverview.getName());
        }

        jobRepository.save(job);
    }

    @Transactional
    public void cancelJob(String jobId) {
        log.info("Cancelling job with flinkJobId={}", jobId);

        // Flink cancel API: PATCH /jobs/{jobId}  body: {"target-state":"canceled"}
        // POST /jobs/{id}/stop is a savepoint-based graceful stop, not an immediate cancel.
        String url = UriComponentsBuilder.fromHttpUrl(flinkBaseUrl)
                .pathSegment("jobs", jobId)
                .build()
                .toUriString();

        ResponseEntity<String> responseEntity = restTemplateService.sendPatchRequest(
                url, "{\"target-state\":\"canceled\"}", MediaType.APPLICATION_JSON);
        log.info("Job cancellation response status={}", responseEntity.getStatusCode());

        jobRepository.findByJobId(jobId).ifPresent(job -> {
            job.setState("CANCELED");
            jobRepository.save(job);
            log.info("Job state updated to CANCELED in DB, flinkJobId={}", jobId);
        });
    }

    public JobOverviewResponse parseJobOverviewResponse(String responseBody) {
        return GSON.fromJson(responseBody, JobOverviewResponse.class);
    }
}
