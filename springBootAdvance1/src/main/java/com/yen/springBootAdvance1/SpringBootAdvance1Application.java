package com.yen.springBootAdvance1;

// https://www.youtube.com/watch?v=Un_YC0fBKls&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=3
// https://www.youtube.com/watch?v=4dRfvI1tnqs&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=5
// https://www.youtube.com/watch?v=JDlq3u_EEWI&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=12
// https://www.youtube.com/watch?v=FhlRZRshF14&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=13

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 *  Init env
 *
 *   1) create DB, tables..
 *   	- springBootAdvance1/sql/
 *   		- department.sql
 *   	 	- employee.sql
 *
 *   2) create javaBean
 *
 *   3) integrate MyBatis for DB op
 *   	- 3-1) config data source
 *      - 3-2) use MyBatis annotation form
 *      	- 1) @MapperScan : declare to-scan Mapper interface module
 *
 *    4) cache demo
 *    	- steps)
 *    		- step 1) enable cache (based on annotation)
 *    				 - @EnableCaching
 *    		- step 2) add cache annotation
 *    				 - @Cacheable
 *    				 - @CacheEvict
 *    				 - @CachePut
 *
 *    5) install Redis via docker
 *       install spring boot Redis starter
 *       config Redis (in spring boot)
 *       test redis
 *       	- theory : CacheManager===Cache  (cache component help cache data)
 *          - 1) import redis starter, RedisCacheManager will be used
 *          - 2) RedisCacheManager help create RedisCache for cache component (via redis)
 *          - 3) by default, redis does cache with k-v format, how to use json format instead ?
 *          		- since we import redis starter, so RedisCacheManager is our cacheManager now
 *          	    - RedisCacheManager use RedisTemplate<Object, Object> by default
 *          	    - RedisTemplate<Object, Object> uses default JDK serialization mechanism
 *          - 4) so we have to define our own cacheManager (based on RedisCacheManager)
 *          		- ref : com.yen.springBootAdvance1.config.MyRedisConfig.java
 *
 *
 *
 */
@MapperScan("com.yen.springBootAdvance1.mapper")
@SpringBootApplication
@EnableCaching
public class SpringBootAdvance1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdvance1Application.class, args);
	}

}
