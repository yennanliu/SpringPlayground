package com.yen.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.yen.web.mapper")
public class DownloadApp {
    public static void main(String[] args) {

        SpringApplication.run(DownloadApp.class, args);
    }

}