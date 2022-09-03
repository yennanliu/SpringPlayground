package com.example.demo.util.aop;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author longzhonghua
 * @data 2018/11/04 22:30
 */
@Aspect
@Service
public class LoggerAdvice {

	protected Logger logger =  LoggerFactory.getLogger(this.getClass());
	// private static final Logger logger = Logger.getLogger(SysLogAspect.class);
	//定義Pointcut，Pointcut的名稱，此方法不能有傳回值，該方法只是一個標示
	//前置知會（Before advice） ：在某連線點（JoinPoint）之前執行的知會，但這個知會不能阻止連線點前的執行。
	@Before("within(com.hua..*) && @annotation(loggerManage)")
	//切入點
	public void addBeforeLogger(JoinPoint joinPoint, LoggerManage loggerManage) {
		logger.info("執行 " + loggerManage.description() + " 開始");
		logger.info(joinPoint.getSignature().toString());
		logger.info(parseParames(joinPoint.getArgs()));
	}
	//後知會（After advice） ：當某連線點離開的時候執行的知會（不論是標準傳回還是例外離開）。
	@AfterReturning("within(com.hua..*) && @annotation(loggerManage)")
	public void addAfterReturningLogger(JoinPoint joinPoint, LoggerManage loggerManage) {
		logger.info("執行 " + loggerManage.description() + " 結束");
	}
	//拋出例外後知會（After throwing advice） ： 在方法拋出例外離開時執行的知會。
	@AfterThrowing(pointcut = "within(com.hua..*) && @annotation(loggerManage)", throwing = "ex")
	public void addAfterThrowingLogger(JoinPoint joinPoint, LoggerManage loggerManage, Exception ex) {
		logger.error("執行 " + loggerManage.description() + " 例外", ex);
	}
	/**
	 * 環繞知會（Around advice） ：包圍一個連線點的知會，類別似Web中Servlet規範中的Filter的doFilter方法。可以在方法的呼叫前後完成自訂的行為，也可以選取不執行。
	 */
/*
	@Around("controllerAspect()")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("=====SysLogAspect 環繞知會開始=====");
		//handleLog(joinPoint, null);
		Object obj= joinPoint.proceed();
		System.out.println("=====SysLogAspect 環繞知會結束=====");
		return  obj;
	}
*/


	private String parseParames(Object[] parames) {
		if (null == parames || parames.length <= 0 || parames.length >1024) {
			return "";
		}
		StringBuffer param = new StringBuffer("傳導入參數數[{}] ");
		for (Object obj : parames) {
			param.append(ToStringBuilder.reflectionToString(obj)).append("  ");
		}
		return param.toString();
	}
	
}
