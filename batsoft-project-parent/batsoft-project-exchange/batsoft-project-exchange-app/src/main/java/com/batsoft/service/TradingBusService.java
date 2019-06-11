package com.batsoft.service;

import java.util.List;

import com.batsoft.common.beans.vo.GetBookVO;
import com.batsoft.common.beans.vo.GetKlineDataVO;
import com.batsoft.common.beans.vo.GetTickerVO;
import com.batsoft.common.beans.vo.GetTradeVO;
import com.batsoft.common.util.result.ResultData;

public interface TradingBusService {
	
	/**
	 * Get K线图数据
	 * 
	 * @param symbol
	 * 			交易对
	 * @param time
	 * 			时间类型
	 * @param from 
	 * 			开始时间（时间戳）
	 * @param to
	 * 			结束时间（时间戳）
	 * @param result
	 */
	void getKlineData(String symbol, String time, Long from, Long to, ResultData<List<GetKlineDataVO>> result);
	
	/**
	 * Get委托单
	 * 
	 * @param symbol
	 * 			交易对
	 * @param result 
	 */
	void getBook(String symbol, ResultData<GetBookVO> result);
	
	/**
	 * Get 成交单
	 * 
	 * @param symbol
	 * 			交易对
	 * @param result
	 */
	void getTrade(String symbol, ResultData<List<GetTradeVO>> result);
	
	/**
	 * Get ticker
	 * 
	 * @param symbol
	 * 			交易对
	 * @param result
	 */
	void getTicker(String symbol, ResultData<GetTickerVO> result);

}
