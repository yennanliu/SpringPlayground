package com.yen.springcloud.service.controller;

// https://www.youtube.com/watch?v=IcNEars4VfM&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=88

import com.yen.springcloud.service.IMessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendMessageController {

    @Autowired
    private IMessageProvider messageProvider;

    @GetMapping(value="/sendMessage")
    public String sendMessage(){
        return messageProvider.send();
    }

}
