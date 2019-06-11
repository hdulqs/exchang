package com.batsoft.common.beans.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 鉴权数据容器
 * 
 * @author simon
 */
public class AuthParamDTO implements Serializable {

	private static final long serialVersionUID = -4572225818564983748L;
	
	private Integer count;
	
	private Date beginTime;
	
	private Date endTime;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
