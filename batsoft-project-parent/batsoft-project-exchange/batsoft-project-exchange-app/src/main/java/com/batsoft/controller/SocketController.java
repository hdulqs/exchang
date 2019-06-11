package com.batsoft.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.batsoft.common.base.BaseController;
import com.batsoft.common.beans.bo.UserCoinBalanceWsBP;
import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.common.util.ModuleAssert;
import com.batsoft.core.common.enums.CHS;

/**
 * WebSocket初始化
 * 
 * @author simon
 */
@EnableAsync
@Controller
@RequestMapping("/socket")
public class SocketController extends BaseController {
	
	/**
     * 初始化K线图
     * 
     * @param message
     * 			BT_USDT@1m
     */
    @MessageMapping(value = { "/initKline" })
    public void pushKlineData(String message){
    	ModuleAssert.notNull(message, ModuleMessageEnum.PARAM_IS_NULL);
    	String[] param = message.split(CHS.arobase.getValue());
    	// 交易对
    	String symbol = param[0];
    	ModuleAssert.notNull(symbol, ModuleMessageEnum.COIN_PAIR_IS_NULL);
    	// 时间类型
    	String time = param[1];
    	ModuleAssert.notNull(time, ModuleMessageEnum.KLINE_DATA_SUBJECT_IS_NULL);
    	
    	socketBusService.initKlineData(symbol, time);
    }
	
	/**
     * 用户货币账号余额
     * 
     * @param message
     */
    @MessageMapping(value = { "/initUserCoin" })
    public void initUserCoin(UserCoinBalanceWsBP param){
    	ModuleAssert.notNull(param, ModuleMessageEnum.PARAM_IS_NULL);
    	// 交易对
    	String symbol = param.getSymbol();
    	ModuleAssert.notNull(symbol, ModuleMessageEnum.SYMBOL_IS_NULL);
    	socketBusService.initUserCoin(param.getCustomerId(), symbol);
    }
    
    /**
	 * 初始化委托单
	 *
	 * @param symbol
	 */
	@MessageMapping(value = { "/initBook" })
	public void initBook(String symbol) {
		ModuleAssert.notNull(symbol, ModuleMessageEnum.SYMBOL_IS_NULL);
		socketBusService.wsInitBook(symbol);
	}
	
	/**
	 * 初始化成交单
	 *
	 * @param symbol
	 */
	@MessageMapping(value = { "/initTrade" })
	public void initTrade(String symbol) {
		ModuleAssert.notNull(symbol, ModuleMessageEnum.SYMBOL_IS_NULL);
		socketBusService.wsInitTrade(symbol);
	}

	/**
	 * 初始化ticker
	 */
	@MessageMapping(value = { "/initTicker" })
	public void initTicker(String symbol) {
		ModuleAssert.notNull(symbol, ModuleMessageEnum.SYMBOL_IS_NULL);
		socketBusService.wsInitTicker(symbol);
	}
	
}
