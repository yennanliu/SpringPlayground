package com.yen.gulimall.search.controller;

// https://youtu.be/PZW2rOit2s8?t=61

import com.yen.gulimall.common.exception.BizCodeEnum;
import com.yen.gulimall.common.to.es.SkuEsModel;
import com.yen.gulimall.common.utils.R;
import com.yen.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RequestMapping("/search")
@RestController
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;

    // put product on shelf
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList){

        Boolean b = false;

        try{
           b = productSaveService.productStatusUp(skuEsModelList);
        }catch (Exception e){
            log.error("(ElasticSaveController) Product up error : {}", e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }

        if (b){
            return R.ok();
        }else{
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }

}
