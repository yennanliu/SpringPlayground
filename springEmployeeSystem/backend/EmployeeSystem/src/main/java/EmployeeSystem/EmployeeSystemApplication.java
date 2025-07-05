package EmployeeSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2 // Fix using Swagger 2.x : https://blog.51cto.com/u_15740726/5540690
@EnableAsync    // Enable async processing
@SpringBootApplication
public class EmployeeSystemApplication {

  public static void main(String[] args) {

    SpringApplication.run(EmployeeSystemApplication.class, args);
  }
}
