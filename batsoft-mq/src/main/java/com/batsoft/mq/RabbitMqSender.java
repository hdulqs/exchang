package com.batsoft.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqSender {

	@Autowired
	private AmqpTemplate rabbitTemplate;
	
	/**
	 * 添加委托
	 */
	public void toAddEntrust(String message, String tradeCoinCode, String pricingCoinCode) {
		String symbol = tradeCoinCode + "_" + pricingCoinCode;
		toAddEntrust(message, symbol);
	}
	
	/**
	 * 添加委托
	 */
	public void toAddEntrust(String message, String symbol) {
		this.rabbitTemplate.convertAndSend(symbol, message);
	}
	
	/**
	 * 取消委托
	 */
	public void cancelEntrust(String message) {
		this.rabbitTemplate.convertAndSend(RabbitConfig.CANCEL, message);
	}
	
	/**
	 * 取消全部委托
	 */
	public void cancelAllEntrust(String message) {
		this.rabbitTemplate.convertAndSend(RabbitConfig.CANCEL_ALL, message);
	}
	
	/**
	 * 操作redis账户
	 */
	public void toRedisAccount(String message) {
		this.rabbitTemplate.convertAndSend(RabbitConfig.REDIS_ACCOUNT_QUEUE, message);
	}

	/**
	 * 同步sql
	 */
	public void toSql(String message) {
		this.rabbitTemplate.convertAndSend(RabbitConfig.SQL_QUEUE, message);
	}

	/**
	 * 注册初始化币账户
	 */
	public void toRegisterInit(String message) {
		this.rabbitTemplate.convertAndSend(RabbitConfig.REGISTER_INIT_QUEUE, message);
	}

	/**
	 * 添加币种初始化用户币账户
	 */
	public void toAddCoinInit(String message) {
		this.rabbitTemplate.convertAndSend(RabbitConfig.ADD_COIN_INIT_QUEUE, message);
	}

	/**
	 * 交易委托单推送至前端 【订阅模式】 添加一条消息到广播台
	 * 
	 */
	public void entrustOrderToFront(String message) {
		this.rabbitTemplate.convertAndSend(EntrustOrderFanoutConfig.ADD_ENTRUST_EXCHANGE_NAME, "", message);
	}

	/**
	 * 取消委托单推送至前端 【订阅模式】 添加一条消息到广播台
	 * 
	 */
	public void cancelEntrustOrderToFront(String message) {
		this.rabbitTemplate.convertAndSend(EntrustOrderFanoutConfig.CANCEL_ENTRUST_EXCHANGE_NAME, "", message);
	}

}
