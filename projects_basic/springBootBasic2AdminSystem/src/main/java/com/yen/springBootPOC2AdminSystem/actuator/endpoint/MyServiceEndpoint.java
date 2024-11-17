//package com.yen.springBootPOC2AdminSystem.actuator.endpoint;
//
//// https://www.youtube.com/watch?v=p8V2S8LHtRw&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=79
//
//import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
//import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
//import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//import java.util.Map;
//
//@Component
//@Endpoint(id = "MyService")
//public class MyServiceEndpoint {
//
//    // have to use @ReadOperation
//    @ReadOperation
//    public Map getDockerInfo(){
//        return Collections.singletonMap("dockerInfo", "Docker run..");
//    }
//
//    // have to use @WriteOperation
//    @WriteOperation
//    public void stopDocker(){
//        System.out.println("docker stopped ...");
//    }
//
//}
