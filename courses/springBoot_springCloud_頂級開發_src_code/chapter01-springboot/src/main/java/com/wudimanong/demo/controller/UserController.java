package com.wudimanong.demo.controller;

import com.wudimanong.demo.dao.UserDao;
import com.wudimanong.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author joe
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserDao userDao;

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public User getUserById(@RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userDao.addUser(user);
        return user;
    }
}
