package com.batsoft.core.common.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * K线图时间节点常量
 * 
 * @author simon
 */
public class KlineTimeNodeConstant {
	
	/**
	 * 1分钟
	 * 
	 */
	public static final String MINUTE_1 = "1m";
	
	/**
	 * 5分钟
	 * 
	 */
	public static final String MINUTE_5 = "5m";
	
	/**
	 * 15分钟
	 * 
	 */
	public static final String MINUTE_15 = "15m";
	
	/**
	 * 30分钟
	 * 
	 */
	public static final String MINUTE_30 = "30m";
	
	/**
	 * 1小时
	 * 
	 */
	public static final String HOUR_1 = "1h";
	
	/**
	 * 2小时
	 * 
	 */
	public static final String HOUR_2 = "2h";
	
	/**
	 * 4小时
	 * 
	 */
	public static final String HOUR_4 = "4h";
	
	/**
	 * 6小时
	 * 
	 */
	public static final String HOUR_6 = "6h";
	
	/**
	 * 12小时
	 * 
	 */
	public static final String HOUR_12 = "12h";
	
	/**
	 * 1天
	 * 
	 */
	public static final String DAY_1 = "1d";
	
	/**
	 * 1周
	 * 
	 */
	public static final String WEEK_1 = "1w";
	
	/**
	 *  将属性转化成数组
	 * 
	 * @return
	 */
	public static String[] convertToArray() {
		List<String> result = new ArrayList<String>();
		try {
			Class<?> clazz = KlineTimeNodeConstant.class;
			Field[] fields = clazz.getFields();
			for(Field field : fields) {
				result.add(String.valueOf(field.get(null)));
			}
		} catch (Exception e) {}
		return result.toArray(new String[0]);
	}
	
}