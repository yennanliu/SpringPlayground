package com.yen.springBootPOC2AdminSystem;

// https://www.youtube.com/watch?v=1uG7UXdiCYM&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=61

/** default test class for Spring boot */

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@SpringBootTest
class SpringBootPoc2AdminSystemApplicationTests {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Test
	void contextLoads() {
		//jdbcTemplate.queryForObject("SELECT * FROM product");
		//jdbcTemplate.queryForList("SELECT * FROM product");
		Long res1 = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM product", Long.class);
		log.info(">>> query res1 = {}", res1);
		System.out.println(">>> query res1 = " + res1);
	}

}
