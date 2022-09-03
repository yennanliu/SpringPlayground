package com.example.demo.aop;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author longzhonghua
 * @data 2019/02/03 09:53
 * 使用@Before在切入點開始處切入內容
 * 使用@After在切入點結尾處切入內容
 * 使用@AfterReturning在切入點return內容之後切入內容（可以用來對處理傳回值做一些加工處理）
 * 使用@Around在切入點前後切入內容，並自己控制何時執行切入點自己的內容
 * 使用@AfterThrowing用來處理當切入內容部分拋出例外之後的處理邏輯
 */
/**
 * Description:  使之成為切面類別
 */
@Aspect
/**
 * Description: 把切面類別加入到IOC容器中
 */
@Component
public class AopLog {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //執行緒局部的變數,解決多執行緒中相同變數的存取沖突問題。
    ThreadLocal<Long> startTime = new ThreadLocal<>();
//定義切點
    @Pointcut("execution(public * com.example..*.*(..))")
    public void aopWebLog() {
    }

    @Before("aopWebLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        // 接收到請求，記錄請求內容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 記錄下請求內容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP方法 : " + request.getMethod());
        logger.info("IP位址 : " + request.getRemoteAddr());
        logger.info("類別的方法 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //logger.info("參數 : " + Arrays.toString(joinPoint.getArgs()));
        logger.info("參數 : " + request.getQueryString());
    }

    @AfterReturning(pointcut = "aopWebLog()",returning = "retObject")
    public void doAfterReturning(Object retObject) throws Throwable {
        // 處理完請求，傳回內容
        logger.info("回應值 : " + retObject);
        logger.info("費時: " + (System.currentTimeMillis() - startTime.get()));
    }

    //拋出例外後知會（After throwing advice） ： 在方法拋出例外離開時執行的知會。
    @AfterThrowing(pointcut = "aopWebLog()", throwing = "ex")
    public void addAfterThrowingLogger(JoinPoint joinPoint, Exception ex) {
        logger.error("執行 " + " 例外", ex);
    }

}
