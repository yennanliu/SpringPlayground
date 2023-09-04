package com.yen.mdblog.config;

// social login : https://youtu.be/us0VjFiHogo?t=241

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class OAuthSecurityConfig {

    // in memory user
    // https://www.youtube.com/watch?v=66DtzkhBlSA&t=515s
    // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/in-memory.html
    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails user = User
                .withUsername("user1")
                .password("{noop}123")
                .authorities("READ", "ROLE_USER")
                .build();

        UserDetails admin = User
                .withUsername("admin")
                .password("{noop}123")
                .authorities("READ", "ROLE_USER")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    // social login : https://youtu.be/us0VjFiHogo?t=241
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity = httpSecurity
                .csrf()
                .disable()
                .cors()
                .disable();

        return httpSecurity
                .authorizeRequests(auth -> {
                    auth.anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())
                .build();
    }

}
