package com.yen.service;

// https://kucw.github.io/blog/2020/2/spring-unit-test-mockito/

import com.yen.bean.User;
import com.yen.mapper.UserMapper;
import org.junit.Test;
import org.junit.Assert;
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
    public void getUserByNameTest1(){
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
    @Test
    public void getUserByNameTest2(){

        Mockito.when(userService.getUserByName(Mockito.anyString())).thenReturn(new User("amy", 10));

        // return User("amy", 10) instance with any name
        User user = userService.getUserByName("xxx");

        Assert.assertNotNull(user);
        Assert.assertEquals(user.getAge(), 10);
        Assert.assertEquals(user.getName(), "amy");
    }

    @Test
    public void getUserByNameTest3(){

        Mockito.when(userService.getUserByName("Zad")).thenThrow(new RuntimeException("mock throw exception"));
        //User user = userService.getUserByName("Zad"); //會拋出一個RuntimeException

        Assert.assertThrows(RuntimeException.class, () -> { userService.getUserByName("Zad");});
    }

    @Test
    public void getUserByNameTest4(){

        // TODO : fix below
        Mockito.when(userMapper.getUserByName("amy")).thenReturn(new User("amy", 10));
        //Mockito.verify(userService, Mockito.times(1)).getUserByName(Mockito.eq("amy"));
    }

    @Test
    public void getUserByNameTest5(){

        // TODO : fix below

        Mockito.when(userMapper.getUserByName("amy")).thenReturn(new User("amy", 10));
        Mockito.when(userMapper.getUserByName("zz")).thenReturn(new User("zz", 10));

//        InOrder inOrder = Mockito.inOrder(userService);
//        inOrder.verify(userService).getUserByName("amy");
//        inOrder.verify(userService).getUserByName("zz");
//        inOrder.verify(userService).insertUser(Mockito.any(User.class));
    }

}
