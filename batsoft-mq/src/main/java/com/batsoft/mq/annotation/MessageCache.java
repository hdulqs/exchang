package com.batsoft.mq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageCache {

	public String cacheName() default "";
	public String cacheKey();
	public String messageArgMapper(); //不支持spel，默认为${arg.xxx}
	public String routingKey() default "";
	public String exchange() default "";
	
}
