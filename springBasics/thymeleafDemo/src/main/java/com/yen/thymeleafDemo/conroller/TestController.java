package com.yen.thymeleafDemo.conroller;

// book p.132, p.138

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class TestController {

    @GetMapping("/test1")
    public String test_1(HttpServletRequest request){
        System.out.println(">>> test1");
        request.setAttribute("description", "this is description value");
        return "test1";
    }

    @GetMapping("/test2")
    public String test_2(ModelMap map){
        System.out.println(">>> test2");
        map.put("title", "thymeleaf title tag demo");
        map.put("th_id", "thymeleaf-id");
        map.put("th_name", "thymeleaf-name");
        map.put("th_value", "777");
        map.put("th_class", "thymeleaf-class");
        map.put("th_href", "https://www.python.org/");
        System.out.println(">>> map = " + map);
        return "test2";
    }

}
