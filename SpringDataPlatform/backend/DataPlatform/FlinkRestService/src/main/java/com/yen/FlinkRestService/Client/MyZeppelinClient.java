package com.yen.FlinkRestService.Client;

import lombok.extern.slf4j.Slf4j;
import org.apache.zeppelin.client.ClientConfig;
import org.apache.zeppelin.client.ZeppelinClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class MyZeppelinClient {

    // attr
    ZeppelinClient zClient;
    private String ZeppelinURL = "http://localhost:8888";

    /**
     * @PostConstruct :
     * - will run after No args constructor is init (when spring boot scan pkg, can init container)
     * - https://youtu.be/dcmhIij3eNM?si=FKF7YAho4jogK1Vb&t=95
     */
    @PostConstruct
    public void init() {

        ClientConfig clientConfig = new ClientConfig(ZeppelinURL);
        try {
            zClient = new ZeppelinClient(clientConfig);
            log.info("Init ZeppelinClient OK : " + zClient);
        } catch (Exception e) {
            log.error("Init ZeppelinClient fail !!!");
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void destory(){
        // TODO : implement zClient close conn ?
    }

}
