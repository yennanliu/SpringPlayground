package com.yen.springBootAdvance1;

// https://www.youtube.com/watch?v=Un_YC0fBKls&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=3

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
 */
@MapperScan("com.yen.springBootAdvance1.mapper")
@SpringBootApplication
public class SpringBootAdvance1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdvance1Application.class, args);
	}

}
