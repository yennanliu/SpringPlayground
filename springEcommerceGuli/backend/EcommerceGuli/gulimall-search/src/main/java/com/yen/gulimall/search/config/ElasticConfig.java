package com.yen.gulimall.search.config;

// https://youtu.be/EIymTNQn8XE?t=662
// https://youtu.be/EIymTNQn8XE?t=770

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 *  1) add dep
 *  2) add config
 *  3) method return RestHighLevelClient, and inject it to spring boot container (@Bean)
 */
@SpringBootConfiguration
public class ElasticConfig {

    // https://youtu.be/YXSti7iDv8Y?t=96
    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esClient() {

        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(
                        // add multiple nodes if ES cluster
                        new HttpHost("localhost", 9200, "http"))
        );
        return restHighLevelClient;
    }

}