package com.yen.SpringAssignmentSystem.config;

// https://github.com/yennanliu/SpringPlayground/blob/main/springBasics/springBootBasic1/src/main/java/com/yen/SpringBootPart1/config/SecurityConfig.java

import com.yen.SpringAssignmentSystem.filter.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

// https://yanbin.blog/springboot-security-jwt-token-how-to-abcs/

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .authorizeHttpRequests()
                .antMatchers("/test").permitAll() // ONLY API with permitAll can be accessed without auth
                .antMatchers("/api/auth/login").permitAll()
                .antMatchers("/api/auth/logout").permitAll()
                .antMatchers("/api/assignments/all").permitAll()
                .antMatchers("/api/assignments").permitAll()
                .anyRequest().authenticated();

        // return Http error 403, instead of 401 if JWT token is not provided
        httpSecurity.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        httpSecurity.addFilterBefore(new JwtTokenFilter(), AuthorizationFilter.class);

        return httpSecurity.build();
    }
}

//import com.yen.SpringBootPart1.filter.JwtTokenFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.intercept.AuthorizationFilter;
//import org.springframework.security.web.authentication.HttpStatusEntryPoint;
//
//// https://yanbin.blog/springboot-security-jwt-token-how-to-abcs/
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
//
//        httpSecurity
//                .authorizeHttpRequests()
//                .antMatchers("/public-api").permitAll() // ONLY API with permitAll can be accessed without auth
//                .anyRequest().authenticated();
//
//        // return Http error 403, instead of 401 if JWT token is not provided
//        httpSecurity.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
//        httpSecurity.addFilterBefore(new JwtTokenFilter(), AuthorizationFilter.class);
//
//        return httpSecurity.build();
//    }
//}
//
//
////
////// https://youtu.be/TOQjvskdl3g?si=5BrFwpBdyynej5ab&t=312
////// https://github.com/tp02ga/AssignmentSubmissionApp/blob/master/back-end/src/main/java/com/coderscampus/AssignmentSubmissionApp/config/SecurityConfig.java
////
////import com.yen.SpringAssignmentSystem.filter.JwtFilter;
////import com.yen.SpringAssignmentSystem.service.UserDetailServiceImpl;
////import com.yen.SpringAssignmentSystem.util.CustomPasswordEncoder;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
////import org.springframework.security.config.http.SessionCreationPolicy;
////import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
////
////import javax.servlet.http.HttpServletResponse;
////
////@EnableWebSecurity
////public class SecurityConfig extends WebSecurityConfigurerAdapter {
////
////    @Autowired
////    UserDetailServiceImpl userDetailService;
////
////    @Autowired
////    CustomPasswordEncoder customPasswordEncoder;
////
////    @Autowired
////    private JwtFilter jwtFilter;
////
////    @Override
////    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////
////        //super.configure(auth);
////        auth.userDetailsService(userDetailService).passwordEncoder(customPasswordEncoder.getPasswordEncoder());
////    }
////
////    @Override
////    public void configure(HttpSecurity http) throws Exception {
////
////        // https://youtu.be/1Mn1AFs8eDo?si=27sAlfeg9E3cQdWq&t=2381
////        //super.configure(http);
////        http = http.csrf().disable().cors().disable();
////
////        http = http.sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and();
////
////        http = http.exceptionHandling()
////                .authenticationEntryPoint((request, response, ex) -> {
////                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
////                }).and();
////
////        http.authorizeRequests()
////                .antMatchers("/api/auth/**").permitAll() // allow request access "/api/auth/**" without login
////                .antMatchers("/api").permitAll() // allow request access "/api/" without login
////                .anyRequest().authenticated();
////
////       http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
////    }
////
////}
