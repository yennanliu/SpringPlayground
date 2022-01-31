package com.yen.springMVC1.controller;

// https://www.youtube.com/watch?v=S3So1anM_6w&list=PLmOn9nNkQxJE3V_Eev79ao-g3a6BplSQG&index=10
// https://www.youtube.com/watch?v=f-mn8mC6nTE&list=PLmOn9nNkQxJE3V_Eev79ao-g3a6BplSQG&index=10

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // label as controller class, so springMVC can scan and use it
public class helloController {

    // request : "/"  -> /WEB-INF/templates/index.html
    @RequestMapping(value="/") // will map request with path = "/" to below method
    public String index(){
        //return view name (/WEB-INF/templates/index.html remove prefix, suffix, so only need to return "index"
        return "index";
    }

}
