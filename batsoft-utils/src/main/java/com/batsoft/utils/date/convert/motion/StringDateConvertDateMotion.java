package com.batsoft.utils.date.convert.motion;

import com.batsoft.utils.date.convert.behavior.DateConvertBehavior;

import java.text.DateFormat;
import java.util.Date;

import static org.nutz.dao.util.Pojos.log;

/**
 * 时间格式字符串转时间【Date】
 * 
 * @author simon
 */
public class StringDateConvertDateMotion implements DateConvertBehavior<Date, String> {

	@Override
	public Date convert(DateFormat format, String source) {
		Date result = null;
		if (format != null && source != null) {
			try {
				result = format.parse(source);
			} catch (Exception e) {
				log.info(" String Date Convert Date Exception " + e.getMessage());
			}
		}
		return result;
	}


}
