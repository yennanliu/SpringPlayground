﻿package com.example.demo.config;


import com.example.demo.service.SysUser.SysSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 指定為Spring Security組態類別
 */
@Configuration
/**
 *  啟用方法安全設定
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
/**
 * @author longzhonghua
 * @data 2018/11/04 22:30
 */
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailHandler;

    @Bean
    /**
     * 使用 BCrypt 加密
     */
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/admin/**").
                //指定登入認證的Controller
                        formLogin().usernameParameter("uname").passwordParameter("pwd").loginPage("/admin/login").successHandler(
                myAuthenticationSuccessHandler).failureHandler(myAuthenticationFailHandler)
                .and()
                .authorizeRequests()
                //登入關聯
                .antMatchers("/admin/login", "/admin/role", "/admin/user").permitAll()
                //rabc關聯
                // .antMatchers("/admin/rbac").access("@rbacService.hasPermission(request,authentication)")
                //.antMatchers("/admin/**").access("hasRole('ADMIN') or @rbacService.hasPermission(request,authentication)")
                .antMatchers("/redis/**").permitAll()
        // .anyRequest().access("@rbacService.hasPermission(request,authentication)")
        ;
        http.logout().logoutUrl("/admin/logout").permitAll();
        //記住我功能
        http.rememberMe().rememberMeParameter("rememberme");
        http.csrf().ignoringAntMatchers("/admin/upload");
        //解決X-Frame-Options deny 造成的頁面空白,不然背景不能用frame
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    UserDetailsService Service() {
        return new SysSecurityService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(Service()).passwordEncoder(new BCryptPasswordEncoder() {
        });
    }
}