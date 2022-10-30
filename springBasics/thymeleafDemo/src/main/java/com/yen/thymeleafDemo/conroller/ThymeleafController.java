package com.yen.thymeleafDemo.conroller;

// p.141

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeleafController {

    @GetMapping("/demo1")
    public String thymeleaf_demo1(ModelMap map){
        System.out.println(">>> thymeleaf_demo1");
        map.put("thymeleafText", "spring-boot");
        map.put("number1", 2021);
        map.put("number2", 2);
        System.out.println(">>> map = " + map);
        return "thymeleafDemo1";
    }

}
