package com.yen.wallet;

// book p.5-9, P.5-44, p.5-56, p.5-58
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/WalletApplication.java

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.yen.wallet.client.PaymentClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableHystrixDashboard
@EnableCircuitBreaker // hystrix
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = PaymentClient.class)
@MapperScan("com.yen.wallet.dao.mapper")
@SpringBootApplication
public class WalletApplication {

	public static void main(String[] args) {

		SpringApplication.run(WalletApplication.class, args);
	}

	// book p.5-63
	// for spring boot > 2.0 version with hystrix, have to set up below for accessing hystrix metrics properly
	@Bean
	public ServletRegistrationBean getServlet() {
		HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
		registrationBean.setLoadOnStartup(1);
		registrationBean.addUrlMappings("/hystrix.stream");
		registrationBean.setName("HystrixMetricsStreamServlet");
		return registrationBean;
	}

}
