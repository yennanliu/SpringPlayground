package com.yen.user;

// https://www.youtube.com/watch?v=tF_Y4G1FovQ&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=34

import com.yen.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class) // https://stackoverflow.com/questions/42550874/intellij-junit-runwith-not-resolved
@SpringBootTest
public class ConsumerUserApplicationTest {

    @Autowired
    UserService userService;

    // TODO : fix below
    @Test
    public void test1(){
        userService.hello();
    }

}
