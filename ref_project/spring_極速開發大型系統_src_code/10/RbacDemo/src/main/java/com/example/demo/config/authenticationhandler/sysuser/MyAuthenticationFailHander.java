﻿package com.example.demo.config.authenticationhandler.sysuser;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * @author longzhonghua
 * @data 2/26/2019 6:53 PM
 */
@Component("myAuthenticationFailHander")
public class MyAuthenticationFailHander extends SimpleUrlAuthenticationFailureHandler {

    //使用者名稱密碼錯誤執行
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse
            httpServletResponse, AuthenticationException e) throws IOException, ServletException, IOException {
        httpServletRequest.setCharacterEncoding("UTF-8");
        // 獲得使用者名稱密碼
        String username = httpServletRequest.getParameter("uname");
        String password = httpServletRequest.getParameter("pwd");

       httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        out.write("{\"status\":\"error\",\"message\":\"使用者名稱或密碼錯誤\"}");
        out.flush();
        out.close();
    }
}


