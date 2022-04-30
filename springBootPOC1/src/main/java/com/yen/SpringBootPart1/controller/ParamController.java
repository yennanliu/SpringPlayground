package com.yen.SpringBootPart1.controller;

// https://www.youtube.com/watch?v=1okUblTs28Q&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=29
// https://www.youtube.com/watch?v=1okUblTs28Q&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=30

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ParamController {

    //@GetMapping("/car?id=1")  // this one is OK as well
    @GetMapping("/car/{id}/owner/{userName}")                       // REST style
    public Map<String, Object> getCar(
                    @PathVariable("id") Integer id,                 // get id from path variable
                    @PathVariable("userName") String userName,      // get userName from path variable
                    // Fix this
                    //@PathVariable("pv") Map<String, String> pv    // get ALL path variables and put into pv
                    @RequestHeader("User-Agent") String userAgent,  // get "User-Agent" info from header
                    @RequestHeader Map<String, String> header,       // get all info from header
                    @RequestParam("age") Integer age,
                    @RequestParam("inters") List<String> inters,
                    @RequestParam() Map<String, String> params//,
                    //@CookieValue("_ga") String _ga
                    //@CookieValue() Cookie cookie

            ){

        Map<String, Object> map = new HashMap<>();

        map.put("id", id);
        map.put("userName", userName);
        //map.put("pv", pv);
        map.put("userAgent", userAgent);
        map.put("header", header);
        map.put("age", age);
        map.put("inters", inters);
        map.put("params", params);
        //map.put("_ga", _ga);
        //map.put("cookie", cookie);

        return map;
    }


    @PostMapping("/save")
    public Map postMethod(@RequestBody String content){
        Map<String, Object> map = new HashMap<>();

        map.put("content", content);
        return map;
    }

}
