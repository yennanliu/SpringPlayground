package com.yen.SpringBootPart1.controller;

// https://www.youtube.com/watch?v=qPp_wuas9Cw&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=31
// https://www.youtube.com/watch?v=tq3AiD2zlfw&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=35

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RequestController {

    @GetMapping("/goto")
    public String goToPage(HttpServletRequest request){

        request.setAttribute("msg", "it works !!!");
        request.setAttribute("msg2", "this is msg2 ~");
        request.setAttribute("code", 200);

        //return "success";          // to success.html page
        return "forward:/success";   // re-direct to /success request
    }

    @ResponseBody
    @GetMapping("/success")
    public Map success(@RequestAttribute(value = "msg", required = false) String msg,   // method 1) : get val from request (RequestAttribute)
                       @RequestAttribute(value = "code", required = false) Integer code, // via required = false, we can make such param as NOT necessary
                       HttpServletRequest request){           // method 2) : get val from request (HttpServletRequest)

        Object msg1 = request.getAttribute("msg");

        Map<String, Object> map = new HashMap<>();

        // from @GetMapping("/params")
        Object hello = request.getAttribute("hello");
        Object world = request.getAttribute("world");
        Object message = request.getAttribute("message");

        map.put("reqMethod_msg", msg1);
        map.put("annotation_msg", msg);
        map.put("code", code);
        map.put("hello", hello);
        map.put("world", world);
        map.put("message", message);

        return map;
    }

    @GetMapping("/params")
    public String testParam(Map<String, Object> map,
                            Model model,
                            HttpServletRequest request,
                            HttpServletResponse response){

        map.put("hello", "111");
        model.addAttribute("world", "222");
        request.setAttribute("message", "yayy");

        Cookie cookie = new Cookie("k1", "v1");
        response.addCookie(cookie);

        return "forward:/success";
    }
}
