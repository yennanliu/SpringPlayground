package com.yen.springBootPOC3.servlet;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** book p.65 */

public class MyInterceptor1 implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(">>> MyInterceptor1 preHandle() : before request is called (before Controller method)");
        //return false;
        return true; // only true, then the program continue run. abort current request if false
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println(">>> MyInterceptor1 postHandle() : after request is called (after controller method, before DispatcherServlet parse view)");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println(">>> MyInterceptor1 afterCompletion() : after request is called (after DispatcherServlet parse view)");
    }

}
