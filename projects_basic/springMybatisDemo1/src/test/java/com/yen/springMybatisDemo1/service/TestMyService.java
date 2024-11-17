package com.yen.springMybatisDemo1.service;

import com.yen.springMybatisDemo1.mapper.MyUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class TestMyService {

    @Autowired
    MyService myService;

    @Autowired
    MyUserMapper myUserMapper;

    @Test
    public void TestSaveStatus(){

        myService.saveStatus();
    }

}
