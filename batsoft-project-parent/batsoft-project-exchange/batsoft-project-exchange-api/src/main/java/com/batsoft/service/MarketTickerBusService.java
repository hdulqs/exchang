package com.batsoft.service;

import com.batsoft.common.beans.vo.TickerVO;

public interface MarketTickerBusService {
	
	/**
	 * 获取ticker
	 * 
	 * @param symbol
	 * 			交易对
	 * @param result
	 * 			响应数据
	 */
	void allTicker(String symbol, TickerVO result);

}
