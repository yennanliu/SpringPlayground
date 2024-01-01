package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Service.JobService;
import com.yen.FlinkRestService.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
