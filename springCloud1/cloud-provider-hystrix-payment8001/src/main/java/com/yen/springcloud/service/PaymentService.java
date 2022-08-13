package com.yen.springcloud.service;

// https://www.youtube.com/watch?v=BFYHIlX_Sts&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=51

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
    public String paymentInfo_TIMEOUT(Integer id){

        int timeNumber = 3;
        // sleep for 3 sec
        try{
            TimeUnit.SECONDS.sleep(timeNumber);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        String msg = ">>> thread pool : " + Thread.currentThread().getName() + " paymentInfo_TIMEOUT" + " id = " + id + " time cost = " + timeNumber + "\t" + "-> OP failed, timeout ...";
        return msg;
    }

}
