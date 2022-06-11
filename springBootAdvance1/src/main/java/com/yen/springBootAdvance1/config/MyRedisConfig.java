package com.yen.springBootAdvance1.config;

// https://www.youtube.com/watch?v=JDlq3u_EEWI&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=12
// https://www.youtube.com/watch?v=FhlRZRshF14&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=13

import com.yen.springBootAdvance1.bean.Department;
import com.yen.springBootAdvance1.bean.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class MyRedisConfig {

    // modify from org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
    @Bean
    public RedisTemplate<Object, Employee> empRedisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<Object, Employee> template = new RedisTemplate<Object, Employee>();
        template.setConnectionFactory(redisConnectionFactory);

        // customize our redis Serializer
        Jackson2JsonRedisSerializer<Employee> ser = new Jackson2JsonRedisSerializer<Employee>(Employee.class);
        template.setDefaultSerializer(ser);
        return template;
    }

    @Bean
    public RedisTemplate<Object, Department> deptRedisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<Object, Department> template = new RedisTemplate<Object, Department>();
        template.setConnectionFactory(redisConnectionFactory);

        // customize our redis Serializer
        Jackson2JsonRedisSerializer<Department> ser = new Jackson2JsonRedisSerializer<Department>(Department.class);
        template.setDefaultSerializer(ser);
        return template;
    }

    // TODO : fix below
//    @Bean
//    public RedisCacheManager employeeCacheManager(RedisTemplate<Object, Employee> empRedisTemplate){
//        // TODO : fix below
//        //RedisCacheManager cacheManager = new RedisCacheManager(new empRedisTemplate());
//        RedisCacheManager cacheManager = new RedisCacheManager(empRedisTemplate);
//        //cacheManager.setUsePrefix(true);
//        return cacheManager;
//    }


}
