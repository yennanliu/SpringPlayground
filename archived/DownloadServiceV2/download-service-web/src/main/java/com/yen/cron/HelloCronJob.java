//package com.yen.cron;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@Configurable
//@EnableScheduling
//public class HelloCronJob {
//
//    // logger instance
//    private static final Logger logger = LogManager.getLogger(HelloCronJob.class);
//
//    // run every 3 sec
//    @Scheduled(cron = "*/3 * *  * * * ")
//    public void runCron1(){
//        System.out.println(">>> cron1 run ....");
//    }
//
//}
