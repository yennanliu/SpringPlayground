package com.yen.FlinkRestService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FlinkRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlinkRestApplication.class, args);
    }
}
