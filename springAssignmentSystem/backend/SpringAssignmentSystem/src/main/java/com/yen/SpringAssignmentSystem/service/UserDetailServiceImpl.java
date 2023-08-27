package com.yen.SpringAssignmentSystem.service;

// https://youtu.be/TOQjvskdl3g?si=SUK3aC9rIqsX67IJ&t=595

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/** Service helps get users from DB for spring security auth */
public class UserDetailServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

}
