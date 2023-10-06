package com.yen.springWarehouse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.Product;
import com.yen.springWarehouse.service.ProductService;
import com.yen.springWarehouse.service.ProductTypeService;
import com.yen.springWarehouse.util.ProductQueryHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController {

    private int PAGE_SIZE = 3;

    @Autowired
    ProductService productService;

    @Autowired
    ProductTypeService productTypeService;

    @GetMapping("/toInput")
    public String toInput(Map<String, Object> map, Product product) {

        map.put("courseTypeList", productTypeService.list());
        product.setProductStatus("O");
        //product.setCourseReqs(new String[]{"a","b"}); // TODO : check if necessary
        log.info("new product = " + product);
        map.put("product", product);
        return "product/input_product";
    }

    @RequestMapping("/list")
    public String list(@RequestParam(value="pageNo", required=false, defaultValue="1") String pageNoStr, Map<String, Object> map, ProductQueryHelper helper) {

        log.info(">>> (ProductController list) pageNoStr = {}, map = {}, helper = {}", pageNoStr, map, helper);
        int pageNo;
        // check pageNo
        pageNo = Integer.parseInt(pageNoStr);
        if(pageNo < 1){
            pageNo = 1;
        }

        Page<Product> page = productService.getProductPage(helper, pageNo, PAGE_SIZE);
        map.put("productTypeList", productTypeService.list());
        map.put("page", page);
        map.put("helper", helper);
        log.info("(ProductController list) map = {}", map);
        return "product/list_product";
    }

    @PostMapping(value="/remove/{courseNo}")
    public String remove(@PathVariable("productNo") String productNo) {

        productService.removeById(productNo);
        return "redirect:/product/list";
    }

    @GetMapping(value="/preUpdate/{productNo}")
    public String preUpdate(@PathVariable("productNo") String productNo, Map<String, Object> map) {

        Product product = productService.getById(productNo);
        // TODO : fix below
        //product.setTypeId(courseTypeService.getById(course.getTypeId()));
        map.put("product" , product);
        map.put("productTypeList", productTypeService.list());
        return "product/update_product";
    }

}
