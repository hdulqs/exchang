package com.batsoft.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.batsoft.common.util.data.ResultData;
import com.batsoft.model.module.member.User;
import com.batsoft.mq.RabbitMqSender;
import com.batsoft.service.HandleOrderBusService;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;
import com.batsoft.service.module.exchange.trade.util.UUIDUtil;

@Service(value = "priorityOrderBusService")
public class HandleOrderBusServiceImpl implements HandleOrderBusService {
	
	@Resource
	private RabbitMqSender rabbitMqSender;
	
	@Override
	public void restingOrder(User user, String tradeCoinCode, String pricingCoinCode, BigDecimal minAmout, BigDecimal maxAmout, 
			BigDecimal entrustPrice, BigDecimal totalEntrustAmout, String entrustType, Long sleepTime, ResultData<Object> result) {
		Random ran = new Random();
		do {
			this.sleepTime(sleepTime);
			BigDecimal entrustAmount = BigDecimal.ZERO;
			if(minAmout.compareTo(maxAmout) == 0) {
				entrustAmount = totalEntrustAmout;
			}else {
				int switctdot = ran.nextInt(4);
				if(switctdot == 0) {
					// 随机整数数量
					do {
		            	entrustAmount = BigDecimal.valueOf(ran.nextInt(maxAmout.intValue()));
					} while (!(entrustAmount.compareTo(minAmout) > 0 && entrustAmount.compareTo(maxAmout) < 0));
				}else if(switctdot == 1) {
					// 最高值
					entrustAmount = maxAmout;
				}else if(switctdot == 2) {
					// 最低值
					entrustAmount = minAmout;
				}else if(switctdot == 3) {
					// 随机带小数数量
					do {
						BigDecimal baseAmount = BigDecimal.valueOf(ran.nextInt(maxAmout.intValue()));
						baseAmount = baseAmount.add(BigDecimal.valueOf(ran.nextDouble()));
						entrustAmount = baseAmount.setScale(ran.nextInt(5), BigDecimal.ROUND_HALF_DOWN);
					} while (!(entrustAmount.compareTo(minAmout) > 0 && entrustAmount.compareTo(maxAmout) < 0));
				}
				if(entrustAmount.compareTo(totalEntrustAmout) > 0) {
					entrustAmount = totalEntrustAmout;
				}
			}
			totalEntrustAmout = totalEntrustAmout.subtract(entrustAmount);
			pushEntrustOrder(entrustPrice, entrustAmount, tradeCoinCode, pricingCoinCode, user.getId(), entrustType);
		} while (totalEntrustAmout.compareTo(BigDecimal.ZERO) > 0);
	}
	
	/**
	 * 增加下单间隔时间
	 * 
	 * @param sleepTime
	 */
	private void sleepTime(Long sleepTime) {
		if(sleepTime != null && sleepTime > 0) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 推送委托订单到队列中
	 * 
	 * @param entrustPrice
	 * 				委托价格
	 * @param entrustAmount
	 * 				委托数量
	 * @param tradeCoinCode
	 * 				交易币
	 * @param pricingCoinCode
	 * 				定价币
	 * @param customerId
	 * 				客户ID
	 * @param type
	 * 				交易类型
	 */
	private void pushEntrustOrder(BigDecimal entrustPrice, BigDecimal entrustAmount, String tradeCoinCode, String pricingCoinCode, String customerId, String type) {
		TradeEntrust entrustDTO = new TradeEntrust();
        
        //交易币
        entrustDTO.setTradeCoinCode(tradeCoinCode);
        
        //定价币
        entrustDTO.setPricingCoinCode(pricingCoinCode);
        
        //委托人
        entrustDTO.setCustomerId(customerId);
        
        //委托类型
        entrustDTO.setEntrustType(type);
        entrustDTO.setCategory(BigInteger.ZERO.toString());
        
        //委托原始数量
        entrustDTO.setEntrustAmoutSql(entrustAmount);
        
        //委托匹配的数量
        entrustDTO.setEntrustAmout(entrustAmount);
        
        //委托价格
        entrustDTO.setEntrustPrice(entrustPrice);
        
        //委托单号
        entrustDTO.setOrderId(UUIDUtil.getUUID());
        entrustDTO.setEntrustState(TradeEntrust.ENTRUSTSTATE0);
        entrustDTO.setExecutedPrice(entrustPrice);
        
        //发送下单的消息
        rabbitMqSender.toAddEntrust(JSON.toJSONString(entrustDTO), tradeCoinCode, pricingCoinCode);
	}

}
