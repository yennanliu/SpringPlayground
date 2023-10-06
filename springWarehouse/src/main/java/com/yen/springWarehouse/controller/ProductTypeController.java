package com.yen.springWarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.Product;
import com.yen.springWarehouse.bean.ProductType;
import com.yen.springWarehouse.service.ProductService;
import com.yen.springWarehouse.service.ProductTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/productType")
@Slf4j
public class ProductTypeController {

    @Autowired
    ProductTypeService productTypeService;

    @GetMapping("/toInput")
    public String input(Map<String, Object> map) {

        map.put("courseType", new ProductType()); // TODO : check necessary ?
        return "productType/input_product_type";
    }

    @PostMapping("/create")
    public String create(ProductType productType) {

        productTypeService.save(productType);
        return "redirect:/productType/list";
    }

    @GetMapping("/list")
    public String list(Map<String, Object> map, @RequestParam(value="pageNo", required=false, defaultValue="1") String pageNoStr) {

        int pageNo;

        // check pageNo
        pageNo = Integer.parseInt(pageNoStr);
        if(pageNo < 1){
            pageNo = 1;
        }

        /*
         * 1st param：which page
         * 2nd param : record count per page
         */
        log.info("pageNo = {}", pageNo);
        Page<ProductType> page = new Page<>(pageNo,3);
        QueryWrapper<ProductType> queryWrapper = new QueryWrapper<>();
        IPage<ProductType> iPage = productTypeService.page(page,
                new LambdaQueryWrapper<ProductType>()
                        .orderByAsc(ProductType::getTypeId)
        );
        log.info("iPage.total = {}, iPage.getPages = {} iPage = {}",  iPage.getTotal(), iPage.getPages(), iPage);
        map.put("page", iPage);

        return "productType/list_product_type";
    }

    @PostMapping("/remove/{typeId}")
    public String remove(@PathVariable("typeId") Integer typeId) {

        productTypeService.deleteProductType(typeId);
        return "redirect:/productType/list";
    }

    @PostMapping(value="/update")
    public String update(ProductType productType) {

        log.info("update product type as {}", productType);
        productTypeService.updateById(productType);
        return "redirect:/courseType/list";
    }

}
