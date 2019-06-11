package com.batsoft.core.cache.motion;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;

/**
 * 24小时数据
 * 
 * @author simon
 */
public class KlineSymbol24dataUtil {
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	/**
	 * 24HUSDT交易额
	 * 
	 */
	public static final String USDT_VOL = "usdtVol";
	
	/**
	 * 24HCNY交易额
	 * 
	 */
	public static final String CNY_VOL = "cnyVol";
	
	/**
	 * 24H交易币交易额
	 * 
	 */
	public static final String AMOUT = "amout";
	
	/**
	 * 24H定价币交易额
	 * 
	 */
	public static final String VOL = "vol";
	
	/**
	 * 最新委托单价
	 * 
	 */
	public static final String NEW_PRICE = "newPrice";
	
	/**
	 * 倒数第二个委托单价
	 * 
	 */
	public static final String PRE_NEW_PRICE = "pre_newPrice";
	
	/**
	 * 24H最高价
	 * 
	 */
	public static final String HIGH_PRICE = "highPrice";
	
	/**
	 * 24H最低价
	 * 
	 */
	public static final String LOW_PRICE = "lowPrice";
	
	/**
	 * 24H涨跌幅
	 * 
	 */
	public static final String RATE = "rate";
	
	// RedisKey
	private String KEY = null;
	
	private Map<String, String> cacheValue = new HashMap<String, String>();

	/**
	 *
	 * @param symbol 交易对 如 BTC_USDT
	 */
	public KlineSymbol24dataUtil(String symbol) {
		KEY = String.format(RedisKeyConstant.KLINE_S_24DATA, symbol);
		cacheValue = jedisClient.hgetall(JedisDataSourceSignleton.DB1, KEY);
	}
	
	/**
	 * 获取值
	 * 
	 * @param field
	 * @return
	 */
	public BigDecimal getValue(String field) {
		BigDecimal result = BigDecimal.ZERO;
		String value = cacheValue.get(field);
		if(value != null && value.length() > 0) {
			result = new BigDecimal(value);
		}
		return result;
	}
	
	/**
	 * 设置值
	 * 
	 * @param value
	 */
	public void setValue(String value, String field) {
		jedisClient.hset(JedisDataSourceSignleton.DB1, KEY, field, value);
	}
	
}
