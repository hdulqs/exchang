package com.batsoft.controller;

import java.math.BigDecimal;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.batsoft.common.base.BaseController;
import com.batsoft.common.beans.bo.ControlTradeBP;
import com.batsoft.common.beans.bo.RestingOrderBP;
import com.batsoft.common.enums.ControlTradeHandleTypeEnum;
import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.common.util.ModuleAssert;
import com.batsoft.common.util.data.ResultData;
import com.batsoft.core.cache.LogicLockSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.enums.AppConfigKeyEnum;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.exception.ModuleBusinessRuntimeException;
import com.batsoft.model.module.member.User;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.service.module.system.service.config.AppConfigService;

/**
 * 优先下单
 * 
 * @author simon
 */
@Controller
@RequestMapping(value = { "/handleOrder" })
public class HandleOrderController extends BaseController {
	
	@Resource
	private UserService userService;
	
	@Resource
	private AppConfigService appConfigService;
	
	private LogicLockSignleton logicLock = LogicLockSignleton.getInstance();
	
	/**
	 * 控制交易
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = { "/controlTrade" }, method = RequestMethod.GET)
	public String controlTrade(ControlTradeBP param, ResultData<Object> result) {
		// 授权码
		String handlerToken = param.getHandlerToken();
		ModuleAssert.notNull(handlerToken, ModuleMessageEnum.TRADE_HANDLER_TOKEN_IS_NULL);
		String handlerTokenConfig = appConfigService.findValueByKey(AppConfigKeyEnum.HANDLE_TRADE_TOKEN.getCode());
		if(!Objects.equals(handlerToken, handlerTokenConfig)) {
			throw new ModuleBusinessRuntimeException(ModuleMessageEnum.TRADE_HANDLER_TOKEN_ERROR);
		}
		
		// 操作类型
		ControlTradeHandleTypeEnum handleType = param.getHandleType();
		ModuleAssert.notNull(handleType, ModuleMessageEnum.TRADE_HANDLE_TYPE_IS_NULL);
		
		// 交易币
		String tradeCoinCode = param.getTradeCoinCode();
		ModuleAssert.notNull(tradeCoinCode, ModuleMessageEnum.TRADE_COIN_CODE_IS_NULL);
		
		// 定价币
		String pricingCoinCode = param.getPricingCoinCode();
		ModuleAssert.notNull(pricingCoinCode, ModuleMessageEnum.PRICING_COIN_CODE_IS_NULL);
		
		// 操作
		String symbol = tradeCoinCode + CHS.underline.getValue() + pricingCoinCode;
		String lockKey = String.format(RedisKeyConstant.HANDLER_LOGIC_LOCK_KEY, symbol);
		if(Objects.equals(handleType, ControlTradeHandleTypeEnum.DISABLE_TRADE)) {
			// 禁用
			logicLock.setLogicLock(lockKey, 60 * 60 * 24 * 365L);
		}else if(Objects.equals(handleType, ControlTradeHandleTypeEnum.ENABLED_TRADE)) {
			// 启用
			logicLock.cencelLogicLock(lockKey);
		}
		result.setSuccessful();
		return result.toJson();
	}
	
	/**
	 * 优先下单
	 * 
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = { "/restingOrder" }, method = RequestMethod.GET)
	public String restingOrder(RestingOrderBP param, ResultData<Object> result) {
		ModuleAssert.notNull(param, ModuleMessageEnum.PARAM_IS_NULL);
    	
		// 授权码
		String handlerToken = param.getHandlerToken();
		ModuleAssert.notNull(handlerToken, ModuleMessageEnum.TRADE_HANDLER_TOKEN_IS_NULL);
		String handlerTokenConfig = appConfigService.findValueByKey(AppConfigKeyEnum.HANDLE_TRADE_TOKEN.getCode());
		if(!Objects.equals(handlerToken, handlerTokenConfig)) {
			throw new ModuleBusinessRuntimeException(ModuleMessageEnum.TRADE_HANDLER_TOKEN_ERROR);
		}
		
    	// 用户名
    	String userName = param.getUserName();
    	ModuleAssert.notNull(userName, ModuleMessageEnum.USER_NAME_IS_NULL);
    	
    	// 密码
    	String password = param.getPassword();
    	ModuleAssert.notNull(password, ModuleMessageEnum.PASSWORD_IS_NULL);
    	
    	// 检验用户
    	User user = userService.findUser(userName);
    	if(user != null && StringUtils.hasText(user.getPassword())) {
    		if(!Objects.equals(user.getPassword(), password)) {
    			throw new ModuleBusinessRuntimeException(ModuleMessageEnum.USER_NAME_OR_PASSWORD_ERROR);
    		}
    	}else {
    		throw new ModuleBusinessRuntimeException(ModuleMessageEnum.USER_NAME_OR_PASSWORD_ERROR);
    	}
    	
    	// 交易币
    	String tradeCoinCode = param.getTradeCoinCode();
    	ModuleAssert.notNull(tradeCoinCode, ModuleMessageEnum.TRADE_COIN_CODE_IS_NULL);

    	// 定价币
    	String pricingCoinCode = param.getPricingCoinCode();
    	ModuleAssert.notNull(pricingCoinCode, ModuleMessageEnum.PRICING_COIN_CODE_IS_NULL);
    	
    	// 最小委托数量
    	BigDecimal minAmout = param.getMinEntrustAmout();
    	ModuleAssert.notNull(minAmout, ModuleMessageEnum.MIN_ENTRUST_AMOUT_IS_NULL);
    	
    	// 最大委托数量
    	BigDecimal maxAmout = param.getMaxEntrustAmout();
    	ModuleAssert.notNull(maxAmout, ModuleMessageEnum.MAX_ENTRUST_AMOUT_IS_NULL);
    	
    	// 委托单价
    	BigDecimal entrustPrice = param.getEntrustPrice();
    	ModuleAssert.notNull(entrustPrice, ModuleMessageEnum.ENTRUST_PRICE_IS_NULL);
    	
    	// 下单总量 
    	BigDecimal totalEntrustAmout = param.getTotalEntrustAmout();
		ModuleAssert.notNull(totalEntrustAmout, ModuleMessageEnum.PORTION_RATE_IS_NULL);
		
		// 下单总量 
    	String entrustType = param.getEntrustType();
		ModuleAssert.notNull(entrustType, ModuleMessageEnum.ENTRUST_TYPE_IS_NULL);
		
		// 下单间隔时间
		Long sleepTime = param.getSleepTime();
		
		// 执行下单
    	priorityOrderBusService.restingOrder(user, tradeCoinCode, pricingCoinCode, minAmout, maxAmout, 
    			entrustPrice, totalEntrustAmout, entrustType, sleepTime, result);
    	
    	result.setSuccessful();
    	return result.toJson();
	}
	
}
