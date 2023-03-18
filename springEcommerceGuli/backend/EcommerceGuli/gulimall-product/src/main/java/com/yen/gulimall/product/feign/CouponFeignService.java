package com.yen.gulimall.product.feign;

import com.yen.gulimall.common.to.SkuReductionTo;
import com.yen.gulimall.common.to.SpuBoundTo;
import com.yen.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// https://youtu.be/2Fgtxnc9ehQ?t=216
// https://youtu.be/2Fgtxnc9ehQ?t=471
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    /**
     *  1) CouponFeignService.saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo)
     *      - step 1-1) @RequestBody transform SpuBoundTo to Json
     *      - step 1-2) find gulimall-coupon service,  put obtained Json to request body
     *             and send a remote request to "/coupon/spubounds/save" end point,
     *      - step 1-3) remote service (gulimall-coupon) receive request (request body with Json data)
     *             @RequestBody SpuBoundsEntity spuBounds : transform Json in request body to SpuBoundsEntity
     *
     *  2) Summary:
     *      - If Json model is compatible, feign service, and feign client CAN use different To (data transfer object)
     */
    @PostMapping("/coupon/spubounds/save") // NOTE : have to use full path (from coupon service's : com.yen.gulimall.coupon.controller.SpuBoundsController)
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo); // @RequestBody : transform json to the class object

    /**
     *  https://youtu.be/2Fgtxnc9ehQ?t=1059
     *
     *  1) make feign client call to coupon service : com.yen.gulimall.coupon.controller.SkuFullReductionController
     *  2)
     */
    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
