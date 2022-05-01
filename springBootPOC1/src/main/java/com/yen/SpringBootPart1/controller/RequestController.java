package com.yen.SpringBootPart1.controller;

// https://www.youtube.com/watch?v=qPp_wuas9Cw&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=31

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
    public Map success(@RequestAttribute("msg") String msg,   // method 1) : get val from request (RequestAttribute)
                       @RequestAttribute("code") Integer code,
                       HttpServletRequest request){           // method 2) : get val from request (HttpServletRequest)

        Object msg1 = request.getAttribute("msg");

        Map<String, Object> map = new HashMap<>();

        map.put("reqMethod_msg", msg1);
        map.put("annotation_msg", msg);
        map.put("code", code);

        return map;
    }
}
