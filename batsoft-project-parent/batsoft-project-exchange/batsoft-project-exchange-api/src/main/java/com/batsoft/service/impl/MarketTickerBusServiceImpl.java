package com.batsoft.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.batsoft.common.beans.vo.TickerVO;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.service.Kline24dataBusService;
import com.batsoft.service.MarketTickerBusService;

@Service(value = "marketTickerBusService")
public class MarketTickerBusServiceImpl implements MarketTickerBusService {
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	@Resource
	private Kline24dataBusService kline24dataBusService;
	
	@Override
	public void allTicker(String symbol, TickerVO result) {
		// 交易对
		result.setSymbol(symbol);
		
		// 24H定价币成交总量
		BigDecimal vol = kline24dataBusService.getVol(symbol);
		result.setVol(vol);
		
		// 24H交易币成交数量
		BigDecimal amount = kline24dataBusService.getAmount(symbol);
		result.setAmount(amount);
		
		// 24H最高价格
		BigDecimal high = kline24dataBusService.getHighPrice(symbol);
		result.setHigh(high);
		
		// 24小时最低价格
		BigDecimal low = kline24dataBusService.getLowPrice(symbol);
		result.setLow(low);
		
		// 最新价格
		BigDecimal close = kline24dataBusService.getNewPrice(symbol);
		result.setClose(close);
		
		// 开盘价格
		BigDecimal open = BigDecimal.ZERO;
		String cacheOpen = jedisClient.get(JedisDataSourceSignleton.DB1, String.format(RedisKeyConstant.KLINE_S_OPENPRICE, symbol));
		if(StringUtils.hasText(cacheOpen)) {
			open = new BigDecimal(cacheOpen);
		}
		result.setOpen(open);
	}

}
