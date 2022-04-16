package com.yen.SpeingBootPOC1;

// https://www.youtube.com/watch?v=N7dsPcsmxM4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=6
// https://www.youtube.com/watch?v=dJIksiVQDj4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=8

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *  Main application : entry point
 *
 *  let Spring knows that this script is a spring application
 */

@SpringBootApplication(scanBasePackages = "com.yen")
public class MainApplication {

    public static void main(String[] args) {

        // 1, return IOC container
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);

        // 2. check above container's content
        String[] names = run.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }

        //SpringApplication.run(MainApplication.class, args);
    }
}
