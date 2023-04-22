package com.yen.gulimall.ware.feign;

// https://youtu.be/L83Bxqy8UEE?t=1591

import com.yen.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-product")
public interface ProductFeignService {

    /**
     *  /product/skuinfo/info/{skuId}
     *   or
     *  /api/product/skuinfo/info/{skuId}
     *
     *   1) Approach 1) : all requests sent to gate-way
     *      -> @FeignClient("gulimall-gateway")
     *      -> send request to the gulimall-gateway instance
     *      -> setup endpoint url : @RequestMapping("/api/product/skuinfo/info/{skuId}")
     *
     *   2) Approach 2) : send request to gulimall-product directly
     *      -> @FeignClient("gulimall-product")
     *      -> setup endpoint url : @RequestMapping("/product/skuinfo/info/{skuId}")
     *
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);

}
