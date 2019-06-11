package com.batsoft.service.module.exchange.trade.service;

import com.batsoft.service.module.exchange.trade.model.TradeEntrust;

/**
 * Created by Administrator on 2018/4/14.
 */
public interface TradeService {

    /**
     * 添加委托单
     * @param tradeEntrust
     */
    void addEntrust(TradeEntrust tradeEntrust);

    /**
     * 撤消方法
     * @param orderId
     */
    void cancel(String userId,String coinPair,String orderId);

    /**
     * 撤消全部方法
     * @param userId
     */
    void cancelAll(String userId,String coinPair);
}
