package com.batsoft.service;

import com.batsoft.common.beans.vo.DataAllVO;

public interface MarketTickerShareBusService {
	
	/**
	 * dataAll接口数据
	 * 
	 * @param key
	 * @param data
	 */
	void myTokenTickerData(String key, DataAllVO data);

}
