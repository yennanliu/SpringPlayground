package com.yen.springBootAdvance1;

// https://www.youtube.com/watch?v=Un_YC0fBKls&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=3
// https://www.youtube.com/watch?v=4dRfvI1tnqs&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=5

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
 */
@MapperScan("com.yen.springBootAdvance1.mapper")
@SpringBootApplication
@EnableCaching
public class SpringBootAdvance1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdvance1Application.class, args);
	}

}
