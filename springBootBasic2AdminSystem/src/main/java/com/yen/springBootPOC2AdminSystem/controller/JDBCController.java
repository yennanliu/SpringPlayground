package com.yen.springBootPOC2AdminSystem.controller;

// https://www.youtube.com/watch?v=AOepp1XLSR0&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=61

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  Controller for JDBC
 */

@Slf4j
@Controller
public class JDBCController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @ResponseBody
    @GetMapping("/sql")
    public String queryFromDB(){
        Long res1 = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM product", Long.class);
        log.info(">>> query res1 = {}", res1);
        return res1.toString();
    }
}
