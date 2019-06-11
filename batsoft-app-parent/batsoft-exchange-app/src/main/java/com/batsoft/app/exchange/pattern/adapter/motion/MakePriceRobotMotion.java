package com.batsoft.app.exchange.pattern.adapter.motion;

import java.math.BigDecimal;

import com.batsoft.app.exchange.pattern.adapter.RobotMakePriceBehavior;
import com.batsoft.core.common.utils.CoinPairConfigUtil;

/**
 * 生成指定区间内随机委托价格
 * 
 * @author simon
 */
public class MakePriceRobotMotion extends RobotMakePriceBehavior {

	/**
	 * 全参构造器
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
	public MakePriceRobotMotion(String type, BigDecimal maxEntrustPrice, BigDecimal minEntrustPrice, String coinPair) {
		super(type, maxEntrustPrice, minEntrustPrice, coinPair);
	}

	@Override
	public BigDecimal getEntrustPrice() {
		Integer priceDecimal = decimalUtil.getCoinConfigField(coinPair, CoinPairConfigUtil.PRICE_DECIMAL);
		BigDecimal multiple = BigDecimal.ONE;
		for(int i = 0; i < priceDecimal; i++) {
			multiple = multiple.multiply(BigDecimal.TEN);
		}
		return findBetweenValue(multiple, priceDecimal);
	}
	
	/**
	 *  随机介于两者之间的值
	 * 
	 * @return
	 */
	public BigDecimal findBetweenValue(BigDecimal multiple, Integer priceDecimal) {
		if(minEntrustPrice.compareTo(maxEntrustPrice) == 0) {
			return minEntrustPrice;
		}
		if(minEntrustPrice.compareTo(maxEntrustPrice) > 0) {
			return maxEntrustPrice;
		}
		if(maxEntrustPrice.compareTo(minEntrustPrice) > 0) {
			BigDecimal maxPrice = maxEntrustPrice.multiply(multiple);
			BigDecimal minPrice = minEntrustPrice.multiply(multiple);
			BigDecimal result = BigDecimal.ZERO;
			do {
				result = BigDecimal.valueOf(rand.nextInt(maxPrice.intValue()));
			} while (minPrice.compareTo(result) > 0);
			return result.divide(multiple, priceDecimal, BigDecimal.ROUND_DOWN);
		}
		return BigDecimal.ZERO;
	}
	
}
