package com.yen.springBootPOC3;

/** Spring boot app entry point
 *
 *  book p.62, p.73, p.77
 */


import com.yen.springBootPOC3.dao.UserRepository;
import com.yen.springBootPOC3.entity.Customer;
import com.yen.springBootPOC3.entity.User;
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
		jdbcTemplate.execute("DROP TABLE IF EXISTS customers");
		jdbcTemplate.execute("CREATE TABLE customers (" + "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

		// split data array into firstName, lastName array
		List<Object[]> splitNames = Arrays
				.asList("Jack woo", "Ann Lee", "Koo Shan")
				.stream()
				.map(name -> name.split(" "))
				.collect(Collectors.toList());

		// inset to table (batchUpdate)
		jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES(?,?)", splitNames);

		List<Map<String, Object>> res1 = jdbcTemplate.queryForList("SELECT id, first_name, last_name FROM customers");

		for (Map<String, Object> record: res1){
			System.out.println(record.toString());
		}
	}

	@Bean
	public CommandLineRunner demo(UserRepository repository){
		return (args) -> {

			// create table Customers
			jdbcTemplate.execute("DROP TABLE IF EXISTS user");
			jdbcTemplate.execute("CREATE TABLE user (" + "firstname VARCHAR(255), lastname VARCHAR(255))");

//
//			// save record to table
//			repository.save(new User("iori", "yagami"));
//			repository.save(new User("Ann", "Wu"));
//			repository.save(new User("Betty", "Martin"));
//
//			// print record to console
//			log.info("Users found with findAll()");
//			log.info("--------------------------");
//			for (Object user: repository.findAll()){
//				log.info(user.toString());
//			}
//
//			// get id = 1 record, and print out in console
//			repository.findById(1L)
//					.ifPresent(
//							User -> {
//								log.info("user found with findById(1L): " );
//								log.info(User.toString());
//							}
//					);
			};
	}

}
