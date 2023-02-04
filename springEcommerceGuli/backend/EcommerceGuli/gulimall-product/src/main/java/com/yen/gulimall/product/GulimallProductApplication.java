package com.yen.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// https://youtu.be/wIR4X0mYSa0?t=244

/**
 *    Integrate with mybatis-plus
 *    	- https://www.youtube.com/watch?v=Ky5BZim-Y94&list=PLmOn9nNkQxJEwPjhNwGliP_bw3RjkgFCf&index=18
 *
 *      1) add dep (gulimall-common)
 *      2) config
 *      	- data source
 *      		- db connector
 *      		- db info in application.yml
 *       	- mybatis-plus info
 *       		- MapperScan in main app
 *       		- let mybatis know mapper xml file location
 */

@MapperScan("com.yen.gulimall.product.dao")
@SpringBootApplication
public class GulimallProductApplication {

	public static void main(String[] args) {

		SpringApplication.run(
				GulimallProductApplication.class, args);
	}

}
