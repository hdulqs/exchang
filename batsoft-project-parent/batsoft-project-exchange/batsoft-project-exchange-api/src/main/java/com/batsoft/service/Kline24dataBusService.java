package com.batsoft.service;

import java.math.BigDecimal;

public interface Kline24dataBusService {
	
	/**
	 * 获取定价币总量
	 * 
	 * @return
	 */
	BigDecimal getVol(String symbol);
	
	/**
	 * 交易币总量
	 * 
	 * @return
	 */
	BigDecimal getAmount(String symbol);
	
	/**
	 * 24H最高价格
	 * 
	 */
	BigDecimal getHighPrice(String symbol);
	
	/**
	 * 24H最低价格
	 * 
	 * @return
	 */
	BigDecimal getLowPrice(String symbol);
	
	/**
	 * 最新价格
	 * 
	 * @return
	 */
	BigDecimal getNewPrice(String symbol);
	
}
