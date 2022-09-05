package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: UserController
 * Author:   longzhonghua
 * Date:     4/9/2019 7:13 PM
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;


    // 加入使用者
    @PostMapping("/")
    public void insertUser() throws Exception {
        User user = new User();
        user.setUsername("zhonghua");
        userService.insertUser(user);
    }

    // 查詢使用者

    @GetMapping("/{id}")

    public void findUserById(@PathVariable long id) throws Exception {
        User user = userService.findUserById(id);
        System.out.println(user.getId() + user.getUsername());
    }


    // 修改使用者

    @PutMapping("/{id}")
    public User updateUserById(User user) {
        return userService.updateUserById(user);
    }
    // 移除使用者

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable long id) {
        userService.deleteUserById(id);
    }

}
