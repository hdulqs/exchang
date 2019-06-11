package com.batsoft.common.base;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.batsoft.service.ExchangeTickerSymbolBusService;
import com.batsoft.service.Kline24dataBusService;
import com.batsoft.service.MarketTickerBusService;
import com.batsoft.service.MarketTickerShareBusService;

/**
 * module Base Controller
 * 
 * @author simon
 */
public abstract class BaseController {
	
	@Resource
	protected MarketTickerBusService marketTickerBusService;
	
	@Resource
	protected MarketTickerShareBusService marketTickerShareBusService;
	
	@Resource
	protected Kline24dataBusService kline24dataBusService;
	
	@Resource
	protected ExchangeTickerSymbolBusService exchangeTickerSymbolBusService;
	
	/**
	 * 设置值到Session域
	 * 
	 * @param request
	 */
	protected void setSessionValue(HttpServletRequest request, String key, Object value) {

		request.getSession().setAttribute(key, value);
	}

	/**
	 * 获取Session域值
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <R> R getSessionValue(HttpServletRequest request, String key) {

		return (R) request.getSession().getAttribute(key);
	}
	
	/**
	 * 移除会话中的值
	 * 
	 * @param key
	 */
	protected void removeSessionValue(HttpServletRequest request,String key){
		
		request.getSession().removeAttribute(key);
	}
	
}
