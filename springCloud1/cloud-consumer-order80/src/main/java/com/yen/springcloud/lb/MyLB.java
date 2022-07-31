package com.yen.springcloud.lb;

// https://www.youtube.com/watch?v=m_MHwd3Dls4&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=43

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyLB implements MyLoadBalancer {

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstanceList) {
        return null;
    }

}
