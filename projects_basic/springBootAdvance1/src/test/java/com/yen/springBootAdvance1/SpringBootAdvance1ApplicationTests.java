package com.yen.springBootAdvance1;

// https://www.youtube.com/watch?v=Un_YC0fBKls&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=4
// https://www.youtube.com/watch?v=JDlq3u_EEWI&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=12

import com.yen.springBootAdvance1.bean.Employee;
import com.yen.springBootAdvance1.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class SpringBootAdvance1ApplicationTests {

	@Autowired
	EmployeeMapper employeeMapper;

	@Autowired
	StringRedisTemplate stringRedisTemplate; // (connect via <k-v> String) StringRedisTemplate(RedisConnectionFactory)

	@Autowired
	RedisTemplate redisTemplate; // (connect via <k-v> Object) RedisTemplate<Object, Object>

	@Autowired
	RedisTemplate<Object, Employee> empRedisTemplate;

	@Test
	void contextLoads() {

		// test employeeMapper, Mysql employee table
		Employee empById = employeeMapper.getEmpById(1);
		System.out.println(">>> empById = " + empById);
	}

	/** Redis op test
	 *
	 *    - Common data structure
	 *    	-> String, List, Set, Hash, ZSet(ordering set)
	 *
	 *    - opsForValue : String op
	 *    - opsForList : List op
	 *    - opsForSet : Set op
	 *    - opsForHash : Hash op
	 *    - opsForZSet : ZSet op
	 */
	@Test
	public void Test1(){

		/** String op demo */
		// save a String to redis
		stringRedisTemplate.opsForValue().append("msg", "hello");
		stringRedisTemplate.opsForValue().append("msg", "yooooo");

		// read String from redis
		String res1 = stringRedisTemplate.opsForValue().get("msg");
		System.out.println(">>> res1 = " + res1);

		// clean
		stringRedisTemplate.delete("msg");

		/** List op demo */
		stringRedisTemplate.opsForList().leftPush("mylist", "1");
		stringRedisTemplate.opsForList().leftPush("mylist", "2");
		stringRedisTemplate.opsForList().leftPush("mylist", "3");
	}

	/** test save object to redis */
	@Test
	public void Test2(){

		Employee empById = employeeMapper.getEmpById(1);
		System.out.println(">>> empById = " + empById);

		// by default, will use jdk serialization mechanism save serializable obj to redis ( not readable)
		redisTemplate.opsForValue().set("emp1", empById); // may have this error if Employee class is not yet Serializable : Caused by: java.lang.IllegalArgumentException: DefaultSerializer requires a Serializable payload but received an object of type [com.yen.springBootAdvance1.bean.Employee]

		// approach 1) save data with json form

		// approach 2) save data with custom RedisTemplate (implemented by Jackson2JsonRedisSerializer)
		// plz check com.yen.springBootAdvance1.config.MyRedisConfig
		empRedisTemplate.opsForValue().set("emp02", empById);
	}

}
