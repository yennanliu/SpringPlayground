package com.yen.user;

// https://www.youtube.com/watch?v=tF_Y4G1FovQ&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=34

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  ConsumerUserApplication
 *
 *   step 1) import dep
 *   step 2) config dubbo registry url
 *   step 3) call service
 */
@SpringBootApplication
public class ConsumerUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerUserApplication.class, args);
    }
}
