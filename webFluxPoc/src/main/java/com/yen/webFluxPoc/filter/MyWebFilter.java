package com.yen.webFluxPoc.filter;

// https://youtu.be/xUux3Ycjh7U?si=BUvCkWNOIiCdJRUg&t=2301

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class MyWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        System.out.println("before filter ...");
        Mono<Void> mono = chain.filter(exchange).doFinally(x -> {
            // NOTE !!! since with doFinally, we are sure that below code runs after chain.filter
            System.out.println("after filter ...");
        });

        // NOTE !!! since RX java run asynchronous, so it's NOT guarantee that below code run after chain.filter
        //System.out.println("after filter ...");
        return mono;
    }

}
