/**
 * 
 */
package com.waylau.spring.boot.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置类.
 * 
 * @since 1.0.0 2017年7月25日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 自定义配置
	 */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.authorizeRequests()
    		.antMatchers("/css/**", "/js/**", "/fonts/**", "/index").permitAll() // 都可以访问
    		.antMatchers("/users/**").hasRole("ADMIN")   // 需要相应的角色才能访问
			.and()
			.formLogin()   //基于 Form 表单登录验证
			.loginPage("/login").failureUrl("/login-error"); // 自定义登录界面
    }
 
    /**
     * 认证信息管理
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()  // 认证信息存储于内存中
                .withUser("waylau").password("123456").roles("ADMIN");
    }
}
