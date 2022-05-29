package com.yen.springBootPOC2AdminSystem;

// https://www.youtube.com/watch?v=oi6ChwpC6rc&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=57
// https://www.youtube.com/watch?v=oJGcVUf4rEM&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=65
// https://www.youtube.com/watch?v=5LGPPPMGn8Y&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=79

import com.yen.springBootPOC2AdminSystem.interceptor.RedisUrlCountInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.redis.connection.RedisConfiguration;

/** String boot app entry point */

//@MapperScan("com.yen.springBootPOC2AdminSystem.mapper") // auto scan all mappers (so we don't need @Mapper in each mapper)
@ServletComponentScan("com.yen.springBootPOC2AdminSystem") // scan com.yen.springBootPOC2AdminSystem.servlet.MyServlet
@SpringBootApplication
//@SpringBootApplication(exclude = RedisConfiguration.class) // if want to DISABLE redis in spring boot app
public class SpringBootPoc2AdminSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootPoc2AdminSystemApplication.class, args);
	}

}
