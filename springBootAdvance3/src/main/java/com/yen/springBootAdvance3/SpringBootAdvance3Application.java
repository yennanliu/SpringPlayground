package com.yen.springBootAdvance3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync  // NOTE !!! we NEED to enable async here (app program)
@SpringBootApplication
public class SpringBootAdvance3Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdvance3Application.class, args);
	}

}
