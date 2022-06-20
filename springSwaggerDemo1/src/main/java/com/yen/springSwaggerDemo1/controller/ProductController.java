package com.yen.springSwaggerDemo1.controller;

import com.yen.springSwaggerDemo1.bean.Product;
import com.yen.springSwaggerDemo1.bean.User;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class ProductController {

    @GetMapping("/product/test")
    public String hello(){
        System.out.println(">>> hello product !!!");
        return "hello product !!!";
    }

    @GetMapping("/product/{id}")
    @Parameter(description = "Define product by id.", required = false, allowEmptyValue = true)
    public Product getProdById(int id){
        // make a fake response below
        return new Product(id, "my_prod", new Date(), "stock");
    }

}
