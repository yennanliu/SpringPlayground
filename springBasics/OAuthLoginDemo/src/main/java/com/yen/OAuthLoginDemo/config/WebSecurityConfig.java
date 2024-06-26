package com.yen.OAuthLoginDemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurity;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// https://waynestalk.com/spring-security-oauth2-google-signin-explained/

@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests() // 定義哪些url需要被保護
                .antMatchers("/login.html").permitAll() // change login page to login.html
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .loginPage("/login.html")
                .defaultSuccessUrl("/success.html");
    }

}

