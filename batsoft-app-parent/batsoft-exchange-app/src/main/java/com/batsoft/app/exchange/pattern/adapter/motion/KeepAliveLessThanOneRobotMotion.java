package com.batsoft.app.exchange.pattern.adapter.motion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.batsoft.app.exchange.pattern.adapter.RobotPriceKeepAliveBehavior;
import com.batsoft.core.common.enums.EntrustTradeTypeEnum;
import com.batsoft.core.common.utils.CoinPairConfigUtil;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;

/**
 * 保活挂单：【生成委托价格小1】
 * 
 * @author simon
 */
public class KeepAliveLessThanOneRobotMotion extends RobotPriceKeepAliveBehavior {
	
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
	public KeepAliveLessThanOneRobotMotion(List<TradeEntrust> matching, String type, BigDecimal maxEntrustPrice, BigDecimal minEntrustPrice, String coinPair) {
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
	 * <ul>
	 * 	<li>最小波动价格</li>
	 * 	<li>【比如小数位数为5，那么最小价格为 1 / 100000 = 0.00001】</li>
	 * </ul>
	 */
	private BigDecimal minFluctuatePrice() {
		String prefix = "1";
		String suffix = "0";
		BigDecimal result = BigDecimal.valueOf(0.01);
		Integer digit = coinPairConfig.getCoinConfigField(coinPair, CoinPairConfigUtil.PRICE_DECIMAL);
		if(digit != null && digit > 0) {
			StringBuilder dividend = new StringBuilder(prefix);
			for(int i = 0; i < digit; i++) {
				dividend.append(suffix);
			}
			result = BigDecimal.valueOf(1 / Double.parseDouble(String.valueOf(dividend)));
		}
		return result;
	}
	
	@Override
	public BigDecimal randomBuyEntrustPrice() {
		// 步长价格
		BigDecimal stepPrice = minFluctuatePrice();
		
		int size = matching == null?0 : matching.size();
		if(matching == null || size == 0) {
			return minEntrustPrice.subtract(stepPrice);
		}
		if(matching != null && size == 1) {
			BigDecimal basePrice = matching.get(0).getEntrustPrice();
			return basePrice.subtract(stepPrice);
		}
		if(matching != null && size < 15) {
			// 价格倒序 【由大到小】
			matching.sort((TradeEntrust pre, TradeEntrust rear) -> rear.getEntrustPrice().compareTo(pre.getEntrustPrice()));
			
			// 校验第一位价格
			TradeEntrust maxTradeEntrust = matching.get(0);
			BigDecimal diffPrice = minEntrustPrice.subtract(maxTradeEntrust.getEntrustPrice());
			if(diffPrice.compareTo(stepPrice) > 0) {
				return minEntrustPrice.subtract(stepPrice);
			}
			
			// 规则：检测价格填空
			for(int i = 0; i < (matching.size() - 1); i++) {
				TradeEntrust max = matching.get(i);
				TradeEntrust min = matching.get(i + 1);
				
				BigDecimal maxPrice = max.getEntrustPrice();
				BigDecimal minPrice = min.getEntrustPrice();
				
				diffPrice = maxPrice.subtract(minPrice);
				if(diffPrice.compareTo(stepPrice) > 0) {
					return maxPrice.subtract(stepPrice);
				}
			}
			
			// 规则：价格往两边端展开
			TradeEntrust minTradeEntrust = matching.get(matching.size() - 1);
			BigDecimal minEntrustPrice = minTradeEntrust.getEntrustPrice();
			return minEntrustPrice.subtract(stepPrice);
		}
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal randomSellEntrustPrice() {
		BigDecimal stepPrice = minFluctuatePrice();
		
		int size = matching == null?0 : matching.size();
		if(matching == null || size == 0) {
			return maxEntrustPrice.add(stepPrice);
		}
		if(matching != null && size == 1) {
			BigDecimal basePrice = matching.get(0).getEntrustPrice();
			return basePrice.add(stepPrice);
		}
		if(matching != null && size < 15) {
			// 排序：价格由小到大
			matching.sort((TradeEntrust pre, TradeEntrust rear) -> pre.getEntrustPrice().compareTo(rear.getEntrustPrice()));
			// 规则
			TradeEntrust minTradeEntrust = matching.get(0);
			BigDecimal diffPrice = maxEntrustPrice.subtract(minTradeEntrust.getEntrustPrice());
			if(diffPrice.compareTo(stepPrice) > 0) {
				return maxEntrustPrice.add(stepPrice);
			}
			// 规则：价格填空
			for(int i = 0; i < (size - 1); i++) {
				TradeEntrust min = matching.get(i);
				TradeEntrust max = matching.get(i + 1);
				
				BigDecimal minPrice = min.getEntrustPrice();
				BigDecimal maxPrice = max.getEntrustPrice();
				
				diffPrice = maxPrice.subtract(minPrice);
				if(diffPrice.compareTo(stepPrice) > 0) {
					return minPrice.add(stepPrice);
				}
			}
			// 规则：价格往两边端展开
			TradeEntrust maxTradeEntrust = matching.get(matching.size() - 1);
			BigDecimal maxEntrustPrice = maxTradeEntrust.getEntrustPrice();
			return maxEntrustPrice.add(stepPrice);
		}
		return BigDecimal.ZERO;
	}
	
}
