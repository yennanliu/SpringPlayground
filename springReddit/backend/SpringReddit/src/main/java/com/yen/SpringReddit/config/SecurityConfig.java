package com.yen.SpringReddit.config;

// https://youtu.be/kpKUMmAmcj0?t=63
// https://youtu.be/1ojKQxVssPQ?t=187
// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/master/src/main/java/com/programming/techie/springredditclone/config/SecurityConfig.java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

// TODO : double check if should use WebSecurityConfigurerAdapter or not
public class SecurityConfig extends SecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

//    @Bean
//    public UserDetailsService myUserDetailsService(){
//        return new InMemoryUserDetailsManager(
//                User.withUsername("admin")
//                        .password("{noop}123")
//                        .authorities("READ", "ROLE_USER")
//                        .build());
//    }

    public void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf()
                .disable()
                .authorizeRequests()
                //.requestMatchers("/api/auth/**")
                //.permitAll()
                .anyRequest()
                .authenticated();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    // encrypt user password (for saving it in DB)
    // https://youtu.be/kpKUMmAmcj0?t=297
//    @Bean
//    PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

}
