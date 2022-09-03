package com.example.demo.controller;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class HomeController {
    @RequestMapping({"/", "/index"})
    public String index() {
        return "/index";
    }
    @RequestMapping("/login")
    public String login(HttpServletRequest request, Map<String, Object> map) throws Exception {
          // 登入失敗從request中取得shiro處理的例外訊息。
        // shiroLoginFailure:就是shiro例外類別的全類別名.
        //起始登陸使用者名稱密碼long/longzhonghua,或是long/123456
        String exception = (String) request.getAttribute("shiroLoginFailure");
        System.out.println("exception=" + exception);
        String msg = "";
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                       msg = "賬號不存在：";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                         msg = "密碼不正確：";
            } else if ("kaptchaValidateFailed".equals(exception)) {
                             msg = "驗證碼錯誤";
            } else {
                msg = "else >> " + exception;

            }
        }
        map.put("msg", msg);
        // 此方法不處理登入成功,由shiro進行處理
        return "/login";
    }
    @RequestMapping("/403")
    public String unauthorizedRole() {
        return "403";
    }
}