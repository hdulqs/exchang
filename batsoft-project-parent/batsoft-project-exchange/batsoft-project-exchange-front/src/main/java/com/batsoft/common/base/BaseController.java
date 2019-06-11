package com.batsoft.common.base;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.batsoft.service.HomeBusService;
import com.batsoft.service.KlineBusService;
import com.batsoft.service.SocketBusService;
import com.batsoft.service.HandleOrderBusService;
import com.batsoft.service.TradingBusService;

public abstract class BaseController {
		
	@Resource
	protected HomeBusService homeBusService;
	
	@Resource
	protected TradingBusService tradingBusService;
	
	@Resource
	protected KlineBusService klineBusService;
	
	@Resource
	protected HandleOrderBusService priorityOrderBusService;
	
	@Resource
	protected SocketBusService socketBusService;

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
