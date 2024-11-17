package com.yen.ticket.service.impl;

// https://www.youtube.com/watch?v=tF_Y4G1FovQ&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=33
// https://www.youtube.com/watch?v=tF_Y4G1FovQ&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=34

import com.alibaba.dubbo.config.annotation.Service;
import com.yen.ticket.service.TicketService;
import org.springframework.stereotype.Component;

@Component
@Service // publish service
public class TicketServiceImpl implements TicketService {
    @Override
    public String getTicket() {
        return "movie ticket";
    }

}
