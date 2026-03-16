package com.yen.FlinkRestService.Task;

import com.yen.FlinkRestService.Service.JobService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateJobStatus {

    private final JobService jobService;

    @Scheduled(cron = "${job.status.update.cron:*/10 * * * * *}")
    public void updateSubmittedJobStatus() {
        log.debug("Starting scheduled job status update...");

        try {
            jobService.updateAllJobs();
            log.debug("Scheduled job status update completed");
        } catch (Exception e) {
            log.error("Failed to update job status: {}", e.getMessage(), e);
        }
    }
}
