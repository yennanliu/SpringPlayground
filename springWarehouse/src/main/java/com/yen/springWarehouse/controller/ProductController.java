package com.yen.springWarehouse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.Product;
import com.yen.springWarehouse.service.MerchantService;
import com.yen.springWarehouse.service.ProductService;
import com.yen.springWarehouse.service.ProductTypeService;
import com.yen.springWarehouse.util.ProductQueryHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/product")
//@RestController
public class ProductController {

    private int PAGE_SIZE = 3;

    @Autowired
    ProductService productService;

    @Autowired
    ProductTypeService productTypeService;

    @Autowired
    MerchantService merchantService;

    @GetMapping("/toInput")
    public String toInput(Map<String, Object> map, Product product) {

        map.put("productTypeList", productTypeService.list());
        map.put("merchantList", merchantService.list());
        product.setProductStatus("O");
        //product.setproductReqs(new String[]{"a","b"}); // TODO : check if necessary
        log.info("(toInput) new product = " + product + " map = " + map);
        map.put("product", product);
        return "product/input_product";
    }

    @PostMapping(value="/create")
    public String create(Product product, Map<String, Object> map) throws Exception{

        //读取文件数据，转成字节数组

//        if(file!=null){
//            product.setCourseTextbookPic(file.getBytes());
//        }

        try{
            log.info("(create) Add new product : " + productTypeService);
            productService.save(product);
            log.info("new product save OK");
        }catch(Exception e){
            log.error("new product save failed : " + e.getMessage(),e);
            map.put("productTypeService", productTypeService.list());
            // TODO : redirect to custom error page or return error msg
            //return "redirect:/product/input_course";
        }

        return "redirect:/product/list";
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
        //return "index";
        //return "OK";
    }

    @PostMapping(value="/remove/{productNo}")
    public String remove(@PathVariable("productNo") String productNo) {

        productService.removeById(productNo);
        return "redirect:/product/list";
    }

    @GetMapping(value="/preUpdate/{productNo}")
    public String preUpdate(@PathVariable("productNo") String productNo, Map<String, Object> map) {

        Product product = productService.getById(productNo);
        // TODO : fix below
        //product.setTypeId(productTypeService.getById(product.getTypeId()));
        map.put("product" , product);
        map.put("productTypeList", productTypeService.list());
        return "product/update_product";
    }

    @PostMapping(value="/update")
    public String update(Product product, Map<String, Object> map) throws Exception{

        // TODO : fix below for upload product picture
//        if(file.getBytes().length > 0){
//            course.setCourseTextbookPic(file.getBytes());
//        }

        log.info(" Update product as " + product);
        try{
            // TODO : fix if modify as invalid/not existed typeId (### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails (`warehouse_system`.`product`, CONSTRAINT `FK_PRODUCT_TYPE` FOREIGN KEY (`type_id`) REFERENCES `product_type` (`type_id`)))
            productService.updateById(product);
            log.info("Product update OK");
        }catch(Exception e){
            log.error("Product update Failed, exception : " + e.getMessage(),e);
            log.info("productTypeList = " + productTypeService.list());
            map.put("productTypeList", productTypeService.list());
            // if update failed, return update product page
            return "/product/update_product";
        }

        // if update success, redirect to product list page
        return "redirect:/product/list";
    }

}
