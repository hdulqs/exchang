package com.batsoft.service;

import java.math.BigDecimal;

import com.batsoft.common.util.data.ResultData;
import com.batsoft.model.module.member.User;

public interface HandleOrderBusService {
	
	/**
	 * 优先下单
	 * 
	 * @param user
	 * 			下单用户
	 * @param tradeCoinCode
	 * 			交易币
	 * @param pricingCoinCode
	 * 			定价币
	 * @param minAmout
	 * 			最小数量
	 * @param maxAmout
	 * 			最大数量
	 * @param entrustPrice
	 * 			委托单价
 	 * @param sleepTime
 	 * 			下单间隔时间
	 * @param result
	 */
	void restingOrder(User user, String tradeCoinCode, String pricingCoinCode, BigDecimal minAmout, BigDecimal maxAmout, 
			BigDecimal entrustPrice, BigDecimal totalEntrustAmout, String entrustType, Long sleepTime, ResultData<Object> result);
	
}
