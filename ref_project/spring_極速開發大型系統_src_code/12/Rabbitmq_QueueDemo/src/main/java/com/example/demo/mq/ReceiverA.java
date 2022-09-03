package com.example.demo.mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author longzhonghua
 * @data 2019/02/03 11:07
 */
@Component
@RabbitListener(queues = "Queue1")//監聽QueueHello的訊息佇列
public class ReceiverA {
    @RabbitHandler//@RabbitHandler來實現實際消費
    public void QueueReceiver(String Queue1) {
        System.out.println("Receiver A: " + Queue1);
    }

}