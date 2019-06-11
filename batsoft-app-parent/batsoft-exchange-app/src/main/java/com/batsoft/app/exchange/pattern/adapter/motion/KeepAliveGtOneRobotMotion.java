package com.batsoft.app.exchange.pattern.adapter.motion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import com.batsoft.app.exchange.pattern.adapter.RobotPriceKeepAliveBehavior;
import com.batsoft.core.common.enums.EntrustTradeTypeEnum;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;

/**
 * 保活挂单：【委托价格大于1】
 * 
 * @author simon
 */
public class KeepAliveGtOneRobotMotion extends RobotPriceKeepAliveBehavior {
	
	// 保活挂单的两单之间最大跨度系数
	private static final BigDecimal span = BigDecimal.valueOf(0.06);
	
	/**
	 * 完全参构造器
	 * 
	 * @param matching
	 * 			当前价格区间内的订单
	 * @param type
	 * 			委托类型
	 * @param maxEntrustPrice
	 * 			最大委托单价
	 * @param minEntrustPrice
	 * 			最小委托单价
	 * @param coinPair
	 * 			交易对
	 */
	public KeepAliveGtOneRobotMotion(List<TradeEntrust> matching, String type, BigDecimal maxEntrustPrice, BigDecimal minEntrustPrice, String coinPair) {
		super(matching, type, maxEntrustPrice, minEntrustPrice, coinPair);
	}
	
	@Override
	public BigDecimal getEntrustPrice() {
		if(Objects.equals(type, EntrustTradeTypeEnum.BUY.getCode())) {
			return randomBuyEntrustPrice();
		}else {
			return randomSellEntrustPrice();
 		}
	}
	
	/**
	 * 得到波动比例
	 * 
	 * @return
	 */
	private BigDecimal fluProbRate() {
		Integer prob = BigInteger.ZERO.intValue();
		do {
			prob = rand.nextInt(6);
		} while (prob < 3);
		return BigDecimal.valueOf(prob / 100.0);
	}
	
	@Override
	protected BigDecimal randomBuyEntrustPrice() {
		// 计算波动比率
		BigDecimal fluProbRate = fluProbRate();
		
		// 计算少于两单
		int size = matching == null?0 : matching.size();
		if(matching == null || size == 0) {
			BigDecimal fluctuatePrice = minEntrustPrice.multiply(fluProbRate);
			return minEntrustPrice.subtract(fluctuatePrice);
		}
		if(matching != null && size == 1) {
			BigDecimal basePrice = matching.get(0).getEntrustPrice();
 			BigDecimal fluctuatePrice = basePrice.multiply(fluProbRate);
			return basePrice.subtract(fluctuatePrice);
		}
		if(matching != null && size < 15) {
			// 价格倒序 【由大到小】
			matching.sort((TradeEntrust pre, TradeEntrust rear) -> rear.getEntrustPrice().compareTo(pre.getEntrustPrice()));
			
			// 校验第一位价格
			TradeEntrust maxTradeEntrust = matching.get(0);
			BigDecimal diffPrice = minEntrustPrice.subtract(maxTradeEntrust.getEntrustPrice());
			BigDecimal rate = diffPrice.divide(minEntrustPrice, 2, BigDecimal.ROUND_HALF_EVEN);
			if(rate.compareTo(span) > 0) {
				return minEntrustPrice.subtract(minEntrustPrice.multiply(fluProbRate));
			}
			
			// 规则：补位填空
			for(int i = 0; i < (matching.size() - 1); i++) {
				TradeEntrust max = matching.get(i);
				TradeEntrust min = matching.get(i + 1);
				
				BigDecimal maxPrice = max.getEntrustPrice();
				BigDecimal minPrice = min.getEntrustPrice();
				
				diffPrice = maxPrice.subtract(minPrice);
				rate = diffPrice.divide(maxPrice, 2, BigDecimal.ROUND_HALF_EVEN);
				if(rate.compareTo(span) > 0) {
					return maxPrice.subtract(maxPrice.multiply(fluProbRate));
				}
			}
			
			// 规则：价格往两边端展开
			TradeEntrust minTradeEntrust = matching.get(matching.size() - 1);
			BigDecimal minEntrustPrice = minTradeEntrust.getEntrustPrice();
			return minEntrustPrice.subtract(minEntrustPrice.multiply(fluProbRate));
		}
		return BigDecimal.ZERO;
	}

	@Override
	protected BigDecimal randomSellEntrustPrice() {
		// 计算波动比率
		BigDecimal fluProbRate = fluProbRate();
		
		// 计算少于两单
		int size = matching == null?0 : matching.size();
		if(matching == null || size == 0) {
			BigDecimal fluctuatePrice = maxEntrustPrice.multiply(fluProbRate);
			return maxEntrustPrice.add(fluctuatePrice);
		}
		if(matching != null && size == 1) {
			BigDecimal basePrice = matching.get(0).getEntrustPrice();
 			BigDecimal fluctuatePrice = basePrice.multiply(fluProbRate);
			return basePrice.add(fluctuatePrice);
		}
		if(matching != null && size < 15) {
			// 价格升序【由小到大】
			matching.sort((TradeEntrust pre, TradeEntrust rear) -> pre.getEntrustPrice().compareTo(rear.getEntrustPrice()));
			
			// 校验第一个挂单价格
			TradeEntrust minTradeEntrust = matching.get(0);
			BigDecimal diffPrice = maxEntrustPrice.subtract(minTradeEntrust.getEntrustPrice());
			BigDecimal rate = diffPrice.divide(maxEntrustPrice, 2, BigDecimal.ROUND_HALF_EVEN);
			if(rate.compareTo(span) > 0) {
				return maxEntrustPrice.add(maxEntrustPrice.multiply(fluProbRate));
			}
			
			// 规则：价格填空
			for(int i = 0; i < (size - 1); i++) {
				TradeEntrust min = matching.get(i);
				TradeEntrust max = matching.get(i + 1);
				
				BigDecimal minPrice = min.getEntrustPrice();
				BigDecimal maxPrice = max.getEntrustPrice();
				
				diffPrice = maxPrice.subtract(minPrice);
				rate = diffPrice.divide(maxPrice, 2, BigDecimal.ROUND_HALF_EVEN);
				if(rate.compareTo(span) > 0) {
					return minPrice.add(minPrice.multiply(fluProbRate));
				}
			}
			
			// 规则：价格往两边端展开
			TradeEntrust maxTradeEntrust = matching.get(matching.size() - 1);
			BigDecimal maxEntrustPrice = maxTradeEntrust.getEntrustPrice();
			return maxEntrustPrice.add(maxEntrustPrice.multiply(fluProbRate));
		}
		return BigDecimal.ZERO;
	}
	
}
