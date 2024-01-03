package com.yen.springWarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yen.springWarehouse.bean.Product;
import com.yen.springWarehouse.bean.ProductType;
import com.yen.springWarehouse.service.ProductService;
import com.yen.springWarehouse.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    ProductTypeService productTypeService;

    @Autowired
    ProductService productService;


    /** productType */
    @GetMapping("/type")
    List<ProductType> getAllProductType(){

        return productTypeService.list(new QueryWrapper<>());
    }


    /** product */
    @GetMapping("/prod")
    List<Product> getAllProduct(){

        return productService.list(new QueryWrapper<>());
    }

    @GetMapping("/prod/deduct/{productId}")
    String deductProduct(@PathVariable("productId") Integer productId){

        productService.deduct(productId);
        return "OK";
    }

}
