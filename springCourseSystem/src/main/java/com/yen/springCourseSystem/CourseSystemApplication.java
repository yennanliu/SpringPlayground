package com.yen.springCourseSystem;

// book p. 262

import tk.mybatis.spring.annotation.MapperScan; // should use this one!! https://blog.csdn.net/fygkchina/article/details/109159608
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackages = "com.yen.springCourseSystem")
//@MapperScan(basePackages = "com.yen.springCourseSystem.mapper")
@org.mybatis.spring.annotation.MapperScan("com.yen.springCourseSystem.mapper")
@EnableTransactionManagement // TODO : recheck this
@SpringBootApplication
public class CourseSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(
				CourseSystemApplication.class, args);
	}

}
