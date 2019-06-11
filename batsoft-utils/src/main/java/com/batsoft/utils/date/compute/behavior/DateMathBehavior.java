package com.batsoft.utils.date.compute.behavior;

import java.util.Date;

/**
 * 时间/日期数学运算
 * 
 * @author ppmle
 */
public interface DateMathBehavior {
	
	/**
	 * 给设定时间加上秒
	 * 
	 * @param setTime
	 * 			设定的时间
	 * @param second
	 * 			秒数【时间单位秒】【备注：当此参数为负数时起相反效果】
	 * @return Date
	 */
	public Date dateAddSecond(Date setTime, Integer second);
	
	/**
	 * 给设置时间加上分钟
	 * 
	 * @param setTime
	 * 			设定的时间
	 * @param minute
	 * 			分钟【时间单位分】【备注：当此参数为负数时起相反效果】
	 * @return
	 */
	public Date dateAddMinute(Date setTime, Integer minute);
	
	/**
	 * 时间加上月数
	 * 
	 * @param setTime
	 * 			设定的时间
	 * @param month
	 * 			月数【时间单位月】【备注：当此参数为负数时起相反效果】
	 * @return Date
	 */
	public Date dateAddMonth(Date setTime, Integer months);
	
	/**
	 * 时间加上天数
	 * 
	 * @param setTime
	 * 			设定的时间
	 * @param days
	 * 			天数【时间单位天】【备注：当此参数为负数时起相反效果】
	 * @return
	 */
	public Date dateAddDay(Date setTime, Integer days);
	
	/**
	 * 时间加上年
	 * 
	 * @param setTime
	 * 			设定的时间
	 * @param years
	 * 			年数【时间单位年】【备注：当此参数为负数时起相反效果】
	 * @return
	 */
	public Date dateAddYear(Date setTime, Integer years);


}
