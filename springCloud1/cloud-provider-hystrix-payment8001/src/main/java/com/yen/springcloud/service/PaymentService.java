package com.yen.springcloud.service;

// https://www.youtube.com/watch?v=BFYHIlX_Sts&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=51
// https://www.youtube.com/watch?v=lKBUCu8rItI&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=54

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    /** normal access (must be no error) */
    public String paymentInfo_OK(Integer id){

        String msg = ">>> thread pool : " + Thread.currentThread().getName() + " paymentInfo_OK" + " id = " + id + "\t" + "-> OP OK ...";
        return msg;
    }

    /** normal access (must be an error) */
    @HystrixCommand(
            fallbackMethod = "paymentInfo_TimeoutHandler",
            commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "3000")}) // backup method when timeout happen
    public String paymentInfo_Timeout(Integer id){

        int timeNumber = 5;
        // sleep for 5 sec

        // int age = 10 / 0 ;
        try{
            TimeUnit.SECONDS.sleep(timeNumber);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        String msg = ">>> thread pool : " + Thread.currentThread().getName() + " paymentInfo_TIMEOUT" + " id = " + id + " time cost = " + timeNumber + "\t" + "-> OP failed, timeout ...";
        return msg;
    }

    public String paymentInfo_TimeoutHandler(Integer id){
        String msg = ">>> (Default result) thread pool : " + Thread.currentThread().getName() + " paymentInfo_TimeoutHandler" + " id = " + id;
        return msg;
    }

}
