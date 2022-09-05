package com.example.demo.util.aop;

import java.lang.annotation.*;

/**
 * @author longzhonghua
 * @data 2018/11/04 22:30
 * 日志註釋類別
 */
//@Target(ElementType.METHOD)
@Target({ElementType.PARAMETER, ElementType.METHOD})//目的是方法
@Retention(RetentionPolicy.RUNTIME)//註釋會在class中存在，執行時可透過反射取得
@Documented//文件產生時，該註釋將被包括在javadoc中，可去掉
public @interface LoggerManage {
	//模組名字
	public String description() default "logger null";
}
