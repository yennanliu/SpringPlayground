package com.yen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.yen.mapper")
@ComponentScan(basePackages = {"com.yen"})
public class DownloadApplicationV2 {
    public static void main(String[] args) {

        SpringApplication.run(DownloadApplicationV2.class, args);
    }

}