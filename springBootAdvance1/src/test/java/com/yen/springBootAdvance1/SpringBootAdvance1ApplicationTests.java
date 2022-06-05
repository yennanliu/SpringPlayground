package com.yen.springBootAdvance1;

// https://www.youtube.com/watch?v=Un_YC0fBKls&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=4

import com.yen.springBootAdvance1.bean.Employee;
import com.yen.springBootAdvance1.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootAdvance1ApplicationTests {

	@Autowired
	EmployeeMapper employeeMapper;

	@Test
	void contextLoads() {

		// test employeeMapper, Mysql employee table
		Employee empById = employeeMapper.getEmpById(1);
		System.out.println(">>> empById = " + empById);

	}

}
