package com.yen.springCourseSystem.annotation;

// https://github.com/yennanliu/SpringPlayground/blob/main/ref_project/easy-springboot-master/src/main/java/com/xiaoze/course/annotation/CurrentUser.java

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Import({UserInfoConfiguration.class})
public @interface CurrentUser {
}
