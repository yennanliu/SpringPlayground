package com.yen.SpringBootPart1.controller;

// https://www.youtube.com/watch?v=MuzLKTp87Vs&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=44

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// for thymeleaf test
@Controller
public class ViewController {

    @GetMapping("/yen_test1")
    public String yenTest1(Model model){

        // info inside model will be passed to "request domain"
        model.addAttribute("msg", "hello Java !!!");
        model.addAttribute("link", "https://www.python.org/");

        /**
         *  NOTE !!!
         *
         *  since thymeleaf already defined `prefix`, `suffix`.
         *  -> so here we ONLY need to offer html name,
         *  -> NO need to offer full path. (e.g. resources/templates/success.html)
         */
        return "success";
    }

}
