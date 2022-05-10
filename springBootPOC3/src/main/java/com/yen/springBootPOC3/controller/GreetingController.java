package com.yen.springBootPOC3.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** book p.41, 42 */

@RestController
public class GreetingController {

    // receive GET request
    @GetMapping("/mapping")
    @ResponseBody   // return json (? or string) format
    public String greeting(){
        return "greeting !!";
    }

    @GetMapping("/hi")
    public String hi(@RequestParam(name="name", required = false, defaultValue = "world") String name, Model model){
        model.addAttribute("name", name);
        return "hi"; // return templates/hi.html
    }

}
