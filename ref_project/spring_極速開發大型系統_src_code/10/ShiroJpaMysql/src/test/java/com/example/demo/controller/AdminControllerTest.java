package com.example.demo.controller;

import com.example.demo.dao.AdminDao;
import com.example.demo.dao.SysRoleDao;
import com.example.demo.entity.Admin;
import com.example.demo.entity.SysRole;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: AdminControllerTest
 * Author:   longzhonghua
 * Date:     2019/5/7 15:50
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
public class AdminControllerTest {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private SysRoleDao sysRoleDao;
    @Test
    public void userInfoAdd() {
        Admin admin = new Admin();
        int hashIterations = 2;//加密的次數
        Object salt = "longyan";//鹽值這裡的salt是 username+salt（一般是使用者名稱加一個隨機字串）, 這裡以字串“long”為例)
        Object credentials = "123456";//密碼
        String hashAlgorithmName = "MD5";//加密模式
        Object simpleHash = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        admin.setUsername("long");
        admin.setPassword(simpleHash.toString());
        admin.setSalt("yan");
        admin.setPassword(simpleHash.toString());
        List<SysRole> roles = new ArrayList<>();
        SysRole role1 = sysRoleDao.findByRole("admin");
        roles.add(role1);
        admin.setRoleList(roles);
        adminDao.save(admin);

    }
}