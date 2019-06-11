package com.batsoft.service.module.exchange.trade.service;

import java.math.BigDecimal;
import java.util.Map;

import com.batsoft.model.module.exchange.vo.KlineVo;
import com.batsoft.model.module.exchange.vo.TickerVo;

public interface TradeKlineService {

    /**
     * socket 推送委托单
     * @param coinPair 交易对
     * @param type 委托类型  买 /卖
     * @param price 委托价格
     * @param vol 委托数量
     */
    void wsBook(String coinPair, String type, BigDecimal price, BigDecimal vol);

    /**
     * 推送成交单
     * 
     * @param type
     * 			交易类型
     * @param coinPair
     * 			交易对
     * @param time
     * 			时间戳
     * @param price
     * 			价格
     * @param vol
     * 			交易总量
     */
    void pushTradeInfo(String type, String coinPair, Long time, BigDecimal price, BigDecimal vol);
    
    
    /**
     * socket 推送allTicker
     * @param  ticker
     */
    void wsTicker(TickerVo ticker);

    /**
     * 推送kline
     */
    void wsKline(String coinPair,String  time, KlineVo kline);
    
    /**
     * 获取买一 卖一
     * @param type
     * @param coinPair
     * @return
     */
    BigDecimal findBuySellFirst(String type,String coinPair);

    /**
     * 推送K线图数据
     * 
     * @param coinPair
     */
    void mathKline(String coinPair);

    /**
     * 24小时数据 和最新的钱包余额
     * 	
     * @param coinPair
     * 				交易对
     */
    void mathTicker(String coinPair);
    
    /**
     * 推送客户指定交易对货币账户可用余额
     * 
     * @param coinPair
     * 				交易对
     * @param customerId
     * 				客户ID
     * @param userCoinAccountBalance
     */
	void pushUserCoinAccountBalance(String coinPair, String customerId, Map<String, BigDecimal> userCoinAccountBalance);
    
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
     */
    void wsInitTicker();
    
    /**
     * 推送当前委托单
     * @param
     * 		  symbol
     * 			交易对
     * @param userId
     * 			用户ID
     * @param orderId
     * 			订单编号
     */
	void pushTradeEntrustIng(String symbol, String userId, String orderId);


}
