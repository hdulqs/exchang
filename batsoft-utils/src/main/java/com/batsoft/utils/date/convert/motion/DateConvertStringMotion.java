package com.batsoft.utils.date.convert.motion;

import com.batsoft.utils.date.convert.behavior.DateConvertBehavior;

import java.text.DateFormat;
import java.util.Date;

/**
 * 日期类型转字符串日期
 * 
 * @author simon
 */
public class DateConvertStringMotion implements DateConvertBehavior<String, Date> {

	@Override
	public String convert(final DateFormat format, final Date source) {
		String result = new String();
		if (format != null && source != null) {
			result = format.format(source);
		}
		return result;
	}

}
