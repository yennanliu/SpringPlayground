package com.yen.springBootPOC2AdminSystem;

// https://www.youtube.com/watch?v=oi6ChwpC6rc&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=57

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/** String boot app entry point */

@ServletComponentScan("com.yen.springBootPOC2AdminSystem") // scan com.yen.springBootPOC2AdminSystem.servlet.MyServlet
@SpringBootApplication
public class SpringBootPoc2AdminSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootPoc2AdminSystemApplication.class, args);
	}

}
