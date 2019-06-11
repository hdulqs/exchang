package com.batsoft.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.batsoft.common.base.BaseController;
import com.batsoft.common.beans.bp.SymbolTickerBP;
import com.batsoft.common.beans.vo.TickerVO;
import com.batsoft.common.util.ApiResultData;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;

/**
 * 市场行情【交易对Tickers】
 * 
 * @author simon
 */
@Controller
@RequestMapping(value = { "/market/ticker" })
public class MarketTickerController extends BaseController {
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	/**
	 * 全部交易对tickers
	 * 
	 * @param result
	 */
	@ResponseBody
	@RequestMapping(value = { "/all" }, method = { RequestMethod.GET }, produces={"application/json"})
	public String all(HttpServletRequest request, ApiResultData<List<TickerVO>> result) {
		Map<String, String> symbols = jedisClient.hgetall(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COINPAIR);
		Set<String> keys = symbols.keySet();
		List<TickerVO> resultData = new ArrayList<TickerVO>();
		for(String key : keys) {
			TickerVO data = new TickerVO();
			marketTickerBusService.allTicker(key, data);
			resultData.add(data);
		}
		result.setSuccessful();
		result.setData(resultData);
		return result.toJson();
	}
	
	/**
	 *    交易对ticker
	 * 
	 * @param result
	 */
	@ResponseBody
	@RequestMapping(value = { "/symbol" }, method = { RequestMethod.GET }, produces={"application/json"})
	public String symbol(SymbolTickerBP param, ApiResultData<TickerVO> result) {
		String symbol = param.getSymbol();
		TickerVO data = result.getData(TickerVO.class);
		marketTickerBusService.allTicker(symbol, data);
		result.setData(data);
		result.setSuccessful();
		return result.toJson();
	}
	
}
