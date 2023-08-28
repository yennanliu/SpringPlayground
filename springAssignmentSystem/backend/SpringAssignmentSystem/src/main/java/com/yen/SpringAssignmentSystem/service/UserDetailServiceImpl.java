package com.yen.SpringAssignmentSystem.service;

// https://youtu.be/TOQjvskdl3g?si=SUK3aC9rIqsX67IJ&t=595

import com.yen.SpringAssignmentSystem.domain.User;
import com.yen.SpringAssignmentSystem.util.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Service helps get users from DB for spring security auth */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //return null;
        /** NOTE : we hardcode user id, pwd for now */
        // TODO : get below from DB, instead of hardcode
        User user = new User();
        user.setUsername("admin");
        user.setPassword(customPasswordEncoder.getPasswordEncoder().encode("123")); // encode pwd and store, so it can be decoded properly as we;;
        user.setId(1L);
        return user;
    }

}
