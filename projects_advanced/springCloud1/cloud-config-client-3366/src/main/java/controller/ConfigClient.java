package controller;

// https://www.youtube.com/watch?v=JJCM5o-zvHw&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=81

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigClient {

    @Value("${server.port}")
    private String serverPort;

    @Value("${config.info}")
    private String configInfo;

    @Value("${/configInfo}")
    public String configInfo(){
        return "serverPort: " + serverPort +  "\t\n\n configInfo:" +  configInfo;
    }

}
