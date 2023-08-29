package com.yen.SpringAssignmentSystem;

// https://youtu.be/_L46CaEI490?si=3d9HXo35GjUFz45B&t=1725

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncodeTest {

    @Test
    public void Test1(){

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("123");

        System.out.println(encodedPassword);
        System.out.println(123);
    }

}
