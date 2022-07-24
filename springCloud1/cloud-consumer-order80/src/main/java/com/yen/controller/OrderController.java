package com.yen.controller;

// https://www.youtube.com/watch?v=8d6BvCZxPwQ&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=13
// https://www.youtube.com/watch?v=uLnMgNai8nc&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=23
// https://www.youtube.com/watch?v=g-xOH7s1zXs&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=38

import com.yen.bean.CommonResult;
import com.yen.bean.Payment;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/** Payment consumer ( consumer-order-80)
 *
 *  -> will call cloud-provider-payment8001 services via httpClient (via RestTemplate)
 */

@RestController
@Slf4j
@RequestMapping("/consumer")
public class OrderController {

    // single node mode
    //public static final String PAYMENT_URL = "http://localhost:8001";
    // cluster mode (NOTE !!! we ONLY care service name; instead of actual url) (with service name http://CLOUD-PAYMENT-SERVICE, consumer-80 will use either payment8001 or payment8002)
    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";

    @Resource
    RestTemplate restTemplate;

    @GetMapping("/payment/create")
    public CommonResult<Payment> create(Payment payment){
        /** NOTE !!! call cloud-provider-payment8001 services via httpClient
         *
         *  -> postForObject for calling POST request
         */
        log.info(">>> consumer-order-80 call /payment/create ...", payment);
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
        /** NOTE !!! call cloud-provider-payment8001 services via httpClient
         *
         *  -> getForObject for calling GET request
         */
        log.info(">>> consumer-order-80 call /payment/get/{id} ...", id);
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
    }

    /**
     *  for restTemplate,
     *      -> if want simple json response -> use getForObject
     *      -> if want detail info response -> use getForEntity
     */
    @GetMapping("/payment/getForEntity/{id}")
    public CommonResult<Payment> getPayment2(@PathVariable("id") Long id){
        
        log.info(">>> consumer-order-80 call /payment/getForEntity/{id} ...", id);
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);

        log.info(">>> entity.getStatusCode() = " + entity.getStatusCode());

        // NOTE here !!! have to check response status via getStatusCode
        if (entity.getStatusCode().is2xxSuccessful()){
            return entity.getBody();
        }else{
            return new CommonResult<>(444, "getPayment2 op failed");
        }
    }

}
