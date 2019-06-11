package com.batsoft.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.common.base.BaseController;
import com.batsoft.common.beans.bo.CancelAllBP;
import com.batsoft.common.beans.bo.CancelBP;
import com.batsoft.common.beans.bo.EntrustBP;
import com.batsoft.common.beans.bo.GetKlineDataBP;
import com.batsoft.common.beans.vo.GetBookVO;
import com.batsoft.common.beans.vo.GetKlineDataVO;
import com.batsoft.common.beans.vo.GetTickerVO;
import com.batsoft.common.beans.vo.GetTradeVO;
import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.common.util.ModuleAssert;
import com.batsoft.common.util.result.ResultData;
import com.batsoft.core.annotation.CheckLogin;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.cache.LogicLockSignleton;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.JsonResult.ResultCode;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.exception.ModuleBusinessRuntimeException;
import com.batsoft.model.module.exchange.ExchangeAction;
import com.batsoft.model.module.exchange.dto.CancelAllDTO;
import com.batsoft.model.module.exchange.dto.CancelDTO;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.member.vo.UserVo;
import com.batsoft.mq.RabbitMqSender;
import com.batsoft.service.module.exchange.service.EntrustInfoService;
import com.batsoft.service.module.exchange.service.EntrustIngService;
import com.batsoft.service.module.exchange.service.ExchangeActionService;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;
import com.batsoft.service.module.exchange.trade.service.impl.TradeKlineServiceImpl;
import com.batsoft.service.module.exchange.trade.util.RedisUserUtil;
import com.batsoft.service.module.exchange.trade.util.UUIDUtil;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.shiro.PasswordHelper;
import com.batsoft.utils.StringUtils;
import com.batsoft.utils.gson.GsonSingleton;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 交易大厅
 * 
 * @author simon
 */
@Controller
@SuppressWarnings("all")
@RequestMapping(value = { "/trade" })
public class TradingController extends BaseController {
	
	@Autowired
	private RabbitMqSender rabbitMqSender;
	
	@Autowired
	private ExchangeActionService exchangeActionService;
	
	@Autowired
	private EntrustInfoService entrustInfoService;
	
	@Autowired
	private EntrustIngService entrustIngService;
	
	@Autowired
	private UserService userService;
	
	private LogicLockSignleton logicLock = LogicLockSignleton.getInstance();
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	private GsonSingleton gsonClient = GsonSingleton.getInstance();
	
	/**
	 * Get K线图
	 * 
	 * @param param
	 */
	@ApiOperation(value = "Get K线图")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "交易对" ,name = "symbol", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "时间类型" ,name = "time", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "开始时间" ,name = "from", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "截至时间" ,name = "to", required = true, dataType = "String", paramType = "query")
	})
	@ResponseBody
    @RequestMapping(value = { "/getKlineData" }, method = { RequestMethod.GET })
    public String getKlineData(GetKlineDataBP param, ResultData<List<GetKlineDataVO>> result){
		ModuleAssert.notNull(param, ModuleMessageEnum.PARAM_IS_NULL);
		// 交易对
    	String symbol = param.getSymbol();
    	ModuleAssert.notNull(symbol, ModuleMessageEnum.COIN_PAIR_IS_NULL);
    	// 时间类型
    	String time = param.getTime();
    	ModuleAssert.notNull(param, ModuleMessageEnum.KLINE_DATA_SUBJECT_IS_NULL);
    	// 开始时间
    	Long from = param.getFrom();
    	ModuleAssert.notNull(param, ModuleMessageEnum.KLINE_DATA_FROM_IS_NULL);
    	// 结束时间
    	Long to = param.getTo();
    	ModuleAssert.notNull(param, ModuleMessageEnum.KLINE_DATA_TO_IS_NULL);
    	
    	tradingBusService.getKlineData(symbol, time, from, to, result);
    	return result.toJson();
    }
	
    /**
	 * Get 委托单
	 *
	 * @param symbol
     * @return 
	 */
	@ApiOperation(value = "Get 委托单")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "交易对" ,name = "symbol", required = true, dataType = "String", paramType = "query"),
	})
	@ResponseBody
    @RequestMapping(value = { "/getBook" }, method = { RequestMethod.GET })
	public String getBook(String symbol, ResultData<GetBookVO> result) {
    	ModuleAssert.notNull(symbol, ModuleMessageEnum.SYMBOL_IS_NULL);
    	tradingBusService.getBook(symbol, result); 
    	return result.toJson();
	}
	
	/**
	 * Get 成交单
	 *
	 * @param symbol
	 * @return 
	 */
	@ApiOperation(value = "Get 成交单")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "交易对" ,name = "symbol", required = true, dataType = "String", paramType = "query"),
	})
	@ResponseBody
    @RequestMapping(value = { "/getTrade" }, method = { RequestMethod.GET })
	public String getTrade(String symbol, ResultData<List<GetTradeVO>> result) {
    	ModuleAssert.notNull(symbol, ModuleMessageEnum.SYMBOL_IS_NULL);
    	tradingBusService.getTrade(symbol, result);
    	return result.toJson();
	}

	/**
	 * Get Ticker
	 * @return 
	 */
	@ApiOperation(value = "Get Ticker")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "交易对" ,name = "symbol", required = true, dataType = "String", paramType = "query"),
	})
	@ResponseBody
    @RequestMapping(value = { "/getTicker" }, method = { RequestMethod.GET })
	public String getTicker(String symbol, ResultData<GetTickerVO> result) {
    	ModuleAssert.notNull(symbol, ModuleMessageEnum.SYMBOL_IS_NULL);
    	tradingBusService.getTicker(symbol, result);
    	return result.toJson();
	}
	
	/**
	 * 撤单接口
	 *
	 * @param orderId
	 * @return
	 */
	@ApiOperation(value = "撤单接口",notes = "根据订单号和币种撤销挂订单")
	@CheckLogin
	@ResponseBody
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public JsonResult cancel(@RequestBody CancelBP param) {
		// 订单号
		String orderId = param.getOrderId();
		ModuleAssert.notNull(orderId, ModuleMessageEnum.ORDER_ID_IS_NULL);
		
		// 交易对
		String symbol = param.getSymbol();
		ModuleAssert.notNull(symbol, ModuleMessageEnum.SYMBOL_IS_NULL);
		
		JsonResult jsonResult = new JsonResult();
		User user = UserUtils.getUser();
		if(user != null && StringUtils.isNotBlank(user.getId())) {
			CancelDTO cancelDTO = new CancelDTO();
			cancelDTO.setOrderId(orderId);
			cancelDTO.setUserId(user.getId());
			cancelDTO.setCoinPair(symbol);
			rabbitMqSender.cancelEntrust(gsonClient.toJson(cancelDTO));
			
			jsonResult.setCode(Constants.SUCCESS);
			jsonResult.setSuccess(true);
			jsonResult.setMsg(Language.L_Success("msg_cancel"));
		}else {
			jsonResult.setSuccess(false);
			jsonResult.setCode(Constants.FAILED);
			jsonResult.setMsg(Language.L_Failed("msg_cancel"));
		}
		return jsonResult;
	}

	/**
	 * 全部撤单接口
	 *
	 * @return
	 */
	@ApiOperation(value = "全部撤单接口", notes = "根据币种撤销挂订单")
	@CheckLogin
	@ResponseBody
	@RequestMapping(value = "/cancelAll", method = RequestMethod.POST)
	public JsonResult cancelAll(@RequestBody CancelAllBP param) {
		// 交易对
		String symbol = param.getSymbol();
		ModuleAssert.notNull(symbol, ModuleMessageEnum.SYMBOL_IS_NULL);
		
		JsonResult jsonResult = new JsonResult();
		User user = UserUtils.getUser();
		if(user != null && StringUtils.isNoneBlank(user.getId())) {
			CancelAllDTO cancelAllDTO = new CancelAllDTO();
			cancelAllDTO.setUserId(user.getId());
			cancelAllDTO.setCoinPair(symbol);
			rabbitMqSender.cancelAllEntrust(gsonClient.toJson(cancelAllDTO));
			
			jsonResult.setSuccess(true);
			jsonResult.setCode(Constants.SUCCESS);
			jsonResult.setMsg(Language.L_Success("msg_cancel"));
		}else {
			jsonResult.setSuccess(false);
			jsonResult.setCode(Constants.FAILED);
			jsonResult.setMsg(Language.L_Failed("msg_cancel"));
		}
		return jsonResult;
	}
	
	/**
	 * 委托下单 --Y.zc 修改
	 *
	 * @param price       委托价格
	 * @param amout       委托数量
	 * @param type        委托类型 买 /卖
	 * @param tradeCode   交易币code
	 * @param pricingCode 定价币code
	 * @param category    订单类型
	 * @param tradePwd    交易密码
	 *
	 * @return
	 */
	@ApiOperation(value = "委托下单")
	@CheckLogin
	@ResponseBody
	@RequestMapping(value = "/entrust", method = RequestMethod.POST)
	public JsonResult add(HttpServletResponse response, @RequestBody EntrustBP param) {
		// 委托价格
		String price = param.getPrice();
		ModuleAssert.notNull(price, ModuleMessageEnum.ENTRUST_PRICE_IS_NULL);
		
		// 委托数量
		String amout = param.getAmout();
		ModuleAssert.notNull(amout, ModuleMessageEnum.ENTRUST_AMOUT_IS_NULL);
		
		// 委托类型
		String type = param.getType();
		ModuleAssert.notNull(type, ModuleMessageEnum.ENTRUST_TYPE_IS_NULL);
		
		// 交易币
		String tradeCode = param.getTradeCode();
		ModuleAssert.notNull(tradeCode, ModuleMessageEnum.ENTRUST_TRADE_CODE_IS_NULL);
		
		// 定价币
		String pricingCode = param.getPricingCode();
		ModuleAssert.notNull(pricingCode, ModuleMessageEnum.ENTRUST_PRICING_CODE_IS_NULL);
		
		// 委托单类型
		String category = param.getCategory();
		ModuleAssert.notNull(category, ModuleMessageEnum.ENTRUST_CATEGORY_IS_NULL);
		
		// 交易密码
		String tradePwd = param.getTradePwd();
		
		JsonResult jsonResult = new JsonResult();
		Date now = new Date();
		UserVo user = UserUtils.getUser(false);
		List<ExchangeAction> exchangeActionList = exchangeActionService.getAction(tradeCode,pricingCode,now);
		if(!exchangeActionList.isEmpty()){
			ExchangeAction exchangeAction = exchangeActionList.get(0);
			if(exchangeAction != null ) {
				List<ExchangeAction> lastActions = exchangeActionService.getLastAction(tradeCode,pricingCode,now);
					//如果上次活动的为空，说明是首次
					if(lastActions.isEmpty()){
						if (exchangeAction.getStartTime().compareTo(now) >= 0) {
							jsonResult.setSuccess(false);
							jsonResult.setMsg(Language.L_Failed("msg_trade_action_no_start"));
							return jsonResult;
						}
					}
					//如果当前时间大于下次的活动时间，那么以上一次的活动为准，否则是活动中
					if(exchangeAction.getStartTime().compareTo(now)>=0){
						exchangeAction = lastActions.get(0);
					}

					if (TradeEntrust.ENTRUST_TYPE_BUY.equals(type)) {
						if (new BigDecimal(price).compareTo(exchangeAction.getBuyLimitPrice()) != 0) {
							jsonResult.setSuccess(false);
							jsonResult.setMsg(Language.L_Failed("msg_trade_action_limit_buy_price") + exchangeAction.getBuyLimitPrice()+pricingCode);
							return jsonResult;
						}
						if (new BigDecimal(amout).compareTo(new BigDecimal(exchangeAction.getBuyLimitAmout())) == 1) {
							jsonResult.setSuccess(false);
							jsonResult.setMsg(Language.L_Failed("msg_trade_action_limit_buy_amout") + exchangeAction.getBuyLimitAmout()+tradeCode);
							return jsonResult;
						}else {
							BigDecimal sumInfo =  entrustInfoService.sumOfBuyAmoutByStartTimeAndId(user.getId(),now,exchangeAction.getStartTime(),tradeCode,pricingCode);
							BigDecimal sumIng=  entrustIngService.sumOfBuyAmoutByStartTimeAndId(user.getId(),exchangeAction.getStartTime(),tradeCode,pricingCode);
							BigDecimal oldSumAmout = sumInfo.add(sumIng);
							if(oldSumAmout.add(new BigDecimal(amout)).compareTo(new BigDecimal(exchangeAction.getBuyLimitAmout())) == 1){
								BigDecimal leadAmout = new BigDecimal(exchangeAction.getBuyLimitAmout()).subtract(oldSumAmout);
								jsonResult.setSuccess(false);
								jsonResult.setMsg(Language.L_Failed("msg_trade_action_limit_sell_amout") + exchangeAction.getBuyLimitAmout()+tradeCode
										+ "," +Language.L("msg_trade_action_limit_buy_amout_have_use")+oldSumAmout.longValue()
										+tradeCode+","+Language.L("msg_trade_action_limit_buy_amout_have_no_use")+leadAmout.longValue()+tradeCode);
								return jsonResult;
							}
						}
					} else {
						if (new BigDecimal(price).compareTo(exchangeAction.getSellLimitPrice()) != 0) {
							jsonResult.setSuccess(false);
							jsonResult.setMsg(Language.L_Failed("msg_trade_action_limit_sell_price") + exchangeAction.getSellLimitPrice()+pricingCode);
							return jsonResult;
						}
						if (new BigDecimal(amout).compareTo(new BigDecimal(exchangeAction.getSellLimitAmout())) == 1) {
							jsonResult.setSuccess(false);
							jsonResult.setMsg(Language.L_Failed("msg_trade_action_limit_sell_amout") + exchangeAction.getSellLimitAmout()+tradeCode);
							return jsonResult;
						}else {
							BigDecimal sumInfo = entrustInfoService.sumOfSellAmoutByStartTimeAndId(user.getId(),now, exchangeAction.getStartTime(), tradeCode, pricingCode);
							BigDecimal sumIng = entrustIngService.sumOfSellAmoutByStartTimeAndId(user.getId(), exchangeAction.getStartTime(), tradeCode, pricingCode);
							BigDecimal oldSumAmout = sumInfo.add(sumIng);
							if (oldSumAmout.add(new BigDecimal(amout)).compareTo(new BigDecimal(exchangeAction.getSellLimitAmout())) == 1) {
								BigDecimal leadAmout = new BigDecimal(exchangeAction.getSellLimitAmout()).subtract(oldSumAmout);
								jsonResult.setSuccess(false);
								jsonResult.setMsg(Language.L_Failed("msg_trade_action_limit_sell_amout") + exchangeAction.getBuyLimitAmout() + tradeCode
										+ "," +Language.L("msg_trade_action_limit_buy_amout_have_use")+oldSumAmout.longValue()
										+tradeCode+","+Language.L("msg_trade_action_limit_buy_amout_have_no_use")+leadAmout.longValue()+tradeCode
								);
								return jsonResult;
							}
						}
					}
				}
		}

		TradeEntrust traEntrustTO = new TradeEntrust();
		// 交易币
		traEntrustTO.setTradeCoinCode(tradeCode);
		// 定价币
		traEntrustTO.setPricingCoinCode(pricingCode);
		// 委托人
		traEntrustTO.setCustomerId(user.getId());
		// 委托类型 1:买或2:卖
		traEntrustTO.setEntrustType(type);
		// 订单类型
		traEntrustTO.setCategory(category);
		// 交易币位数 // 定价币位数
		Integer tradeCodeLength = RedisUserUtil.getAmountDecimal(tradeCode + CHS.underline.getValue() + pricingCode);
		Integer pricingCodeLength = RedisUserUtil.getPriceDecimal(tradeCode + CHS.underline.getValue() + pricingCode);
		// 委托价格
		traEntrustTO.setEntrustPrice(new BigDecimal(price).setScale(pricingCodeLength, BigDecimal.ROUND_DOWN));
		// 委托原始数量
		traEntrustTO.setEntrustAmoutSql(new BigDecimal(amout).setScale(tradeCodeLength, BigDecimal.ROUND_DOWN));
		// 委托匹配的数量
		traEntrustTO.setEntrustAmout(new BigDecimal(amout).setScale(tradeCodeLength, BigDecimal.ROUND_DOWN));
		// 委托单号
		traEntrustTO.setOrderId(UUIDUtil.getUUID());
		traEntrustTO.setEntrustState(TradeEntrust.ENTRUSTSTATE0);
		traEntrustTO.setExecutedPrice(traEntrustTO.getExecutedPrice());
		
		// 校验订单数量价格
		if (RedisUserUtil.validateTradeEntrust(traEntrustTO)) {
			throw new ModuleBusinessRuntimeException(Constants.FAILED.getCode(), Language.L_Failed("msg_invalid_order", false));
		}
		
		//检查账户余额
		if (RedisUserUtil.validateAccount(traEntrustTO)) {
			throw new ModuleBusinessRuntimeException(Constants.FAILED.getCode(), Language.L_Failed("msg_trade_insufficient_accounts", false));
		}
		
		//判断交易密码逻辑
		jsonResult = valideTradePassword(tradePwd, traEntrustTO, user);
		if(jsonResult.getCode().equals(Constants.FAILED.getCode())){
			return jsonResult; 
		}
		
		// 检查该用户该交易对委托订单数量
		String entrustingKey = String.format(RedisKeyConstant.USER_ENTRUSTING, tradeCode, pricingCode, user.getId());
		Long entrustingCount = jedisClient.hlen(JedisDataSourceSignleton.DB1, entrustingKey);
		if(entrustingCount != null && entrustingCount >= 60) {
			throw new ModuleBusinessRuntimeException(Constants.FAILED.getCode(), Language.L_Failed("msg_entrusting_order_toomany", false));
		}
		
		// 下单频率限制
		String lockKey = RedisKeyConstant.ORDER_LOCK_KEY + user.getId();
		if(!logicLock.setLogicLock(lockKey, Long.valueOf(1))){
			throw new ModuleBusinessRuntimeException(Constants.FAILED.getCode(), Language.L_Failed("msg_entrusting_order_too_often", false));
		}
		
		//发送下单的消息
		rabbitMqSender.toAddEntrust(JSON.toJSONString(traEntrustTO), traEntrustTO.getTradeCoinCode(), traEntrustTO.getPricingCoinCode());
		jsonResult.setSuccess(true);
		jsonResult.setCode(ResultCode.SUCCESS);
		jsonResult.setMsg(Language.L_Success("msg_entrust"));
		jsonResult.setData(ModuleMessageEnum.ENTRUST_SUCCESSFUL.getCode());
		return jsonResult;
	}
	/**
	 * 验证交易密码是否正确
	 * @param tradePwd
	 * @param traEntrustTO
	 * @param user
	 * @return
	 */
	public JsonResult valideTradePassword(String tradePwd, TradeEntrust traEntrustTO,User user){
		JsonResult jsonResult = new JsonResult(true);
		if(tradePwd == null){	//没有发送密码
			//缓存里面没有密码 
			String passwd = RedisUserUtil.getTradePassword(traEntrustTO.getCustomerId());
			if(passwd == null){
				String tradePassword = userService.findUserInfo(user.getUserName()).getTradePassword();
				jsonResult.setCode(Constants.FAILED);
				if (StringUtils.isNull(tradePassword)) {
					jsonResult.setData("setTradepwd");
					jsonResult.setMsg(Language.L_Failed("msg_input_user_center_set_trade_password",true));
				}else{
					jsonResult.setData("input_password");
					jsonResult.setMsg(Language.L_Failed("msg_input_trade_password",true));
				}
				return jsonResult;
			}else{ //如果有密码那么判断密码
				String tradePassword = userService.findUserInfo(user.getUserName()).getTradePassword();
				if (StringUtils.isNull(tradePassword)) {
					jsonResult.setCode(Constants.FAILED);
					jsonResult.setData("setTradepwd");
					jsonResult.setMsg(Language.L_Failed("msg_input_user_center_set_trade_password",true));
					return jsonResult;
				}else{
					//加密密码
					PasswordHelper passwordHelper = new PasswordHelper();
					String encryPassword = passwordHelper.encryString(passwd, user.getSalt());
					if(!encryPassword.equals(tradePassword)){ //如果密码不对
						jsonResult.setSuccess(true);
						jsonResult.setCode(Constants.FAILED);
						jsonResult.setData("password");
						jsonResult.setMsg(Language.L_Failed("msg_trade_password_error",true));
						return jsonResult;
					}
				}
			}
		}else{ //如果发送了密码
			String tradePassword = userService.findUserInfo(user.getUserName()).getTradePassword();
			//检查账户是否有支付密码
			if (StringUtils.isNull(tradePassword)) {
				jsonResult.setCode(Constants.FAILED);
				jsonResult.setData("password");
				jsonResult.setMsg(Language.L_Failed("msg_input_user_center_set_trade_password",true));
				return jsonResult;
			}else{
				//加密验证
				PasswordHelper passwordHelper = new PasswordHelper();
				String tradePassWord = passwordHelper.encryString(tradePwd,user.getSalt());
				if(!tradePassWord.equals(tradePassword)){
					jsonResult.setCode(Constants.FAILED);
					jsonResult.setData("password");
					jsonResult.setMsg(Language.L_Failed("msg_trade_password_error",true));
					return jsonResult;
				} else{ //如果密码正确 存放到缓存中
					RedisUserUtil.saveTradePassWord(traEntrustTO.getCustomerId(),tradePwd,1*60*60);
				}
			}
		}
		return jsonResult;
	}
	
	/**
	 * 查询用户当前委托单
	 *
	 * 修改成了WebSocket {@link TradeKlineServiceImpl#pushTradeEntrustIng(String, String, String)}
	 * @return
	 */
	@ApiOperation(value = "查询用户当前委托单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "coinPair" ,value = "币种",paramType = "path" ,dataType = "String")
	})
	@CheckLogin
	@ResponseBody
	@RequestMapping(value = "/findOwnerEntrust/{coinPair}",method = RequestMethod.GET)
	public JsonResult findOwnerEntrust(@PathVariable("coinPair") String coinPair) {
		JsonResult jsonResult = new JsonResult();
		User user = UserUtils.getUser();
		List<JSONObject> ingList = new ArrayList<JSONObject>();
		String entrustingKey = String.format(RedisKeyConstant.USER_ENTRUSTING_S_S, coinPair, user.getId());
		Map<String, String> entrustingMap = jedisClient.hgetall(JedisDataSourceSignleton.DB1, entrustingKey);
		List<TradeEntrust> tradeEntrustList = new ArrayList<TradeEntrust>();
		for(String key : entrustingMap.keySet()) {
			TradeEntrust entrustIng = JSON.parseObject(entrustingMap.get(key), TradeEntrust.class);
			tradeEntrustList.add(entrustIng);
		}
		tradeEntrustList.sort((TradeEntrust pre, TradeEntrust rear) -> Long.valueOf(rear.getEntrustTime()).compareTo(Long.valueOf(pre.getEntrustTime())));
		//查当前委托
		for (TradeEntrust traEntrustTO : tradeEntrustList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("entrustTime", traEntrustTO.getEntrustTime() / 1000);
			jsonObject.put("entrustPrice", traEntrustTO.getEntrustPrice());
			jsonObject.put("entrustAmout", traEntrustTO.getEntrustAmout()); //数量
			jsonObject.put("entrustAmoutSql", traEntrustTO.getEntrustAmoutSql());
			jsonObject.put("tradeCoinCode", traEntrustTO.getTradeCoinCode());
			jsonObject.put("pricingCoinCode", traEntrustTO.getPricingCoinCode());
			jsonObject.put("orderId", traEntrustTO.getOrderId());
			jsonObject.put("entrustType", traEntrustTO.getEntrustType());
			jsonObject.put("entrustState", traEntrustTO.getEntrustState());
			jsonObject.put("volume", traEntrustTO.getEntrustAmoutSql().subtract(traEntrustTO.getEntrustAmout())); //成交量
			jsonObject.put("transactionAmount", (traEntrustTO.getEntrustAmoutSql().subtract(traEntrustTO.getEntrustAmout())).multiply(traEntrustTO.getEntrustPrice()));//交易额
			ingList.add(jsonObject);
		}
		jsonResult.setSuccess(true);
		jsonResult.setData(ingList);
		return jsonResult;
	}
	
}
