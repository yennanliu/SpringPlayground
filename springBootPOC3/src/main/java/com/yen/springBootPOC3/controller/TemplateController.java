package com.yen.springBootPOC3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/** book p.50 */

// TODO : fix this (val parse in html)
@Controller
public class TemplateController {

    //@GetMapping("/hello_ft1")
    @RequestMapping("/hello_ft1")
    public String hello_ft1(Map<String, Object> map){

        //model.addAttribute("hello", "based on freemarker with TemplateController.hello_ft1");
        map.put("hello", "based on freemarker with TemplateController.hello_ft1");
        return "/hello_ft1";
    }
}
