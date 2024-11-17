package com.yen.springBootPOC2AdminSystem.actuator.info;

// https://www.youtube.com/watch?v=p8V2S8LHtRw&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=79

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AppInfoContributor implements InfoContributor {
    @Override
    public void contribute(Info.Builder builder) {

        builder.withDetail("hello", "world")
                .withDetail("msg", 123)
                .withDetails(Collections.singletonMap("keyyy", "vallll"));
    }

}
