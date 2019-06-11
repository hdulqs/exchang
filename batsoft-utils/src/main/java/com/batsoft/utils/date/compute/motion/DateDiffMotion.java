package com.batsoft.utils.date.compute.motion;

import com.batsoft.utils.date.compute.behavior.DateDiffBehavior;

import java.util.Date;

/**
 * 计算两个时间之间时间间隔
 * 
 * @author ppmle
 */
public class DateDiffMotion implements DateDiffBehavior {

	@Override
	public Long dateDiffDays(Date beginTime, Date endTime) {
		Long beginTimestamp = null;
		if(beginTime == null) {
			System.out.println(" Class DateDiffMotion#dateDiffDays beginTime NullException ");
		}else {
			beginTimestamp = beginTime.getTime();
		}
		Long endTimestamp = null;
		if(endTime == null) {
			System.out.println(" Class DateDiffMotion#dateDiffDays endTime NullException ");
		}else {
			endTimestamp = endTime.getTime();
		}
		if(beginTimestamp == null || endTimestamp == null) {
			return null;
		}
		return (endTimestamp - beginTimestamp) / 86400000;
	}

	@Override
	public Long dateDiffHour(Date beginTime, Date endTime) {
		Long beginTimestamp = null;
		if(beginTime == null) {
			System.out.println(" Class DateDiffMotion#dateDiffHour beginTime NullException ");
		}else {
			beginTimestamp = beginTime.getTime();
		}
		Long endTimestamp = null;
		if(endTime == null) {
			System.out.println(" Class DateDiffMotion#dateDiffHour endTime NullException ");
		}else {
			endTimestamp = endTime.getTime();
		}
		if(beginTimestamp == null || endTimestamp == null) {
			return null;
		}
		return (endTimestamp - beginTimestamp) / 3600000;
	}

	@Override
	public Long dateDiffMinute(Date beginTime, Date endTime) {
		Long beginTimestamp = null;
		if(beginTime == null) {
			System.out.println(" Class DateDiffMotion#dateDiffMinute beginTime NullException ");
		}else {
			beginTimestamp = beginTime.getTime();
		}
		Long endTimestamp = null;
		if(endTime == null) {
			System.out.println(" Class DateDiffMotion#dateDiffMinute endTime NullException ");
		}else {
			endTimestamp = endTime.getTime();
		}
		if(beginTimestamp == null || endTimestamp == null) {
			return null;
		}
		return (endTimestamp - beginTimestamp) / 60000;
	}

	@Override
	public Long dateDiffSecond(Date beginTime, Date endTime) {
		Long beginTimestamp = null;
		if(beginTime == null) {
			System.out.println(" Class DateDiffMotion#dateDiffSecond beginTime NullException ");
		}else {
			beginTimestamp = beginTime.getTime();
		}
		Long endTimestamp = null;
		if(endTime == null) {
			System.out.println(" Class DateDiffMotion#dateDiffSecond endTime NullException ");
		}else {
			endTimestamp = endTime.getTime();
		}
		if(beginTimestamp == null || endTimestamp == null) {
			return null;
		}
		return (endTimestamp - beginTimestamp) / 1000;
	}

}
