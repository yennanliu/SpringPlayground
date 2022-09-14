package com.yen.springcloud.filter;

// https://www.youtube.com/watch?v=DyBm-5AAsuM&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=74

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;


/** NOTE !!! for user-defined filter, we HAVE to implement GlobalFilter, Ordered */
@Component
public class MyLogGateWayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println(">>> MyLogGateWayFilter : " + new Date());
        // exchange is request stuff, encapsulated by spring boot
        String uname = exchange.getRequest().getQueryParams().getFirst("uname");
        if (uname == null){
            System.out.println(">>> uname is null, invalid user");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    // for ordering purpose, not important here
    @Override
    public int getOrder() {
        return 0;
    }

}
