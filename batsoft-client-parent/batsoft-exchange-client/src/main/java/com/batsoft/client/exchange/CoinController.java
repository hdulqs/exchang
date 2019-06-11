/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.exchange;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.batsoft.client.exchange.common.beans.po.CoinConvertPO;
import com.batsoft.client.exchange.common.enums.ClientMessageEnum;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.coin.CoinUtil;
import com.batsoft.core.common.coin.model.CoinConvertResult;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.core.common.enums.CoinEnum;
import com.batsoft.core.common.enums.KeyEnum;
import com.batsoft.core.common.enums.MessageEnum;
import com.batsoft.core.web.controller.GenericController;
import com.batsoft.model.module.exchange.CoinPairUserCollection;
import com.batsoft.model.module.exchange.ExchangeAction;
import com.batsoft.model.module.member.vo.UserVo;
import com.batsoft.service.module.exchange.service.CoinPairService;
import com.batsoft.service.module.exchange.service.CoinPairUserCollectionService;
import com.batsoft.service.module.exchange.service.CoinService;
import com.batsoft.service.module.exchange.service.ExchangeActionService;
import com.batsoft.service.module.exchange.trade.util.RedisUserUtil;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.utils.StringUtils;
import com.batsoft.utils.gson.GsonSingleton;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>Exchange项目管理</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Api(value = "exchangeCoinController",description = "Exchange项目管理")
@Controller("exchangeCoinController")
@RequestMapping("/exchange")
public class CoinController extends GenericController {
	
	@Autowired
	private CoinService coinService;
	
	@Autowired
	private CoinPairUserCollectionService coinPairUserCollectionService;

	@Autowired
	private CoinPairService coinPairService;
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();

	@Autowired
	private ExchangeActionService exchangeActionService;
	/**
	 * 获取coins
	 * 
	 * @param
	 * @return
	 */
	@ApiOperation(value = "查询可充值或者体现的币",notes = "根据参数是查询体现的还是充值的")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "withdraw" ,value = "是否体现",paramType = "query" ,dataType = "Boolean"),
			@ApiImplicitParam(name = "recharge" ,value = "是否充值",paramType = "query" ,dataType = "Boolean")
	})
	@RequestMapping(value = "/coins", method = RequestMethod.GET)
	@ResponseBody
	public String coins(@RequestParam(name = "withdraw",required = false) boolean withdraw,@RequestParam(name="recharge",required = false) boolean recharge) {
		if(withdraw){
			return coinService.findJsonCoinsAllowWithDraw();
		}
		if(recharge){
			return coinService.findJsonCoinsAllowRecharge();
		}
		return coinService.findJsonCoins();
	}

	/**
	 * 获取可充值coins
	 *
	 * @param
	 * @return
	 */
	@ApiOperation(value = "获取可充值coins")
	@RequestMapping(value = "/coinsAllowRecharge", method = RequestMethod.GET)
	@ResponseBody
	public String coinsAllowRecharge() {
		return coinService.findJsonCoinsAllowRecharge();
	}

	/**
	 * 获取可体现coins
	 *
	 * @param
	 * @return
	 */
	@ApiOperation(value = "获取可体现coins")
	@RequestMapping(value = "/coinsAllowWithDraw", method = RequestMethod.GET)
	@ResponseBody
	public String coinsAllowWithDraw() {
		return coinService.findJsonCoinsAllowWithDraw();
	}

	/**
	 * 保存搜藏币种
	 * 李泉豫修改s693547423
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/svCoinCollection", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult saveCoinsCollection(CoinPairUserCollection coinPairUserCollection) {
		UserVo userVo = UserUtils.getUser();
		JsonResult jsonResult = new JsonResult();
		if (userVo.getId() == null) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg("未登录");
			jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
			return jsonResult;
		}
		QueryFilter queryFilter = new QueryFilter(CoinPairUserCollection.class);
		queryFilter.addFilter("trade_coin_code=",coinPairUserCollection.getTradeCoinCode());
		queryFilter.addFilter("pricing_coin_code=",coinPairUserCollection.getPricingCoinCode());
		queryFilter.addFilter("del=",0);
		queryFilter.addFilter("user_id=",userVo.getId());
		List<CoinPairUserCollection> collections = coinPairUserCollectionService.find(queryFilter);
		if(!collections.isEmpty()){
			jsonResult.setSuccess(true);
			jsonResult.setMsg("");
			return jsonResult;
		}
		coinPairUserCollection.setUserId(userVo.getId());
		jsonResult.setSuccess(true);
		jsonResult.setData(coinPairUserCollectionService.save(coinPairUserCollection));
		return jsonResult;
	}



    /**
     * 获取用户收藏币种列表
     * 李泉豫修改
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取用户收藏Bt币种列表",notes = "从用户信息里面读取用户的Bt收藏币种信息")
    @RequestMapping(value = "/listCoinBtCollection", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getCoinBtPairUser() {
        JsonResult jsonResult = new JsonResult();
        UserVo userVo = UserUtils.getUser();
        if (userVo.getId() == null) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg("未登录");
			jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
            return jsonResult;
        }

        //得到用户搜藏比重的pricingCoinCode  tradeCoinCode
        List<CoinPairUserCollection> list = coinPairUserCollectionService.findCoinPairUserCollectionList(userVo.getId());
        if (CollectionUtils.isEmpty(list)) {
            jsonResult.setSuccess(false);
            jsonResult.setData("");
            jsonResult.setMsg("无收藏");
            return jsonResult;
        }
        String coins = this.coins_area();//得到所有的市场
        JSONObject pricingCoinCodes = JSON.parseObject(coins);
        Set<String> set = pricingCoinCodes.keySet();
        Iterator<String> it = set.iterator();
        JSONArray jsonArray = new JSONArray();
        while (it.hasNext()) {
            String next = it.next();
            String a = pricingCoinCodes.getString(next);
            JSONArray os = JSONArray.parseArray(a);
            for (int i = 0; i<os.size();i++) {
                JSONObject jsonObject = os.getJSONObject(i);
                for (CoinPairUserCollection coinPairUserCollection : list) {
                    //如果在表里找到相应的定价币和交易币代码就返回
                    if ("BT".equals(coinPairUserCollection.getPricingCoinCode()) && coinPairUserCollection.getPricingCoinCode().equals(jsonObject.getString("pricingCoinCode")) && coinPairUserCollection.getTradeCoinCode().equals(jsonObject.getString("tradeCoinCode"))) {
                        jsonObject.put("id", coinPairUserCollection.getId());
                        if(!jsonArray.contains(jsonObject)){
                            jsonArray.add(jsonObject);
                        }
                    }
                }
            }
        }
        jsonResult.setSuccess(true);
        jsonResult.setMsg("");
        jsonResult.setData(JSON.toJSONString(jsonArray, SerializerFeature.DisableCircularReferenceDetect));
        return jsonResult;
    }


	@ApiOperation(value = "获取用户收藏USDT币种列表",notes = "从用户信息里面读取用户的Bt收藏币种信息")
	@RequestMapping(value = "/listCoinCollection", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getCoinPairUser() {
		JsonResult jsonResult = new JsonResult();
		UserVo userVo = UserUtils.getUser();
		if (userVo.getId() == null) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg("未登录");
			jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
			return jsonResult;
		}

		//得到用户搜藏比重的pricingCoinCode  tradeCoinCode
		List<CoinPairUserCollection> list = coinPairUserCollectionService.findCoinPairUserCollectionList(userVo.getId());
		if (CollectionUtils.isEmpty(list)) {
			jsonResult.setSuccess(false);
			jsonResult.setData("");
			jsonResult.setMsg("无收藏");
			return jsonResult;
		}
		String coins = this.coins_area();//得到所有的市场
		JSONObject pricingCoinCodes = JSON.parseObject(coins);
		Set<String> set = pricingCoinCodes.keySet();
		Iterator<String> it = set.iterator();
		JSONArray jsonArray = new JSONArray();
		while (it.hasNext()) {
			String next = it.next();
			String a = pricingCoinCodes.getString(next);
			JSONArray os = JSONArray.parseArray(a);
			for (int i = 0; i<os.size();i++) {
				JSONObject jsonObject = os.getJSONObject(i);
				for (CoinPairUserCollection coinPairUserCollection : list) {
					//如果在表里找到相应的定价币和交易币代码就返回
					if ("USDT".equals(coinPairUserCollection.getPricingCoinCode()) && coinPairUserCollection.getPricingCoinCode().equals(jsonObject.getString("pricingCoinCode")) && coinPairUserCollection.getTradeCoinCode().equals(jsonObject.getString("tradeCoinCode"))) {
						jsonObject.put("id", coinPairUserCollection.getId());
						if(!jsonArray.contains(jsonObject)){
							jsonArray.add(jsonObject);
						}
					}
				}
			}
		}
		jsonResult.setSuccess(true);
		jsonResult.setMsg("");
		jsonResult.setData(JSON.toJSONString(jsonArray, SerializerFeature.DisableCircularReferenceDetect));
		return jsonResult;
	}

	/**
	 * 获取用户交易所收藏币种列表
	 * 李泉豫修改
	 *
	 * @param
	 * @return
	 */
	@ApiOperation(value = "获取用户收藏币种USDT和BT列表",notes = "从用户信息里面读取用户的USDT和ETH和BT收藏币种信息")
	@RequestMapping(value = "/listExchangeCoinCollection", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getExchangeCoinPairUser() {
		JsonResult jsonResult = new JsonResult();
		UserVo userVo = UserUtils.getUser();
		if (userVo.getId() == null) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg("未登录");
			jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
			return jsonResult;
		}

		//得到用户搜藏比重的pricingCoinCode  tradeCoinCode
		List<CoinPairUserCollection> list = coinPairUserCollectionService.findCoinPairUserCollectionList(userVo.getId());
		if (CollectionUtils.isEmpty(list)) {
			jsonResult.setSuccess(false);
			jsonResult.setData("");
			jsonResult.setMsg("无收藏");
			return jsonResult;
		}
		String coins = this.coins_area();//得到所有的市场
		JSONObject pricingCoinCodes = JSON.parseObject(coins);
		Set<String> set = pricingCoinCodes.keySet();
		Iterator<String> it = set.iterator();
		JSONArray jsonArray = new JSONArray();
		while (it.hasNext()) {
			String next = it.next();
			String a = pricingCoinCodes.getString(next);
			JSONArray os = JSONArray.parseArray(a);
			for (int i = 0; i<os.size();i++) {
				JSONObject jsonObject = os.getJSONObject(i);
				for (CoinPairUserCollection coinPairUserCollection : list) {
					//如果在表里找到相应的定价币和交易币代码就返回
					if (("USDT".equals(coinPairUserCollection.getPricingCoinCode())|| "BT".equals(coinPairUserCollection.getPricingCoinCode()))  && coinPairUserCollection.getPricingCoinCode().equals(jsonObject.getString("pricingCoinCode")) && coinPairUserCollection.getTradeCoinCode().equals(jsonObject.getString("tradeCoinCode"))) {
						jsonObject.put("id", coinPairUserCollection.getId());
						if(!jsonArray.contains(jsonObject)){
                            jsonArray.add(jsonObject);
                        }
					}
				}
			}
		}
		jsonResult.setSuccess(true);
		jsonResult.setMsg("");
		jsonResult.setData(JSON.toJSONString(jsonArray, SerializerFeature.DisableCircularReferenceDetect));
		return jsonResult;
	}

	/**
	 * 删除用户收藏币种
	 * 李泉豫修改
	 *
	 * @param
	 * @return
	 */
	@ApiOperation(value = "删除用户收藏币种",notes = "根据币种id")
	@RequestMapping(value = "/dteCoinCollection", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult deleteCoinCollection(@RequestParam(value = "id") String id) {
		JsonResult jsonResult = new JsonResult();
		UserVo userVo = UserUtils.getUser();
		if (userVo.getId() == null) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg("未登录");
			jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
			return jsonResult;
		}
		int result = coinPairUserCollectionService.deleteByKeyId(id,userVo.getId());
		jsonResult.setSuccess(result>=1?true:false);
		return jsonResult;
	}

	/**
	 * 获取定价币coins
	 *
	 * @param
	 * @return
	 */
	@ApiOperation(value = "获取定价币coins")
	@RequestMapping(value = "/price/coins", method = RequestMethod.GET)
	@ResponseBody
	public String priceCoins() {
		return coinService.findJsonPriceCoins();
	}

	/**
	 * 获取推荐币
	 */
	@ApiOperation(value = "获取推荐币")
	@RequestMapping(value = "/price/recommends", method = RequestMethod.GET)
	@ResponseBody
	public String recommends() {
		return coinPairService.findRecommentSession();
	}

	/**
	 * 获取定价币对应的交易币
	 *
	 * @param
	 * @return
	 */
	@ApiOperation(value = "获取定价币对应的交易币",notes = "在请求的路径上添加币种")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "priceCoin" ,value = "币种",paramType = "path" ,dataType = "String")
	})
	@RequestMapping(value = "/coins/{priceCoin}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> tradeCoins(@PathVariable String priceCoin) {
		Map<String, Object> data = new HashMap<>();
		data.put("priceCoin", priceCoin);
		data.put("tradeCoins", coinPairService.findJsonTradeCoins(priceCoin));
		return data;
	}

	/**
	 * 获取全部交易区对应的交易对
	 *
	 * @param
	 * @return
	 */
	@ApiOperation(value = " 获取全部交易区对应的交易对",notes = "读取现在交易区里面的所有交易对")
	@RequestMapping(value = "/coins_area", method = RequestMethod.GET)
	@ResponseBody
	public String coins_area() {
		//CACHE_TRADE_AREA_COINS redis读取数据
		String str = coinPairService.findTradeAreaCoins();
		UserVo userVo = UserUtils.getUser();
		List<CoinPairUserCollection> list = new ArrayList<>();
		if (userVo.getId() != null) {
			list = coinPairUserCollectionService.findCoinPairUserCollectionList(userVo.getId());
		}
		
		Jedis jedis = jedisClient.getJedis(JedisDataSourceSignleton.DB1);
		try {
			JSONObject result = new JSONObject();
			if (!StringUtils.isEmpty(str)) {
				JSONObject jsonObject = JSON.parseObject(str);
				Set<String> set = jsonObject.keySet();
				Iterator<String> it = set.iterator();
				List<ExchangeAction> nextActions = exchangeActionService.getNextAllAction(new Date());
				while (it.hasNext()) {
					String next = it.next();
					String value = jsonObject.getString(next);
					JSONArray jsonArray = JSON.parseArray(value);
					JSONArray resultArry = new JSONArray();
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject obj = jsonArray.getJSONObject(i);
						obj.put("isCollect", false);
						if (!list.isEmpty()) {
							for (CoinPairUserCollection coinPairUserCollection : list) {
								if ((obj.getString("tradeCoinCode").equals(coinPairUserCollection.getTradeCoinCode())
										&& (obj.getString("pricingCoinCode").equals(coinPairUserCollection.getPricingCoinCode())))) {
									obj.put("isCollect", true);
									obj.put("collectionId", coinPairUserCollection.getId());
								}
							}
						}
						String symbol = obj.getString("tradeCoinCode") + CHS.underline.getValue() + obj.getString("pricingCoinCode");
						String data24HourKey = String.format(RedisKeyConstant.KLINE_SYMBOL_24DATA, symbol);
						String highPrice = jedis.hget(data24HourKey, "highPrice");
						String lowPrice = jedis.hget(data24HourKey, "lowPrice");
						String newPrice = jedis.hget(data24HourKey, "newPrice");
						String pre_newPrice = jedis.hget(data24HourKey, "pre_newPrice");
						String amout = jedis.hget(data24HourKey, "amout");
						String openPrice = jedis.get(String.format(RedisKeyConstant.KLINE_S_OPENPRICE, symbol));
						
						BigDecimal bigHighPrice = BigDecimal.ZERO;
						if(StringUtils.isNotBlank(highPrice)) {
							bigHighPrice = new BigDecimal(highPrice);
						}
						
						BigDecimal bigLowPrice = BigDecimal.ZERO;
						if(StringUtils.isNotBlank(lowPrice)) {
							bigLowPrice = new BigDecimal(lowPrice);
						}
						
						BigDecimal bigNewPrice = BigDecimal.ZERO;
						if(StringUtils.isNotBlank(newPrice)) {
							bigNewPrice = new BigDecimal(newPrice);
						}
						
						BigDecimal bigPreNewPrice = BigDecimal.ZERO;
						if(StringUtils.isNotBlank(pre_newPrice)) {
							bigPreNewPrice = new BigDecimal(pre_newPrice);
						}
						
						BigDecimal bigAmout = BigDecimal.ZERO;
						if(StringUtils.isNotBlank(amout)) {
							bigAmout = new BigDecimal(amout);
						}
						
						BigDecimal bigOpenPrice = BigDecimal.ZERO;
						if(StringUtils.isNotBlank(openPrice)) {
							bigOpenPrice = new BigDecimal(openPrice);
						}

						// 24小时涨跌
						BigDecimal rate = BigDecimal.ZERO;
						if(bigOpenPrice.compareTo(BigDecimal.ZERO) > 0 && bigNewPrice.compareTo(BigDecimal.ZERO) > 0) {
							//先减去24小时前的再除以24小时前的
							rate = bigNewPrice.subtract(bigOpenPrice).divide(bigOpenPrice, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
						}
						
						//最新价兑换钱
						if (!"USDT".equals(obj.getString("pricingCoinCode"))) {  //如果定价币不是QC
							String newPriceRedis = RedisUserUtil.getKlineCovertToUSDT(obj.getString("pricingCoinCode"), "USDT").toString();
							if (!StringUtils.isEmpty(newPriceRedis) && !StringUtils.isNull(newPrice)) {
								BigDecimal duihuan = new BigDecimal(newPriceRedis).multiply(bigNewPrice).setScale(2, BigDecimal.ROUND_HALF_UP); //改币种的预估值 （保留两位小数）
								obj.put("duihuan", duihuan);
							} else {
								obj.put("duihuan", 0);
							}
						} else {
							obj.put("duihuan", StringUtils.isNull(newPrice) ? 0 : bigNewPrice.multiply(RedisUserUtil.getUsdtToCnyRate(jedis))); //如果是qc则当前价钱就是人民币
						}
						
						obj.put("high", bigHighPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
						obj.put("low", bigLowPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
						obj.put("last", bigNewPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
						obj.put("preLast", bigPreNewPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
						obj.put("rate", rate.setScale(2, BigDecimal.ROUND_HALF_UP));
						obj.put("volume", bigAmout.setScale(2, BigDecimal.ROUND_HALF_UP));

						if(!nextActions.isEmpty()) {
							ExchangeAction exchangeAction = nextActions.stream().sorted(new Comparator<ExchangeAction>() {
								@Override
								public int compare(ExchangeAction o1, ExchangeAction o2) {
									return o1.getStartTime().compareTo(o2.getStartTime());
								}
							}).filter(f -> f.getCoinCode().equals(obj.getString("tradeCoinCode")) && f.getPriceCode().equals(obj.getString("pricingCoinCode"))).findFirst().orElse(null);
							if (exchangeAction !=null) {
								obj.put("actionTime", exchangeAction.getStartTime());
							}
						}
						resultArry.add(obj);
					}
					result.put(next, resultArry.toJSONString());
				}
				return result.toJSONString();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return str;
	}

	/**
	 * 获取所有定价币对应交易币json串
	 */
	@ApiOperation(value = "查询定价币和交易币的比例")
	@RequestMapping(value = "/findPricingCoins", method = RequestMethod.GET)
	@ResponseBody
	public String findPricingCoins() {
		return coinPairService.findMapTradeCoins();
	}

	/**
	 * 查询交易币小数定价规则
	 */
	@ApiOperation(value = "查询交易币小数定价规则")
	@RequestMapping(value = "/findCoinDigits", method = RequestMethod.GET)
	@ResponseBody
	public String findCoinDigits() {
		return coinPairService.findCoinPairDigit();
	}

	/**
	 * 
	 * C
	 * @return
	 */
	@RequestMapping(value = "/findDigitsMap", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> findDigitsMap() {
		String tradeCoins = coinPairService.findMapTradeCoins();
		String digits = coinPairService.findCoinPairDigit();
		Map<String, String> map = new HashMap<String, String>();
		map.put("tradeCoins", tradeCoins);
		map.put("digits", digits);
		return map;
	}
	
	
	/**
	 *  有参数时获取该币种转换成USDT后的数量<br />
	 *  无参数时获取BT作为定价币的币种转换成USDT的汇率
	 */
	@ApiOperation(value = "币种之间的转换")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "param" ,value = "是否充值",paramType = "body" ,dataType = "CoinConvertPO")
	})
	@ResponseBody
	@RequestMapping(value = { "/coinConvert" }, method = { RequestMethod.GET, RequestMethod.POST })
	public JsonResult coinConvert(@RequestBody(required = false) CoinConvertPO param) {
		JsonResult result = new JsonResult();
		CoinUtil util = new CoinUtil();
		if(param == null || StringUtils.isNull(param.getOriginalType())) {
			List<String> coins = new ArrayList<String>();
			Map<String, String> coinPairMap = jedisClient.hgetall(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COINPAIR);
			Set<String> keys = coinPairMap.keySet();
			for(String key : keys) {
				Map<String, Object> values = new HashMap<String, Object>();
				values = GsonSingleton.getInstance().fromJson(coinPairMap.get(key), values.getClass());
				Object pricingCoinCode = values.get(KeyEnum.pricingCoinCode.name());
				if(Objects.equals(pricingCoinCode, CoinEnum.BT.getCode())) {
					coins.add(String.valueOf(values.get(KeyEnum.tradeCoinCode.name())));
				}
			}
			Map<String, String> data = new HashMap<String, String>();
			String usdtPrice = jedisClient.get(JedisDataSourceSignleton.DB0, RedisKeyConstant.USDT_TO_PRICE);
			data.put(KeyEnum.usdtToCnyRate.name(), usdtPrice);
			for(String coin : coins) {
				data.put(coin, util.selectUsdt(BigDecimal.ONE, coin).toPlainString());
			}
			result.setSuccess(true); 
			result.setData(data);
			result.setCode(JsonResult.ResultCode.SUCCESS);
			result.setMsg(MessageEnum.SUCCESS.getMessage());
		}else {
			BigDecimal coinAmount = param.getCoinAmount();
			if(coinAmount == null || coinAmount.compareTo(BigDecimal.ZERO) <= 0) {
				result.setSuccess(false);
				result.setCode(JsonResult.ResultCode.FAILE);
				result.setMsg(ClientMessageEnum.COIN_AMOUNT_NOT_NULL.getMessage());
				return result;
			}
			String originalType = param.getOriginalType();
			CoinConvertResult convertResult = util.convetUSDT(coinAmount.abs(), originalType);
			if(convertResult.isSuccess()) {
				result.setSuccess(true); 
				result.setData(convertResult);
				result.setCode(JsonResult.ResultCode.SUCCESS);
				result.setMsg(MessageEnum.SUCCESS.getMessage());
			}
		}
		return result;
	}
	
}
