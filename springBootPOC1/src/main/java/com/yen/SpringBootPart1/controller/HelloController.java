package com.yen.SpringBootPart1.controller;

// https://www.youtube.com/watch?v=N7dsPcsmxM4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=6
// https://www.youtube.com/watch?v=aVNw04JjjSw&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=12
// https://www.youtube.com/watch?v=lDzXRsOODXA&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=16
// https://www.youtube.com/watch?v=s5GTuFsCSWw&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=21

import com.yen.SpringBootPart1.bean.Car;
import com.yen.SpringBootPart1.bean.Car2;
import com.yen.SpringBootPart1.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// either use @ResponseBody + @Controller, or use  @RestController directly
//@ResponseBody
//@Controller
@RestController
public class HelloController {

    // spring's auto injection
    // https://www.baeldung.com/spring-annotations-resource-inject-autowire
    @Autowired
    Car car;

    @Autowired
    Car2 car2;

    @Autowired
    Person person;

    @RequestMapping("/car")
    public Car car(){
        return car;
    }

    @RequestMapping("/car2")
    public Car2 car2(){
        return car2;
    }

    @RequestMapping("/hello")
    public String handle1(){
        return "hello !!! spring boot 2 !!";
    }

    @RequestMapping("/hello2")
    public String handle2(@RequestParam("name") String name){

        return "hello2 !!! spring boot 2 !!" + name;
    }

    @RequestMapping("/person")
    public Person person(){
        return person;
    }

}
