package com.yen.hello.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

// https://www.youtube.com/watch?v=gFz5MLFSQKQ&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=84

@Data
@ConfigurationProperties("yen.hello")
public class HelloProperties {

    private String prefix;
    private String suffix;
}
