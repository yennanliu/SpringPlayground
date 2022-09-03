package com.wudimanong.authserver.config;

import com.wudimanong.authserver.config.provider.UserNameAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author jiangqiao
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 授权用户Service处理类
     */
    @Autowired
    UserDetailsService baseUserDetailService;

    /**
     * 安全路径过滤
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/fonts/**", "/icon/**", "/images/**", "/favicon.ico");
    }

    /**
     * 放开部分认证授权入口服务的访问限制
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/check_token").and().authorizeRequests()
                .anyRequest().authenticated().and().formLogin().loginPage("/login").failureUrl("/login-error")
                .permitAll();
        http.csrf().disable();
    }

    /**
     * 认证管理配置
     *
     * @param auth
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /**
     * 授权用户信息数据库提供者对象配置
     *
     * @return
     */
    @Bean
    public AbstractUserDetailsAuthenticationProvider daoAuthenticationProvider() {
        UserNameAuthenticationProvider authProvider = new UserNameAuthenticationProvider();
        // 设置userDetailsService
        authProvider.setUserDetailsService(baseUserDetailService);
        // 禁止隐藏用户未找到异常
        authProvider.setHideUserNotFoundExceptions(false);
        // 使用BCrypt进行密码的hash
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(6));
        return authProvider;
    }
}
