package com.yen.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// https://youtu.be/wIR4X0mYSa0?t=244
// https://youtu.be/KKFJPtW3730?t=341

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
 *
 *     3) Mybatis with Logic deletion:
 *     		- https://youtu.be/6in5XKRnxNY?t=406
 *     		- application.yml add below:
 *     			      - logic-delete-value: 1
 *       			  - logic-not-delete-value: 0
 *          - add "TableLogic" annotation to bean attr
 *          		- in CategoryEntity:
 *          			@TableLogic
 *     					private Integer showStatus;
 *
 *   4) backend product validation
 *   	- JSR303
 *   	- https://youtu.be/8sIUw0bQyKU?t=23
 *   	- step 1) add validation annotation logic to bean : javax.validation.constraints
 *   		- can define custom error msg
 *   		- example : com.yen.gulimall.product.entity.BrandEntity
 *   	- step 2) add @Valid to controller which use the bean
 *   		- example : com.yen.gulimall.product.controller.BrandController
 *   		- result: when verification failed, there will be an error msg
 *   	- step 3) can use "BindingResult" next to bean, so can get validation result
 *   		- example : com.yen.gulimall.product.controller.BrandController
 *   					- public R save(@RequestBody CategoryEntity category, BindingResult result)
 *
 */

@EnableDiscoveryClient
@MapperScan("com.yen.gulimall.product.dao")
@SpringBootApplication
public class GulimallProductApplication {

	public static void main(String[] args) {

		SpringApplication.run(
				GulimallProductApplication.class, args);
	}

}
