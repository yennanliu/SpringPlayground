package com.example.demo;

import com.example.MyConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//用於指定使用範圍，該處限定只能在字段上使用
@Target({ElementType.FIELD})
//表示註釋在執行時可以透過反射取得到
@Retention(RetentionPolicy.RUNTIME)
//@Constraint註釋，裡面傳入了一個validatedBy的字段，指定該註釋驗證邏輯
@Constraint(validatedBy = MyConstraintValidator.class)
public @interface MyConstraint {
    /**
     * @Description: 錯誤提示
     */
    String message() default "請輸入中國政治或是經濟中心的城市名";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
