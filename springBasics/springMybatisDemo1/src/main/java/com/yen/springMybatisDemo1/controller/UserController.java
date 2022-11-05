package com.yen.springMybatisDemo1.controller;

import com.yen.springMybatisDemo1.bean.Pager;
import com.yen.springMybatisDemo1.bean.User;
import com.yen.springMybatisDemo1.service.User2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@ResponseBody
@RestController
public class UserController {

    @Autowired
    User2Service user2Service;

    @PostMapping("/user/get_user")
    public Pager<User> getUserPage(@RequestParam int page, @RequestParam int size){

        Pager<User> response = user2Service.findByPager(page, size);
        System.out.println(">>> response = " + response);

        return response;
    }

    @GetMapping("/user/total")
    public long getTotal(){

        return user2Service.getTotal();
    }

}
