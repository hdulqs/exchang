package com.batsoft.utils.date.compute.motion;

import com.batsoft.utils.date.compute.behavior.DateMathBehavior;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间/日期数学运算
 * 
 * @author ppmle
 */
public class DateMathMotion implements DateMathBehavior {
	
	@Override
	public Date dateAddSecond(Date setTime, Integer second) {
		if(setTime == null) {
			System.out.println(" Class DateMathMotion#dateAddSecond setTime Param NullException ");
		}
		if(second == null) {
			System.out.println(" Class DateMathMotion#dateAddSecond second Param NullException ");
		}
		if(setTime == null || second == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(setTime);
		calendar.add(Calendar.SECOND, second.intValue());
		return calendar.getTime();
	}
	
	@Override
	public Date dateAddMonth(Date setTime, Integer months) {
		if(setTime == null) {
			System.out.println(" Class DateMathMotion#dateAddMonth setTime Param NullException ");
		}
		if(months == null) {
			System.out.println(" Class DateMathMotion#dateAddMonth months Param NullException ");
		}
		if(setTime == null || months == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(setTime);
		calendar.add(Calendar.MONTH, months.intValue());
		return calendar.getTime();
	}

	@Override
	public Date dateAddDay(Date setTime, Integer days) {
		if(setTime == null) {
			System.out.println(" Class DateMathMotion#dateAddDay setTime Param NullException ");
		}
		if(days == null) {
			System.out.println(" Class DateMathMotion#dateAddDay days Param NullException ");
		}
		if(setTime == null || days == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(setTime);
		calendar.add(Calendar.DAY_OF_YEAR, days.intValue());
		return calendar.getTime();
	}

	@Override
	public Date dateAddYear(Date setTime, Integer years) {
		if(setTime == null) {
			System.out.println(" Class DateMathMotion#dateAddYear setTime Param NullException ");
		}
		if(years == null) {
			System.out.println(" Class DateMathMotion#dateAddYear years Param NullException ");
		}
		if(setTime == null || years == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(setTime);
		calendar.add(Calendar.YEAR, years.intValue());
		return calendar.getTime();
	}


	@Override
	public Date dateAddMinute(Date setTime, Integer minute) {
		if(setTime == null) {
			System.out.println(" Class DateMathMotion#dateAddMinute setTime Param NullException ");
		}
		if(minute == null) {
			System.out.println(" Class DateMathMotion#dateAddMinute minute Param NullException ");
		}
		if(setTime == null || minute == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(setTime);
		calendar.add(Calendar.MINUTE, minute.intValue());
		return calendar.getTime();
	}


}
