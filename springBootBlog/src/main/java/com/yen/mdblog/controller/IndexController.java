package com.yen.mdblog.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Log4j2
public class IndexController {

    @GetMapping("/")
    public ModelAndView index(ModelMap model){

        model.addAttribute("attribute", "redirectWithXMLConfig");
        return new ModelAndView("redirect:/posts/all", model);
    }

}
