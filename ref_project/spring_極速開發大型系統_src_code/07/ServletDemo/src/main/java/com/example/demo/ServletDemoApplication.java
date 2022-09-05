package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@ServletComponentScan
@SpringBootApplication
public class ServletDemoApplication {

    /**
     * Description: 登錄一個bean 物件
     */
    @Bean
    public ServletRegistrationBean ServletDemo01() {
/**
 * Description: 登錄ServletRegistrationBean獲得對應的控制
 */

        return new ServletRegistrationBean(new ServletDemo01(), "/ServletDemo01/*");
    }
    public static void main(String[] args) {
        SpringApplication.run(ServletDemoApplication.class, args);
    }

}
