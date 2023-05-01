package com.yen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class}) // ignore mysql data source : https://stackoverflow.com/questions/36387265/disable-all-database-related-auto-configuration-in-spring-boot
//@EnableFeignClients(basePackages = "com.yen.gulimall.product.feign")
@EnableDiscoveryClient
//@MapperScan("com.yen.gulimall.search.dao")
@SpringBootApplication
public class GulimallSearchApplication {

	public static void main(String[] args) {

		SpringApplication.run(
				GulimallSearchApplication.class, args);
	}

}
