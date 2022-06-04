package com.yen.hello.service;

// https://www.youtube.com/watch?v=gFz5MLFSQKQ&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=84

import com.yen.hello.bean.HelloProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  NOTE !!! we DON'T need any annotation (e.g. @Service, @Componenet...) here
 *
 *  -> HelloService will be loaded by other modules via autoConfigured
 */
public class HelloService {

    @Autowired
    HelloProperties helloProperties;
    
    public String sayHello(String userName){
        return helloProperties.getPrefix() + " : " + userName + helloProperties.getSuffix();
    }

}
