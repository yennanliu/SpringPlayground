package com.yen.springProfilesChangeDemo.springProfilesChangeDemo.controller;

// https://www.youtube.com/watch?v=newMNCS4sik&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=82

import com.yen.springProfilesChangeDemo.springProfilesChangeDemo.bean.Person;
import com.yen.springProfilesChangeDemo.springProfilesChangeDemo.bean.Person2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    /**
     *  1) get person.name val from application.properties
     *  2) ${person.name:Joe} means : if can't get person.name, use "Joe" as default
     */
    @Value("${person.name:Joe}")

    private String name;

    @GetMapping("/")
    public String hello(){
        return "HELLO " + name;
    }

    @Autowired
    private Person person;

    @GetMapping("/test")
    public Person person(){
        return person;
    }

    @Autowired
    private Person2 person2;

    @GetMapping("/test2")
    public Person person2(){
        return person;
    }

}