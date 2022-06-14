package com.yen.springBootAdvance3.controller;

// https://www.youtube.com/watch?v=QL_JfKt-n4o&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=25

import com.yen.springBootAdvance3.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AsyncController {

    @Autowired
    AsyncService asyncService;

    @GetMapping("/hello")
    public String hello(){
        asyncService.hello();
        return "success";
    }

}
