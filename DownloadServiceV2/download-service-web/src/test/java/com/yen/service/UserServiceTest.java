package com.yen.service;

// https://kucw.github.io/blog/2020/2/spring-unit-test-mockito/

import com.yen.bean.User;
import com.yen.mapper.UserMapper;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
//import org.junit.jupiter.api.Test;
import org.junit.Assert;
//import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean // NOTE !!! here we have to use @MockBean annotation; instead of @Autowired
    UserMapper userMapper;

    @Test
    public void getUserByIdTest(){
        /**
         *  define when mock userMapper is called, and param = "amy"
         *  -> it will return new User("amy", 10) new instance
         */
        Mockito.when(userMapper.getUserByName("amy")).thenReturn(new User("amy", 10));

        // return User("amy", 10) instance
        User user = userService.getUserByName("amy");

        Assert.assertNotNull(user);
        Assert.assertEquals(user.getAge(), 10);
        Assert.assertEquals(user.getName(), "amy");
    }

}
