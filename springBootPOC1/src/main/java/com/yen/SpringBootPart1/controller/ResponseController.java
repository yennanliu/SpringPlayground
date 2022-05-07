package com.yen.SpringBootPart1.controller;

// https://www.youtube.com/watch?v=s9NuhNSD4Mo&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=38

import com.yen.SpringBootPart1.bean.Person2;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class ResponseController {

    @ResponseBody
    @GetMapping("/test/person2")
    public Person2 getPerson2(){
        Person2 person = new Person2();
        person.setAge(20);
        person.setBirth(new Date());
        person.setUserName("Abby");
        return person;
    }

    @ResponseBody // RequestResponseBodyMethodProducer -> messageConverter
    @GetMapping("/test/file_resource")
    public FileSystemResource file(){
        // should return "doc" format (processed by messageConverter), instead of json format
        // TODO : implement this
        return null;
    }

}
