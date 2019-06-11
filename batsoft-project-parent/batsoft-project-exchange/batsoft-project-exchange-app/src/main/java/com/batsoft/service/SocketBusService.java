package com.batsoft.service;

public interface SocketBusService {
	
	 /**
     * socket 初始化委托单【买单和卖单】
     * 
     * @param coinPair 
     * 				交易对
     */
    void wsInitBook(String coinPair);
    
    /**
     * socket 初始化成交单【成交栏】
     * 
     * @param coinPair 
     * 			交易对
     */
    void wsInitTrade(String coinPair);
    
    /**
     * socket 初始化ticker
     * 
     */
    void wsInitTicker(String symbol);
    
    /**
     * K线图推送
     * 
     * @param symbol
     * 			交易对
     * @param subject
     * 			时间类型
     */
	void initKlineData(String symbol, String subject);
	
	/**
	 * 初始化用户交易币余额
	 * 
	 * @param customerId
	 * 				客户ID
	 * @param symbol
	 * 				交易对
	 */
	void initUserCoin(String customerId, String symbol);
    
}
