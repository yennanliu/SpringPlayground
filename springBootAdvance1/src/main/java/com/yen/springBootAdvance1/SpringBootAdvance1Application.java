package com.yen.springBootAdvance1;

// https://www.youtube.com/watch?v=Un_YC0fBKls&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=3

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
 */
@SpringBootApplication
public class SpringBootAdvance1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdvance1Application.class, args);
	}

}
