package com.batsoft.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.batsoft.common.base.BaseController;
import com.batsoft.common.beans.bo.PushKlineDataBP;
import com.batsoft.common.beans.bo.UserCoinBalanceWsBP;
import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.common.util.ModuleAssert;

/**
 * WebSocket初始化
 * 
 * @author simon
 */
@Controller
@RequestMapping("/socket")
public class SocketController extends BaseController {
	
	/**
     * K线图推送：交易大厅
     * 
     * @param message
     */
    @MessageMapping(value = { "/pushKlineData" })
    public void pushKlineData(PushKlineDataBP param){
    	ModuleAssert.notNull(param, ModuleMessageEnum.PARAM_IS_NULL);
    	
    	String symbol = param.getSymbol();
    	ModuleAssert.notNull(symbol, ModuleMessageEnum.COIN_PAIR_IS_NULL);
    	
    	String subject = param.getSubject();
    	ModuleAssert.notNull(param, ModuleMessageEnum.KLINE_DATA_SUBJECT_IS_NULL);
    	
    	String channel = param.getChannel();
    	
    	socketBusService.pushKlineData(symbol, subject, channel);
    }
	
	/**
     * 用户货币账号余额
     * 
     * @param message
     */
    @MessageMapping(value = { "/initUserCoin" })
    public void initUserCoin(UserCoinBalanceWsBP param){
    	socketBusService.initUserCoin(param.getCustomerId(), param.getSymbol());
    }
    
    /**
	 * 初始化委托单
	 *
	 * @param coinPair
	 */
	@MessageMapping(value = { "/initBook" })
	public void initBook(String coinPair) {
		socketBusService.wsInitBook(coinPair);
	}
	
	/**
	 * 初始化成交单
	 *
	 * @param coinPair
	 */
	
	@MessageMapping(value = { "/initTrade" })
	public void initTrade(String coinPair) {
		socketBusService.wsInitTrade(coinPair);
	}

	/**
	 * 初始化ticker
	 */
	@MessageMapping(value = { "/initTicker" })
	public void initTicker() {
		socketBusService.wsInitTicker();
	}
}
