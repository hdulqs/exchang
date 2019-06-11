package com.batsoft.service;

import java.util.Date;

public interface KlineJobBusService {
	
	/**
	 * K线图定时更新
	 * 
	 * @param timeType K线时间类型
	 * @param currentTime 当前时间
	 */
	public void updateKlineNode(String timeType, Date currentTime);
	
	/**
	 * K线图定时推送
	 * 
	 * @param timeType
	 */
	public void pushKlineNode(String timeType);

}
