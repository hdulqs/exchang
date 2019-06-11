package com.batsoft.mq;

/**
 * 委托订单推送到前台【发布订阅模式 交换机+队列】
 * 
 * {@link EntrustOrderFanoutQueue} 
 * 
 * @author simon
 */
public class EntrustOrderFanoutConfig {
	
	/**
	 * 添加委托订单推送
	 * 
	 */
	public static final String ADD_ENTRUST_EXCHANGE_NAME = "ENTRUST_ORDER_FANOUT";
	
	/**
	 * 取消委托订单推送
	 * 
	 */
	public static final String CANCEL_ENTRUST_EXCHANGE_NAME = "CANCEL_ENTRUST_FANOUT";
	
}
