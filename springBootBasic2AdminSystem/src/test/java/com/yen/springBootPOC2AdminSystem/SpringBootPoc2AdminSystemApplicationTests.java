package com.yen.springBootPOC2AdminSystem;

// https://www.youtube.com/watch?v=1uG7UXdiCYM&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=61
// https://www.youtube.com/watch?v=njvVPhCFH6o&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=66
// https://www.youtube.com/watch?v=HcZCvC7jBlU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=70
// https://www.youtube.com/watch?v=X60MOsSfSTk&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=72

/** default test class for Spring boot */

import com.yen.springBootPOC2AdminSystem.bean.User2;
import com.yen.springBootPOC2AdminSystem.mapper.User2Mapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest  // declare this is a test for spring boot
class SpringBootPoc2AdminSystemApplicationTests {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	DataSource dataSource;

	@Autowired
	User2Mapper user2Mapper;

	@Autowired
	StringRedisTemplate redisTemplate;

	@Autowired
	RedisConnectionFactory redisConnectionFactory;

	@Test
	void contextLoads() {

		//jdbcTemplate.queryForObject("SELECT * FROM product");
		//jdbcTemplate.queryForList("SELECT * FROM product");

		/** Mysql tests */
		// test 1
		Long res1 = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM product", Long.class);
		log.info(">>> query res1 = {}", res1);
		System.out.println(">>> query res1 = " + res1);

		// test 2
		List<Map<String, Object>> res2 = jdbcTemplate.queryForList("SELECT * FROM product");
		//System.out.println(">>> query res2 = " + res2.toString());
		for (Map<String, Object> record: res2){
			System.out.println(record.toString());
		}

		/** Druid tests */
		log.info("data source type : {}", dataSource.getClass());
	}

	@Test
	void testUser2Mapper(){
		/** Mybatis plus tests */
		User2 u1 = user2Mapper.selectById(1);
		log.info("user1 = {}", u1.toString());
	}

	@Test
	void testRedis(){

		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		operations.set("heyyyy", "wazzuppppp");
		String res1 = operations.get("heyyyy");

		System.out.println(redisConnectionFactory.getClass());
		System.out.println("res1 = " + res1);
	}

}
