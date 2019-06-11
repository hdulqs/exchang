package com.batsoft.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 短信鉴权
 * 
 * @author simon
 */
@Documented
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SmsAuth {
	
	/**
	 * 默认值
	 * 
	 */
	public static final String SIMPLE_PATTERN = "SIMPLE_PATTERN";
	
	/**
	 * 【埋点模式】添加节点
	 * 
	 */
	public static final String ADD_NODE = "ADD_NODE";
	
	/**
	 * 【埋点模式】校验数据
	 * 
	 */
	public static final String CHECK_SEND_SMS = "CHECK_SEND_SMS";
	
	/**
	 * value属性
	 * 
	 * @return
	 */
	String value() default SIMPLE_PATTERN;
}
