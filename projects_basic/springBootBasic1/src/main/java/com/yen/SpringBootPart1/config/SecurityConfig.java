package com.yen.SpringBootPart1.config;

import com.yen.SpringBootPart1.filter.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

// https://yanbin.blog/springboot-security-jwt-token-how-to-abcs/

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .authorizeHttpRequests()
                .antMatchers("/public-api").permitAll() // ONLY API with permitAll can be accessed without auth
                //.antMatchers("/private-api").permitAll()
                .anyRequest().hasRole("contributors"); // only role "contributors" can be authorized to access /public-api endpoint
                //.anyRequest().authenticated(); // can enable this, comment above, make all requests being auto authenticated

        // return Http error 403, instead of 401 if JWT token is not provided
        httpSecurity.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        httpSecurity.addFilterBefore(new JwtTokenFilter(), AuthorizationFilter.class);

        // Not let spring boot manage session
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return httpSecurity.build();
    }
}
