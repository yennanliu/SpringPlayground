package com.example.demo.controller;

import com.example.demo.mq.CustomSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: CustomControllerTest
 * Author:   longzhonghua
 * Date:     2019/5/8 17:01
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CustomControllerTest {
    /**
     * @Description: 測試 訊息延遲
     */
    @Autowired
    private CustomSender customSender;
    @Test
    public void send() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            customSender.sendMsg("delay_queue_1","支付逾時，取消訂單知會！");
        }
    }
