package com.yen.SpringAssignmentSystem.config;

// https://youtu.be/TOQjvskdl3g?si=5BrFwpBdyynej5ab&t=312

import com.yen.SpringAssignmentSystem.service.UserDetailServiceImpl;
import com.yen.SpringAssignmentSystem.util.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    CustomPasswordEncoder customPasswordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //super.configure(auth);
        auth.userDetailsService(userDetailService).passwordEncoder(customPasswordEncoder.getPasswordEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        super.configure(http);
    }

//    // password encoder
//    // https://youtu.be/TOQjvskdl3g?si=QBxYh5QVseKKZIBR&t=2030
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }


}
