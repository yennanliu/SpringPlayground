package com.yen.mdblog.config;

// https://ithelp.ithome.com.tw/articles/10250502

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 設置角色定義
        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder()) // password encoding : // https://blog.csdn.net/qyb19970829/article/details/110006513
                .withUser("admin")
                .password(new BCryptPasswordEncoder().encode("123"))
                .roles("ADMIN", "USER") // 擁有ADMIN 與 USER角色
                .and()
                .withUser("user")
                .password(new BCryptPasswordEncoder().encode("123"))
                .roles("USER");// 擁有USER角色
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() // 定義哪些url需要被保護
                .antMatchers("/user").hasRole("USER")// 定義匹配到"/user"底下的需要有USER的這個角色才能進去
                .antMatchers("/admin").hasRole("ADMIN") // 定義匹配到"/admin"底下的需要有ADMIN的這個角色才能進去
                .anyRequest().authenticated() // 其他尚未匹配到的url都需要身份驗證
                .and()
                .formLogin()
                .defaultSuccessUrl("/posts/all") // default success url : https://juejin.cn/post/7032566211414065159
                .and()
                .httpBasic();
    }

}
