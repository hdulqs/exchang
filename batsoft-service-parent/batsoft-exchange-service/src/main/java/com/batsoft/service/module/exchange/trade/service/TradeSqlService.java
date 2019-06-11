package com.batsoft.service.module.exchange.trade.service;

public interface TradeSqlService {

    /**
     * 消息转换成sql,同步到数据中
     * @param sql
     */
    void messageToSql(String sql);

}
