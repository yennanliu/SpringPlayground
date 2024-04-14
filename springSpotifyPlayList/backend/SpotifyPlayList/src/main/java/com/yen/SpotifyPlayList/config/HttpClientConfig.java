//package com.yen.SpotifyPlayList.config;
//
//import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
//import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
//@Configuration
//public class HttpClientConfig {
//
//    @Bean
//    public RestTemplate restTemplate() {
//
//        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
//        connectionManager.setMaxTotal(100); // Set the maximum number of connections
//        connectionManager.setDefaultMaxPerRoute(20); // Set the maximum number of connections per route
//
//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//        httpClientBuilder.setConnectionManager(connectionManager);
//
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClientBuilder.build());
//        return new RestTemplate(factory);
//    }
//}
