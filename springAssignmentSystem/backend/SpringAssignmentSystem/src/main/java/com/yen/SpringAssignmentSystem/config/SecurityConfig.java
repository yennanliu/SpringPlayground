package com.yen.SpringAssignmentSystem.config;

// https://youtu.be/TOQjvskdl3g?si=5BrFwpBdyynej5ab&t=312

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //super.configure(auth);
        //auth.userDetailsService()
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }

}
