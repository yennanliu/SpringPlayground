package com.yen.springcloud.lb;

// https://www.youtube.com/watch?v=m_MHwd3Dls4&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=43

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyLB implements MyLoadBalancer {

    // idea from src code (IRule class)
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public final int getAndIncrement(){

        // attr
        int current;
        int next;

        do {

            current = this.atomicInteger.get();
            next = current >= 2147483647 ? 0 : current + 1;

        }while(!this.atomicInteger.compareAndSet(current, next));

        System.out.println(">>> how many times have visited  ? -> next = " + next);

        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {

        int index = getAndIncrement() % serviceInstances.size();
        System.out.println(">>> index = " + index);
        return serviceInstances.get(index);
    }

}
