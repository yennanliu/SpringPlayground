package com.yen.SpringBootPart1.controller;

// https://www.youtube.com/watch?v=1okUblTs28Q&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=29
// https://www.youtube.com/watch?v=1okUblTs28Q&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=30
// https://www.youtube.com/watch?v=2IBSZvwWq5w&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=31
// https://www.youtube.com/watch?v=rGgzYESEe84&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=36

import com.yen.SpringBootPart1.bean.Person2;
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

    /**
     *  1) MatrixVariable syntax :  /cars/sell;low=34;brand=LEXUS,porsche,audi
     *  2) NOTE : by default, springBoot DISABLE MatrixVariable
     *      -> we need to enable it if want to use
     *      -> theory:
     *          - urlPathHelper for parse
     *          - removeSemicolonContent for allowing MatrixVariable or not (remove content after ;)
     *
     *  3) MatrixVariable NEED to with url path variable ( e.g. /cars/{path} ), then can be parsed
     *
     */
    @GetMapping("/cars/{path}")  // Note !!! we need set "/.../{path}" here
    public Map carsSell(@MatrixVariable("low") Integer low,
                        @MatrixVariable("brand") List<String> brand,
                        @PathVariable("path") String path){

        Map<String, Object> map = new HashMap<>();

        map.put("low", low);
        map.put("brand", brand);
        map.put("path", path);

        return map;
    }

    // /boss/1;age=20/2;age=10
    @GetMapping("/boss/{bossId}/{empId}")
    public Map boss(@MatrixVariable(value = "age", pathVar = "bossId") Integer bossAge,
                    @MatrixVariable(value = "age", pathVar = "empId") Integer empAge){

        Map<String, Object> map = new HashMap<>();

        map.put("bossAge", bossAge);
        map.put("empAge", empAge);

        return map;
    }

    /**
     *  Data binding:
     *    -> whatever request (GET, POST..), we can bind request with Bean class attr
     */
    @PostMapping("/saveuser")
    public Person2 saveuser(Person2 person){
        return person;
    }

}
