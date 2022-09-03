package com.wudimanong.devops.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangqiao
 */
@RestController
@RequestMapping("/devops")
public class DevopsTestController {

    @GetMapping("/test")
    public String devopsTest() {
        return "自动化发布示范工程测试接口返回->OK!";
    }
}
