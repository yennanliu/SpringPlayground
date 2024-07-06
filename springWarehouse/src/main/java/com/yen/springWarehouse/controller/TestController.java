package com.yen.springWarehouse.controller;

import com.yen.springWarehouse.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("/getLock")
    @ResponseBody
    public String getValueAndLock() throws Exception {

        //System.out.println(testService.getValue("some param"));
        System.out.println(testService.getValue("sleep"));
        return "getValueAndLock done";
    }

}
