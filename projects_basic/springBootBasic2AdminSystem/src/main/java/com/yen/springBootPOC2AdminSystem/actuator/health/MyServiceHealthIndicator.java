package com.yen.springBootPOC2AdminSystem.actuator.health;

// https://www.youtube.com/watch?v=p8V2S8LHtRw&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=79

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
// name shown in http://localhost:8888/actuator/health : "MyService"
public class MyServiceHealthIndicator extends AbstractHealthIndicator {

    /**
     * Implement our health check logic
     */
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {

        // if mongoDB, do connection test
        Map<String, Object> map = new HashMap<>();

        if (1 == 1){ // fake logic
            map.put("count", 1);
            map.put("ms", 100);
            //builder.up(); // health, method 1
            builder.status(Status.UP); // method 2
        }else {
            map.put("err", "conn fail");
            map.put("ms", 3000);
            //builder.down(); // un-health, method 1
            builder.status(Status.OUT_OF_SERVICE); // method 2
        }

        // add user-defined k-v to builder detail
        builder.withDetail("code", 100)
                .withDetails(map);
    }

}
