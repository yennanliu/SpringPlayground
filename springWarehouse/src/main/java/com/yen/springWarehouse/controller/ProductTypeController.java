package com.yen.springWarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.ProductType;
import com.yen.springWarehouse.service.ProductTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        final String TYPE_ID = "type_id";
        final String TYPE_NAME = "type_name";

        log.info(">>> (create_batch) file = " + file.getSize());

        if (file.isEmpty()) {
            map.put("message", "Please select a CSV file to upload.");
            map.put("status", false);
            return "redirect:/productType/list";
        }

        BufferedReader bufferedReader;
        List result = new ArrayList<>();

        // TODO : refactor below as a service
        try {
            String line;
            InputStream inputStream = file.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                String[] productTypeData = line.split(",");
                log.info(">>> (create_batch) line = " + line);
                if (productTypeData == null || productTypeData.length != 2) {
                    throw new RuntimeException("Input csv schema is wrong");
                }
                if (TYPE_ID.equalsIgnoreCase(productTypeData[0]) && TYPE_NAME.equalsIgnoreCase(productTypeData[1])) {
                    continue; // first line is header
                }
                ProductType productType = new ProductType();
                productType.setTypeId(Integer.parseInt(productTypeData[0]));
                productType.setTypeName(productTypeData[1]);
                result.add(productType);
            }
            // save to DB
            productTypeService.saveBatch(result);
            map.put("status", true);
        } catch (Exception e) {
            log.error(">>> (create_batch) exception : " + e);
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
