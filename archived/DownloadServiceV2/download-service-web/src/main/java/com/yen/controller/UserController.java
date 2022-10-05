package com.yen.controller;

import com.yen.api.UserApi;
import com.yen.bean.User;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

    @Override
    public User getUser() {

        User u1 = new User("maria", 17);
        User u2 = new User("lynn", 28);

        System.out.println(">>> u1 = " + u1);
        
        return u1;
    }
}
