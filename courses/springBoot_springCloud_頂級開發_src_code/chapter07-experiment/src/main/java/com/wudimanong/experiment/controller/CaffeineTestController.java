package com.wudimanong.experiment.controller;

import com.wudimanong.experiment.dao.model.AbtestExpInfoPO;
import com.wudimanong.experiment.service.CaffeineTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangqiao
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class CaffeineTestController {

    /**
     * 实验配置业务层依赖接口
     */
    @Autowired
    CaffeineTestService caffeineTestService;

    /**
     * 根据实验业务标签获取实验信息
     *
     * @param factorTag
     */
    @GetMapping("/findByFactorTag")
    public AbtestExpInfoPO findByFactorTag(@RequestParam("factorTag") String factorTag) throws InterruptedException {
        Long startTime1 = System.currentTimeMillis();
        AbtestExpInfoPO abtestExpInfoPO = caffeineTestService.getExpInfoByFactorTag(factorTag);
        long endTime1 = System.currentTimeMillis();
        System.out.println("第一次耗时（数据库获取）->" + (endTime1 - startTime1) + " 毫秒");

        Long startTime2 = System.currentTimeMillis();
        AbtestExpInfoPO abtestExpInfoPO2 = caffeineTestService.getExpInfoByFactorTag(factorTag);
        long endTime2 = System.currentTimeMillis();
        System.out.println("第二次耗时（从缓存获取）->" + (endTime2 - startTime2) + " 毫秒");

        //线程休眠5秒，以便缓存失效后查看效果
        Thread.sleep(5000);

        Long startTime3 = System.currentTimeMillis();
        AbtestExpInfoPO abtestExpInfoPO3 = caffeineTestService.getExpInfoByFactorTag(factorTag);
        long endTime3 = System.currentTimeMillis();
        System.out.println("第三次次耗时（从数据库获取）->" + (endTime3 - startTime3) + " 毫秒");

        Long startTime4 = System.currentTimeMillis();
        AbtestExpInfoPO abtestExpInfoPO4 = caffeineTestService.getExpInfoByFactorTag(factorTag);
        long endTime4 = System.currentTimeMillis();
        System.out.println("第四次次耗时（从缓存获取）->" + (endTime4 - startTime4) + " 毫秒");
        return abtestExpInfoPO4;
    }
}
