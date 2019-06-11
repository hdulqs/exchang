package com.batsoft.common.config;

import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.socketjs.WebSocketProperties;

/**
 * WebSocket订阅地址配置
 * 
 * @author simon
 */
public class WebSocketSubscibeConfig {
	
	/**
	 * Socket Properties
	 * 
	 */
	private static WebSocketProperties webSocketProperties;
	
	private static void initialize() {
		if(webSocketProperties == null) {
			webSocketProperties = (WebSocketProperties) SpringContextUtil.getBean("webSocketProperties");
		}
	}
	
	/**
	 * K线图订阅地址
	 * 
	 * @param symbol
	 * 			交易对
	 * @param timeNode
	 * 			时间节点类型
	 * @return
	 */
	public static String klineWs(String symbol, String timeNode) {
		initialize();
		return webSocketProperties.getBroker() + "/market/" + symbol + "@kline_" + timeNode;
	}
	
	/**
	 * 成交单订阅地址
	 * 
	 * @param symbol
	 * 			交易对
	 * @return
	 */
	public static String tradeWs(String symbol){
		initialize();
		return webSocketProperties.getBroker() + "/" + symbol + "@trade";
	}
	
	/**
	 * 委托挂单订阅地址
	 * 
	 * @param symbol
	 * 			交易对
	 * @return
	 */
	public static String bookWs(String symbol) {
		initialize();
		return webSocketProperties.getBroker() + "/" + symbol + "@book";
	}
	
	/**
	 * 账户信息订阅地址
	 * 
	 * @param symbol
	 * 			交易对
	 * @return
	 */
	public static String userCoinWs(String symbol, String customerId) {
		initialize();
		return webSocketProperties.getBroker() + "/" + symbol + "@user_" + customerId;
	}
	
	/**
	 * 成交量和涨跌幅订阅地址
	 * 
	 * @return
	 */
	public static String tickerWs() {
		initialize();
		return webSocketProperties.getBroker() + "/" + "all@ticker";
	}
	
}
