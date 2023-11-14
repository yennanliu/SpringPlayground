package com.yen.mdblog.config;

// social login : https://youtu.be/us0VjFiHogo?t=241

import com.yen.mdblog.handler.MyLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class OAuthSecurityConfig {

    @Autowired
    MyLogoutHandler myLogoutHandler;

    // in memory user
    // https://www.youtube.com/watch?v=66DtzkhBlSA&t=515s
    // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/in-memory.html
    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails user = User
                .withUsername("user1")
                .password("{noop}123")
                .authorities("READ", "ROLE_USER")
                .build();

        UserDetails admin = User
                .withUsername("admin")
                .password("{noop}123")
                .authorities("READ", "ROLE_USER")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    // social login : https://youtu.be/us0VjFiHogo?t=241
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

//        LogoutConfigurer<HttpSecurity> LogoutConfigurer = httpSecurity.authorizeRequests()
//                .and()
//                .logout().addLogoutHandler(myLogoutHandler); // 新增自訂的登出處理器

        httpSecurity = httpSecurity
                .csrf()
                .disable()
                .cors()
                .and()
                .logout(logout -> logout.permitAll()
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        }));

        return httpSecurity
                // login
                .formLogin()
                .defaultSuccessUrl("/posts/all", true)
                .permitAll()
                .and()
                // logout // TODO : fix logout redirect
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("classpath:/templates/logout.html") // Redirect to "/logout.html" after logout
                .permitAll()
                .and()
                // auth
                .authorizeRequests(auth -> {
                    auth.anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())
                .build();
    }

}
