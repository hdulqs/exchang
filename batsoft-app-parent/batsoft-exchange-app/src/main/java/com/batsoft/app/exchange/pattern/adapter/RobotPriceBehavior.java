package com.batsoft.app.exchange.pattern.adapter;

import java.math.BigDecimal;
import java.util.Random;

import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.utils.CoinPairConfigUtil;

/**
 * 生成价格行为
 * 
 * @author simon
 */
public abstract class RobotPriceBehavior {
	
	protected Random rand = new Random();
	
	protected JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	protected CoinPairConfigUtil coinPairConfig = new CoinPairConfigUtil();
	
	// 交易类型
	protected String type;
	
	// 最大委托单价
	protected BigDecimal maxEntrustPrice; 
	
	// 最小委托单价
	protected BigDecimal minEntrustPrice;
	
	// 交易对
	protected String coinPair;
	
	/**
	 * 完全参构造器
	 * 
	 * @param type
	 * 			委托单类型
	 * @param maxEntrustPrice
	 * 			最大委托单价
	 * @param minEntrustPrice
	 * 			最小委托单价
	 * @param coinPair
	 * 			交易对
	 */
	public RobotPriceBehavior(String type, BigDecimal maxEntrustPrice, BigDecimal minEntrustPrice, String coinPair) {
		super();
		this.type = type;
		this.maxEntrustPrice = maxEntrustPrice;
		this.minEntrustPrice = minEntrustPrice;
		this.coinPair = coinPair;
	}
	
	/**
	 * 校验订单类型是否为目标类型
	 * 
	 * @return
	 */
	protected boolean verifyEntrustType(String assignType) {
		return this.type.equals(assignType);
	}
	
	/**
	 * 获取委托价格
	 * 
	 * @return
	 */
	public abstract BigDecimal getEntrustPrice();
	
}
