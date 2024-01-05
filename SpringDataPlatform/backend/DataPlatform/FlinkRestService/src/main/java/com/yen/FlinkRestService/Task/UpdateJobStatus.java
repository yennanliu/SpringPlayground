//package com.yen.FlinkRestService.Task;
//
//import com.yen.FlinkRestService.Service.JobService;
//import com.yen.FlinkRestService.model.Job;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class UpdateJobStatus {
//
//    @Autowired
//    JobService jobService;
//
////    @Scheduled(cron = "*/10 * * * * *") // every 10 sec
////    public void test(){
////        System.out.println("----------> this is cron test job !!!");
////    }
//
//    @Scheduled(cron = "*/10 * * * * *") // every 10 sec
//    public void updateSubmittedJobStatus(){
//
//        System.out.println("----------> updateSubmittedJobStatus ...");
//
//        // get all jid (job id)
//        List<String> JobIds = jobService.getJobs().stream().map(job -> {
//            return job.getJobId();
//        }).collect(Collectors.toList());
//
//        jobService.updateAllJobs();
//    }
//
//}
