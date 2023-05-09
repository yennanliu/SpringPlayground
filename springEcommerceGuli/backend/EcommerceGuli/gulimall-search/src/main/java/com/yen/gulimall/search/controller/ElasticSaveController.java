package com.yen.gulimall.search.controller;

// https://youtu.be/PZW2rOit2s8?t=61

import com.yen.gulimall.common.to.es.SkuEsModel;
import com.yen.gulimall.common.utils.R;
import com.yen.gulimall.search.service.ProductSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/search")
@RestController
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;

    // put product on shelf
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList){

        //productSaveService.productStatusUp(skuEsModelList);
        return R.ok();
    }
}
