package com.batsoft.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.batsoft.common.beans.vo.DataAllVO;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.service.Kline24dataBusService;
import com.batsoft.service.MarketTickerShareBusService;

@Service(value = "marketTickerShareBusService")
public class MarketTickerShareBusServiceImpl implements MarketTickerShareBusService {
	
	@Resource
	private Kline24dataBusService kline24dataBusService;

	@Override
	public void myTokenTickerData(String key, DataAllVO data) {
		// 24H定价币交易总量
		BigDecimal vol = kline24dataBusService.getVol(key);
		data.setVolume_24h(vol);
		
		// 24H交易币交易总量
		BigDecimal amount = kline24dataBusService.getAmount(key);
		data.setAmount_24h(amount);
		
		// 最新价格
		BigDecimal lastPrice = kline24dataBusService.getNewPrice(key);
		data.setLastPrice(lastPrice);
		
		data.setSymbol(key.split(CHS.underline.getValue())[0]);
		data.setAnchor(key.split(CHS.underline.getValue())[1]);
		data.setPriceUpdatedAt(System.currentTimeMillis() / 1000);
	}

}
