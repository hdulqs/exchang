package com.batsoft.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.service.Kline24dataBusService;

@Service(value = "kline24dataBusService")
public class Kline24dataBusServiceImpl implements Kline24dataBusService {
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	// 定价币总量
	private static final String FAILED_VOL = "vol";
	// 交易币总量
	private static final String FAILED_AMOUT = "amout";
	// 24H最高价格
	private static final String FAILED_HIGHPRICE = "highPrice";
	// 24H最低价格
	private static final String FAILED_LOWPRICE = "lowPrice";
	// 最新价格
	private static final String FAILED_NEWPRICE = "newPrice";
	
	@Override
	public BigDecimal getVol(String symbol) {
		String cacheVol = jedisClient.hget(JedisDataSourceSignleton.DB1, String.format(RedisKeyConstant.KLINE_SYMBOL_24DATA, symbol), FAILED_VOL);
		if(StringUtils.hasText(cacheVol)) {
			return new BigDecimal(cacheVol);
		}
		return BigDecimal.ZERO;
	}
	@Override
	public BigDecimal getAmount(String symbol) {
		String cacheVol = jedisClient.hget(JedisDataSourceSignleton.DB1, String.format(RedisKeyConstant.KLINE_SYMBOL_24DATA, symbol), FAILED_AMOUT);
		if(StringUtils.hasText(cacheVol)) {
			return new BigDecimal(cacheVol);
		}
		return BigDecimal.ZERO;
	}
	@Override
	public BigDecimal getHighPrice(String symbol) {
		String cacheVol = jedisClient.hget(JedisDataSourceSignleton.DB1, String.format(RedisKeyConstant.KLINE_SYMBOL_24DATA, symbol), FAILED_HIGHPRICE);
		if(StringUtils.hasText(cacheVol)) {
			return new BigDecimal(cacheVol);
		}
		return BigDecimal.ZERO;
	}
	@Override
	public BigDecimal getLowPrice(String symbol) {
		String cacheVol = jedisClient.hget(JedisDataSourceSignleton.DB1, String.format(RedisKeyConstant.KLINE_SYMBOL_24DATA, symbol), FAILED_LOWPRICE);
		if(StringUtils.hasText(cacheVol)) {
			return new BigDecimal(cacheVol);
		}
		return BigDecimal.ZERO;
	}
	@Override
	public BigDecimal getNewPrice(String symbol) {
		String cacheVol = jedisClient.hget(JedisDataSourceSignleton.DB1, String.format(RedisKeyConstant.KLINE_SYMBOL_24DATA, symbol), FAILED_NEWPRICE);
		if(StringUtils.hasText(cacheVol)) {
			return new BigDecimal(cacheVol);
		}
		return BigDecimal.ZERO;
	}
}
