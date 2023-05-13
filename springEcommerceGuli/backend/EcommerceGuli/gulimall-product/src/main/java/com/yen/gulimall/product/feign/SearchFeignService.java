package com.yen.gulimall.product.feign;

// https://youtu.be/PZW2rOit2s8?t=932

import com.yen.gulimall.common.to.es.SkuEsModel;
import com.yen.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient("gulimall-search")
public interface SearchFeignService {

    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList);
}

