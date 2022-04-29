package com.yen.crud;

// https://www.baeldung.com/spring-boot-crud-thymeleaf
// https://github.com/eugenp/tutorials/blob/master/spring-boot-modules/spring-boot-crud/src/main/java/com/baeldung/crud/Application.java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.yen.crud"})
@EnableJpaRepositories(basePackages="com.yen.crud.repositories")
@EnableTransactionManagement
@EntityScan(basePackages="com.yen.crud.entities")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}