package com.batsoft.core.common;

import com.batsoft.core.cache.motion.KlineSymbol24dataUtil;

/**
 * Redis键常量声明
 * 
 * @author simon
 */
public class RedisKeyConstant {
	
	//--------------------------------------------------------Begin:DB1----------------------------------------------------------------
	// DB1说明：DB1内仅放入撮合交易相关的数据
	/**
	 * user:entrusting:%s_%s:%s
	 * 
	 */
	public static final String USER_ENTRUSTING = "user:entrusting:%s_%s:%s";
	
	/**
	 * user:entrusting:%s:%s
	 * 
	 */
	public static final String USER_ENTRUSTING_S_S = "user:entrusting:%s:%s";
	
	/**
	 * K线瞬时数据【kline:%s:timeZoneData】
	 * 
	 */
	public static final String KLINE_TIME_ZONE_DATA = "kline:%s:timeZoneData";
	
	/**
	 * K线Last瞬时数据【kline:%s:timeZoneDataLast】
	 * 
	 */
	public static final String KLINE_TIME_ZONE_DATA_LAST = "kline:%s:timeZoneDataLast";
	
	/**
	 * 交易对委托单数据 【tra:BTC_USDT:buy:3415.44000】
	 * 
	 */
	public static final String TRA_COINPAIR_TRADE_KEY = "tra:%s:%s:%s";
	
	/**
	 * 委托单价格 【tra:%s:%sPriceZset】
	 * 
	 */
	public static final String TRA_PRICE_ZSET = "tra:%s:%sPriceZset";
	
	/**
	 * OpenPrice 开盘价格
	 * 
	 * kline:%s:openPrice
	 */
	public static final String KLINE_S_OPENPRICE = "kline:%s:openPrice";
	
	/**
	 * 货币24小数数据
	 * 
	 */
	public static final String KLINE_S_24DATA = "kline:%s:24data";
	
	/**
	 * kline:BTC_USDT:order
	 * 
	 */
	public static final String KLINE_S_ORDER = "kline:%s:order";
	
	/**
	 * tra:coin
	 * 
	 */
	public static final String TRA_COIN = "tra:coin";
	
	/**
	 * tra:coinpair
	 * 
	 */
	public static final String TRA_COINPAIR = "tra:coinpair";
	
	/**
	 * user:entrusthistory:%s:%s
	 * 
	 */
	public static final String USER_ENTRUSTHISTORY = "user:entrusthistory:%s:%s";
	
	/**
	 * user:dcAccount:%s
	 * 
	 */
	public static final String USER_DCACCOUNT = "user:dcAccount:%s";
	
	/**
     * K线图24小数数据
     * 
     * kline:BT_ETH:24data
     */
    public static final String KLINE_SYMBOL_24DATA = "kline:%s:24data";
    
    /**
     * 24小时订单
     * 
     * kline:%s_%s:24order
     */
    public static final String KLINE_SYMBOL_24ORDER = "kline:%s_%s:24order";
	
	//--------------------------------------------------------END:DB1----------------------------------------------------------------
	
	/**
	 * K线数据
	 * 
	 * exchange:kline:%s:%s
	 */
	public static final String EXCHANGE_KLINE_DATA = "exchange:kline:%s:%s";
	
	/**
	 * 交易对配置管理
	 * 
	 */
	public static final String EXCHANGE_TRADE_COINS = "exchange:trade_digits_coins";
	
	/**
	 * 取消委托交易逻辑锁
	 * 
	 */
	public static final String CANCEL_ENTRUSTING_LOGIC = "COUNT:%s";
	
	/**
	 * 货币成交量涨跌幅<br />
	 * 
	 * exchange:ticker:%s =》 【exchange:ticker:BTC_USDT】
	 * 
	 * @see KlineSymbol24dataUtil
	 */
	@Deprecated
	public static final String EXCHANGE_TICKER = "exchange:ticker:%s";
	
	/**
	 * USDT 转 CNY 汇率
	 * 
	 * appConfig:usdtPrice
	 */
	public static final String USDT_TO_PRICE = "appConfig:usdtPrice";
	
	/**
	 * 交易控制逻辑锁键
	 * 
	 */
	public static final String HANDLER_LOGIC_LOCK_KEY = "HANDLER_LOGIC_LOCK_KEY_%s";
	
	/**
	 * 区块高度缓冲Key
	 * 
	 */
    public static final String BLOCK_HIGH = "block_high:";
    
    /**
     *	下单频率控制
     * 
     */
    public static final String ORDER_LOCK_KEY = "ORDER_LOCK";
    
}
