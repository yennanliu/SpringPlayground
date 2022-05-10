package com.yen.springBootPOC3.controller;

/** book p.18 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // inject attr val
    // book p.20
    @Value("${mysql.jdbcName}")
    private String jdbcName;

    @Value("${mysql.dburl}")
    private String dburl;

    @GetMapping("/hello")
    public String hello(){
        return "hello world!!!";
    }

    @GetMapping("/showJDBC")
    public String showJDBC(){
        String msg = "jdbcName = " + jdbcName + "<br/>" + " dburl = " + dburl;
        return msg;
    }

}
