package com.yen.springCourseSystem.service;

// book p. 249

import com.yen.springCourseSystem.bean.User;

import java.util.List;

public interface UserService {

    List<User> loadUserByUserName(String userName);
    void addUser(User user);
}
