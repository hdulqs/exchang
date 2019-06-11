package com.batsoft.utils.date.convert.behavior;

public interface TimestampConvertDataBehavior<R, T> {
	
	/**
	 * 长整型时间戳转时间对象
	 * 
	 * @param source
	 * 			长整形时间戳
	 * @return
	 */
	R convert(T source);
}
