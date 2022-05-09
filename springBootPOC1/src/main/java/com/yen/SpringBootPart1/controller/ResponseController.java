package com.yen.SpringBootPart1.controller;

// https://www.youtube.com/watch?v=s9NuhNSD4Mo&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=38
// https://www.youtube.com/watch?v=NEGzyvm1IBc&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=42

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

    /**
     * user-defined MessageConverter
     *
     *  1) if browser request -> return xml [application/xml] jacksonXmlConverter
     *  2) if ajax request -> return json [application/json] jacksonJsonConverter
     *  3) if "yen-app" request -> return user-defined data type
     */
    @ResponseBody
    @GetMapping("/test/person3")
    public Person2 getPerson3(){
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
