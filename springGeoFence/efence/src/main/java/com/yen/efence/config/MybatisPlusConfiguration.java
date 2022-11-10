package com.yen.efence.config;

// book p.4-52
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/config/MybatisPlusConfiguration.java

import com.baomidou.mybatisplus.extension.incrementer.PostgreKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MybatisPlusConfiguration {

    /**
     * MyBatis-Plus Postgresql序列Key生成器配置
     *
     * @return
     */
    @Bean
    public PostgreKeyGenerator createPostgreKeyGenerator() {

        return new com.baomidou.mybatisplus.extension.incrementer.PostgreKeyGenerator();
    }
}