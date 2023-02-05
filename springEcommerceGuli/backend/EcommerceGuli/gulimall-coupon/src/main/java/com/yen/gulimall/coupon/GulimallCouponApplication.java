package com.yen.gulimall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// https://youtu.be/wIR4X0mYSa0?t=243

@MapperScan("com.yen.gulimall.coupon.dao")
@SpringBootApplication
public class GulimallCouponApplication {

	public static void main(String[] args) {

		SpringApplication.run(GulimallCouponApplication.class, args);
	}

}
