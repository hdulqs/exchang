/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:19:04
 */
package com.batsoft.service.module.exchange.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.batsoft.core.common.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.Coin;
import com.batsoft.model.module.exchange.CoinPair;
import com.batsoft.service.module.exchange.dao.CoinDao;
import com.batsoft.service.module.exchange.dao.CoinPairDao;
import com.batsoft.service.module.exchange.service.CoinService;
import com.batsoft.utils.StringUtils;

/**
 * <p> CoinServiceImpl </p>
 *
 * @author: Bat Admin
 * @Date :  2018-04-14 10:19:04
 */
@Service("coinService")
public class CoinServiceImpl extends BaseServiceImpl<Coin, String> implements CoinService {

	@Autowired
	private CoinDao coinDao;
	@Autowired
	private CoinPairDao coinPairDao;

	@Autowired
	private RedisService redisService;

	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();

	public static final String CACHE_COINS = Constants.CACHE_EX_PREFIX + "coins";
	public static final String CACHE_PRICE_COINS = Constants.CACHE_EX_PREFIX + "price_coins";
	public static final String CACHE_KLINE_COINS = Constants.CACHE_EX_PREFIX + "kline:";

	@Override
	public List<Coin> findCoins() {
		return coinDao.findListCoins();
	}

	@Override
	public String findJsonCoins() {
		String coins = redisService.get(CACHE_COINS);
		if (StringUtils.isEmpty(coins)) {
			coins = JSON.toJSONString(findCoins());
			redisService.set(CACHE_COINS, coins, RedisService.CACHE_TIME);
		}
		return coins;
	}

	@Override
	public PageResult selectCoin(String coinCode, int page, int pageSize) {
		PageResult pageResult = new PageResult();
		HashMap<String,Object> map = new HashMap<>();
		map.put("coinCode",coinCode);
		int from = (page-1)*pageSize;
		map.put("from",from);
		map.put("pageSize",pageSize);
		pageResult.setRows(coinDao.selectCoins(map));
		pageResult.setTotal(coinDao.selectCoinsTotal(map));
		pageResult.setPageSize(pageSize);
		pageResult.setPage(page);
		return pageResult;
	}

	@Override
	public String findJsonCoinsAllowWithDraw() {
		List<Coin>  coins = coinDao.findListCoinsAllowWithDraw();
		if(!coins.isEmpty()){
			return JSONObject.toJSONString(coins);
		}
		return null;
	}

	@Override
	public String findJsonCoinsAllowRecharge() {
		List<Coin>  coins = coinDao.findListCoinsAllowRecharge();
		if(!coins.isEmpty()){
			return JSONObject.toJSONString(coins);
		}
		return null ;
	}

	@Override
	public String updateJsonCoins() {
		String coins = JSON.toJSONString(findCoins());
		redisService.set(CACHE_COINS, coins, RedisService.CACHE_TIME);
		return coins;
	}

	@Override
	public List<Coin> findPriceCoins() {
		return coinDao.findListPriceCoins();
	}

	@Override
	public String findJsonPriceCoins() {
		String coins = redisService.get(CACHE_PRICE_COINS);
		if (StringUtils.isEmpty(coins)) {
			coins = JSON.toJSONString(findPriceCoins());
			redisService.set(CACHE_PRICE_COINS, coins, RedisService.CACHE_TIME);
		}
		return coins;
	}


	@Override
	public void updateJsonPriceCoins() {
		String coins = JSON.toJSONString(findPriceCoins());
		redisService.set(CACHE_PRICE_COINS, coins, RedisService.CACHE_TIME);
	}


	/**
	 * 获取提币币种数据
	 *
	 * @param coinCode
	 * @return
	 */
	@Override
	public Coin findWithdrawData(String coinCode) {
		return coinDao.findWithdrawData(coinCode);
	}


	@Override
	public void saveRedisCache() {
		// 买卖费率
		List<Coin> listRate = coinDao.selectAll();
		for (Coin coin : listRate) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("coinCode", coin.getCoinCode());
			jsonObject.put("calculationLen", coin.getCalculationLen());
			jsonObject.put("buyRate", coin.getBuyRate());
			jsonObject.put("sellRate", coin.getSellRate());
			jedisClient.hset(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COIN, coin.getCoinCode(), jsonObject.toJSONString());
		}
		
		// 货币配置
		List<CoinPair> listCoinPairConfig = coinPairDao.selectAll();
		for (CoinPair coinPair : listCoinPairConfig) {
			String symbol = coinPair.getTradeCoinCode() + CHS.underline.getValue() + coinPair.getPricingCoinCode();
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("tradeCoinCode", coinPair.getTradeCoinCode());
			jsonObject.put("pricingCoinCode", coinPair.getPricingCoinCode());
			jsonObject.put("amount_decimal", coinPair.getAmountDecimal());
			jsonObject.put("amt_decimal", coinPair.getAmtDecimal());
			jsonObject.put("price_decimal", coinPair.getPriceDecimal()); 
			jedisClient.hset(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COINPAIR, symbol, jsonObject.toJSONString());
			
			// 设置开盘价格到缓存kline:BTC_USDT:openPrice
			String symbolOpenpriceKey = String.format(RedisKeyConstant.KLINE_S_OPENPRICE, symbol);
			if (coinPair.getOpenPrice() != null && coinPair.getOpenPrice().compareTo(BigDecimal.ZERO) > 0) {
				jedisClient.set(JedisDataSourceSignleton.DB1, symbolOpenpriceKey, coinPair.getOpenPrice().toPlainString());
			}
		}
	}
}
