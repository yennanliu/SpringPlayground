package com.yen.springSSO;

// book p. 3-14
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-authserver/src/main/java/com/wudimanong/authserver/AuthServer.java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.SessionAttributes;

@EnableDiscoveryClient
@SpringBootApplication
@SessionAttributes("authorizationRequest")
public class SSOApplication {

	public static void main(String[] args) {

		SpringApplication.run(SSOApplication.class, args);
	}

}
