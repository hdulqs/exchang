package com.batsoft.utils.date.convert.motion;

import com.batsoft.utils.date.convert.behavior.DateConvertBehavior;
import com.batsoft.utils.date.convert.behavior.TimestampConvertDataBehavior;
import com.batsoft.utils.date.convert.config.DateFormatConfig;

import java.text.DateFormat;
import java.util.Date;

/**
 * 时间戳转字符串日期
 * 
 * @author simon
 */
public class TimestampConvertStringMotion implements DateConvertBehavior<String, Long>, TimestampConvertDataBehavior<String, Long> {

	@Override
	public String convert(DateFormat format, Long timestamp) {
		String result = new String();
		if (format != null && timestamp > 0) {
			Date date = new Date(timestamp);
			result = format.format(date);
		}
		return result;
	}

	@Override
	public String convert(Long source) {
		String result = new String();
		if (source != null && source > 0) {
			Date date = new Date(source);
			DateFormat format = DateFormatConfig.YYYYMMDDMMHHSS();
			result = format.format(date);
		}
		return result;
	}

}
