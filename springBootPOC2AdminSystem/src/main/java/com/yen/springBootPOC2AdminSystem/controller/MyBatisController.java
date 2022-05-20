package com.yen.springBootPOC2AdminSystem.controller;

// https://www.youtube.com/watch?v=Iz69cdsPFkA&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=64

import com.yen.springBootPOC2AdminSystem.bean.Product;
import com.yen.springBootPOC2AdminSystem.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class MyBatisController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ProductService productService;

    @ResponseBody
    @GetMapping("/acct")
    public Product getId(@RequestParam("id") Long id){
        return productService.getProduct(id);
    }

}
