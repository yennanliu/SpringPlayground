package com.yen.user;

// https://www.youtube.com/watch?v=tF_Y4G1FovQ&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=34

import com.yen.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConsumerUserApplicationTest {

    @Autowired
    UserService userService;

    @Test
    public void test1(){
        userService.hello();
    }

}
