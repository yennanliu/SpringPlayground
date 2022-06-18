package com.yen.consumeruser.controller;

// https://www.youtube.com/watch?v=sXc0bmtJ-cw&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=37

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    /**
     *  run command via RPC (remote procedure call)
     */
    @GetMapping("/buy")
    public String buyTicket(String name){
        return ">>>" + name + "buy a ticket !!!";
    }

}
