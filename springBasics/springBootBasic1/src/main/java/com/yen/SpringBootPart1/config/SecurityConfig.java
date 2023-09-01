package com.yen.SpringBootPart1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// https://yanbin.blog/springboot-security-jwt-token-how-to-abcs/

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests().antMatchers("/public-api").permitAll()
                .anyRequest().authenticated();
        return httpSecurity.build();
    }
}
