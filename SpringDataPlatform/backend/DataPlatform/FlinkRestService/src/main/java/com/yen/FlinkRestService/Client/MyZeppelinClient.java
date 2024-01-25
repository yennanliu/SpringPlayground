package com.yen.FlinkRestService.Client;

import lombok.extern.slf4j.Slf4j;
import org.apache.zeppelin.client.ClientConfig;
import org.apache.zeppelin.client.ZeppelinClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class MyZeppelinClient {

    // attr
    ZeppelinClient zeppelinClient;
    private String ZeppelinURL = "http://localhost:9080"; //"http://localhost:8082";

    /**
     * @PostConstruct :
     * - will run after No args constructor is init (when spring boot scan pkg, can init container)
     * - https://youtu.be/dcmhIij3eNM?si=FKF7YAho4jogK1Vb&t=95
     */
    @Bean
    public ZeppelinClient init() {

        ClientConfig clientConfig = new ClientConfig(ZeppelinURL);
        try {
            this.zeppelinClient = new ZeppelinClient(clientConfig);
            log.info("ZeppelinClient RestUrl = " + zeppelinClient.getClientConfig().getZeppelinRestUrl());
            log.info("Init ZeppelinClient OK : " + zeppelinClient.toString());
        } catch (Exception e) {
            log.error("Init ZeppelinClient fail !!!");
            throw new RuntimeException(e);
        }
        return this.zeppelinClient;
    }

//    @PreDestroy
//    public void destory(){
//        // TODO : implement zClient close conn ?
//    }

}
