package com.batsoft.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.mvc.view.HttpServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.batsoft.common.base.BaseController;
import com.batsoft.common.beans.vo.DataAllVO;
import com.batsoft.common.util.ApiResultData;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.core.common.enums.EntrustTradeTypeEnum;
import com.batsoft.utils.gson.GsonSingleton;
import com.google.gson.FieldNamingPolicy;

@Controller
@RequestMapping(value = { "/market/ticker" })
public class MarketTickerShareController extends BaseController {
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	@ResponseBody
	@RequestMapping(value = { "/feixiaohao" }, method = { RequestMethod.GET }, produces={"application/json"})
	public String all(HttpServletRequest request, HttpServerResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
		Map<String, String> symbols = jedisClient.hgetall(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COINPAIR);
		Set<String> keys = symbols.keySet();
		for(String symbol : keys) {
			Map<String, String> result = new HashMap<String, String>();
			String cacheOpen = jedisClient.get(JedisDataSourceSignleton.DB1, String.format(RedisKeyConstant.KLINE_S_OPENPRICE, symbol));
			result.put("opening_price", cacheOpen);
			
			BigDecimal close = kline24dataBusService.getNewPrice(symbol);
			result.put("closing_price", String.valueOf(close));
			
			BigDecimal low = kline24dataBusService.getLowPrice(symbol);
			result.put("min_price", String.valueOf(low));
			
			BigDecimal high = kline24dataBusService.getHighPrice(symbol);
			result.put("max_price", String.valueOf(high));
			
			result.put("average_price", String.valueOf((low.add(high)).divide(BigDecimal.valueOf(2), 2, BigDecimal.ROUND_DOWN)));
			result.put("units_traded", String.valueOf(close));
			
			BigDecimal vol = kline24dataBusService.getVol(symbol);
			result.put("volume_1day", String.valueOf(vol));
			
			result.put("volume_7day", String.valueOf(vol.multiply(BigDecimal.valueOf(7))));
			
			// 买一
			String priceZsetKey = String.format(RedisKeyConstant.TRA_PRICE_ZSET, symbol, EntrustTradeTypeEnum.BUY.getCode());
			Set<String> priceZSet = jedisClient.zrange(JedisDataSourceSignleton.DB1, priceZsetKey, 0, 1);
			if(priceZSet != null && priceZSet.size() > 0) {
				result.put("buy_price", priceZSet.iterator().next());
			}
			
			// 卖一
			String priceZsetKey1 = String.format(RedisKeyConstant.TRA_PRICE_ZSET, symbol, EntrustTradeTypeEnum.SELL.getCode());
			Long sellSize = jedisClient.zcard(JedisDataSourceSignleton.DB1, priceZsetKey1);
			Set<String> priceZSet1 = jedisClient.zrange(JedisDataSourceSignleton.DB1, priceZsetKey1, -sellSize, 0);
			if(priceZSet1 != null && priceZSet1.size() > 0) {
				result.put("sell_price", priceZSet1.iterator().next());
			}
			// 24H涨跌幅
        	result.put("change", exchangeTickerSymbolBusService.getRate(symbol));
			result.put("symbol", symbol);
			data.put(symbol.split(CHS.underline.getValue())[0], result);
		}
		resultMap.put("data", data);
		resultMap.put("date", System.currentTimeMillis());
		resultMap.put("status", "0000");
		return GsonSingleton.getInstance().toJson(resultMap);
	}
	
	/**
	 * 24小时成交量
	 * 
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = { "/myToken" }, method = { RequestMethod.GET }, produces={"application/json"})
	public String data(HttpServletRequest request, HttpServletResponse response, ApiResultData<List<DataAllVO>> result) {
		Map<String, String> symbols = jedisClient.hgetall(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COINPAIR);
		Set<String> keys = symbols.keySet();
		List<DataAllVO> resultData = new ArrayList<DataAllVO>();
		for(String key : keys) {
			DataAllVO data = new DataAllVO();
			marketTickerShareBusService.myTokenTickerData(key, data);
			resultData.add(data);
		}
		result.setSuccessful();
		result.setData(resultData);
		return GsonSingleton.getInstance().toJson(result, FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
	}
	
}
