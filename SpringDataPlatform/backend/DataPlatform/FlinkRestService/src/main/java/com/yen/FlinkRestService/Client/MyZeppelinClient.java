package com.yen.FlinkRestService.Client;

import lombok.extern.slf4j.Slf4j;
import org.apache.zeppelin.client.ClientConfig;
import org.apache.zeppelin.client.ZeppelinClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Slf4j
@Component
public class MyZeppelinClient {

    private ZeppelinClient zeppelinClient;

    @Value("${zeppelin.base_url}")
    private String zeppelinUrl;

    @Bean
    public ZeppelinClient zeppelinClient() {
        ClientConfig clientConfig = new ClientConfig(zeppelinUrl);
        try {
            this.zeppelinClient = new ZeppelinClient(clientConfig);
            log.info("ZeppelinClient initialized, RestUrl={}", zeppelinClient.getClientConfig().getZeppelinRestUrl());
        } catch (Exception e) {
            log.error("Failed to initialize ZeppelinClient: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize ZeppelinClient", e);
        }
        return this.zeppelinClient;
    }

    @PreDestroy
    public void cleanup() {
        if (zeppelinClient != null) {
            try {
                // ZeppelinClient uses HTTP client internally, no explicit close needed
                // but we log the shutdown for visibility
                log.info("ZeppelinClient shutting down");
                zeppelinClient = null;
            } catch (Exception e) {
                log.warn("Error during ZeppelinClient cleanup: {}", e.getMessage());
            }
        }
    }
}
