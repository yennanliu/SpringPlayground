package com.yen.springBootPOC2AdminSystem.interceptor;

// https://www.youtube.com/watch?v=PMaonqe9XCU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=49

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *   for login check
 *
 *    1) need to implement HandlerInterceptor
 *
 *
 *
 */

public class LoginInterceptor implements HandlerInterceptor {

    /** before "target method" */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return false;
    }

    /** after "target method" */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /** after "page is render" */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
