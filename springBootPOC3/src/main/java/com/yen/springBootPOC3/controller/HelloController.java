package com.yen.springBootPOC3.controller;

/** book p.18 */

import com.yen.springBootPOC3.properties.MysqlProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HelloController {

    // p.23
    @Resource // attr, TODO : check this
    private MysqlProperties mysqlProperties;


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


    @GetMapping("/showJDBC2")
    public String showJDBC2(){

        mysqlProperties.setJdbcName("com.mysql.jdbc.Driver");
        mysqlProperties.setDbUrl("jdbc:mysql://localhost:3036/mytest");
        String msg = "jdbcName = " + mysqlProperties.getJdbcName() + "<br/>" + " dburl = " + mysqlProperties.getDbUrl();

        return msg;
    }

}
