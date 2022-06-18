package com.yen.springBootAdvance6;

// https://www.youtube.com/watch?v=dZS2D_kHhCM&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=41

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 *  Custom service health indicator
 *    step 1) create indicator (implement HealthIndicator interface)
 *    step 2) indicator name should be as XXXIndicator
 *    step 3) add above to container
 */
@SpringBootApplication
public class SpringBootAdvance6Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdvance6Application.class, args);
	}

}
