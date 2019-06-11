package com.batsoft.utils.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 敏感词验证
 *
 */
@Documented
@Constraint(validatedBy = {WordsValidators.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface Words {
	
	String message() default "{com.batsoft.utils.annotation.Words.message}";
	
	Class<?>[] groups() default{};
	
	Class<? extends Payload>[] payload() default {};
	
	String field() default "";
}
