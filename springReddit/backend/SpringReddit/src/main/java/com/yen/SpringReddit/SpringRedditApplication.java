package com.yen.SpringReddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

//@SpringBootApplication
// disable spring security for now
//@EnableAsync // https://youtu.be/PMr2Mh9xzm4?t=305
@ComponentScan(basePackages={
"com.yen.SpringReddit.mapper",
"com.yen.SpringReddit.security"}
)
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SpringRedditApplication {

	public static void main(String[] args) {
		SpringApplication.run(
				SpringRedditApplication.class, args);
	}

}
