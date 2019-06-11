package com.batsoft.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.batsoft.common.base.BaseController;
import com.batsoft.common.beans.bo.LoadKlineDataBP;
import com.batsoft.common.beans.bo.RepairKeepUpBP;
import com.batsoft.common.beans.bo.RepairKlineDataBP;
import com.batsoft.common.beans.bo.SyncKlineBP;
import com.batsoft.common.beans.vo.LoadKlineDataVO;
import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.common.util.ModuleAssert;
import com.batsoft.common.util.data.ResultData;
import com.batsoft.core.common.enums.AppConfigKeyEnum;
import com.batsoft.exception.ModuleBusinessRuntimeException;
import com.batsoft.service.module.system.service.config.AppConfigService;

/**
 * K线图
 * 
 * 
 * @author simon
 */
@Controller
@RequestMapping(value = { "/kline" })
public class KlineController extends BaseController {
	
	@Resource
	private AppConfigService appConfigService;
	
    /**
     * K线图获取：交易大厅
     * 
     * @param message
     */
    @ResponseBody
    @RequestMapping(value = { "/loadKlineData" }, method = { RequestMethod.GET })
    public String loadKlineData(LoadKlineDataBP param, ResultData<List<LoadKlineDataVO>> result){
    	ModuleAssert.notNull(param, ModuleMessageEnum.PARAM_IS_NULL);
    	
    	String symbol = param.getSymbol();
    	ModuleAssert.notNull(symbol, ModuleMessageEnum.COIN_PAIR_IS_NULL);
    	
    	String subject = param.getSubject();
    	ModuleAssert.notNull(param, ModuleMessageEnum.KLINE_DATA_SUBJECT_IS_NULL);
    	
    	Long from = param.getFrom();
    	ModuleAssert.notNull(param, ModuleMessageEnum.KLINE_DATA_FROM_IS_NULL);
    	
    	Long to = param.getTo();
    	ModuleAssert.notNull(param, ModuleMessageEnum.KLINE_DATA_TO_IS_NULL);
    	
    	klineBusService.loadKlineData(symbol, subject, from, to, result);
    	return result.toJson();
    }
    
    /**
     * K线图修复
     * 
     * @return
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(value = { "/repairKlineData" }, method = { RequestMethod.GET })
    public String repairKlineData(RepairKlineDataBP param, ResultData<Object> result) {
    	ModuleAssert.notNull(param, ModuleMessageEnum.PARAM_IS_NULL);
    	
    	String symbol = param.getSymbol();
    	ModuleAssert.notNull(symbol, ModuleMessageEnum.SYMBOL_IS_NULL);
    	
    	BigDecimal maxPrice = param.getMaxPrice();
    	BigDecimal minPrice = param.getMinPrice();
    	
    	Long beginTime = param.getBeginTime();
    	ModuleAssert.notNull(beginTime, ModuleMessageEnum.BEGIN_TIME_IS_NULL);
    	
    	Long endTime = param.getEndTime();
    	if(endTime == null) {
    		endTime = new Date().getTime();
    	}
    	ModuleAssert.notNull(endTime, ModuleMessageEnum.END_TIME_IS_NULL);
    	
    	// klineBusService.repairKlineData(symbol, maxPrice, minPrice, beginTime, endTime);
    	
    	result.setSuccessful();
    	return result.toJson();
    }
    
    /**
     * 修改BTK线图为一直涨
     * 
     * @param param
     * @param result
     * @return
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(value = { "/repairKeepUp" }, method = RequestMethod.GET)
    public String repairKeepUp(RepairKeepUpBP param, ResultData<Object> result) {
    	// 交易对
    	String symbol = param.getSymbol();
    	ModuleAssert.notNull(symbol, ModuleMessageEnum.SYMBOL_IS_NULL);
    	
    	// 最大单价
    	BigDecimal maxPrice = param.getMaxPrice();
    	ModuleAssert.notNull(symbol, ModuleMessageEnum.MAX_PRICE_IS_NULL);
    	
    	// 最小单价
    	BigDecimal minPrice = param.getMinPrice();
    	ModuleAssert.notNull(symbol, ModuleMessageEnum.MIN_PRICE_IS_NULL);
    	
    	// 开始时间
    	Long beginTime = param.getBeginTime();
    	ModuleAssert.notNull(beginTime, ModuleMessageEnum.BEGIN_TIME_IS_NULL);
    	
    	// 结束时间
    	Long endTime = param.getEndTime();
    	ModuleAssert.notNull(endTime, ModuleMessageEnum.END_TIME_IS_NULL);
    	
    	// klineBusService.repairKeepUp(symbol, maxPrice, minPrice, beginTime, endTime, result);
    	
    	result.setSuccessful();
    	return result.toJson();
    }
    
    /**
     * 同步K线图维度
     * 
     */
    @ResponseBody
    @RequestMapping(value = { "/syncKlineRef" }, method = { RequestMethod.GET })
    public String syncKlineRef(SyncKlineBP param, ResultData<Object> result) {
    	// 授权码
		String handlerToken = param.getHandlerToken();
		ModuleAssert.notNull(handlerToken, ModuleMessageEnum.TRADE_HANDLER_TOKEN_IS_NULL);
		String handlerTokenConfig = appConfigService.findValueByKey(AppConfigKeyEnum.HANDLE_TRADE_TOKEN.getCode());
		if(!Objects.equals(handlerToken, handlerTokenConfig)) {
			throw new ModuleBusinessRuntimeException(ModuleMessageEnum.TRADE_HANDLER_TOKEN_ERROR);
		}
    			
    	// 参照交易对
    	String refSymbol = param.getRefSymbol();
    	ModuleAssert.notNull(refSymbol, ModuleMessageEnum.SYMBOL_IS_NULL);
    	
    	// 更新交易对
    	String upSymbol = param.getUpSymbol();
    	ModuleAssert.notNull(upSymbol, ModuleMessageEnum.SYMBOL_IS_NULL);
    	
    	// 初始价格
    	BigDecimal openPrice = param.getOpenPrice();
    	ModuleAssert.notNull(openPrice, ModuleMessageEnum.OPEN_PRICE_IS_NULL);
    	
    	// 开始时间
    	Long beginTime = param.getBeginTime();
    	ModuleAssert.notNull(beginTime, ModuleMessageEnum.BEGIN_TIME_IS_NULL);
    	
    	// 结束时间
    	Long endTime = param.getEndTime();
    	ModuleAssert.notNull(endTime, ModuleMessageEnum.END_TIME_IS_NULL);
    	
    	klineBusService.syncKlineRef(refSymbol, upSymbol, openPrice, beginTime, endTime, result);
    	
    	result.setSuccessful();
    	return result.toJson();
    }
    
    
}
