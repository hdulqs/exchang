package com.batsoft.utils.date;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础时间对象
 *
 */
public abstract class BaseDate implements Serializable {

	private static final long serialVersionUID = 5019858322366697375L;
	
	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static Date getNowTime() {
		return new Date();
	}
}
