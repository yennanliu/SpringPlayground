package com.example.demo.exception;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author longzhonghua
 * @createdata 3/18/2019 2:02 PM
 * @description 自訂錯誤
 */
@RestController
/*springboot提供了預設的錯誤映射位址error
@RequestMapping("${server.error.path:${error.path:/error}}")
@RequestMapping("/error")
上面2種寫法都可以
*/
@RequestMapping("/error")
//繼承springboot提供的ErrorController
public class TestErrorController implements ErrorController {
    //一定要重新定義方法,預設傳回null就可以,不然顯示出錯,因為getErrorPath為空.
    @Override
    public String getErrorPath() {
        return null;
    }

    //一定要加入url映射,指向error
/*    @RequestMapping()
    public Map<String, Object> handleError() {
        //用Map容器傳回訊息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 404);
        map.put("msg", "不存在");
        return map;
    }*/

    /*這裡加一個能標準存取的頁面,作為比較
    因為寫在一個控制器所以它的存取路徑是
    http://localhost:8080/error/ok*/
    @RequestMapping("/ok")
    @ResponseBody
    public Map<String, Object> noError() {
        //用Map容器傳回訊息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code ", 200);
        map.put("msg", "標準，這是測試頁面");

        return map;
    }

//這裡不要加consumes="text/html;charset=UTF-8",不然不成功,有部分瀏覽器傳送的是空值
    @RequestMapping( value = "",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String errorHtml4040(HttpServletRequest request, HttpServletResponse response) {
        //跳躍到error 目錄下的 404範本
        return "404錯誤,不存在";
    }
    @RequestMapping(value = "", consumes="application/json;charset=UTF-8",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public  Map<String, Object> errorJson() {
        //用Map容器傳回訊息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rspCode", 404);
        map.put("rspMsg", "不存在");
        return map;
    }


}
