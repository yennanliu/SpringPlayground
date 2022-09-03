﻿package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: ProductControllerTest
 * Author:   longzhonghua
 * Date:     2019/5/5 11:01
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductControllerTest {
    //每頁數量
    private Integer PAGESIZE=10;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void save() {
        long id= System.currentTimeMillis();
        Product product = new Product(id,
                "紅富士","水果",7.99,"/img/p1.jpg","這是一個測試商品");
        productRepository.save(product);

        System.out.println(product.getId());
    }


    @Test
    public void getProduct() {
        Product product = productRepository.findByName("紅富士");
        System.out.println(product.getId());

    }
    @Test
    public void update() {
        long id=1557032203515L;
  Product product = new Product(id,
          "金帥","水果",7.99,"/img/p1.jpg","金帥也和紅富士一樣，非常好吃，脆脆的");
   productRepository.save(product);
    }

    @Test
    public void getProductById() {
        Product product = productRepository.findById(1557032203515L);
        System.out.println(product.getName()+product.getBody());
    }
    @Test
    public void delete() {
        long id=1557032203515L;
      productRepository.deleteById(id);
    }

    @Test
    public void getAll() {
        Iterable<Product> list = productRepository.findAll(Sort.by("id").ascending());
        for (Product product : list) {
            System.out.println(product);
        }

    }

}