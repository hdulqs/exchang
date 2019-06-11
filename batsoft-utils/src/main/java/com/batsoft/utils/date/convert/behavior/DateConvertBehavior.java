package com.batsoft.utils.date.convert.behavior;

import java.text.DateFormat;

/**
 * 日期转换
 * 
 * @author simon
 */
public interface DateConvertBehavior<R, T> {

	/**
	 * 日期转换
	 * 
	 * @param format
	 *            格式化对象
	 * @param source
	 *            待格式化对象
	 * @return
	 */
	R convert(DateFormat format, T source);
}
