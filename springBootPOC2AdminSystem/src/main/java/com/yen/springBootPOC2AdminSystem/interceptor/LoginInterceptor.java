package com.yen.springBootPOC2AdminSystem.interceptor;

// https://www.youtube.com/watch?v=PMaonqe9XCU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=49

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *   for login check
 *
 *    1) need to implement HandlerInterceptor
 *    2) define which requests NEED to be "intercepted"
 *    3) put these setting into container
 */

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    /** before "target method" */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info(">>> preHandle : intercepted request : " + requestURI);

        // check if already login
        HttpSession session = request.getSession();
        Object loginUser = session.getAttribute("loginUser");


        if (loginUser != null){
            // if login, pass
            return true;
        }
        // if not login, intercept, re-direct to login page
//        session.setAttribute("msg", "plz login first");
//        response.sendRedirect("/");
        request.setAttribute("msg", "plz login first");
        request.getRequestDispatcher("/").forward(request, response);
        return false;
    }

    /** after "target method" */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        log.info(">>> postHandle run : " + modelAndView);
    }

    /** after "page is render" */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        log.info(">>> afterCompletion run (print as well if exception) : " + ex);
    }

}
