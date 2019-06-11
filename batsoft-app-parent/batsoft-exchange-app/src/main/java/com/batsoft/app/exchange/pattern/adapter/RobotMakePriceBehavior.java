package com.batsoft.app.exchange.pattern.adapter;

import java.math.BigDecimal;

import com.batsoft.core.common.utils.CoinPairConfigUtil;

/**
 * 生成成交价格行为
 * 
 * @author simon
 */
public abstract class RobotMakePriceBehavior extends RobotPriceBehavior {
	
	protected CoinPairConfigUtil decimalUtil = new CoinPairConfigUtil();
	
	/**
	 * 完全参构造器
	 * 
	 * @param type
	 * 			委托单类型
	 * @param maxEntrustPrice
	 * 			最大委托价格
	 * @param minEntrustPrice
	 * 			最小委托价格
	 * @param coinPair
	 * 			委托单交易对
	 */
	public RobotMakePriceBehavior(String type, BigDecimal maxEntrustPrice, BigDecimal minEntrustPrice, String coinPair) {
		super(type, maxEntrustPrice, minEntrustPrice, coinPair);
	}
	
}
