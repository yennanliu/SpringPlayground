//package com.yen.springcloud.config;
//
//// https://www.youtube.com/watch?v=Yx6V1UJnHnE&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=71
//
///** optional, we should use application.yml conf as 1st priority
// *
// *   2 ways we can set up gateway conf
// *
// *      1) application.yml
// *      2) code (e.g. GatewayConfig)
// */
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class GatewayConfig {
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
//
//        RouteLocatorBuilder.Builder routes =  routeLocatorBuilder.routes();
//
//        /**
//         *   Route rule :
//         *
//         *   when visit localhost:9527/xxx
//         *   -> will re-direct to https://www.google.com/
//         *
//         */
//        routes.route("my-path-1",
//                r -> r.path("/xxx")
//                      .uri("https://www.google.com/")
//                ).build();
//
//        return routes.build();
//    }
//
//}
