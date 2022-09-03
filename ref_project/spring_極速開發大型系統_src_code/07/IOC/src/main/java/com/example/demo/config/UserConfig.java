package com.example.demo.config;
import com.example.demo.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: StudentConfig
 * Author:   longzhonghua
 * Date:     2019/4/16 17:13
 *
 * @Description: $description$
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@Configuration
public class UserConfig {
    //將此傳回的值產生一個bean
    @Bean("user1")
    public User user() {
        User user = new User();
        user.setId(1);
        user.setName("longzhiran");
        return user;
    }

}
