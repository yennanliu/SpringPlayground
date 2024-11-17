package com.yen.thymeleafDemo.conroller;

// book p.132, p.138, p.144

import com.yen.thymeleafDemo.bean.GoodsDetail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class TestController {

    @GetMapping("/test1")
    public String test_1(HttpServletRequest request){

        System.out.println(">>> test1");
        request.setAttribute("description", "this is description value");
        return "test1";
    }

    @GetMapping("/test2")
    public String test_2(ModelMap map){

        System.out.println(">>> test2");
        map.put("title", "thymeleaf title tag demo");
        map.put("th_id", "thymeleaf-id");
        map.put("th_name", "thymeleaf-name");
        map.put("th_value", "777");
        map.put("th_class", "thymeleaf-class");
        map.put("th_href", "https://www.python.org/");
        System.out.println(">>> map = " + map);
        return "test2";
    }

    @GetMapping("/test3")
    public String test_3(HttpServletRequest request, HttpSession session, ModelMap map){ // https://ithelp.ithome.com.tw/articles/10277283

        System.out.println(">>> test3");

        // HttpSession
        session.setAttribute("sessionObject", "(session) this is bbb");

        // HttpServletRequest
        request.setAttribute("requestObject", "(request) this is aaa");

        // ModelMap
        map.put("number1", 17);
        GoodsDetail goodsDetail = new GoodsDetail(1,"TSLA",1000);
        map.put("goodsDetail", goodsDetail);

        System.out.println(">>> session = " + session.getAttribute("sessionObject"));
        System.out.println(">>> request = " + request.getAttribute("requestObject"));
        System.out.println(">>> map = " + map);

        return "test3";
    }

}
