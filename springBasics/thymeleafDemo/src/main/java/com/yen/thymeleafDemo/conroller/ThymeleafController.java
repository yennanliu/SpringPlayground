package com.yen.thymeleafDemo.conroller;

// p.141, p.148

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/demo2")
    public String thymeleaf_demo2(ModelMap map){

        System.out.println(">>> thymeleaf_demo2");

        map.put("title", "(title) thymeleaf_demo2");
        map.put("testString", "spring boot ecommerce");
        map.put("bool", true);
        map.put("testArray", new Integer[]{2021, 2022, 2023, 2030, 9999});
        map.put("testList", Arrays.asList("spring", "spring-boot", "scala", "play-framework"));

        Map<String, Object> testMap = new HashMap<>();
        testMap.put("platform", "book");
        testMap.put("title", "spring boot enterprise platform implementation");
        testMap.put("author", "kk");

        map.put("testMap", testMap);
        map.put("testDate", new Date());

        System.out.println(">>> testMap = " + testMap);
        System.out.println(">>> map = " + map);

        return "thymeleafDemo2";
    }

}
