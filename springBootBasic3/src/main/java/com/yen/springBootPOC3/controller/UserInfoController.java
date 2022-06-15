package com.yen.springBootPOC3.controller;

import com.yen.springBootPOC3.entity.DemoInfo;
import com.yen.springBootPOC3.service.DemoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/** book p.147 */


@RestController
public class UserInfoController {

    @Autowired
    private DemoInfoService demoInfoService;

    @GetMapping("/test")
    public String test(){

        // save 2 records
        DemoInfo demoInfo = new DemoInfo();
        demoInfo.setName("JACK");
        demoInfo.setPwd("123");

        DemoInfo demoInfo2 = demoInfoService.save(demoInfo);

        // no cache
        System.out.println(demoInfoService.findById(demoInfo2.getId()));

        // cache
        System.out.println(demoInfoService.findById(demoInfo2.getId()));
        DemoInfo demoInfo3 = new DemoInfo();
        demoInfo3.setName("ANN");
        demoInfo3.setPwd("123");

        return "ok";
    }

}
