package com.batsoft.core.common.coin;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.util.StringUtils;

import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.coin.model.CoinConvertResult;
import com.batsoft.core.common.enums.CoinEnum;
import com.batsoft.core.common.enums.MessageEnum;
import com.batsoft.utils.gson.GsonSingleton;

/**
 * 货币度量信息
 * 
 * @author simon
 */
public class CoinUtil {
	
	// USDT 转 CNY 汇率
	private static final String USDT_TO_PRICE = "appConfig:usdtPrice";
	
	// 虚拟货币汇率转换Redis目录
	private static final String REDIS_EXCHANGE_TICKER = "exchange:ticker:%s_%s";
	
	// 转换货币
	private static final String[] convertCoinType = {"USDT", "ETH", "BT"};
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	/**
	 * 将货币进行转换
	 * 
	 * @param redisService
	 * 				Redis服务
	 * @param coinAmount
	 * 				当前货币数量
	 * @param originalType
	 * 				当前货币类型
	 * @return
	 */
	public CoinConvertResult convetUSDT(BigDecimal coinAmount, String originalType) {
		CoinConvertResult result = new CoinConvertResult();
		this.findUsdtToCnyPrice(result);
		
		BigDecimal udtsAmount = selectUsdt(coinAmount, originalType);
		if(udtsAmount != null) {
			result.setTargetCoinAmount(udtsAmount);
			result.setSuccess();
		}else {
			result.setTargetCoinAmount(BigDecimal.ZERO);
			result.setFail(MessageEnum.ORIGINAL_COIN_TYPE_NOT_TO_USDT_COIN);
		}
		return result;
	}
	
	/**
	 * 迭代选中寻找USDT
	 * 
	 * @param coinAmount
	 * 				货币数量
	 * @param originalType
	 * 				货币类型
	 * @return
	 */
	public BigDecimal selectUsdt(BigDecimal coinAmount, String originalType) {
		if(CoinEnum.USDT.getCode().equalsIgnoreCase(originalType)) {
			return coinAmount;
		}else {
			for(int i = 0; i < convertCoinType.length; i++) {
			 	BigDecimal rate = findCoinPrice(originalType, convertCoinType[i]);
			 	if(rate != null && rate.compareTo(BigDecimal.ZERO) > 0 && Objects.equals(convertCoinType[i], CoinEnum.USDT.getCode())) {
			 		// 命中USDT; 立即返回
			 		return coinAmount.multiply(rate);
			 	}else if(rate != null && rate.compareTo(BigDecimal.ZERO) > 0 && !Objects.equals(convertCoinType[i], CoinEnum.USDT.getCode())) {
			 		// 命中中间货币，进行递归
			 		return selectUsdt(rate.multiply(coinAmount), convertCoinType[i]);
			 	}
			}
			return BigDecimal.ZERO;
		}
	}
	
	/**
	 * 获取USDT转CNY的汇率
	 * 
	 * @param result
	 * 				结果容器
	 */
	public void findUsdtToCnyPrice(CoinConvertResult result) {
		String usdtPrice = jedisClient.get(JedisDataSourceSignleton.DB0, USDT_TO_PRICE);
		if(StringUtils.hasText(usdtPrice)) {
			result.setUsdtToCnyRate(new BigDecimal(usdtPrice));
		}
	}
	
	/**
	 * 获取货币的转换汇率
	 * 
	 * @param originalTypeParam
	 * 				源币种代码 {@link CoinEnum#BT }
	 * @param targetTypeParam
	 * 				目标币种代码 {@link CoinEnum#USDT }
	 * @return
	 */
	public BigDecimal findCoinPrice(String originalTypeParam, String targetTypeParam) {
		String key = String.format(REDIS_EXCHANGE_TICKER, originalTypeParam, targetTypeParam);
		String paramJson = jedisClient.get(JedisDataSourceSignleton.DB0, key);
		String[] param = null;
		if(StringUtils.hasText(paramJson)) {
			param = new String[20];
			param = GsonSingleton.getInstance().fromJson(paramJson, param.getClass());
		}
		if(!Objects.equals(param, null) && StringUtils.hasText(param[7])) {
			return new BigDecimal(param[7]);
		}
		return BigDecimal.ZERO;
	}

	
}
