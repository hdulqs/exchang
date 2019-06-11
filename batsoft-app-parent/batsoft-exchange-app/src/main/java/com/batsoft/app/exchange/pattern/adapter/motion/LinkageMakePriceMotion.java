package com.batsoft.app.exchange.pattern.adapter.motion;

import java.math.BigDecimal;

import com.batsoft.app.exchange.pattern.adapter.RobotMakePriceBehavior;
import com.batsoft.app.exchange.pattern.singleton.HuobiMarketDataSyncSingleton;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.utils.CoinPairConfigUtil;

/**
 * 联动一个交易对涨跌幅生成价格
 * 
 * @author simon
 */
public class LinkageMakePriceMotion extends RobotMakePriceBehavior {
	
	// Coin.BTC.getType() + CHS.underline.getValue() + Coin.USDT.getType()
	private String refSymbol;
	
	// 当前交易对参照价格
	private BigDecimal basePrice;
	
	public LinkageMakePriceMotion(BigDecimal basePrice, String coinPair, String refSymbol) {
		super(null, null, null, coinPair);
		this.basePrice = basePrice;
		this.refSymbol = refSymbol;
	}

	@Override
	public BigDecimal getEntrustPrice() {
		Integer priceDecimal = decimalUtil.getCoinConfigField(coinPair, CoinPairConfigUtil.PRICE_DECIMAL);
		return consult(refSymbol, priceDecimal);
	}
	
	/**
	 * 参考指定交易对涨跌幅
	 * 
	 * @param refSymbol
	 * 				被参考交易对
	 * @param priceDecimal
	 * 				价格最大小时位数控制
	 * @return
	 */
	public BigDecimal consult(String refSymbol, Integer priceDecimal) {
		// 获取火币指定交易对的最新价
		BigDecimal refClose = HuobiMarketDataSyncSingleton.getInstance().getHuobiMarketPrice(refSymbol);
		BigDecimal refOpen = new BigDecimal(jedisClient.get(JedisDataSourceSignleton.DB1, String.format(RedisKeyConstant.KLINE_S_OPENPRICE, refSymbol)));
		BigDecimal refRate = refClose.subtract(refOpen).divide(refOpen, 8, BigDecimal.ROUND_DOWN);
		
		// 当前交易对开盘价  * （1 + 被参考交易对的涨跌幅） = 发布价格
		return basePrice.multiply(refRate).add(basePrice).setScale(priceDecimal, BigDecimal.ROUND_DOWN);
	}
}
