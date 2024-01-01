package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Common.ApiResponse;
import com.yen.FlinkRestService.Controller.dto.UploadJarDto;
import com.yen.FlinkRestService.Service.JobService;
import com.yen.FlinkRestService.model.Job;
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

    /**
     *  curl cmd :
     *
     *  curl -X POST -H "Expect:" -F "jarfile=@table/StreamSQLExample.jar" http://localhost:8081/jars/upload
     *
     */
    @PostMapping("/add_jar")
    public ResponseEntity<ApiResponse> addJobJar(@RequestBody UploadJarDto uploadJarDto){

        jobService.addJobJar(uploadJarDto);
        return new ResponseEntity<>(new ApiResponse(true, "Job jar has been added"), HttpStatus.CREATED);
    }

}
