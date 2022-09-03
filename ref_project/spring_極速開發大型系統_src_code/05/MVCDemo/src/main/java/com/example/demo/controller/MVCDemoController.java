package com.example.demo.controller;
import com.example.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * Author:   longzhonghua
 * Date:     3/22/2019 10:42 AM
 */
@Controller
public class MVCDemoController {
    //映射URL位址
    @GetMapping("/mvcdemo")
    public ModelAndView hello() {
        //案例化物件
        User user=new User();
        user.setName("zhonghua");
        user.setAge(28);
        //定義mvc中的檢視範本
        ModelAndView modelAndView=new ModelAndView("mvcdemo");
        //傳遞user實體物件給檢視
        modelAndView.addObject("user",user);
        return modelAndView;
    }
}
