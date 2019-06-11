package com.batsoft.utils.date.convert.motion;

import com.batsoft.utils.date.convert.behavior.DateConvertBehavior;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static org.nutz.dao.util.Pojos.log;

/**
 * 格式化日期
 * 
 * @author simon
 */
public class DateConvertDateMotion implements DateConvertBehavior<Date, Date> {

	@Override
	public Date convert(DateFormat format, Date source) {
		Date result = null;
		if (format != null && source != null) {
			try {
				String emp = format.format(source);
				result = format.parse(emp);
			} catch (ParseException e) {
				log.info(" Date Convert format Date ParseException " + e.getMessage());
			}
		}
		return result;
	}

}
