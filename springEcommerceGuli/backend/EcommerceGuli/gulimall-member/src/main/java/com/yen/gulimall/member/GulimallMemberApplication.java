package com.yen.gulimall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// https://youtu.be/wIR4X0mYSa0?t=350

@MapperScan("com.yen.gulimall.member.dao")
@SpringBootApplication
public class GulimallMemberApplication {

	public static void main(String[] args) {

		SpringApplication.run(GulimallMemberApplication.class, args);
	}

}
