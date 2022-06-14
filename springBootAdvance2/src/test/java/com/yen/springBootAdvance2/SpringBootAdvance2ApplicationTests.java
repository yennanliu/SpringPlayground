package com.yen.springBootAdvance2;

// https://www.youtube.com/watch?v=FqHO8tiUthQ&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=18

import com.yen.springBootAdvance2.bean.Book;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SpringBootAdvance2ApplicationTests {

	@Autowired
	RabbitTemplate rabbitTemplate;

	/**
	 *   Send msg to RabbitMQ
	 *    - p2p (point to point)
	 */
	@Test
	void sendMsg() {

		// we need to define our own message (can customize body, header)
		//rabbitTemplate.send(exchange, routeKey, message);

		// we only need to send msg object (object is default body), it will be serialized and sent automatically (to RabbitMQ)
		//rabbitTemplate.convertAndSend(exchange, routeKey, object);

		Map<String, Object> map = new HashMap<>();
		map.put("k1", "v1");
		map.put("data", Arrays.asList("hello", 123, true));

		// object is serialized (default method) and sent to RabbitMQ
		rabbitTemplate.convertAndSend("exchange.direct", "yen.news", map);

		rabbitTemplate.convertAndSend("exchange.direct", "yen.news", new Book("scala playbook", "tim"));
	}

	/**
	 *   Receive msg from RabbitMQ
	 */
	@Test
	public void receiveMsg(){

		Object o = rabbitTemplate.receiveAndConvert("com.yen");
		System.out.println(o.getClass());
		System.out.println(o);

	}

	/**
	 *   Broadcast msg to RabbitMQ
	 */
	@Test
	public void broadCastMsg(){

		rabbitTemplate.convertAndSend("exchange.fanout", "", new Book("python manual","jack"));
	}

}
