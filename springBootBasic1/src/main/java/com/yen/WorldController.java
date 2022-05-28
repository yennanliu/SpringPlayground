package com.yen;

// https://www.youtube.com/watch?v=dJIksiVQDj4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=8

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  NOTE !!! this file can be scanned & used by main app is because
 *      -> we add @SpringBootApplication(scanBasePackages = "com.yen") to MainApplication.java
 *
 *      ref : https://youtu.be/dJIksiVQDj4?t=590
 *
 */
@RestController
public class WorldController {

    @RequestMapping("/world")
    public String world1(){
        return "world 111";
    }
}
