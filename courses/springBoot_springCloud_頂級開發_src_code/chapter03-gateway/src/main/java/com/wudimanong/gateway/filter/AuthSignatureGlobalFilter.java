package com.wudimanong.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author jiangqiao
 * @说明：全局过滤器，进行统一的URL过滤及权限认证
 */
@Configuration
public class AuthSignatureGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().value();
        //判断接口情况地址如果为内部服务定义，则拦截
        if (requestPath.contains("internal")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        //应用accessToken有效性（这里只是简单判空，可以根据实际业务场景进行扩展）
        String accessToken = exchange.getRequest().getHeaders().getFirst("access_token");
        if (accessToken == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        //正常进行返回
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
