package com.example.demo.controller.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: SeckillController
 * Author:   longzhonghua
 * Date:     2019/4/14 14:29
 *
 * @Description: $description$
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@RestController
@RequestMapping("seckill")
public class SeckillController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @Desciption: 建立秒殺庫存
     * @Param: a     * @return: a
     * @Author: longzhonghua
     * @Date: 2019/4/14
     */
    @GetMapping("createSeckillStockCount")
    public void seckillStockCount() {

        //有10個庫存
        Integer count = 10;
        //新增到redis list中
        for (Integer i = 0; i < count; i++) {
            redisTemplate.opsForList().leftPush("slist", 1);
        }
        System.out.println(redisTemplate.opsForList().range("slist", 0, -1));
    }

    @GetMapping("seckill")
    public void seckill() {
        //判斷計數器
        if (redisTemplate.opsForList().leftPop("slist").equals(1)) {
            long user_id = 1903;
            redisTemplate.opsForList().leftPush("ulist", user_id);
        }
        System.out.println(redisTemplate.opsForList().range("slist", 0, -1));
        System.out.println(redisTemplate.opsForList().range("ulist", 0, -1));
    }
}
