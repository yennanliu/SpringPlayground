package com.yen.springSSO.config;

// book p. 3-23

import com.yen.springSSO.config.provider.UserNameAuthenticationProvider;
import com.yen.springSSO.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailService userDetailService;

    private UserDetailsService userDetailsService;

    /** path filter */
    @Override
    public void configure(WebSecurity web) throws Exception {

        //super.configure(web);
        web.ignoring().antMatchers("/css/**", "/js/**", "/fonts/**", "/icon/**", "/images/**", "/favicon.ico");
    }

    /** enable some requests to access auth entrance */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        System.out.println(">>> http request : " + http);
        //super.configure(http);
        http.requestMatchers()
            .antMatchers("/login/", "/oauth/authorize", "/oauth/check_token")
            .and().authorizeRequests().anyRequest().authenticated().and().formLogin()
            .loginPage("/login").failureUrl("/login-error")
            .permitAll();

        http.csrf().disable();
    }

    /** auth token management */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //super.configure(auth);
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /**
     * object setting that grants user info DB access
     */
    @Bean
    public AbstractUserDetailsAuthenticationProvider daoAuthenticationProvider(){

        UserNameAuthenticationProvider authProvider = new UserNameAuthenticationProvider();

        // set up userDetailService
        authProvider.setUserDetailsService(userDetailsService);

        // NOT hide not-found-exception
        authProvider.setHideUserNotFoundExceptions(false);

        // do password Hash computing via BCrypt
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(6));
        return authProvider;
    }

}
