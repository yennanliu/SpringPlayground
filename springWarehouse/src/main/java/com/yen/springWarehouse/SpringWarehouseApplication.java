package com.yen.springWarehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackages = "com.yen.springWarehouse")
@org.mybatis.spring.annotation.MapperScan("com.yen.springWarehouse.mapper")
@EnableTransactionManagement // TODO : recheck this
@SpringBootApplication
public class SpringWarehouseApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringWarehouseApplication.class, args);
	}

}
