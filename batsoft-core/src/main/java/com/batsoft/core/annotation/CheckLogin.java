package com.batsoft.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录拦截注解
 * 
 * @author simon
 */
@Documented
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLogin {
	
	/**
	 * 默认值
	 * 
	 */
	public static final String CHECK_USER_LOGIN = "CHECK_USER_LOGIN";
	

	String value() default CHECK_USER_LOGIN;

}
