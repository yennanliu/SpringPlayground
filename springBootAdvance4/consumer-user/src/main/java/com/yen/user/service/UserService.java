package com.yen.user.service;

// https://www.youtube.com/watch?v=tF_Y4G1FovQ&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=33

import com.alibaba.dubbo.config.annotation.Reference;
import com.yen.ticket.service.TicketService;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Reference
    TicketService ticketService;

    public void hello(){
        String ticket = ticketService.getTicket();
        System.out.println(">>> got a ticket !!! " + ticket);
    }
}
