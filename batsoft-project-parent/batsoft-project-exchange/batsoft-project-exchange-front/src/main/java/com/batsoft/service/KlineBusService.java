package com.batsoft.service;

import java.math.BigDecimal;
import java.util.List;

import com.batsoft.common.beans.vo.LoadKlineDataVO;
import com.batsoft.common.util.data.ResultData;

public interface KlineBusService {
	
    /**
     * 	MysqlK线图数据初始化到Redis;无实现
     * 
     */
    void initKline();

	/**
	 * 根据时间区间匹配K线图数据
	 * 
	 * @param symbol
	 * 			交易对
	 * @param subject
	 * 			时间类型
	 * @param from
	 * 			开始时间
	 * @param to
	 * 			结束时间
	 * @return
	 */
	void loadKlineData(String symbol, String subject, Long from, Long to, ResultData<List<LoadKlineDataVO>> result);
	
	/**
	 * K线图修复
	 * 
	 * @param symbol
	 * 			交易对
	 * @param maxPrice
	 * 			指定最大单价限制
	 * @param minPrice
	 * 			指定最小单价限制
	 * @param beginTime
	 * 			修复数据的起点时间
	 * @param endTime
	 * 			修复数据的终点时间
	 */
	@Deprecated
	void repairKlineData(String symbol, BigDecimal maxPrice, BigDecimal minPrice, Long beginTime, Long endTime);
	
	/**
	 * 修改K线图为一直涨
	 * 
	 * @param symbol
	 * 			交易对
	 * @param maxPrice
	 * 			指定最大单价限制
	 * @param minPrice
	 * 			指定最小单价限制
	 * @param beginTime
	 * 			修复数据的起点时间
	 * @param endTime
	 * 			修复数据的终点时间
	 * @param result
	 */
	void repairKeepUp(String symbol, BigDecimal maxPrice, BigDecimal minPrice, Long beginTime, Long endTime,
			ResultData<Object> result);
	
	/**
	 * 参照指定交易对K线图走向
	 * 
	 * @param refSymbol
	 * 			被参考交易对
	 * @param upSymbol
	 * 			更新交易对
	 * @param openPrice
	 * 			开盘价格
	 * @param beginTime
	 * 			开始时间
	 * @param endTime
	 * 			结束时间
	 * @param result
	 */
	void syncKlineRef(String refSymbol, String upSymbol, BigDecimal openPrice, Long beginTime, Long endTime,
			ResultData<Object> result);

}
