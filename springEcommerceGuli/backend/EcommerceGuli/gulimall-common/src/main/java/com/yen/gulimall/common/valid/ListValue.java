package com.yen.gulimall.common.valid;

// https://youtu.be/r8naBc3IBNE?t=120

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {ListValueConstraintValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RUNTIME)
public @interface ListValue {

    /**
     *  In order to create our own annotation,
     *  we need to have below methods, so here
     *  we simply refer from javax.validation.constraints.NotBlank.java
     *   -  https://youtu.be/r8naBc3IBNE?t=151
     *
     */
    String message() default "{com.yen.gulimall.common.valid.ListValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] vals() default {};
}
