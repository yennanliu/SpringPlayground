package com.yen.springBootPOC2AdminSystem;

// https://www.youtube.com/watch?v=1uG7UXdiCYM&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=61
// https://www.youtube.com/watch?v=njvVPhCFH6o&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=66

/** default test class for Spring boot */

import com.yen.springBootPOC2AdminSystem.bean.User2;
import com.yen.springBootPOC2AdminSystem.mapper.User2Mapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
class SpringBootPoc2AdminSystemApplicationTests {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	DataSource dataSource;

	@Autowired
	User2Mapper user2Mapper;

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

}
