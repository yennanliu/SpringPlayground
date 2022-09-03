package com.example.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;



/*
 * @EnableGlobalMethodSecurity(securedEnabled=true)
 * 開啟@Secured 註釋過濾權限
 *
 * @EnableGlobalMethodSecurity(jsr250Enabled=true)
 * 開啟@RolesAllowed 註釋過濾權限
 *
 * @EnableGlobalMethodSecurity(prePostEnabled=true)
 * 使用表達式時間方法等級的安全性 4個註釋可用
 * -@PreAuthorize 在方法呼叫之前,基於表達式的計算結果來限制對方法的存取
 * -@PostAuthorize 容許方法呼叫,但是若果表達式計算結果為false,將拋出一個安全性例外
 * -@PostFilter 容許方法呼叫,但必須按照表達式來過濾方法的結果
 * -@PreFilter 容許方法呼叫,但必須在進入方法之前過濾輸入值
 **/
@Configuration
@EnableWebSecurity//指定為Spring Security組態類別
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/*", "/user/*").permitAll()

                .anyRequest().authenticated()
                .and()
                .logout().permitAll();
    }



}
