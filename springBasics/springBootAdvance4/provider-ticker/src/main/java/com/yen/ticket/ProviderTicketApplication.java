package com.yen.ticket;

// https://www.youtube.com/watch?v=tF_Y4G1FovQ&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=33
// https://www.youtube.com/watch?v=tF_Y4G1FovQ&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=34

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  ProviderTicketApplication
 *
 *    Register provider service to registry center:
 *    - step 1) import dubbo and ZK client dependency
 *    - step 2) config dubbo scan package and registry center url
 *    - step 3) publish service via @Service (TicketServiceImpl in this project)
 */
@SpringBootApplication
public class ProviderTicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderTicketApplication.class, args);
    }
}