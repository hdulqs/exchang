package com.batsoft.utils.date.compute.behavior;

import java.util.Date;

/**
 * 时间差
 * 
 * @author ppmle
 */
public interface DateDiffBehavior {
	
	/**
	 * 两个时间相差多少天
	 * 
	 * @param beginTime
	 * 				开始时间
	 * @param endTime
	 * 				结束时间
	 * @return
	 */
	public Long dateDiffDays(Date beginTime, Date endTime);
	
	/**
	 * 两个时间相差多少个小时
	 * 
	 * @param beginTime
	 * 				开始时间
	 * @param endTime
	 * 				结束时间
	 * @return
	 */
	public Long dateDiffHour(Date beginTime, Date endTime);
	
	/**
	 * 两个时间相差多少分钟
	 * 
	 * @param beginTime
	 * 				开始时间
	 * @param endTime
	 * 				结束时间
	 * @return
	 */
	public Long dateDiffMinute(Date beginTime, Date endTime);
	
	/**
	 * 两个时间相差多少秒
	 * 
	 * @param beginTime
	 * 				开始时间
	 * @param endTime
	 * 				结束时间
	 * @return
	 */
	public Long dateDiffSecond(Date beginTime, Date endTime);
	
}
