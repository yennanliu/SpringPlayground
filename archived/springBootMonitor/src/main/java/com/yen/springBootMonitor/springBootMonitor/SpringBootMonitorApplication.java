package com.yen.springBootMonitor.springBootMonitor;

// https://www.youtube.com/watch?v=yFI4QwPZMew&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=81

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class SpringBootMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMonitorApplication.class, args);
	}

}
