package com.yen.FlinkRestService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableAsync
@EnableScheduling
@EnableSwagger2 // Fix using Swagger 2.x : https://blog.51cto.com/u_15740726/5540690
@SpringBootApplication
public class FlinkRestApplication {
    public static void main(String[] args) {

        SpringApplication.run(FlinkRestApplication.class, args);
    }

}