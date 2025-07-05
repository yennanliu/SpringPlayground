//package com.yen.springCourseSystem.Aspect;
//
//// book p. 254
//
//import org.aopalliance.intercept.Joinpoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//// TODO: finish this
//
///** implement web layer log */
//@Aspect
//@Component
//public class WebLogAspect {
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    ThreadLocal<Long> startTime = new ThreadLocal<Long>();
//    @Pointcut("execution(* com.yen.springCourseSystem.service.impl.*.*(..))")
//    public void webLog(){}
//    @Before("webLog()")
//    public void doBefore(Joinpoint joinpoint){
//
//        startTime.set(System.currentTimeMillis());
//        // receive request, then record content
//        logger.info(">>> WebLogAspect doBefore ...");
//        ServletRequestAttributes attributes = (ServletRequestAttributes);
//        RequestContextHolder.getRequestAttributes();
//
//    }
//
//
//}
