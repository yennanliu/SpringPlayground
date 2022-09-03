package com.example.demo.controller;


import com.example.demo.util.result.ExceptionMsg;
import com.example.demo.util.result.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * @author longzhonghua
 * @data 2018/11/04 22:30
 */
@Controller
public class BaseController {

  /*  ERROR 為嚴重錯誤 主要是程式的錯誤
    WARN 為一般警示，例如session遺失
    INFO 為一般要顯示的訊息，例如登入登出
    DEBUG 為程式的除錯訊息
     //logger 	logger.error("例外：",e); logger.info("standard end :"+ getUserId());
    //logger.debug("這是個測試時間{}"+new Date());*/
    protected Logger logger =  LoggerFactory.getLogger(this.getClass());

    protected Response result(ExceptionMsg msg){
        return new Response(msg);
    }
    protected Response result(){
        return new Response();
    }
}
