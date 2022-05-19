package com.yen.springBootPOC3;

/** Spring boot app entry point
 *
 *  book p.62, p.73
 */

import com.yen.springBootPOC3.entity.Customer;
import com.yen.springBootPOC3.servlet.MyServlet1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
@ServletComponentScan("com.yen.springBootPOC3")
public class SpringBootPoc3Application implements CommandLineRunner {

//	@Bean
//	public ServletRegistrationBean MyServlet1(){
//		return new ServletRegistrationBean(new MyServlet1(), "/myServlet/**");
//	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootPoc3Application.class, args);
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... args) throws Exception {

		log.info("Create table ...");

		// create table Customers
		jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
		jdbcTemplate.execute("CREATE TABLE customers (" + "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

		// split data array into firstName, lastName array
		List<Object[]> splitNames = Arrays
				.asList("Jack", "Ann", "Koo")
				.stream()
				.map(name -> name.split(" "))
				.collect(Collectors.toList());


		jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES(?,?)", splitNames);

		List<Map<String, Object>> res1 = jdbcTemplate.queryForList("SELECT id, first_name, last_name FROM customers");
		
		for (Map<String, Object> record: res1){
			System.out.println(record.toString());
		}
	}

}
