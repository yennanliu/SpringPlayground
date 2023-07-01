package com.yen.gulimall.product.feign;

import com.yen.gulimall.common.utils.R;
import com.yen.gulimall.common.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

/**
 *  Feign client call remote ware service
 *  https://youtu.be/JRPBm5sK4Gg?t=565
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {

    /**
     *  Fetch info in remote feign call response
     *      Method 1) : add generic type to R  ---> used here
     *      Method 2) : return list type (required type) data directly
     *      Method 3) : encapsulate result by ourselves
     */
    // src/main/java/com/yen/gulimall/ware/controller/WareSkuController.java
    @PostMapping("/ware/waresku/hasStock")
    R<List<SkuHasStockVo>> getSkuHasStock(@RequestBody List<Long> skuIds);

}
