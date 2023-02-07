package com.yen.gulimall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// https://youtu.be/wIR4X0mYSa0?t=243

/**
 *  How to use Nacos as general conf setting
 *
 *  	PART 1)
 *
 *      - https://youtu.be/NMSk_q8czyI?t=669
 *
 *      - 1) add Nacos dep (pom.xml)
 *      - 2) create "bootstrap.properties" under resources
 *          - spring.application.name=gulimall-coupon
 *          - spring.cloud.nacos.config.server-addr=127.0.0.1:8848
 *      - 3) setup a conf in Nacos
 *      	- example : gulimall-coupon.properties (as Data ID) (default name : <application-name>.properties)
 *      - 4) setup whatever setting (k-v) in gulimall-coupon.properties
 *      - 5) sync with gulimall-coupon.properties in real-time ?
 *      	- go to controller (e.g. CouponController)
 *      	- add below annotation:
 *      		- @RefreshScope
 *      		- @Value("${key-name}") (get val from conf)
 *      - 6) priority:
 *      	- Nacos conf > java conf (e.g. : application.properties)
 *
 *
 *
 *      PART 2)
 *
 *      Details
 *      - https://youtu.be/bDCtpZpNQXE?t=17
 *
 *      	- 1) name space:
 *      		- setting isolation
 *      		- (default : public)
 *      		- different env for dev, qa, prod
 *      		- Note: have to set up which name space you need in bootstrap.properties
 *       	- 2) setting collection:
 *       		- all setting collection
 *       	- 3) setting collection ID:
 *       			- setting collection's ID (Data ID)
 *       	- 4) setting group
 *       		- By default, all setting collections belong to "DEFAULT_GROUP"
 *
 *
 */
@EnableDiscoveryClient
@MapperScan("com.yen.gulimall.coupon.dao")
@SpringBootApplication
public class GulimallCouponApplication {

	public static void main(String[] args) {

		SpringApplication.run(GulimallCouponApplication.class, args);
	}

}
