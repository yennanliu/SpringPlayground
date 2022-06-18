package com.yen.consumeruser.controller;

// https://www.youtube.com/watch?v=sXc0bmtJ-cw&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=37

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserController {

    @Autowired
    RestTemplate restTemplate;

    /**
     *  run command via RPC (remote procedure call)
     */
    @GetMapping("/buy")
    public String buyTicket(String name){

        // ticket provider service
        String s = restTemplate.getForObject("http://PROVIDER-TICKET/ticket", String.class);

        return ">>>" + name + "buy a ticket !!!" + s;
    }

}
