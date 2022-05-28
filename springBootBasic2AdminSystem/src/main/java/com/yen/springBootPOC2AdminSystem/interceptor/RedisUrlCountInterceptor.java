package com.yen.springBootPOC2AdminSystem.interceptor;

// https://www.youtube.com/watch?v=HcZCvC7jBlU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=70

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RedisUrlCountInterceptor implements HandlerInterceptor {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // get request uri
        String uri = request.getRequestURI();

        // save to redis, plus one when there is a new request
        redisTemplate.opsForValue().increment(uri);

        return true; // we always pass request (when in RedisUrlCountInterceptor)
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
