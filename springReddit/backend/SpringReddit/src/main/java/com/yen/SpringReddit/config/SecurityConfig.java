//package com.yen.SpringReddit.config;
//
//// https://youtu.be/kpKUMmAmcj0?t=63
//// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/master/src/main/java/com/programming/techie/springredditclone/config/SecurityConfig.java
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
//// TODO : double check if should use WebSecurityConfigurerAdapter or not
//public class SecurityConfig extends SecurityConfigurerAdapter {
//
//    @Bean
//    public UserDetailsService userDetailsService(){
//        return new InMemoryUserDetailsManager(
//                User.withUsername("admin")
//                        .password("{noop}123")
//                        .authorities("READ", "ROLE_USER")
//                        .build());
//    }
//
//    public void configure(HttpSecurity httpSecurity) throws Exception {
//
//        httpSecurity
//                .csrf()
//                .disable()
//                .authorizeRequests()
//                //.requestMatchers("/api/auth/**")
//                //.permitAll()
//                .anyRequest()
//                .authenticated();
//    }
//
//    // encrypt user password (for saving it in DB)
//    // https://youtu.be/kpKUMmAmcj0?t=297
////    @Bean
////    PasswordEncoder passwordEncoder(){
////        return new BCryptPasswordEncoder();
////    }
//
//}
