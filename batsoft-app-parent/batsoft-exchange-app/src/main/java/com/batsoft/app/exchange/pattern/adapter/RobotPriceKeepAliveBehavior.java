package com.batsoft.app.exchange.pattern.adapter;

import java.math.BigDecimal;
import java.util.List;

import com.batsoft.service.module.exchange.trade.model.TradeEntrust;

/**
 * 保活挂单行为
 * 
 * @author simon
 */
public abstract class RobotPriceKeepAliveBehavior extends RobotPriceBehavior {
	
	protected List<TradeEntrust> matching;
	
	/**
	 * 完全参构造器
	 * 
	 * @param matching
	 * 			当前价格区间已有订单
	 * @param type
	 * 			当前委托单类型
	 * @param maxEntrustPrice
	 * 			最大委托价格
	 * @param minEntrustPrice
	 * 			最小委托价格
	 * @param coinPair
	 * 			交易对
	 */
	public RobotPriceKeepAliveBehavior(List<TradeEntrust> matching, String type, BigDecimal maxEntrustPrice, BigDecimal minEntrustPrice, String coinPair) {
		super(type, maxEntrustPrice, minEntrustPrice, coinPair);
		this.matching = matching;
	}

	/**
	 * 随机买单委托价格
	 * 
	 * @return
	 */
	protected abstract BigDecimal randomBuyEntrustPrice();
	
	/**
	 * 随机买单委托价格
	 * 
	 * @return
	 */
	protected abstract BigDecimal randomSellEntrustPrice();
	

}
