//package com.yen.mdblog.controllers;
//
//// https://www-twblogs-net.translate.goog/a/6109464436a248ffb45f37ea?_x_tr_sl=zh-TW&_x_tr_tl=en&_x_tr_hl=en&_x_tr_pto=sc
//
//import com.yen.mdblog.entities.User;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//public class TestController {
//
//    @GetMapping("/index")
//    public String index(ModelMap map){
//
//        //單個數據
//        map.put("username", "入門案例");
//
//        User user = new User();
//        user.setPassword("test_ps");
//        user.setUsername("test");
//
//        map.put("userInfo", user);
//        return "index";
//    }
//
//    @ResponseBody
//    @RequestMapping(value="/add", method= RequestMethod.POST)
//    public String add(@ModelAttribute User user){
//
//        String username = user.getUsername();
//        String password = user.getPassword();
//
//        return "result : " + username +" __ " + password;
//    }
//
//}
