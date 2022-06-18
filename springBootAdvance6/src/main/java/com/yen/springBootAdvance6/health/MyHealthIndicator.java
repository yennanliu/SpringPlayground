package com.yen.springBootAdvance6.health;

// https://www.youtube.com/watch?v=dZS2D_kHhCM&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=41

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MyHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {

        /** define health check logic*/

        // if health
        //return Health.up().build();

        // if NOT health
        return Health.down().withDetail("msg", "service is wrong").build();
    }

}
