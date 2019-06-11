package com.batsoft.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.service.ExchangeTickerSymbolBusService;

@Service(value = "exchangeTickerSymbolBusService")
public class ExchangeTickerSymbolBusServiceImpl implements ExchangeTickerSymbolBusService {

	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	private String symbol(String symbol, Integer index) {
		String key = String.format(RedisKeyConstant.EXCHANGE_TICKER, symbol);
        String value = jedisClient.get(JedisDataSourceSignleton.DB0, key);
        if(StringUtils.hasText(value)) {
        	JSONArray jsonData = JSONArray.parseArray(value);
        	return jsonData.getString(index);
        }
        return null;
	}
	
	@Override
	public String getRate(String symbol) {
		return symbol(symbol, 6);
	}

	@Override
	public String getNewPrice(String symbol) {
		return symbol(symbol, 7);
	}

	@Override
	public String getVol(String symbol) {
		return symbol(symbol, 8);
	}

	@Override
	public String getHigh(String symbol) {
		return symbol(symbol, 9);
	}

	@Override
	public String getLow(String symbol) {
		return symbol(symbol, 10);
	}
	
}
