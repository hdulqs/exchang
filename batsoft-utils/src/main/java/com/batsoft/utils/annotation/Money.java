package com.batsoft.utils.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2017/5/11.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=MoneyValidator.class)
public @interface Money {

    String message() default"金额格式错误";
    int point() default 2;
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}