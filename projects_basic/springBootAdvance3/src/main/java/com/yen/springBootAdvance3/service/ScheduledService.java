package com.yen.springBootAdvance3.service;

// https://www.youtube.com/watch?v=NZ39FYUS_5U&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=25

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/** Scheduled Service demo */

@Service
public class ScheduledService {

    /**
     *  1) pattern: second, minute, hour, day of month, month, day of week
     *
     *  2) example :
     *     0 * * * * MON-FRI
     *
     *  3) have to add @EnableScheduling for enabling scheduling
     *
     */
    //@Scheduled(cron = "0 * * * * MON-FRI")
    //@Scheduled(cron = "0,1,2,3,4 * * * * MON-FRI")
    //@Scheduled(cron = "0-4 * * * * MON-FRI")
    @Scheduled(cron = "0/4 * * * * MON-FRI")  // run every 4 sec
    public void hello(){
        System.out.println(">>> hello from cronjob !!!");
    }

}
