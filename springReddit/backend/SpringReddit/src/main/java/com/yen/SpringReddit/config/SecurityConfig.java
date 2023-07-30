package com.yen.SpringReddit.config;

// https://youtu.be/kpKUMmAmcj0?t=63
// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/master/src/main/java/com/programming/techie/springredditclone/config/SecurityConfig.java

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// TODO : double check if should use WebSecurityConfigurerAdapter or not
public class SecurityConfig extends SecurityConfigurerAdapter {

    public void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf()
                .disable()
                .authorizeRequests()
                .requestMatchers("/api/auth/**")
                .permitAll()
                .anyRequest()
                .authenticated();
    }
}
