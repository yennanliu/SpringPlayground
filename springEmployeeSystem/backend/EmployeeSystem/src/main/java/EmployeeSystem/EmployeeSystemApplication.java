package EmployeeSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// no need to add annotation for swagger 3, swagger UI can be accessed via below
// http://localhost:9998/webjars/swagger-ui/index.html
@SpringBootApplication
public class EmployeeSystemApplication {

  public static void main(String[] args) {

    SpringApplication.run(EmployeeSystemApplication.class, args);
  }
}
