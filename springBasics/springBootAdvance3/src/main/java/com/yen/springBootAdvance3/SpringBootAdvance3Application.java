package com.yen.springBootAdvance3;

// https://www.youtube.com/watch?v=QL_JfKt-n4o&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=24
// https://www.youtube.com/watch?v=NZ39FYUS_5U&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=25

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync  // NOTE !!! we NEED to enable async here (app program)
@EnableScheduling // enable scheduling
@SpringBootApplication
public class SpringBootAdvance3Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdvance3Application.class, args);
	}

}
