package com.batsoft.service;

public interface KlineJobService {
	
	/**
	 * 更新Redis种openPrice
	 * 
	 */
	void updateOpenPrice();
	
	
	/**
	 * 更新【最新价、24H最高价、24H最低价】
	 * 
	 */
	void updateOpenAndMaxAndMinPrice();

}
