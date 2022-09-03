package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author longzhonghua
 * @data 2/24/2019 9:59 PM
 */
@RestController
public class UserListControllerB {
    @Autowired
    UserMapper userMapper;
    //http://localhost:8080/listall2?pageNum=1&pageSize=2
    @RequestMapping("/listall2")
    // 若果方法的參數不指定預設值，且請求位址也沒有指定參數值，則專案執行時會顯示出錯。此處為網友ygl提供的修正建議。
    public Page<User> getUserList(@RequestParam(value="pageNum",defaultValue="0")int pageNum, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize)
    //public Page<User> getUserList(Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        Page<User>  userList= userMapper.getUserList();
        return userList;
    }
}
