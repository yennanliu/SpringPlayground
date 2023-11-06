//package com.yen.mdblog.controller;
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.security.Principal;
//
///** Test controller */
//
//@Controller
//@Log4j2
//public class TestController {
//
//    @GetMapping("/")
//    public ModelAndView index(ModelMap model){
//
//        model.addAttribute("attribute", "redirectWithXMLConfig");
//        return new ModelAndView("redirect:/posts/all", model);
//    }
//
//    // get spring security login info
//    // https://dzone.com/articles/how-to-get-current-logged-in-username-in-spring-se
//    @RequestMapping(value = "/username", method = RequestMethod.GET)
//    @ResponseBody
//    public String currentUserName(Principal principal) {
//        System.out.println(">>> current login = " + principal.getName());
//        System.out.println(">>> current login = " + principal.toString());
//        return principal.getName();
//    }
//
//}
