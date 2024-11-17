package com.yen.springMybatisDemo1.service;

import com.yen.springMybatisDemo1.bean.Emp;
import com.yen.springMybatisDemo1.bean.MyUser;
import com.yen.springMybatisDemo1.mapper.EmpMapper;
import com.yen.springMybatisDemo1.mapper.MyUserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** @Transactional  Demo
 *
 */

@Log4j2
@Service
public class MyService {

    @Autowired
    MyUserMapper myUserMapper;

    @Transactional(rollbackFor = ArithmeticException.class)
    public void saveStatus() {

        List<MyUser> users = myUserMapper.getAllUser();

//        try {
//            int userCount = myUserMapper.getAllUser().size();
//            log.info(">>> userCount = " + userCount);
//
//            log.info(">>> insert new user to db");
//            myUserMapper.insertUser(new MyUser());
//
//            int userCount2 = myUserMapper.getAllUser().size();
//            log.info(">>> userCount2 = " + userCount2);
//
//            int x = 10 / 0;
//            log.info(">>> x= " + x);
//        } catch (Exception e) {
//            log.error(">>> saveStatus failed : {}", e);
//        }


        /** NOTE : CAN'T use try-catch, or Transactional will NOT work */
        int userCount = myUserMapper.getAllUser().size();
        log.info(">>> userCount = " + userCount);

        log.info(">>> insert new user to db");
        myUserMapper.insertUser(new MyUser());

        int userCount2 = myUserMapper.getAllUser().size();
        log.info(">>> userCount2 = " + userCount2);

        int x = 10 / 0;
        log.info(">>> x= " + x);
    }

}
