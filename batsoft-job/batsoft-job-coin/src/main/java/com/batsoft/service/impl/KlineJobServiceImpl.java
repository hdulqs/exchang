package com.batsoft.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.core.common.enums.KeyEnum;
import com.batsoft.model.module.exchange.CoinPair;
import com.batsoft.model.module.exchange.dto.KlineDTO;
import com.batsoft.service.KlineJobService;
import com.batsoft.service.module.exchange.dao.CoinPairDao;
import com.batsoft.service.module.exchange.service.CoinPairService;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.gson.GsonSingleton;

@Service(value = "klineJobService")
public class KlineJobServiceImpl implements KlineJobService {
	
	@Resource
	private CoinPairService coinPairService;
	
	@Resource
	private CoinPairDao coinPairDao;
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	private GsonSingleton gsonClient = GsonSingleton.getInstance();

	@Override
	public void updateOpenPrice() {
		// 交易对列表
		List<CoinPair> pairList = coinPairDao.selectAll();
		for (CoinPair record : pairList) {
			String coinPair = record.getTradeCoinCode() + CHS.underline.getValue() + record.getPricingCoinCode();
			
			// 获取当前交易对最新价格
			String twentyFourHourdataKey = String.format(RedisKeyConstant.KLINE_S_24DATA, coinPair);
			String newPrice = jedisClient.hget(JedisDataSourceSignleton.DB1, twentyFourHourdataKey, KeyEnum.newPrice.name());
			
			if(StringUtils.hasText(newPrice)) {
				// 更新OpenPrice 到Redis
				String openPriceKey = String.format(RedisKeyConstant.KLINE_S_OPENPRICE, coinPair);
				jedisClient.set(JedisDataSourceSignleton.DB1, openPriceKey, newPrice);
				
				// 更新OpenPrice 到数据库
				record.setOpenPrice(new BigDecimal(newPrice));
				coinPairDao.updateByPrimaryKeySelective(record);
			}
		}
	}

	@Override
	public void updateOpenAndMaxAndMinPrice() {
		// 交易对列表
		List<CoinPair> pairList = coinPairDao.selectAll();
		for (CoinPair record : pairList) {
			// 交易对
			String coinPair = record.getTradeCoinCode() + CHS.underline.getValue() + record.getPricingCoinCode();
			
			// 【更新】最新价格
			String twentyFourHourdataKey = String.format(RedisKeyConstant.KLINE_S_24DATA, coinPair);
			String newPrice = jedisClient.hget(JedisDataSourceSignleton.DB1, twentyFourHourdataKey, KeyEnum.newPrice.name());
			if(StringUtils.hasText(newPrice)) {
				// 更新OpenPrice 到Redis
				String openPriceKey = String.format(RedisKeyConstant.KLINE_S_OPENPRICE, coinPair);
				jedisClient.set(JedisDataSourceSignleton.DB1, openPriceKey, newPrice);
				
				// 更新OpenPrice 到数据库
				record.setOpenPrice(new BigDecimal(newPrice));
				coinPairDao.updateByPrimaryKeySelective(record);
			}
			
			// 最高价、最低价
			String klineOneTimeDataKey = String.format(RedisKeyConstant.EXCHANGE_KLINE_DATA, coinPair, "1h");
			List<String> klineDataJson = jedisClient.lrange(JedisDataSourceSignleton.DB0, klineOneTimeDataKey, 0, 23);
			if(klineDataJson != null && klineDataJson.size() > 0) {
				List<KlineDTO> result = new ArrayList<KlineDTO>();
				klineDataJson.forEach((String value) -> { // 将Json转一下
					KlineDTO dto = gsonClient.fromJson(value, KlineDTO.class);
					result.add(dto);
				});
				
				// 最高价:倒序
				result.sort((KlineDTO pre, KlineDTO rear) -> rear.getHigh().compareTo(pre.getHigh()));
				String highPrice = String.valueOf(result.iterator().next().getHigh());
				jedisClient.hset(JedisDataSourceSignleton.DB1, twentyFourHourdataKey, KeyEnum.highPrice.name(), highPrice);
				
				// 最低价：升序
				result.sort((KlineDTO pre, KlineDTO rear) -> pre.getLow().compareTo(rear.getLow()));
				String lowPrice = String.valueOf(result.iterator().next().getLow());
				jedisClient.hset(JedisDataSourceSignleton.DB1, twentyFourHourdataKey, KeyEnum.lowPrice.name(), lowPrice);
				
				// 24h 涨幅
				BigDecimal rate = BigDecimal.ZERO;
				
				// 24小时交易总量
				Map<String, BigDecimal> sumCacheMap = sumCacheAmount(record.getTradeCoinCode(), record.getPricingCoinCode());
				String exchangeTickerKey = String.format(RedisKeyConstant.EXCHANGE_TICKER, coinPair);
				String[] reset = new String[0];
				String empVal = jedisClient.get(JedisDataSourceSignleton.DB0, exchangeTickerKey);
				reset = GsonSingleton.getInstance().fromJson(empVal, reset.getClass());
				if(reset != null && reset.length > 0) {
					reset[6] = String.valueOf(rate);
					reset[7] = highPrice;
					reset[8] = String.valueOf(sumCacheMap.get(KeyEnum.sumCacheAmout.name()));
					reset[9] = highPrice;
					reset[10] = lowPrice;
					jedisClient.set(JedisDataSourceSignleton.DB0, exchangeTickerKey, JSON.toJSONString(reset));
				}
			}
		}
	}
	
	/**
	 * 统计24小时交易币成交量
	 * 
	 * @param tradeCoinCode
	 * 				交易币
	 * @param pricingCoinCode
	 * 				定价币
	 * @return
	 */
	private Map<String, BigDecimal> sumCacheAmount(String tradeCoinCode, String pricingCoinCode) {
		Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
		String klineSymbol24order = String.format(RedisKeyConstant.KLINE_SYMBOL_24ORDER, tradeCoinCode, pricingCoinCode);
		long splitTime = DateUtils.addDayToDate(new Date(), -1).getTime();
        BigDecimal sumCacheVol = BigDecimal.ZERO;
        BigDecimal sumCacheAmout = BigDecimal.ZERO;
        List<String> listSymbol24OrderData = jedisClient.lrange(JedisDataSourceSignleton.DB1, klineSymbol24order, 0, -1);
        if (listSymbol24OrderData != null && listSymbol24OrderData.size() > 0) {
            int length = listSymbol24OrderData.size() - 1;
            for (int i = length; i > -1; i--) {
                JSONObject recordVal = JSON.parseObject(listSymbol24OrderData.get(i));
                long longValue = recordVal.getLongValue("entrustTime");
                if (longValue < splitTime) {
                	//如果成交时间不在24小时界线内，从队尾删除数据
                	jedisClient.rpop(JedisDataSourceSignleton.DB1, klineSymbol24order);
                }else {
                	sumCacheVol = sumCacheVol.add(recordVal.getBigDecimal("entrustVol"));
                    sumCacheAmout = sumCacheAmout.add(recordVal.getBigDecimal("entrustAmout"));
                }
            }
        }
        result.put(KeyEnum.sumCacheVol.name(), sumCacheVol);
        result.put(KeyEnum.sumCacheAmout.name(), sumCacheAmout);
        return result;
	}
	
	
}
