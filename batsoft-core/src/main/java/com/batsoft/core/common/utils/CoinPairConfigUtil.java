package com.batsoft.core.common.utils;

import java.util.HashMap;
import java.util.Map;

import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.utils.StringUtils;
import com.batsoft.utils.gson.GsonSingleton;
import com.google.common.reflect.TypeToken;

/**
 * 交易对配置
 * 
 * @author simon
 */
public class CoinPairConfigUtil {
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	/**
	 * 总量小数位
	 * 
	 */
	public static final String AMOUNT_DECIMAL = "amount_decimal";
	
	/**
	 * 数量小数位
	 * 
	 */
	public static final String AMT_DECIMAL = "amt_decimal";
	
	/**
	 * 价格小数位
	 * 
	 */
	public static final String PRICE_DECIMAL = "price_decimal";
	
	/**
	 * 获取交易对配置
	 *
	 * @param coinPair
	 * 			交易对代码【BTC_USDT】
	 * @return
	 */
	@SuppressWarnings("serial")
	public Map<String, Object> getCoinConfig(String coinPair) {
		Map<String, Object> result = new HashMap<String, Object>();
		String configJson = jedisClient.hget(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COINPAIR, coinPair);
		if (!StringUtils.isEmpty(configJson)) {
			result = GsonSingleton.getInstance().fromJson(configJson, new TypeToken<Map<String, Object>>() {}.getType());
		}
		return result;
	}
	
	/**
	 * 获取货币配置数量小数位数
	 * 
	 * @param coinPair
	 * 			交易对
	 * @return
	 */
	public Integer getCoinConfigField(String coinPair, String field) {
		Map<String, Object> config = getCoinConfig(coinPair);
		if(config != null && config.size() > 0) {
			Object val = config.get(field);
			if(val instanceof Integer) {
				return Integer.valueOf(String.valueOf(val));
			}else if(val instanceof Double){
				return Double.valueOf(String.valueOf(val)).intValue();
			}
		}
		return 2;
	}
	
}
