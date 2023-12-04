package spring.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Created 13 / 04 / 2020 - 10:21 AM
 * @project SpringSecurityForm
 * @Author Hamdamboy
 */

@Controller
@RequestMapping("/")
public class HomeController {
    //
    @GetMapping("index")
    public String index(){
        return "index";
    }

    @GetMapping("login")
    public String login(){
        return "login";
    }
}
