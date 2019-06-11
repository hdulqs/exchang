package com.batsoft.controller;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.common.base.BaseController;
import com.batsoft.common.beans.bo.CancelCoinCollectionBP;
import com.batsoft.common.beans.bo.CoinsAreaBP;
import com.batsoft.common.beans.bo.SaveCoinsCollectionBP;
import com.batsoft.common.beans.bo.SymbolDecimalBP;
import com.batsoft.common.beans.vo.CoinsAreaVO;
import com.batsoft.common.beans.vo.CollectStateVO;
import com.batsoft.common.beans.vo.SymbolDecimalVO;
import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.common.util.ModuleAssert;
import com.batsoft.common.util.result.ResultData;
import com.batsoft.core.annotation.CheckLogin;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.cache.motion.KlineSymbol24dataUtil;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.JsonResult.ResultCode;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.core.common.enums.CoinEnum;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.core.common.utils.CoinPairConfigUtil;
import com.batsoft.core.common.validator.MobileCodeToken;
import com.batsoft.core.model.SaveResult;
import com.batsoft.model.BaseModel;
import com.batsoft.model.module.exchange.CoinPairUserCollection;
import com.batsoft.model.module.exchange.ExchangeAction;
import com.batsoft.model.module.exchange.WithdrawAddress;
import com.batsoft.model.module.exchange.vo.CustomerAccountVo;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.member.vo.UserVo;
import com.batsoft.service.module.blockchain.service.CoinAccountService;
import com.batsoft.service.module.exchange.service.CoinPairService;
import com.batsoft.service.module.exchange.service.CoinPairUserCollectionService;
import com.batsoft.service.module.exchange.service.CoinService;
import com.batsoft.service.module.exchange.service.CoinWithdrawService;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.exchange.service.ExchangeActionService;
import com.batsoft.service.module.exchange.service.WithdrawAddressService;
import com.batsoft.service.module.exchange.trade.util.RedisUserUtil;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.shiro.PasswordHelper;
import com.batsoft.utils.StringUtils;
import com.batsoft.utils.date.BaseDate;
import com.batsoft.utils.gson.GsonSingleton;
import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.Jedis;

@RestController
@Api(value = "/coin",description = "coinController 币相关的接口")
@RequestMapping(value = "/coin")
public class CoinController extends BaseController {

    @Autowired
    private CustomerAccountService customerAccountService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private WithdrawAddressService withdrawAddressService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinWithdrawService coinWithdrawService;

    @Autowired
    private CoinPairUserCollectionService coinPairUserCollectionService;

    @Autowired
    private CoinPairService coinPairService;

    private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
    
    private GsonSingleton gsonClient = GsonSingleton.getInstance();
    
    private CoinPairConfigUtil decimalUtil = new CoinPairConfigUtil();

    @Autowired
    private ExchangeActionService exchangeActionService;

    @Autowired
    private CoinAccountService coinAccountService;
    /**
     * 获取具体币种充值数据
     *
     * @return
     */
    @ApiOperation(value = "充值初始化接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "交易对", name = "coinCode", paramType = "path", dataType = "String")
    })
    @RequestMapping(value = "/findCoinAccount/{coinCode}",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult findCoinAccount(@PathVariable(name = "coinCode") String coinCode) {
        JsonResult jsonResult = new JsonResult(true);
        UserVo userVo = UserUtils.getUser(false);
        if(userVo == null || userVo.getId() == null){
            jsonResult.setSuccess(false);
            jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
            jsonResult.setMsg(Language.L_Failed("msg_no_login"));
        }
		CustomerAccountVo customerAccountVo = customerAccountService.findCoinAccount(userVo.getId(), coinCode);
        jsonResult.setData(customerAccountVo);
        return jsonResult;
    }

	@ApiOperation(value = "充值提币地址")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "交易对", name = "coinCode", paramType = "path", dataType = "String")
	})
	@RequestMapping(value = "/findAddress/{coinCode}",method = RequestMethod.GET)
	@CheckLogin
	@ResponseBody
	public JsonResult findAddress(@PathVariable String coinCode) {
		JsonResult result = new JsonResult(true);
		String address = "";
		//查询用户是否有地址
		//从exchange_coin_account 查询coin_address 地址
		JSONObject ret = customerAccountService.findCoinAddress(UserUtils.getUser().getId(), coinCode);

		// 非 ERC20 代币
		if (!(Boolean) ret.get("ERC") || ret.get("address") == null) {
			address = coinAccountService.findAddress(coinCode);//从blockchain_coin_account 更加coinCode差处理
			if (address != null) {
				//保存地址
				customerAccountService.updateAccountAddress(UserUtils.getUser().getId(), coinCode, address);
			}
		}

		// ERC 20 代币
		if ((Boolean) ret.get("ERC") && ret.get("address") == null) {
			// 直接获取 eth 地址
			address = coinAccountService.findAddress("ETH");
			if (address != null) {
				// 保存代币地址
				customerAccountService.updateAccountAddress(UserUtils.getUser().getId(), coinCode, address);

				if (!coinCode.equals("ETH")) {
					// 同时保存以太地址
					customerAccountService.updateAccountAddress(UserUtils.getUser().getId(), "ETH", address);
				}
			}
		}

		if ((Boolean) ret.get("ERC") && ret.get("address") != null) {
			address = ret.get("address").toString();
		}
		result.setData(address);
		return result;
	}

	@ApiOperation(value = "搜索币种")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "交易币",name = "coinCode",required = false,dataType = "String",paramType = "path"),
			@ApiImplicitParam( name ="page",paramType = "query",dataType = "int",required = false,value = "页码 1开始"),
			@ApiImplicitParam( name ="pageSize",paramType = "query",dataType = "int",required = false,value = "页大小 默认10")
	})
	@RequestMapping(value = "/searchCoin",method = RequestMethod.GET)
	public JsonResult searchCoin(@RequestParam(value = "coinCode",required = false) String coinCode,
	 @RequestParam(name = "page",required = false,defaultValue = "1") int page,
	@RequestParam(name = "pageSize",required = false,defaultValue = "10") int pageSize
	){
		JsonResult jsonResult = new JsonResult(true);
		PageResult pageResult = coinService.selectCoin(coinCode,page,pageSize);
		jsonResult.setData(pageResult);
		return jsonResult;
	}


    @ApiOperation(value = "体现初始化接口")
    @RequestMapping(value = "/withDrawCoinInit/{coinCode}",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "交易币",name = "coinCode",required = true,dataType = "String",paramType = "path")
    })
    public JsonResult withDrawCoinInit(@PathVariable(value = "coinCode") String coinCode){
        JsonResult jsonResult = new JsonResult(true);
        UserVo userVo = UserUtils.getUser(false);
        if(userVo == null || userVo.getId() == null){
            jsonResult.setSuccess(false);
            jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
            jsonResult.setMsg(Language.L_Failed("msg_no_login"));
        }
        Map<String, Object> data = new HashMap<>();
        data.put("coin", coinService.findWithdrawData(coinCode));
        CustomerAccountVo account = customerAccountService.findCoinAccount(userVo.getId(), coinCode);
        if (account == null) {
            account = new CustomerAccountVo();
        }
        data.put("account", account);
        data.put("address", withdrawAddressService.findAddresList(coinCode,userVo.getId()));
        data.put("openGoogleAuth", userVo.getOpenGoogleAuth());
        jsonResult.setSuccess(true);
        jsonResult.setCode(JsonResult.ResultCode.SUCCESS);
        jsonResult.setData(data);
        return  jsonResult;
    }


    @ApiOperation(value = "提币接口")
    @RequestMapping(value = "/withDrawCoin",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "提币地址", name = "address", paramType = "query", dataType = "String",required = true),
            @ApiImplicitParam(value = "备注", name = "remark", paramType = "query", dataType = "String",required = true),
            @ApiImplicitParam(value = "提币数量", name = "amout", paramType = "query", dataType = "String",required = true),
            @ApiImplicitParam(value = "币种", name = "symbol", paramType = "query", dataType = "String",required = true),
            @ApiImplicitParam(value = "手机验证码", name = "mobileCode", paramType = "query", dataType = "String",required = true),
            @ApiImplicitParam(value = "交易密码", name = "tradePwd", paramType = "query", dataType = "String",required = true),
            @ApiImplicitParam(value = "google认证码", name = "googleCode", paramType = "query", dataType = "String"),
    })
    public JsonResult withDrawCoin(@RequestParam(value = "address") String address,
                                   @RequestParam(value = "remark") String memo,
                                   @RequestParam(value = "amout") String amt,
                                   @RequestParam(value = "symbol") String symbol,
                                   @RequestParam(value = "mobileCode") String valid_code,
                                   @RequestParam(value = "tradePwd") String tradePwd,
                                   @RequestParam(value = "googleCode",required = false) String googleCode) {
        JsonResult jsonResult = new JsonResult(true);
        User user = UserUtils.getUser(false);
        if(user == null || user.getId() == null){
            jsonResult.setSuccess(false);
            jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
            jsonResult.setMsg(Language.L_Failed("msg_no_login"));
        }
        if(user.getOpenGoogleAuth() == 1) {
            //验证goodleCode
            if (googleCode == null) {
                jsonResult.setSuccess(false);
                jsonResult.setMsg(Language.L("input_google_code"));
                return  jsonResult;
            }else{
                if (!userService.checkGoogleAuth(user.getGoogleCode(), googleCode)) {
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg(Language.L("google_invalid"));
                    return jsonResult;
                }
            }
        }
        try {
        //1.校验验证码是否正确
        MobileCodeToken mobileCodeToken = new MobileCodeToken(valid_code, UserUtils.getUser().getUserMobile(), false, null);
        mobileCodeToken.isValid();
        //验证码单次有效，遇到异常删除验证码
        //2.校验交易密码是否正确
        PasswordHelper passwordHelper = new PasswordHelper();
        String tradePassWord = passwordHelper.encryString(tradePwd,UserUtils.getUser().getSalt());
        if(!tradePassWord.equals(UserUtils.getUser().getTradePassword())){
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setMsg(Language.L_Failed("msg_trade_password_error",true));
            return jsonResult;
        }
    }catch (Exception e){
        jsonResult.setMsg(e.getMessage());
        jsonResult.setSuccess(false);
        return jsonResult;
    }
        //校验提币规则并且保存提币历史
     return coinWithdrawService.saveWithdrawOrder( address, memo, amt, symbol);

    }

	@ApiOperation(value = "删除提币地址")
    @RequestMapping(value = "/removeCoinAddress",method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(value = "地址id",name ="id",required = true,dataType = "String",paramType = "query")
	})
    public JsonResult removeCoinAddress(@RequestParam(value = "id") String id){
		JsonResult jsonResult = new JsonResult(true);
		User user = UserUtils.getUser(false);
		if(user == null || user.getId() == null){
			jsonResult.setSuccess(false);
			jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
			jsonResult.setMsg(Language.L_Failed("msg_no_login"));
		}
		int res =  withdrawAddressService.deleteUserCoinAddress(user.getId(),id);
		if(res==1){
			jsonResult.setSuccess(true);
			jsonResult.setMsg("删除成功");
		}else{
			jsonResult.setSuccess(false);
			jsonResult.setMsg("删除失败");
		}
		return jsonResult;
	}

    @ApiOperation(value = "保存提币地址")
    @RequestMapping(value = "/saveCoinAddress",method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(value = "币种", name = "coinCode", paramType = "query", dataType = "String",required = true),
			@ApiImplicitParam(value = "地址", name = "address", paramType = "query", dataType = "String",required = true),
			@ApiImplicitParam(value = "备注", name = "tag", paramType = "query", dataType = "String",required = true)
	})
    public JsonResult saveCoinAddress(@RequestParam(value = "coinCode") String coinCode,@RequestParam(value = "address") String addres,@RequestParam(value = "tag") String tag){
        JsonResult jsonResult = new JsonResult(true);
        User user = UserUtils.getUser(false);
        if(user == null || user.getId() == null){
            jsonResult.setSuccess(false);
            jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
            jsonResult.setMsg(Language.L_Failed("msg_no_login"));
        }
		if(addres == null || !addres.substring(0,2).equals("0x") || addres.length() != 42){
			new JsonResult(false).setMsg(Language.L("msg_withdraw_invalid_address"));
		}
        WithdrawAddress withdrawAddress = new WithdrawAddress();
        withdrawAddress.setRemark(tag);
        withdrawAddress.setUserId(user.getId());
        withdrawAddress.setUserMobile(user.getUserMobile());
        withdrawAddress.setUserName(user.getUserName());
        withdrawAddress.setCoinCode(coinCode);
        withdrawAddress.setCoinAddress(addres);
        SaveResult saveResult = withdrawAddressService.save(withdrawAddress);
        if(saveResult != null && saveResult.getId() != null){
            jsonResult.setCode(JsonResult.ResultCode.SUCCESS);
            jsonResult.setSuccess(true);
        }else{
            jsonResult.setSuccess(false);
            jsonResult.setCode(JsonResult.ResultCode.FAILE);
            jsonResult.setMsg(Language.L("msg_save_fail"));
        }
        return  jsonResult;
    }

	/**
	 * 收藏币种
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "收藏币种")
	@CheckLogin
	@ResponseBody
	@RequestMapping(value = "/svCoinCollection", method = RequestMethod.POST)
	public JsonResult saveCoinsCollection(HttpServletResponse response, @RequestBody SaveCoinsCollectionBP param) {
		UserVo userVo = UserUtils.getUser();
		JsonResult jsonResult = new JsonResult();
		QueryFilter queryFilter = new QueryFilter(CoinPairUserCollection.class);
		queryFilter.addFilter("trade_coin_code=",param.getTradeCoinCode());
		queryFilter.addFilter("pricing_coin_code=",param.getPricingCoinCode());
		queryFilter.addFilter("del=",0);
		queryFilter.addFilter("user_id=",userVo.getId());
		List<CoinPairUserCollection> collections = coinPairUserCollectionService.find(queryFilter);
		if(collections.isEmpty()){
			CoinPairUserCollection entity = new CoinPairUserCollection();
			entity.setDel(BaseModel.DEL_FLAG_NORMAL);
			entity.setUserId(userVo.getId());
			entity.setTradeCoinCode(param.getTradeCoinCode());
			entity.setPricingCoinCode(param.getPricingCoinCode());
			entity.setCreateTime(BaseDate.getNowTime());
			jsonResult.setData(coinPairUserCollectionService.save(entity));
		}
		jsonResult.setSuccess(true);
		jsonResult.setCode(ResultCode.SUCCESS);
		return jsonResult;
	}
	
	/**
	 * 用户取消收藏币种
	 *
	 * @param
	 * @return
	 */
	@ApiOperation(value = "用户取消收藏币种")
	@CheckLogin
	@ResponseBody
	@RequestMapping(value = "/dteCoinCollection", method = RequestMethod.POST)
	public JsonResult<Boolean> deleteCoinCollection(HttpServletResponse response,@RequestBody CancelCoinCollectionBP param) {
		JsonResult<Boolean> jsonResult = new JsonResult<Boolean>();
		UserVo userVo = UserUtils.getUser();
		String tradeCoinCode = param.getTradeCoinCode();
		String pricingCoinCode = param.getPricingCoinCode();
		int result = coinPairUserCollectionService.deleteByCoinCode(tradeCoinCode, pricingCoinCode, userVo.getId());
		jsonResult.setSuccess(result>=1?true:false);
		return jsonResult;
	}

	/**
	 * 获取货币收藏状态
	 * 
	 * @param symbol
	 * 			交易对
	 * @param result
	 */
	@ApiOperation(value = "交易对收藏状态")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "交易对" ,name = "symbol", required = true, dataType = "String", paramType = "query"),
	})
	@CheckLogin
	@ResponseBody
	@RequestMapping(value = { "/collectState" }, method = { RequestMethod.GET })
	public String collectState(String symbol, ResultData<CollectStateVO> result) {
		ModuleAssert.notNull(symbol, ModuleMessageEnum.SYMBOL_IS_NULL);
		UserVo user = UserUtils.getUser();
		CollectStateVO resultVo = result.getData(CollectStateVO.class);
		if(user != null && StringUtils.isNotBlank(user.getId())) {
			List<CoinPairUserCollection> listCoinPair = coinPairUserCollectionService.findCoinPairUserCollectionList(user.getId());
			for(CoinPairUserCollection record : listCoinPair) {
				String coinPair = record.getTradeCoinCode() + CHS.underline.getValue() + record.getPricingCoinCode();
				if(Objects.equals(coinPair, symbol)) {
					resultVo.setState(true);
				}
			}
		}else {
			resultVo.setState(false);
			result.setFailed();
		}
		result.setData(resultVo);
		return result.toJson();
	}


	/**
	 * 获取交易对
	 *
	 * @param
	 */
	@ApiOperation(value = "获取全部交易对")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "定价币" ,name = "pricingCoinCode", dataType = "String", paramType = "query"),
	})
	@ResponseBody
	@RequestMapping(value = "/coins_area", method = RequestMethod.GET)
	public String coinsArea(CoinsAreaBP param, ResultData<Map<String, List<CoinsAreaVO>>> result) {
		// 定价币
		String pricingCoinCode = param.getPricingCoinCode();
		
		// 用户登录状态下获取收藏情况
		UserVo userVo = UserUtils.getUser();
		List<CoinPairUserCollection> listCollectCoin = new ArrayList<>();
		if (userVo != null && StringUtils.isNotBlank(userVo.getId())) {
			listCollectCoin = coinPairUserCollectionService.findCoinPairUserCollectionList(userVo.getId());
		}
		
		Map<String, List<CoinsAreaVO>> resultData = new HashMap<String, List<CoinsAreaVO>>();
		String symbolValue = coinPairService.findTradeAreaCoins();
		JSONObject jsonObject = JSON.parseObject(symbolValue);
		Set<String> keys = jsonObject.keySet();
		List<ExchangeAction> nextActions = exchangeActionService.getNextAllAction(new Date());
		for(String next : keys) {
			// 如果指定定价币
			if(pricingCoinCode != null && (!Objects.equals(pricingCoinCode, next) || !next.equalsIgnoreCase(pricingCoinCode))) {
				continue;
			}
			
			List<CoinsAreaVO> resultList = new ArrayList<CoinsAreaVO>();
			String value = jsonObject.getString(next);
			JSONArray jsonArray = JSON.parseArray(value);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				CoinsAreaVO resultVo = new CoinsAreaVO();
				resultVo = gsonClient.fromJson(obj.toJSONString(), CoinsAreaVO.class);
				
				resultVo.setIsCollect(false);
				if (listCollectCoin != null && listCollectCoin.size() > 0) {
					for (CoinPairUserCollection coinPairUserCollection : listCollectCoin) {
						if ((resultVo.getTradeCoinCode().equals(coinPairUserCollection.getTradeCoinCode()) && (resultVo.getPricingCoinCode().equals(coinPairUserCollection.getPricingCoinCode())))) {
							resultVo.setIsCollect(true);
							resultVo.setCollectionId(coinPairUserCollection.getId());
						}
					}
				}
				String symbol = resultVo.getTradeCoinCode() + CHS.underline.getValue() + resultVo.getPricingCoinCode();
				KlineSymbol24dataUtil dataUtil = new KlineSymbol24dataUtil(symbol);
				
				// 最高价
				BigDecimal highPrice = dataUtil.getValue(KlineSymbol24dataUtil.HIGH_PRICE);
				if(highPrice == null) {
					highPrice = BigDecimal.ZERO;
				}
				
				// 最低价
				BigDecimal lowPrice = dataUtil.getValue(KlineSymbol24dataUtil.LOW_PRICE);
				if(lowPrice == null) {
					lowPrice = BigDecimal.ZERO;
				}
				
				// 最新价
				BigDecimal newPrice = dataUtil.getValue(KlineSymbol24dataUtil.NEW_PRICE);
				if(newPrice == null) {
					newPrice = BigDecimal.ZERO;
				} 
				
				// 倒数第二的价格
				BigDecimal preNewPrice = dataUtil.getValue(KlineSymbol24dataUtil.PRE_NEW_PRICE);
				if(preNewPrice == null) {
					preNewPrice = BigDecimal.ZERO;
				}
				
				// 交易币总额
				BigDecimal amout = dataUtil.getValue(KlineSymbol24dataUtil.AMOUT);
				if(amout == null) {
					amout = BigDecimal.ZERO;
				}
				
				// 开盘价格
				String openPrice = jedisClient.get(JedisDataSourceSignleton.DB1, String.format(RedisKeyConstant.KLINE_S_OPENPRICE, symbol));
				BigDecimal bigOpenPrice = BigDecimal.ZERO;
				if(StringUtils.isNotBlank(openPrice)) {
					bigOpenPrice = new BigDecimal(openPrice);
				}

				// 24小时涨跌
				BigDecimal rate = BigDecimal.ZERO;
				if(bigOpenPrice.compareTo(BigDecimal.ZERO) > 0 && newPrice.compareTo(BigDecimal.ZERO) > 0) {
					//先减去24小时前的再除以24小时前的
					rate = newPrice.subtract(bigOpenPrice).divide(bigOpenPrice, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				}
				
				//最新价兑换钱
				resultVo.setEvaluateCny(BigDecimal.ZERO);
				if (!CoinEnum.USDT.getCode().equals(resultVo.getPricingCoinCode())) {  //如果定价币不是QC
					String newPriceRedis = RedisUserUtil.getKlineCovertToUSDT(resultVo.getPricingCoinCode(), CoinEnum.USDT.getCode()).toString();
					if (!StringUtils.isEmpty(newPriceRedis) && !StringUtils.isNull(newPrice)) {
						BigDecimal evaluateCny = new BigDecimal(newPriceRedis).multiply(newPrice).setScale(2, BigDecimal.ROUND_HALF_UP); //改币种的预估值 （保留两位小数）
						resultVo.setEvaluateCny(evaluateCny);
					}
				} else {
					//如果是QC则当前价钱就是人民币
					Jedis jedis = jedisClient.getJedis(JedisDataSourceSignleton.DB1);
					resultVo.setEvaluateCny(StringUtils.isNull(newPrice) ? BigDecimal.ZERO : newPrice.multiply(RedisUserUtil.getUsdtToCnyRate(jedis)));
				}
				
				resultVo.setHigh(highPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
				resultVo.setLow(lowPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
				resultVo.setColse(newPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
				resultVo.setPreLast(preNewPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
				resultVo.setRate(rate.setScale(2, BigDecimal.ROUND_HALF_UP));
				resultVo.setVolume(amout.setScale(2, BigDecimal.ROUND_HALF_UP));

				if(!nextActions.isEmpty()) {
					ExchangeAction exchangeAction = nextActions.stream().sorted(new Comparator<ExchangeAction>() {
						@Override
						public int compare(ExchangeAction o1, ExchangeAction o2) {
							return o1.getStartTime().compareTo(o2.getStartTime());
						}
					}).filter(f -> f.getCoinCode().equals(obj.getString("tradeCoinCode")) && f.getPriceCode().equals(obj.getString("pricingCoinCode"))).findFirst().orElse(null);
					if (exchangeAction !=null) {
						resultVo.setActionTime(exchangeAction.getStartTime());
					}
				}
				resultList.add(resultVo);
			}
			resultData.put(next, resultList);
		}
		result.setData(resultData);
		return result.toJson();
	}


	/**
	 * 获取推荐币
	 */
	@SuppressWarnings("serial")
	@ApiOperation(value = "获取推荐交易对")
	@RequestMapping(value = "/price/recommends", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult recommends() {
		JsonResult jsonResult=new JsonResult();
		jsonResult.setSuccess(Boolean.TRUE);
		jsonResult.setCode(Constants.SUCCESS);
		String jsonValue = coinPairService.findRecommentSession();
		jsonResult.setData(JSONObject.parseArray(jsonValue));
		return jsonResult;
	}



	
	/**
	 * 交易对单价数量成交量小数位数
	 * 
	 */
    @ApiOperation(value = "交易对单价数量成交量小数位数")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "交易对" ,name = "symbol", required = true, dataType = "String", paramType = "query"),
	})
	@ResponseBody
	@RequestMapping(value = { "/symbolDecimal" }, method = { RequestMethod.GET })
	public String symbolDecimal(SymbolDecimalBP param, ResultData<SymbolDecimalVO> result) {
		String symbol = param.getSymbol();
		ModuleAssert.notNull(symbol, ModuleMessageEnum.SYMBOL_IS_NULL);
		
		Map<String, Object> cacheCoinConfig = decimalUtil.getCoinConfig(symbol);
		SymbolDecimalVO data = gsonClient.fromJson(gsonClient.toJson(cacheCoinConfig), SymbolDecimalVO.class, FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
		result.setData(data);
		return result.toJson();
    }
    
    
}
