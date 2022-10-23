package com.yen.gateway.filter;

// book p.3-68
// https://github.com/yennanliu/SpringPlayground/blob/ca0dd43a8c11abdf20116887b33e039859e6ef09/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-gateway/src/main/java/com/wudimanong/gateway/filter/AuthSignatureGlobalFilter.java#L16


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class AuthSignatureGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().value();
        // check api endpoint with routing (routes predicates)(application.yml), block it if not pass
        if (requestPath.contains("internal")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        // verify accessToken（here we only check if it's null, can extend based on requirement ）
        String accessToken = exchange.getRequest().getHeaders().getFirst("access_token");
        if (accessToken == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        // return if normal state
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

}
