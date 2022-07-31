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

        // do-while loop
        do {

            current = this.atomicInteger.get();
            next = current >= 2147483647 ? 0 : current + 1;

            // spinlock (自旋鎖)
            // https://codertw.com/%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/748267/
        }while(!this.atomicInteger.compareAndSet(current, next));

        System.out.println(">>> how many times have visited  ? -> next = " + next);

        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {

        /**
         *  LB (load balance) algorithm
         *
         *  -> (# of rest request) % (# of machines in cluster) =  index (indicate which machine is going to handle request)
         *  -> index starts from 1
         */
        int index = getAndIncrement() % serviceInstances.size();
        System.out.println(">>> index = " + index);

        return serviceInstances.get(index);
    }

}
