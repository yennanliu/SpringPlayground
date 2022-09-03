﻿package com.example.demo.controller.shop;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: ProductController
 * Author:   longzhonghua
 * Date:     2019/4/12 20:05
 *
 * @Description: $description$
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@Controller
@RequestMapping("product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    private long id;

    @GetMapping("{id}")
    /**
     * @Description: 產品展示頁
     * @Param: [id]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: longzhonghua
     * @Date: 2019/4/12
     */
    public ModelAndView showProduct(@PathVariable("id") long id)  throws  Exception{
        Product product = productRepository.findByid(id);
        ModelAndView mav = new ModelAndView("web/shop/show");
        //產品訊息
        mav.addObject("product", product);
        //取得登入使用者訊息
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        mav.addObject("principals", principal);
        System.out.println(principal.toString());
        return mav;
    }


}
