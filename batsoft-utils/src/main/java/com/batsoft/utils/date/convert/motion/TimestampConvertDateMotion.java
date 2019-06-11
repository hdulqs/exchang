package com.batsoft.utils.date.convert.motion;

import com.batsoft.utils.date.convert.behavior.DateConvertBehavior;
import com.batsoft.utils.date.convert.behavior.TimestampConvertDataBehavior;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static org.nutz.dao.util.Pojos.log;

/**
 * 时间戳转时间【Date】
 * 
 * @author simon
 */
public class TimestampConvertDateMotion implements DateConvertBehavior<Date, Long>, TimestampConvertDataBehavior<Date, Long> {

	@Override
	public Date convert(DateFormat format, Long timestamp) {
		Date date = null;
		if (format != null && timestamp > 0) {
			try {
				date = new Date(timestamp);
				String emp = format.format(date);
				date = format.parse(emp);
			} catch (ParseException e) {
				log.info("Timestamp Convert Date ParseException ");
			}
		}
		return date;
	}
	
	@Override
	public Date convert(Long source) {
		Date date = null;
		if (source != null && source > 0) {
			date = new Date(source);
		}
		return date;
	}

}
