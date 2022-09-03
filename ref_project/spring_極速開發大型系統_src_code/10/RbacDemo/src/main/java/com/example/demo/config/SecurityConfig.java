package com.example.demo.config;
import com.example.demo.Service.SysUser.SysSecurityService;
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
 * @author longzhonghua
 * @data 2018/11/04 22:30
 */
@Configuration//指定為組態類別
@EnableWebSecurity//指定為Spring Security組態類別
@EnableGlobalMethodSecurity(prePostEnabled = true) // 啟用方法安全設定
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailHander;
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();   // 使用 BCrypt 加密
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/admin/**").//這裡必須要加,不然走這裡了,就不進行下面的幾個驗證了
                    //指定登入認證的Controller
                            formLogin().usernameParameter("uname").passwordParameter("pwd").loginPage("/admin/login").successHandler(
                    myAuthenticationSuccessHandler).failureHandler(myAuthenticationFailHander)
                    .and()
                    .authorizeRequests()
                    //登入關聯
                    .antMatchers("/admin/login").permitAll()
                    //  .antMatchers("/admin/**").permitAll()//.hasRole("ADMIN")
                    //rabc關聯
                    .antMatchers("/admin/rbac").access("@rbacService.hasPermission(request,authentication)")

                    .antMatchers("/redis/**").permitAll();

            http.logout().logoutUrl("/admin/logout").permitAll();
            http.rememberMe().rememberMeParameter("rememberme");//記住我功能
            http.csrf().ignoringAntMatchers("/admin/upload");
            http.headers().frameOptions().sameOrigin();//解決X-Frame-Options deny 造成的頁面空白,不然背景不能用frame
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

