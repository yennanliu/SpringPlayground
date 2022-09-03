package com.wudimanong.access.demo.controller;

import com.wudimanong.access.demo.entity.TestShuntEffectBO;
import com.wudimanong.access.demo.entity.TestShuntEffectDTO;
import com.wudimanong.access.demo.service.AbtestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangqiao
 */
@Slf4j
@RestController
@RequestMapping("/abtest")
public class AbtestController {

    @Autowired
    AbtestService abtestServiceImpl;

    @GetMapping("/testShuntEffect")
    public TestShuntEffectBO testShuntEffect(TestShuntEffectDTO testShuntEffectDTO) {
        return abtestServiceImpl.testShuntEffect(testShuntEffectDTO);
    }
}
