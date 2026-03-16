package com.yen.FlinkRestService.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI 3.0 configuration for Swagger UI.
 * Access Swagger UI at: /swagger-ui.html
 * Access OpenAPI spec at: /v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:9999}")
    private String serverPort;

    @Bean
    public OpenAPI flinkRestServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Flink REST Service API")
                        .description("REST API for managing Apache Flink clusters, jobs, JARs, and Zeppelin notebooks")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Data Platform Team"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local Development Server")
                ));
    }
}
