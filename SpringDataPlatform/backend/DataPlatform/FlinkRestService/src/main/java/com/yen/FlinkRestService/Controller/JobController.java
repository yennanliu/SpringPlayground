package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Common.ApiResponse;
import com.yen.FlinkRestService.Service.JobService;
import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.dto.JobSubmitDto;
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

    @GetMapping("/detail/{jobId}")
    public void testGetJobDetail(@RequestParam("savedJobId") String savedJobId){

        jobService.updateJob(savedJobId);
        //return new ResponseEntity<>("xxx", HttpStatus.OK);
    }

}
