package com.yen.gulimall.member.feign;

import com.yen.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *  https://youtu.be/G1SNCTRcKdE?t=292
 *
 *    1) via below setting
 *      - member service can access coupon service via registry center (Nacos),
 *        then call the endpoint (/coupon/coupon/member/list), so can call CouponController remotely via Feign client
 *
 *    2) this is a declared remote API call (via feign client)
 */
@FeignClient("gulimall-coupon") // the remote service name that you want to call
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list") // copy from CouponController directly, NOTE: HAVE to use full endpoint path
    public R memberCoupons();
}
