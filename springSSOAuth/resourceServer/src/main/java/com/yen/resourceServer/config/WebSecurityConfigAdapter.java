package com.yen.resourceServer.config;

// book p.3-44

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class WebSecurityConfigAdapter extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //super.configure(http);
        http.authorizeRequests().antMatchers("/users/**").authenticated();
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        //super.configure(web);
        web.ignoring().antMatchers("/auth/**", "/actuator/health");
    }

}
