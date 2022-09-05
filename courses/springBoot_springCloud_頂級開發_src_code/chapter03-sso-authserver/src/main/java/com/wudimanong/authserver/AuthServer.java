package com.wudimanong.authserver;

import com.wudimanong.authserver.client.ResourceServerClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * @author joe
 */
@EnableDiscoveryClient
@SpringBootApplication
@SessionAttributes("authorizationRequest")
@EnableFeignClients(basePackageClasses = ResourceServerClient.class)
public class AuthServer {

    public static void main(String[] args) {
        SpringApplication.run(AuthServer.class, args);
    }
}
