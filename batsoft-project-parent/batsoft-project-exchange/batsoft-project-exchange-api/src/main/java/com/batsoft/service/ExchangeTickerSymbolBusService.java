package com.batsoft.service;

public interface ExchangeTickerSymbolBusService {
	
	/**
	 * 24H涨跌幅度
	 * 
	 * @param symbol
	 * @return
	 */
	String getRate(String symbol);
	
	
	/**
	 * 最新价格
	 * 
	 * @param symbol
	 * @return
	 */
	String getNewPrice(String symbol);
	
	/**
	 * 24H成交量
	 * 
	 * @param symbol
	 * @return
	 */
	String getVol(String symbol);
	
	/**
	 * 24H最高价
	 * 
	 * @param symbol
	 * @return
	 */
	String getHigh(String symbol);
	
	/**
	 * 24H最高价
	 * 
	 * @param symbol
	 * @return
	 */
	String getLow(String symbol);
	
}
