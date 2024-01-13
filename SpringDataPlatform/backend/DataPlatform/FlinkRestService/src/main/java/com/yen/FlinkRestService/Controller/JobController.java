package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Common.ApiResponse;
import com.yen.FlinkRestService.Service.JobService;
import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.dto.job.JobSubmitDto;
import com.yen.FlinkRestService.model.dto.job.JobUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    JobService jobService;

    @GetMapping("/")
    public ResponseEntity<List<Job>> getAllJobs(){

        List<Job> jobs = jobService.getJobs();
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<Job> getJobByJobId(@PathVariable("jobId") Integer jobId){

        Job job = jobService.getJobById(jobId);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }


    // https://blog.csdn.net/weixin_41062002/article/details/106398295
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addJob(@RequestBody JobSubmitDto jobSubmitDto){

        jobService.addJob(jobSubmitDto);
        return new ResponseEntity<>(new ApiResponse(true, "Job has been added"), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateJob(@RequestBody JobUpdateDto jobUpdateDto) {

        jobService.updateJob(jobUpdateDto);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Job has been updated"), HttpStatus.OK);
    }

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse> cancelJob(@RequestBody String jobID) {

        jobService.cancelJob(jobID);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Job has been cancelled"), HttpStatus.OK);
    }

}
