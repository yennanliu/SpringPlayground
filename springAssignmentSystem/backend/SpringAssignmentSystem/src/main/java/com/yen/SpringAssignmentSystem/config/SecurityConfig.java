package com.yen.SpringAssignmentSystem.config;

// https://youtu.be/TOQjvskdl3g?si=5BrFwpBdyynej5ab&t=312
// https://github.com/tp02ga/AssignmentSubmissionApp/blob/master/back-end/src/main/java/com/coderscampus/AssignmentSubmissionApp/config/SecurityConfig.java

import com.yen.SpringAssignmentSystem.filter.JwtFilter;
import com.yen.SpringAssignmentSystem.service.UserDetailServiceImpl;
import com.yen.SpringAssignmentSystem.util.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    CustomPasswordEncoder customPasswordEncoder;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //super.configure(auth);
        auth.userDetailsService(userDetailService).passwordEncoder(customPasswordEncoder.getPasswordEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        // https://youtu.be/1Mn1AFs8eDo?si=27sAlfeg9E3cQdWq&t=2381
        //super.configure(http);
        http = http.csrf().disable().cors().disable();

        http = http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        http = http.exceptionHandling()
                .authenticationEntryPoint((request, response, ex) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                }).and();

        http.authorizeRequests()
                .antMatchers("/api/auth/**").permitAll() // allow request access "/api/auth/**" without login
                .antMatchers("/api").permitAll() // allow request access "/api/" without login
                .anyRequest().authenticated();

       http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
