package com.yen.springWarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yen.springWarehouse.bean.ProductType;
import com.yen.springWarehouse.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/test")
@RestController
public class TestController {

    /** productType */
    @Autowired
    ProductTypeService productTypeService;

    @GetMapping("/type_list")
    List<ProductType> getAllProductType(){
        return productTypeService.list(new QueryWrapper<>());
    }

}
