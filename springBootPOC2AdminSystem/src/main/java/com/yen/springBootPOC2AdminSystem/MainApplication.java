package com.yen.springBootPOC2AdminSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.yen")
public class MainApplication {
    public static void main(String[] args) {

        System.out.println("-------------------- START");
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);

        System.out.println("-------------------- END");
    }
}
