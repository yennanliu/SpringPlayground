package com.yen.springProfilesChangeDemo.springProfilesChangeDemo;

// https://www.youtube.com/watch?v=Eic1OfY_1ZY&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=83

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

@SpringBootApplication
public class SpringProfilesChangeDemoApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext run =  SpringApplication.run(SpringProfilesChangeDemoApplication.class, args);

		ConfigurableEnvironment environment = run.getEnvironment();

		Map<String, Object>  systemEnvironment = environment.getSystemEnvironment();

		Map<String, Object>  systemProperties = environment.getSystemProperties();

		System.out.println(">>> systemEnvironment = " + systemEnvironment);
		System.out.println(">>> systemProperties = " + systemProperties);
	}

}
