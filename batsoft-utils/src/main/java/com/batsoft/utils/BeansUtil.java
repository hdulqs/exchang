package com.batsoft.utils;

import java.lang.annotation.Annotation;
import java.util.Objects;

import javax.persistence.Table;

/**
 * Bean 反射
 * 
 * @author simon
 */
public class BeansUtil {
	
	/**
	 * 通过反射获取标注在bean上的Table注解的Value
	 * 
	 * @param clazz
	 * 			类的字节码
	 * @return
	 */
	public static String findBeanTableValue(final Class<?> clazz) {
		String result = new String();
		Annotation[] annotation = clazz.getAnnotations();
		for(Annotation tation : annotation) {
			if(Objects.equals(tation.annotationType().getSimpleName(), Table.class.getSimpleName())) {
				result = ((Table)tation).name();
			}
		}
		return result;
	}
	
}
