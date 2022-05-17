package com.yen.springBootPOC3;

/** Spring boot app entry point
 *
 *  book p.62
 *
 */

import com.yen.springBootPOC3.servlet.MyServlet1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ServletComponentScan
public class SpringBootPoc3Application {

//	@Bean
//	public ServletRegistrationBean MyServlet1(){
//		return new ServletRegistrationBean(new MyServlet1(), "/myServlet/**");
//	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootPoc3Application.class, args);
	}

}
