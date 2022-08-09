package com.yen.mdblog.controller;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Log4j2
public class EditController {

//    @GetMapping("/edit")
//    public String EditPost(@PathVariable int id, Model model){
//        return "edit_post";
//    }

    @GetMapping("/edit")
    public String EditPost(){
        return "edit_post";
    }

}
