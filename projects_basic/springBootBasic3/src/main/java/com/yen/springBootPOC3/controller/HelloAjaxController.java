package com.yen.springBootPOC3.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** book p.51
 *
 *  test on resources/static/index_ajax.html
 */

@RestController
@RequestMapping("/ajax")
public class HelloAjaxController {

    @RequestMapping("/hello")
    public String say(){
        String msg = "{ 'msg1' : 'hello', 'msg2' : 'msg from Ajax' }";
        return msg;
    }
    
}
