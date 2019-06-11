/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.exchange;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.cache.LogicLockSignleton;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.core.web.controller.GenericController;
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
import com.batsoft.service.module.exchange.trade.service.TradeKlineService;
import com.batsoft.service.module.exchange.trade.service.impl.TradeKlineServiceImpl;
import com.batsoft.service.module.exchange.trade.util.RedisUserUtil;
import com.batsoft.service.module.exchange.trade.util.UUIDUtil;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.shiro.PasswordHelper;
import com.batsoft.utils.StringUtils;
import com.batsoft.utils.gson.GsonSingleton;
import com.google.gson.JsonObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>Exchange项目管理</p>
 * 委托交易
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Api(description = "委托交易")
@EnableAsync
@Controller("tradeController")
@RequestMapping("/exchange")
public class TradeController extends GenericController {
	
	private LogicLockSignleton logicLock = LogicLockSignleton.getInstance();
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();

	@Autowired
	private EntrustInfoService entrustInfoService;
	
	@Autowired
	private RabbitMqSender rabbitMqSender;

	@Autowired
	private UserService userService;
	
	@Autowired
	private TradeKlineService tradeKlineService;

	@Autowired
	private ExchangeActionService exchangeActionService;

	@Autowired
	private EntrustIngService entrustIngService;
	
	private GsonSingleton gsonClient = GsonSingleton.getInstance();


	// 以下代码2018年4月14日 17:17:26 增加

	@ApiOperation(value = "币的买卖价格")
	@ApiImplicitParams({@ApiImplicitParam(value = "买卖类型", name = "type", required = true, paramType = "path", dataType = "String"),
			@ApiImplicitParam(value = "交易对", name = "coinPair", required = true, paramType = "path", dataType = "String")
	})
	@RequestMapping(value = "/buy_sell_first/{type}/{coinPair}",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult init_buy_sell_first(@PathVariable String type, @PathVariable String coinPair) {
		JsonResult result = new JsonResult();
		BigDecimal price = tradeKlineService.findBuySellFirst(type, coinPair);
		if(result == null){
			result.setSuccess(false);
			result.setData("page404");
		}else{
			result.setSuccess(true);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("price",price);
			Map<String, String> coinPairs = jedisClient.hgetall(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COINPAIR);
			if(coinPairs!=null&&coinPair.contains(coinPair)){
				String str = coinPairs.get(coinPair);
				JSONObject json = JSONObject.parseObject(str);
				if(json!=null && json.containsKey("price_decimal")){
					jsonObject.addProperty("price_decimal",json.getInteger("price_decimal"));
				}
			}
			result.setData(jsonObject.toString());
		}
		return  result;
	}

	/**
	 * 撤单接口
	 *
	 * @param orderId
	 * @return
	 */
	@ApiOperation(value = "撤单接口",notes = "根据订单号和币种撤销挂订单")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "订单Id" ,name = "orderId",required = true,dataType = "String",paramType = "query"),
			@ApiImplicitParam(value = "交易对" ,name = "coinPair",required = true,dataType = "String",paramType = "query")
	})
	@ResponseBody
	@RequestMapping(value = "/trade/cancel", method = RequestMethod.POST)
	public JsonResult cancel(@RequestParam(value = "orderId") String orderId, @RequestParam(value = "coinPair") String coinPair) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(false);
		User user = UserUtils.getUser();
		try {
			CancelDTO cancelDTO = new CancelDTO();
			cancelDTO.setOrderId(orderId);
			cancelDTO.setUserId(user.getId());
			cancelDTO.setCoinPair(coinPair);
			rabbitMqSender.cancelEntrust(gsonClient.toJson(cancelDTO));

			jsonResult.setCode(Constants.SUCCESS);
			jsonResult.setSuccess(true);
			jsonResult.setMsg(Language.L_Success("msg_cancel"));
			return jsonResult;
		} catch (Exception e) {
			e.printStackTrace();
		}

		jsonResult.setCode(Constants.FAILED);
		jsonResult.setMsg(Language.L_Failed("msg_cancel"));
		return jsonResult;
	}

	/**
	 * 全部撤单接口
	 *
	 * @return
	 */
	@ApiOperation(value = "全部撤单接口",notes = "根据币种撤销挂订单")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "交易对" ,name = "coinPair",required = true,dataType = "String",paramType = "query"),
	})
	@RequestMapping(value = "/trade/cancelAll",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult<String> cancelAll(@RequestParam(value = "coinPair") String coinPair) {
		JsonResult<String> jsonResult = new JsonResult<String>();
		
		User user = UserUtils.getUser();
		CancelAllDTO cancelAllDTO = new CancelAllDTO();
		cancelAllDTO.setUserId(user.getId());
		cancelAllDTO.setCoinPair(coinPair);
		rabbitMqSender.cancelAllEntrust(gsonClient.toJson(cancelAllDTO));
		
		jsonResult.setSuccess(true);
		jsonResult.setCode(Constants.SUCCESS);
		jsonResult.setMsg(Language.L_Success("msg_cancel"));
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
	 * @param category    订单类型 参考：  EntrustIg.ENTRUSTSTATE0 1 2 3
	 * @param tradePwd    交易密码
	 *
	 * @return
	 */
	@ApiOperation(value = "委托下单")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "委托价格" ,name = "price",required = true,dataType = "String",paramType = "query"),
			@ApiImplicitParam(value = "委托数量" ,name = "amout",required = true,dataType = "String",paramType = "query"),
			@ApiImplicitParam(value = "委托类型 买 /卖" ,name = "type",required = true,dataType = "String",paramType = "query"),
			@ApiImplicitParam(value = "交易币code" ,name = "tradeCode",required = true,dataType = "String",paramType = "query"),
			@ApiImplicitParam(value = "定价币code" ,name = "pricingCode",required = true,dataType = "String",paramType = "query"),
			@ApiImplicitParam(value = "订单类型" ,name = "category",required = true,dataType = "String",paramType = "query"),
			@ApiImplicitParam(value = "交易密码" ,name = "tradePwd",required = false,dataType = "String",paramType = "query"),
	})
	@RequestMapping(value = "/trade/entrust", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult add(@RequestParam(value = "price") String price,
	                      @RequestParam(value = "amout") String amout,
	                      @RequestParam(value = "type") String type,
	                      @RequestParam(value = "tradeCode") String tradeCode,
	                      @RequestParam(value = "pricingCode") String pricingCode,
	                      @RequestParam(value = "category") String category,
	                      @RequestParam(value = "tradePwd", required = false) String tradePwd ) {
		JsonResult jsonResult = new JsonResult(true);
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
							jsonResult.setMsg(Language.L_Failed("msg_trade_action_limit_buy_price") + exchangeAction.getBuyLimitPrice()+pricingCode);
							return jsonResult;
						}
						if (new BigDecimal(amout).compareTo(new BigDecimal(exchangeAction.getBuyLimitAmout())) == 1) {
							jsonResult.setMsg(Language.L_Failed("msg_trade_action_limit_buy_amout") + exchangeAction.getBuyLimitAmout()+tradeCode);
							return jsonResult;
						}else {
							BigDecimal sumInfo =  entrustInfoService.sumOfBuyAmoutByStartTimeAndId(user.getId(),now,exchangeAction.getStartTime(),tradeCode,pricingCode);
							BigDecimal sumIng=  entrustIngService.sumOfBuyAmoutByStartTimeAndId(user.getId(),exchangeAction.getStartTime(),tradeCode,pricingCode);
							BigDecimal oldSumAmout = sumInfo.add(sumIng);
							if(oldSumAmout.add(new BigDecimal(amout)).compareTo(new BigDecimal(exchangeAction.getBuyLimitAmout())) == 1){
								BigDecimal leadAmout = new BigDecimal(exchangeAction.getBuyLimitAmout()).subtract(oldSumAmout);
								jsonResult.setMsg(Language.L_Failed("msg_trade_action_limit_sell_amout") + exchangeAction.getBuyLimitAmout()+tradeCode
										+ "," +Language.L("msg_trade_action_limit_buy_amout_have_use")+oldSumAmout.longValue()
										+tradeCode+","+Language.L("msg_trade_action_limit_buy_amout_have_no_use")+leadAmout.longValue()+tradeCode);
								return jsonResult;
							}
						}
					} else {
						if (new BigDecimal(price).compareTo(exchangeAction.getSellLimitPrice()) != 0) {
							jsonResult.setMsg(Language.L_Failed("msg_trade_action_limit_sell_price") + exchangeAction.getSellLimitPrice()+pricingCode);
							return jsonResult;
						}
						if (new BigDecimal(amout).compareTo(new BigDecimal(exchangeAction.getSellLimitAmout())) == 1) {
							jsonResult.setMsg(Language.L_Failed("msg_trade_action_limit_sell_amout") + exchangeAction.getSellLimitAmout()+tradeCode);
							return jsonResult;
						}else {
							BigDecimal sumInfo = entrustInfoService.sumOfSellAmoutByStartTimeAndId(user.getId(),now, exchangeAction.getStartTime(), tradeCode, pricingCode);
							BigDecimal sumIng = entrustIngService.sumOfSellAmoutByStartTimeAndId(user.getId(), exchangeAction.getStartTime(), tradeCode, pricingCode);
							BigDecimal oldSumAmout = sumInfo.add(sumIng);
							if (oldSumAmout.add(new BigDecimal(amout)).compareTo(new BigDecimal(exchangeAction.getSellLimitAmout())) == 1) {
								BigDecimal leadAmout = new BigDecimal(exchangeAction.getSellLimitAmout()).subtract(oldSumAmout);
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

		try {
			TradeEntrust traEntrustTO = new TradeEntrust();
			//交易币
			traEntrustTO.setTradeCoinCode(tradeCode);
			//定价币
			traEntrustTO.setPricingCoinCode(pricingCode);
			//委托人
			traEntrustTO.setCustomerId(user.getId());
			//委托类型 1:买或2:卖
			traEntrustTO.setEntrustType(type);
			traEntrustTO.setCategory(category);

			//交易币位数
			Integer tradeCodeLength = RedisUserUtil.getAmountDecimal(tradeCode + "_" + pricingCode);
			//定价币位数
			Integer pricingCodeLength = RedisUserUtil.getPriceDecimal(tradeCode + "_" + pricingCode);

			//委托价格
			traEntrustTO.setEntrustPrice(new BigDecimal(price).setScale(pricingCodeLength, BigDecimal.ROUND_DOWN));
			
			//委托原始数量
			traEntrustTO.setEntrustAmoutSql(new BigDecimal(amout).setScale(tradeCodeLength, BigDecimal.ROUND_DOWN));
			
			//委托匹配的数量
			traEntrustTO.setEntrustAmout(new BigDecimal(amout).setScale(tradeCodeLength, BigDecimal.ROUND_DOWN));
			 
			//委托单号
			traEntrustTO.setOrderId(UUIDUtil.getUUID());
			traEntrustTO.setEntrustState(TradeEntrust.ENTRUSTSTATE0);
			traEntrustTO.setExecutedPrice(traEntrustTO.getExecutedPrice());
			
			if (RedisUserUtil.validateTradeEntrust(traEntrustTO)) {
				jsonResult.setSuccess(false);
				jsonResult.setCode(Constants.FAILED);
				jsonResult.setMsg(Language.L_Failed("msg_invalid_order",false));
				return jsonResult;
			}
			//检查账户余额
			if (RedisUserUtil.validateAccount(traEntrustTO)) {
				jsonResult.setSuccess(false);
				jsonResult.setCode(Constants.FAILED);
				jsonResult.setMsg(Language.L_Failed("msg_trade_insufficient_accounts",false));
				return jsonResult;
			}
			
			//判断交易密码逻辑
			jsonResult = valideTradePassword(tradePwd, traEntrustTO, user);
			if(jsonResult.getCode().equals(Constants.FAILED.getCode())){
				return jsonResult; 
			}
			
			// 检查该用户委托订单是否太多了
			String entrustingKey = String.format(RedisKeyConstant.USER_ENTRUSTING, tradeCode, pricingCode, user.getId());
			Long entrustingCount = jedisClient.hlen(JedisDataSourceSignleton.DB1, entrustingKey);
			if(entrustingCount != null && entrustingCount > 60) {
				jsonResult.setSuccess(false);
				jsonResult.setCode(Constants.FAILED);
				jsonResult.setMsg(Language.L_Failed("msg_entrusting_order_toomany",false));
				return jsonResult;
			}
			
			// 频率限制
			String lockKey = RedisKeyConstant.ORDER_LOCK_KEY + user.getId();
			if(!logicLock.setLogicLock(lockKey, Long.valueOf(1))){
				jsonResult.setSuccess(false);
				jsonResult.setCode(Constants.FAILED);
				jsonResult.setMsg(Language.L_Failed("msg_entrusting_order_too_often",false));
				return jsonResult;
			}
			
			//发送下单的消息
			rabbitMqSender.toAddEntrust(JSON.toJSONString(traEntrustTO), traEntrustTO.getTradeCoinCode(), traEntrustTO.getPricingCoinCode());
			
			jsonResult.setMsg(Language.L_Success("msg_entrust"));
			jsonResult.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setMsg(Language.L_Failed("msg_entrust"));
			jsonResult.setSuccess(false);
		}
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
				jsonResult.setSuccess(false);
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
					jsonResult.setSuccess(false);
					jsonResult.setCode(Constants.FAILED);
					jsonResult.setData("setTradepwd");
					jsonResult.setMsg(Language.L_Failed("msg_input_user_center_set_trade_password",true));
					return jsonResult;
				}else{
					//加密密码
					PasswordHelper passwordHelper = new PasswordHelper();
					String encryPassword = passwordHelper.encryString(passwd, user.getSalt());
					if(!encryPassword.equals(tradePassword)){ //如果密码不对
						jsonResult.setSuccess(false);
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
				jsonResult.setSuccess(false);
				jsonResult.setCode(Constants.FAILED);
				jsonResult.setData("password");
				jsonResult.setMsg(Language.L_Failed("msg_input_user_center_set_trade_password",true));
				return jsonResult;
			}else{
				//加密验证
				PasswordHelper passwordHelper = new PasswordHelper();
				String tradePassWord = passwordHelper.encryString(tradePwd,user.getSalt());
				if(!tradePassWord.equals(tradePassword)){
					jsonResult.setSuccess(false);
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
	@Deprecated
	@ApiOperation(value = "查询用户当前委托单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "coinPair" ,value = "币种",paramType = "path" ,dataType = "String")
	})
	@ResponseBody
	@RequestMapping(value = "/trade/findOwnerEntrust/{coinPair}",method = RequestMethod.GET)
	public JsonResult findOwnerEntrust(@PathVariable("coinPair") String coinPair) {
		JsonResult jsonResult = new JsonResult();
		User user = UserUtils.getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		List<JSONObject> ingList = new ArrayList<JSONObject>();
		String entrustingKey = String.format(RedisKeyConstant.USER_ENTRUSTING_S_S, coinPair, user.getId());
		Map<String, String> entrustingMap = jedisClient.hgetall(JedisDataSourceSignleton.DB1, entrustingKey);
		List<TradeEntrust> tradeEntrustList = new ArrayList<TradeEntrust>();
		for(String key : entrustingMap.keySet()) {
			TradeEntrust entrustIng = JSON.parseObject(entrustingMap.get(key), TradeEntrust.class);
			tradeEntrustList.add(entrustIng);
		}
		
		// 对委托中的订单进行排序
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
			jsonObject.put("category", traEntrustTO.getCategory());
			BigDecimal bigDecimal = (traEntrustTO.getEntrustAmoutSql().subtract(traEntrustTO.getEntrustAmout())).divide((traEntrustTO.getEntrustAmoutSql()), 2, RoundingMode.HALF_UP);
			NumberFormat percent = NumberFormat.getPercentInstance();
			percent.setMaximumFractionDigits(2);
			
			jsonObject.put("ratio", percent.format(bigDecimal.doubleValue()));
			jsonObject.put("entrustState", traEntrustTO.getEntrustState());
			jsonObject.put("volume", traEntrustTO.getEntrustAmoutSql().subtract(traEntrustTO.getEntrustAmout())); //成交量
			jsonObject.put("transactionAmount", (traEntrustTO.getEntrustAmoutSql().subtract(traEntrustTO.getEntrustAmout())).multiply(traEntrustTO.getEntrustPrice()));//交易额
			ingList.add(jsonObject);
		}
		map.put("ing", ingList);
		jsonResult.setSuccess(true);
		jsonResult.setData(map);
		return jsonResult;
	}
	
	/**
	 * 查询用户历史委托单
	 *
	 * @return
	 */
	@ApiOperation(value = "查询用户历史委托单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "coinPair" ,value = "币种",paramType = "path" ,dataType = "String"),
			@ApiImplicitParam(name = "start_time" ,value = "开始时间",paramType = "path" ,dataType = "Long"),
			@ApiImplicitParam(name = "end_time" ,value = "结束时间",paramType = "path" ,dataType = "Long")
	})
	@ResponseBody
	@RequestMapping(value = "/trade/findHistoryEntrust/{coinPair}",method = RequestMethod.GET)
	public JsonResult findHistoryEntrust(@PathVariable("coinPair") String coinPair, @RequestParam(value = "start_time") long startTime, @RequestParam(value = "end_time") long endTime) {
		JsonResult jsonResult = new JsonResult();
		User user = UserUtils.getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		List<JSONObject> historyList = new ArrayList<JSONObject>();
		//查历史委托
		String historyKey = String.format(RedisKeyConstant.USER_ENTRUSTHISTORY, coinPair, user.getId());
		List<String> entrusthistoryList = jedisClient.lrange(JedisDataSourceSignleton.DB1, historyKey,  0, -1);
		if (entrusthistoryList != null && !entrusthistoryList.isEmpty()) {
			for (String record : entrusthistoryList) {
				TradeEntrust traEntrustTO = JSON.parseObject(record, TradeEntrust.class);
				if ((traEntrustTO.getEntrustTime() / 1000 > startTime && traEntrustTO.getEntrustTime() / 1000 < endTime)) { //如果在时间区间内
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("entrustTime", traEntrustTO.getEntrustTime() / 1000);
					jsonObject.put("entrustPrice", traEntrustTO.getEntrustPrice());
					jsonObject.put("volume", traEntrustTO.getEntrustAmoutSql()); //成交量     器其逻辑好像有点问题  这个字段应该存总量  但是这里存的当前交易量   只能将错就错
					jsonObject.put("transactionAmount", traEntrustTO.getEntrustAmoutSql().multiply(traEntrustTO.getEntrustPrice()));//交易额
					jsonObject.put("tradeCoinCode", traEntrustTO.getTradeCoinCode());
					jsonObject.put("pricingCoinCode", traEntrustTO.getPricingCoinCode());
					jsonObject.put("orderId", traEntrustTO.getOrderId());
					jsonObject.put("entrustType", traEntrustTO.getEntrustType());
					jsonObject.put("category", traEntrustTO.getCategory());
					jsonObject.put("executedPrice", entrustInfoService.junjia(traEntrustTO.getTradeCoinCode(), traEntrustTO.getPricingCoinCode()));
					historyList.add(jsonObject);
				}
			}
		}
		map.put("history", historyList);
		jsonResult.setSuccess(true);
		jsonResult.setData(map);
		return jsonResult;
	}

	
	/**
	 * 初始化委托单
	 *
	 * @param coinPair
	 */
	@Async
	@MessageMapping("/init_book")
	public void init_book(String coinPair) {
		tradeKlineService.wsInitBook(coinPair);
	}
	
	/**
	 * 初始化成交单
	 *
	 * @param coinPair
	 */
	@Async
	@MessageMapping("/init_trade")
	public void init_trade(String coinPair) {
		tradeKlineService.wsInitTrade(coinPair);
	}

	/**
	 * 初始化ticker
	 */
	@Async
	@MessageMapping("/init_ticker")
	public void init_ticker() {
		tradeKlineService.wsInitTicker();
	}

}
