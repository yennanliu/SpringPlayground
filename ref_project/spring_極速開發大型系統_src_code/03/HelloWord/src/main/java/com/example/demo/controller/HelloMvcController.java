package com.example.demo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//mvc模式的控制器
@Controller
public class HelloMvcController {
    @RequestMapping("/helloworld")
    public String helloWorld (Model model)  throws Exception{
        model.addAttribute("mav", "HelloWorldController ,Spring Boot!");
//檢視(view)的位置和名稱，檢視位於example資料夾下，檢視檔案為hello.html。
        return "example/hello";
    }
}
