package com.yen.springBootAdvance2.service;

// https://www.youtube.com/watch?v=nvI3YmQz0OE&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=18

import com.yen.springBootAdvance2.bean.Book;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    /** listen every msg sent to RabbitMQ queue with Book class */
    @RabbitListener(queues = "yen.news")
    public void receive(Book book){
        System.out.println(">>> msg received : " + book);
    }

    /** listen every msg sent to RabbitMQ queue with queues name == yen */
    @RabbitListener(queues = "yen")
    public void receive2(Message msg){

        System.out.println(msg.getBody());
        System.out.println(msg.getMessageProperties());
    }

}
