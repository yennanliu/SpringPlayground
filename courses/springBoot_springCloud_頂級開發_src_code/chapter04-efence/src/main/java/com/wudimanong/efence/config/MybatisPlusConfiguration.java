package com.wudimanong.efence.config;

import com.baomidou.mybatisplus.extension.incrementer.PostgreKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangqiao
 */
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
