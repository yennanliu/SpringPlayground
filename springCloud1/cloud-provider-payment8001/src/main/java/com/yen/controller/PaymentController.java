package com.yen.controller;

// https://www.youtube.com/watch?v=4wWM7MmfxXw&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=10
// https://www.youtube.com/watch?v=38XoPk5l5DQ&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=14
// https://www.youtube.com/watch?v=uLnMgNai8nc&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=23
// https://www.youtube.com/watch?v=5o6W57mRJA0&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=25
// https://www.youtube.com/watch?v=m_MHwd3Dls4&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=43
// https://www.youtube.com/watch?v=6o4pd_B62SE&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=45

import com.yen.bean.CommonResult;
import com.yen.bean.Payment;
import com.yen.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    @Value("${server.port}") // load server port from application.yml (server: port: 8001)
    private String serverPort;

    @Resource
    PaymentService paymentService;

    @Resource
    private DiscoveryClient discoveryClient;

    /**
     * NOTE !!! since front-end, backend split,
     *  -> it's better to return CommonResult to FE; instead of complex result (binding with biz logic)
     */
    @PostMapping("/create")
    public CommonResult create(@RequestBody Payment payment){ // NOTE !!! we need @RequestBody , so  cloud-consumer-order80 can call cloud-provider-payment8001's payment create method properly

        int result = paymentService.create(payment);
        log.info(">>> create payment = " + payment );
        log.info(">>> paymentService result = " + result);

        if (result > 0){
            return new CommonResult(200, "insert DB ok, serverPort = " + serverPort, result);
        }else{
            return new CommonResult(444, "insert DB failed, serverPort = " + serverPort, null);
        }
    }

    @GetMapping("/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){

        Payment payment = paymentService.getPaymentById(id);
        log.info(">>> id = " + id);
        log.info(">>> getPaymentById result = " + payment);

        if (payment != null){
            return new CommonResult(200, "check payment ok + serverPort = " + serverPort, payment);
        }else{
            return new CommonResult(444, "check payment failed , no such id. id = " + id + " serverPort = " + serverPort, null);
        }
    }

    @GetMapping("/discovery")
    public Object discovery(){

        // get all services
        // https://blog.csdn.net/xixiyuguang/article/details/108484899
        List<String> services = discoveryClient.getServices();

        for (String s: services){
            log.info(">>> service = {}", s);
        }

        // get service instances by service name
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance i : instances){
            log.info(">>> instance ... | serviceId = {}, host = {}, port = {}, uri = {}", i.getServiceId(), i.getHost(), i.getPort(), i.getUri());
        }

        return this.discoveryClient;
    }

    @GetMapping("/lb")
    public String getPaymentLB(){
        return serverPort;
    }

    @GetMapping("/feign/timeout")
    // method for time out demo
    public String paymentFeignTimeout(){
        try{
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return serverPort;
    }

}
