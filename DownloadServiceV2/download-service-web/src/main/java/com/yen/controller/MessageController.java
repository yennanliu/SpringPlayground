package com.yen.controller;

import com.yen.bean.Message;
import com.yen.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("/msg")
    public List<Message> getMessage(@RequestParam int page, @RequestParam int size){

        System.out.println(">>> page = " + page + ", size = " + size);
        System.out.println(messageService.getPageMessage(page, size));

        return messageService.getPageMessage(page, size);
    }

}
