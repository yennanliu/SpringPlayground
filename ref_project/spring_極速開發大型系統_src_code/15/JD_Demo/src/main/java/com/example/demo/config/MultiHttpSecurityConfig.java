package com.example.demo.config;

import com.example.demo.config.authenticationhandler.member.MemberAuthenticationFailHandler;
import com.example.demo.config.authenticationhandler.member.MemberAuthenticationSuccessHandler;
import com.example.demo.module.filter.JWTAuthorizationFilter;
import com.example.demo.service.SysUser.SysSecurityService;
import com.example.demo.service.jwt.JwtUserSecurityService;
import com.example.demo.service.member.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
//@EnableGlobalAuthentication
public class MultiHttpSecurityConfig {
    @Configuration
    @Order(1)

    public class WebSecurityConfigForAdmin extends WebSecurityConfigurerAdapter {
        @Autowired
        private AuthenticationSuccessHandler myAuthenticationSuccessHandler;
        @Autowired
        private AuthenticationFailureHandler myAuthenticationFailHandler;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();   // 使用 BCrypt 加密
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/admin/**").//這裡必須要加,不然走這裡了,就不進行下面的幾個驗證了
                    //指定登入認證的Controller
                            formLogin().usernameParameter("uname").passwordParameter("pwd").loginPage("/admin/login").successHandler(
                    myAuthenticationSuccessHandler).failureHandler(myAuthenticationFailHandler)
                    .and()
                    .authorizeRequests()
                    //登入關聯
                    .antMatchers("/admin/login").permitAll()
                    //  .antMatchers("/admin/**").permitAll()//.hasRole("ADMIN")
                    //rabc關聯
                    .antMatchers("/admin/**").access("hasRole('ADMIN') or @rbacService.hasPermission(request,authentication)")

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

    @Configuration
    @Order(2)
    public class WebSecurityConfigForUser extends WebSecurityConfigurerAdapter {
        @Autowired
        private MemberAuthenticationSuccessHandler MemberAuthenticationSuccessHandler;
        @Autowired
        private MemberAuthenticationFailHandler MemberAuthenticationFailHandler;


        @Bean
        public PasswordEncoder passwordEncoder2() {
            return new BCryptPasswordEncoder();   // 使用 BCrypt 加密
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
           // http.antMatcher("/home/**").
            //為了在product頁面取得到使用者訊息,進行了url修改.2019.4.12
            http.antMatcher("/**").
                    //指定登入認證的Controller
                            formLogin().usernameParameter("uname").passwordParameter("pwd").loginPage("/home/login").successHandler(
                    MemberAuthenticationSuccessHandler).failureHandler(MemberAuthenticationFailHandler)
                    .and()
                    .authorizeRequests()
                    //登入關聯
                    .antMatchers("/home/login", "/home/register/mobile", "/home/register/email").permitAll()
                    .antMatchers("/home/**").hasRole("USER")
                    //限制購物車必須登入
                    .antMatchers("/cart/","/cart").hasRole("USER");


            //rabc關聯

            http.logout().logoutUrl("/home/logout").permitAll();
            http.rememberMe().rememberMeParameter("rememberme");//記住我功能
            http.headers().frameOptions().sameOrigin();//解決X-Frame-Options deny 造成的頁面空白,不然背景不能用frame
        }

        @Bean
        UserDetailsService UserService() {
            return new UserSecurityService();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(UserService()).passwordEncoder(new BCryptPasswordEncoder() {
            });
        }
    }

    @Configuration
    @Order(3)
    public class WebSecurityConfig3 extends WebSecurityConfigurerAdapter {
        @Autowired
        private AuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
        @Autowired
        private AuthenticationFailureHandler jwtAuthenticationFailHandler;

        //加載BCrypt密碼解碼器
        @Bean
        public PasswordEncoder passwordEncoder3() {
            return new BCryptPasswordEncoder();   // 使用 BCrypt 加密
        }


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/jwt/**").
                    //指定登入認證的Controller
                            formLogin().usernameParameter("name").passwordParameter("pwd").loginPage("/jwt/login").successHandler(
                    jwtAuthenticationSuccessHandler).failureHandler(jwtAuthenticationFailHandler)
                    .and()
                    .authorizeRequests()
                    //登入關聯
                    .antMatchers("/register/mobile").permitAll()
                    .antMatchers("/article/**").authenticated()
                   .antMatchers("/tasks/**").hasRole("USER")
                    //.antMatchers(HttpMethod.POST, "/jwt/tasks/**").hasRole("USER")
                    .and()//.addFilter(new JWTAuthenticationFilter(authenticationManager()))
                    .addFilter(new JWTAuthorizationFilter(authenticationManager()));


            http.logout().permitAll();
            //http.rememberMe().rememberMeParameter("rememberme");//記住我功能
            //jwt組態
            // http.antMatcher("/article/**").addFilter(new JWTAuthenticationFilter(authenticationManager()));

            http.cors().and().csrf().ignoringAntMatchers("/jwt/**");


            //jwt組態

        }

        @Bean
        UserDetailsService JwtUserSecurityService() {
            return new JwtUserSecurityService();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(JwtUserSecurityService()).passwordEncoder(new BCryptPasswordEncoder() {
            });
        }
    }

    @Configuration
    @Order(4)
    public class WebSecurityConfig4 extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(WebSecurity web) throws Exception {
            // 不攔截靜態資源
            web.ignoring().antMatchers("/", "/static/html/**", "/js/**", "/jquery/**", "/js/plugins/layer/**", "/css/**", "/images/**", "/img/**", "/UPLOAD/img/**", "/mqtt/send");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/")
                    .authorizeRequests()//啟用基於 HttpServletRequest 的存取限制，開始組態哪些URL需要被保護、哪些不需要被保護
                    //設定靜態資源均可以存取
                    .antMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "**/favicon.ico", "/").permitAll()
                    .antMatchers("/static/html/**", "/js/**", "/jquery/**", "/js/plugins/layer/**", "/css/**", "/images/**", "/img/**", "/UPLOAD/img/**", "/mqtt/send").permitAll()
                    .antMatchers("/druid/").permitAll()
                    // 除上面外的所有請求全部需要鑑權認證
                    .anyRequest().authenticated();
            //所有請求都不需要權限就可以存取，這樣的話所有請求內都無法得到認證訊息，所以是anonymous
            // .anyRequest().permitAll();這裡最好不要設定避免錯誤,因為上面的 http.antMatcher("/")是全站
        }
    }

}