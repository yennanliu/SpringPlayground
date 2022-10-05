package com.yen.providerticket.controller;

// https://www.youtube.com/watch?v=0yzA8_In-vg&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=35

import com.yen.providerticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketController {

    @Autowired
    TicketService ticketService;

    @GetMapping("/ticket")
    public String getTicket(){
        return ticketService.getTicket();
    }

}
