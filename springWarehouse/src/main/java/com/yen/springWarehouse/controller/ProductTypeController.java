package com.yen.springWarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.ProductType;
import com.yen.springWarehouse.service.ProductTypeService;
import com.yen.springWarehouse.util.csvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/productType")
@Slf4j
public class ProductTypeController {

    @Autowired
    ProductTypeService productTypeService;

    @GetMapping("/toInput")
    public String input(Map<String, Object> map) {

        map.put("productType", new ProductType()); // TODO : check necessary ?
        return "productType/input_product_type";
    }

    @PostMapping("/create")
    public String create(ProductType productType) {

        productTypeService.save(productType);
        return "redirect:/productType/list";
    }

    // https://betterjavacode.com/java/how-to-file-upload-using-spring-boot
    @PostMapping("/create_batch")
    public String createBatch(@RequestParam("file") MultipartFile file, Map<String, Object> map) {

        if (file.isEmpty()) {
            map.put("message", "Please select a CSV file to upload.");
            map.put("status", false);
            return "redirect:/productType/list";
        }

        BufferedReader bufferedReader;
        List<String> res = new ArrayList<>();
        csvUtil csv_util = new csvUtil();
        try {
            res = csv_util.loadCsvAsList(file);
            //System.out.println(">>> res = " + res); // >>> res = ["100001,type-1", "100002,type-2", "100003,type-3"]
            List<ProductType> productTypeList = res.stream().map(data -> {
                ProductType productType = new ProductType();
                String[] _data = data.toString().replace("|", "").split(",");
                productType.setTypeId(Integer.parseInt(_data[0]));
                productType.setTypeName(_data[1]);
                return productType;
            }).collect(Collectors.toList());
            productTypeService.saveBatch(productTypeList);
            map.put("status", true);
        } catch (Exception e) {
            log.error(">>> load csv failed : " + e);
            map.put("status", false);
        }
        return "redirect:/productType/list";
    }

    @GetMapping("/list")
    public String list(Map<String, Object> map, @RequestParam(value = "pageNo", required = false, defaultValue = "1") String pageNoStr) {

        int pageNo;

        // check pageNo
        pageNo = Integer.parseInt(pageNoStr);
        if (pageNo < 1) {
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

    @GetMapping(value="/preUpdate/{typeId}")
    public String preUpdate(@PathVariable("typeId") Integer typeId, Map<String, Object> map) {

        log.info(">>> preUpdate typeId : {}, map = {}", typeId, map);
        ProductType productType = productTypeService.getById(typeId);
        log.info(">>> productType = {}", productType);
        map.put("productType", productType);
        return "productType/update_product_type";
    }

    @PostMapping(value="/update")
    public String update(ProductType productType) {

        log.info("update product type as {}", productType);
        productTypeService.updateById(productType);
        return "redirect:/productType/list";
    }

}
