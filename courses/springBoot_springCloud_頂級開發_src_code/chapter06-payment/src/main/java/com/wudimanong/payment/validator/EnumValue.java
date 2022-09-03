package com.wudimanong.payment.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.wudimanong.payment.validator.EnumValue.EnumValueValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * @author jiangqiao
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValueValidator.class})
public @interface EnumValue {

    //默认错误消息
    String message() default "必须为指定值";

    String[] strValues() default {};

    int[] intValues() default {};

    //分组
    Class<?>[] groups() default {};

    //负载
    Class<? extends Payload>[] payload() default {};

    //指定多个时使用
    @Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        EnumValue[] value();
    }

    class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

        private String[] strValues;
        private int[] intValues;

        @Override
        public void initialize(EnumValue constraintAnnotation) {
            strValues = constraintAnnotation.strValues();
            intValues = constraintAnnotation.intValues();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value instanceof String) {
                for (String s : strValues) {
                    if (s.equals(value)) {
                        return true;
                    }
                }
            } else if (value instanceof Integer) {
                for (Integer s : intValues) {
                    if (s == value) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
