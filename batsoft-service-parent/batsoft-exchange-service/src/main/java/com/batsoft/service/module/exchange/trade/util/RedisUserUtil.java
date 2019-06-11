package com.batsoft.service.module.exchange.trade.util;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.model.module.exchange.vo.CustomerAccountVo;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;
import com.batsoft.service.module.member.service.UserUtils;

import redis.clients.jedis.Jedis;

public class RedisUserUtil {

	private static final String USDT = "USDT";
	private static final String BT = "BT";
	private static final String ETH = "ETH";
	
	/**
	 * redis存储的键值 在设置交易密码的时候有读取 需要同步
	 */
	public static final String TRADE_PASSWD_KEY = "exchange:tradepassword:";

	private static JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();

	/**
	 * 获取用户的一个币账户
	 *
	 * @param coinCode 
	 * 			币种代码
	 * @return
	 */
	public static CustomerAccount getByCoinCode(String coinCode) {
		String userString = jedisClient.get(JedisDataSourceSignleton.DB1, "user:dcAccount:" + UserUtils.getUser().getId());
		List<CustomerAccountVo> list = null;
		if (!StringUtils.isEmpty(userString)) {
			list = JSON.parseArray(userString, CustomerAccountVo.class);
		}
		if (list != null && !list.isEmpty()) {
			for (CustomerAccount account : list) {
				if (coinCode.equals(account.getCoinCode())) {
					return account;
				}
			}
		}
		return null;
	}

	/**
	 * 获取用户的一个币账户
	 *
	 * @param userId   
	 * 				用户Id
	 * @param coinCode 
	 * 				币种代码
	 * @return
	 */
	public static CustomerAccount getByCoinCode(String userId, String coinCode) {
		String userDcAccountKey = String.format(RedisKeyConstant.USER_DCACCOUNT, userId);
		String userString = jedisClient.get(JedisDataSourceSignleton.DB1, userDcAccountKey);
		List<CustomerAccountVo> list = null;
		if (!StringUtils.isEmpty(userString)) {
			list = JSON.parseArray(userString, CustomerAccountVo.class);
		}
		if (list != null && !list.isEmpty()) {
			for (CustomerAccount account : list) {
				if (coinCode.equals(account.getCoinCode())) {
					return account;
				}
			}
		}
		return null;
	}


	/**
	 * 获取用户全部 资产
	 *
	 * @return
	 */
	public static List<CustomerAccount> getAllCoinsAccount() {
		String userCustomerAccount = jedisClient.get(JedisDataSourceSignleton.DB1, "user:dcAccount:" + UserUtils.getUser().getId());
		if (!StringUtils.isEmpty(userCustomerAccount)) {
			List<CustomerAccount> list = JSON.parseArray(userCustomerAccount, CustomerAccount.class);
			return list;
		}
		return null;
	}

	/**
	 * 获取用户全部 资产
	 *
	 * @return
	 */
	public static List<CustomerAccountVo> getAllCoinsAccount(String userId) {
		String userCustomerAccount = jedisClient.get(JedisDataSourceSignleton.DB1, "user:dcAccount:" + userId);
		if (!StringUtils.isEmpty(userCustomerAccount)) {
			List<CustomerAccountVo> list = JSON.parseArray(userCustomerAccount, CustomerAccountVo.class);
			return list;
		}
		return null;
	}
	/**
	 * 获得价格小数位
	 *
	 * @param coinPair
	 * @return
	 */
	public static Integer getPriceDecimal(String coinPair) {
		String coinpairString = jedisClient.hget(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COINPAIR, coinPair);
		if (!StringUtils.isEmpty(coinpairString)) {
			JSONObject jsonObject = JSON.parseObject(coinpairString);
			return jsonObject.getInteger("price_decimal");
		}
		return 2;
	}

	/**
	 * 获得数量小数位
	 *
	 * @param coinPair
	 * @return
	 */
	public static Integer getAmountDecimal(String coinPair) {
		String coinPairString = jedisClient.hget(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COINPAIR, coinPair);
		if (!StringUtils.isEmpty(coinPairString)) {
			JSONObject jsonObject = JSON.parseObject(coinPairString);
			return jsonObject.getInteger("amt_decimal");
		}
		return 2;
	}
	
	/**
	 * 获取货币交易手续费率
	 * 
	 * @param coinCode
	 * 			货币代码
	 * @return
	 */
	public static JSONObject getCoinConf(String coinCode) {
		String s = jedisClient.hget(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COIN, coinCode);
		if (!StringUtils.isEmpty(s)) {
			JSONObject jsonObject = JSON.parseObject(s);
			return jsonObject;
		}
		return null;
	}


	/**
	 * 验证账户是否为空
	 *
	 * @param tradeEntrust
	 */
	public static boolean validateAccount(TradeEntrust tradeEntrust) {
		if (TradeEntrust.ENTRUST_TYPE_BUY.equals(tradeEntrust.getEntrustType())) {
			CustomerAccount account = RedisUserUtil.getByCoinCode(tradeEntrust.getCustomerId(), tradeEntrust.getPricingCoinCode());
			if (account != null) {
				BigDecimal totalmoney = tradeEntrust.getEntrustAmout().multiply(tradeEntrust.getEntrustPrice());
				if (account.getAvailable().compareTo(totalmoney) < 0) {
					return true;
				}
			} else {
				return true;
			}
		} else {
			CustomerAccount account = RedisUserUtil.getByCoinCode(tradeEntrust.getCustomerId(), tradeEntrust.getTradeCoinCode());
			if (account != null) {
				if (account.getAvailable().compareTo(tradeEntrust.getEntrustAmout()) < 0) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 校验委托单价格数量
	 *
	 * @param tradeEntrust
	 */
	public static boolean validateTradeEntrust(TradeEntrust tradeEntrust) {
		if (tradeEntrust.getEntrustPrice() == null || tradeEntrust.getEntrustPrice().compareTo(BigDecimal.ZERO) <= 0) {
			return true;
		}
		if (tradeEntrust.getEntrustAmout() == null || tradeEntrust.getEntrustAmout().compareTo(BigDecimal.ZERO) <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 验收是否有交易密码
	 *  
	 * @param userId
	 * 			用户ID
	 * @return
	 */
	public static String getTradePassword(String userId){
		String passwordKey =  TRADE_PASSWD_KEY+userId ;
		String tradePassword = jedisClient.get(JedisDataSourceSignleton.DB0, passwordKey);
		if (!StringUtils.isEmpty(tradePassword)) {
			return tradePassword;
		}else{
			return null;
		}
	}
	/**
	 * 设置交易密码到Redis中
	 *
	 * @param userId
	 * 			 	用户ID
	 * @param password
	 * 				密码
	 * @param expireSeconds
	 * 				过期时间
	 */
	public static  void saveTradePassWord(String userId, String password, int expireSeconds){
		String passwordKey = TRADE_PASSWD_KEY + userId ;
		jedisClient.setex(JedisDataSourceSignleton.DB0, passwordKey, password, expireSeconds);
	}



	/**
	 *  返回用户的账号余额
	 * @param customerAccountVoList
	 * @return
	 */
	public static BigDecimal getTotalCNY(List<CustomerAccountVo> customerAccountVoList){
		Jedis jedis = jedisClient.getJedis();
		BigDecimal totalCNY = new BigDecimal(0);
		try {
			String bt_usdt_str= jedis.get("exchange:ticker:BT_USDT");
			if(com.batsoft.utils.StringUtils.isNull(bt_usdt_str)){
				return totalCNY;
			}
			JSONArray array = JSONArray.parseArray(bt_usdt_str);
			if(array.size() < 7){
				return totalCNY;
			}
			String bt_usdt = array.get(7).toString();
			for (CustomerAccountVo accountVo : customerAccountVoList){
				if(accountVo.getTotalMoney().doubleValue()>0) {
					String coinCode = accountVo.getCoinCode();
					if(BT.equals(coinCode)){
						totalCNY =	totalCNY.add(accountVo.getTotalMoney().multiply(new BigDecimal( bt_usdt)).multiply(getUsdtToCnyRate(jedis)));
					}else if (USDT.equals(coinCode)){
						totalCNY  = totalCNY.add(accountVo.getTotalMoney()).multiply(getUsdtToCnyRate(jedis));
					} else{
						totalCNY = totalCNY.add(accountVo.getTotalMoney().multiply(covertToUSDT(jedis,coinCode,USDT)));
					}
				}
			}
		} finally {
			jedisClient.close(jedis);
		}
		return totalCNY;
	}



	/**
	 * 根据名称取得转换的费率
	 * @param jedis
	 * @param coinCode
	 * @param coverCoinCode
	 * @return
	 */
	public static BigDecimal covertToUSDT(Jedis jedis,String coinCode,String coverCoinCode){
		if(USDT.equals(coinCode)){
			return getUsdtToCnyRate(jedis);
		}else if(USDT.equals(coverCoinCode)){
			String value = jedis.get("exchange:ticker:"+coinCode+"_"+coverCoinCode);
			JSONArray jsonArray = JSONArray.parseArray(value);
			if(jsonArray!=null&&jsonArray.size()>7){
				return 	new BigDecimal(jsonArray.get(7).toString()).multiply(getUsdtToCnyRate(jedis));
			}else{//如果不存在
				return covertToUSDT(jedis,coinCode,BT);
			}
		}else  if (BT.equals(coverCoinCode)){
			String value = jedis.get("exchange:ticker:"+coinCode+"_"+coverCoinCode);
			JSONArray jsonArray = JSONArray.parseArray(value);
			if(jsonArray!=null&&jsonArray.size()>7){
				return 	new BigDecimal(jsonArray.get(7).toString()).multiply(covertToUSDT(jedis,BT,USDT));
			}else {
				return  covertToUSDT(jedis,coinCode,ETH);
			}
		}else if(ETH.equals(coverCoinCode)){
			String value = jedis.get("exchange:ticker:"+coinCode+"_"+coverCoinCode);
			JSONArray jsonArray = JSONArray.parseArray(value);
			if(jsonArray!=null&&jsonArray.size()>7){
				return 	new BigDecimal(jsonArray.get(7).toString()).multiply(covertToUSDT(jedis, ETH,USDT));
			}else{
				return new BigDecimal(0);
			}
		}
		return new BigDecimal(0);
	}





	/**
	 * USDT转换成cny的汇率
	 * @param jedis
	 * @return
	 */
	public static BigDecimal getUsdtToCnyRate(Jedis jedis){
		BigDecimal cny = new BigDecimal("6.88");
		String usdt_cny = jedis.get("appConfig:usdtPrice");
		if(!StringUtils.isEmpty(usdt_cny)){
			return  new BigDecimal(usdt_cny);
		}
		return cny;
	}

	public static BigDecimal getKlineCovertToUSDT(String coinCode,String coverCoinCode){
		Jedis jedis = jedisClient.getJedis(JedisDataSourceSignleton.DB1);
		try {
			if(USDT.equals(coverCoinCode)){
				String converPrice = jedis.hget("kline:" +coinCode + "_" + coverCoinCode + ":24data", "newPrice");
				if(!StringUtils.isEmpty(converPrice)){
					return  new BigDecimal(converPrice).multiply(getUsdtToCnyRate(jedis));
				}else{
					return  getKlineCovertToUSDT(coinCode, BT);
				}
			}else if(BT.equals(coverCoinCode)){
				String converPrice = jedis.hget("kline:" +coinCode + "_" + coverCoinCode + ":24data", "newPrice");
				if(!StringUtils.isEmpty(converPrice)){
					return  new BigDecimal(converPrice).multiply(getKlineCovertToUSDT(BT,USDT));
				}else{
					return  getKlineCovertToUSDT(coinCode, ETH);
				}
			}else if(ETH.equals(coverCoinCode)){
				String converPrice = jedis.hget("kline:" +coinCode + "_" + coverCoinCode + ":24data", "newPrice");
				if(!StringUtils.isEmpty(converPrice)){
					return  new BigDecimal(converPrice).multiply(getKlineCovertToUSDT(ETH,USDT) );
				}else{
					return new BigDecimal(0);
				}
			}
		} finally {
			jedisClient.close(jedis);
		}
		return new BigDecimal(0);
	}


}
