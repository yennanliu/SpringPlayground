package com.yen.controller;

// https://www.youtube.com/watch?v=4wWM7MmfxXw&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=10
// https://www.youtube.com/watch?v=38XoPk5l5DQ&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=14

import com.yen.entities.CommonResult;
import com.yen.entities.Payment;
import com.yen.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    @Resource
    PaymentService paymentService;

    /**
     * NOTE !!! since front-end, backend split,
     *  -> it's better to return CommonResult to FE; instead of complex result (binding with biz logic)
     */
    @PostMapping("/create")
    public CommonResult create(@RequestBody Payment payment){ // NOTE !!! we need @RequestBody , so  cloud-consumer-order80 can call cloud-provider-payment8001's payment create method properly

        int result = paymentService.create(payment);
        log.info(">>> create payment = " + payment);
        log.info(">>> paymentService result = " + result);

        if (result > 0){
            return new CommonResult(200, "insert DB ok", result);
        }else{
            return new CommonResult(444, "insert DB failed", null);
        }
    }

    @GetMapping("/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){

        Payment payment = paymentService.getPaymentById(id);
        log.info(">>> id = " + id);
        log.info(">>> getPaymentById result = " + payment);

        if (payment != null){
            return new CommonResult(200, "check payment ok", payment);
        }else{
            return new CommonResult(444, "check payment failed , no such id. id = " + id, null);
        }
    }

}
