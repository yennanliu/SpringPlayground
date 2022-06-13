package com.yen.springBootAdvance2;

// https://www.youtube.com/watch?v=FqHO8tiUthQ&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=18

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** RabbitMQ msg app demo */

/**
 *   Auto config
 *   	1) RabbitAutoConfiguration
 *         -> auto config connection factory : ConnectionFactory
 *      2) RabbitProperties encrypt RabbitMQ conf
 *      3) RabbitTemplate : send & receive msg from RabbitMQ
 *      4) AmqpAdmin : RabbitMQ system management component
 *
 */
@SpringBootApplication
public class SpringBootAdvance2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdvance2Application.class, args);
	}

}
