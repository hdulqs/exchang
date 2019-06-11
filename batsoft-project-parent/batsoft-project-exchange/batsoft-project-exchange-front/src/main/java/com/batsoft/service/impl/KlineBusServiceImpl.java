package com.batsoft.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.batsoft.common.beans.vo.LoadKlineDataVO;
import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.common.util.data.ResultData;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.constant.KlineTimeNodeConstant;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.core.common.utils.CoinPairConfigUtil;
import com.batsoft.model.module.exchange.dto.KlineDTO;
import com.batsoft.service.KlineBusService;
import com.batsoft.service.module.exchange.service.KlineService;
import com.batsoft.utils.gson.GsonSingleton;

@Service(value = "klineBusService")
public class KlineBusServiceImpl implements KlineBusService {

	@Resource
	private KlineService klineService;

	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	private GsonSingleton gsonClient = GsonSingleton.getInstance();
	
	private CoinPairConfigUtil decimalUtil = new CoinPairConfigUtil();
	
	private Random ran = new Random();
	
	@Override
	public void initKline() {
		// 数据库数据不足
	}

	@Override
	public void loadKlineData(String symbol, String subject, Long from, Long to, ResultData<List<LoadKlineDataVO>> result) {
		List<LoadKlineDataVO> data = new ArrayList<LoadKlineDataVO>();
		String klineKey = String.format(RedisKeyConstant.EXCHANGE_KLINE_DATA, symbol, subject);
		
		// 1.检验K线图中是否存在数据
		String lastValJson = jedisClient.lindex(JedisDataSourceSignleton.DB0, klineKey, 0);
		KlineDTO lastKlineDto = gsonClient.fromJson(lastValJson, KlineDTO.class);
		if(lastKlineDto == null || lastKlineDto.getTime() == null){
			result.setMessage(ModuleMessageEnum.NO_DATA);
			result.setData(data);
			return;
		}
		
		// 2.如果最新的一条数据时间小于指定索引的时间
		if(from > lastKlineDto.getTime()) {
			result.setMessage(ModuleMessageEnum.FROM_GT_LAST_KLINE_DATA_TIME);
			result.setData(data);
			return;
		}
		
		// 寻找合适的时间节点
		Long maxDiffTime = lastKlineDto.getTime() - from;
		to = to > lastKlineDto.getTime()? lastKlineDto.getTime() : to;
		Long minDiffTime = lastKlineDto.getTime() - to;
		
		Long maxIndex = 0L, minIndex = 0L;
		String m = CHS.M.getValue().toLowerCase();
		String h = CHS.H.getValue().toLowerCase();
		String d = CHS.D.getValue().toLowerCase();
		String w = CHS.W.getValue().toLowerCase();
		if(subject.indexOf(m) > 0) { // 每分钟级别
			Long mantissa = Long.valueOf(subject.substring(0, subject.indexOf(m)));
			Long minute = 1000L * 60L;
			maxIndex = maxDiffTime / minute / mantissa;
			minIndex = minDiffTime / minute / mantissa;
		}else if(subject.indexOf(h) > 0) { // 每小时级别
			Long mantissa = Long.valueOf(subject.substring(0, subject.indexOf(h)));
			Long hour = 1000L * 60L * 60L;
			maxIndex = maxDiffTime / hour / mantissa;
			minIndex = minDiffTime / hour / mantissa;
		}else if(subject.indexOf(d) > 0) { // 每天级别
			Long mantissa = Long.valueOf(subject.substring(0, subject.indexOf(d)));
			Long day = 1000L * 60L * 60L * 24L;
			maxIndex = maxDiffTime / day / mantissa;
			minIndex = minDiffTime / day / mantissa;
		}else if(subject.indexOf(w) > 0) { // 每星期级别
			Long mantissa = Long.valueOf(subject.substring(0, subject.indexOf(w)));
			Long week = 1000L * 60L * 60L * 24L * 7L;
			maxIndex = maxDiffTime / week / mantissa;
			minIndex = minDiffTime / week / mantissa;
		}
		maxIndex = maxIndex > 10000L? 10000L : maxIndex;  // 最大限制一次性最多只能拉出一万个节点数据
		maxIndex = maxIndex <= 0L? 0L : --maxIndex;		  // 最小不能小于零
		
		List<String> finalReqJson = jedisClient.lrange(JedisDataSourceSignleton.DB0, klineKey, minIndex, maxIndex);
		if(finalReqJson == null || finalReqJson.size() == 0) {
			// 时间节点没有数据
			result.setMessage(ModuleMessageEnum.NO_DATA);
		}else {
			// 时间节点前面有数据
			result.setMessage(ModuleMessageEnum.FROM_GT_LAST_KLINE_DATA_TIME);
		}
		
		// 迭代数据
		for(String value : finalReqJson) {
			LoadKlineDataVO vo = new LoadKlineDataVO();
			vo = gsonClient.fromJson(value, vo.getClass());
			if(vo.valid()) {
				data.add(vo);
			}
		}
		result.setData(data);
	}

	@Deprecated
	@Override
	public void repairKlineData(String symbol, BigDecimal maxPrice, BigDecimal minPrice, Long beginTime, Long endTime) {
		String[] timeNode = KlineTimeNodeConstant.convertToArray();
		for(String time : timeNode) {
			String klineKey = String.format(RedisKeyConstant.EXCHANGE_KLINE_DATA, symbol, time);
			// 查询出该时间节点最新的一个数据
			String lastValJson = jedisClient.lindex(JedisDataSourceSignleton.DB0, klineKey, 0);
			KlineDTO lastData = gsonClient.fromJson(lastValJson, KlineDTO.class);
			
			// 2.如果最新的一条数据时间小于指定索引的时间
			if(beginTime > lastData.getTime()) {
				continue;
			}
			
			// 寻找合适的时间节点
			Long maxDiffTime = lastData.getTime() - beginTime;
			Long to = endTime > lastData.getTime()? lastData.getTime() : endTime;
			Long minDiffTime = lastData.getTime() - to;
			
			Long maxIndex = 0L, minIndex = 0L;
			String m = CHS.M.getValue().toLowerCase();
			String h = CHS.H.getValue().toLowerCase();
			String d = CHS.D.getValue().toLowerCase();
			String w = CHS.W.getValue().toLowerCase();
			if(time.indexOf(m) > 0) { // 每分钟级别
				Long mantissa = Long.valueOf(time.substring(0, time.indexOf(m)));
				Long minute = 1000L * 60L;
				maxIndex = maxDiffTime / minute / mantissa;
				minIndex = minDiffTime / minute / mantissa;
			}else if(time.indexOf(h) > 0) { // 每小时级别
				Long mantissa = Long.valueOf(time.substring(0, time.indexOf(h)));
				Long hour = 1000L * 60L * 60L;
				maxIndex = maxDiffTime / hour / mantissa;
				minIndex = minDiffTime / hour / mantissa;
			}else if(time.indexOf(d) > 0) { // 每天级别
				Long mantissa = Long.valueOf(time.substring(0, time.indexOf(d)));
				Long day = 1000L * 60L * 60L * 24L;
				maxIndex = maxDiffTime / day / mantissa;
				minIndex = minDiffTime / day / mantissa;
			}else if(time.indexOf(w) > 0) { // 每星期级别
				Long mantissa = Long.valueOf(time.substring(0, time.indexOf(w)));
				Long week = 1000L * 60L * 60L * 24L * 7L;
				maxIndex = maxDiffTime / week / mantissa;
				minIndex = minDiffTime / week / mantissa;
			}
			maxIndex = maxIndex <= 0L? 0L : --maxIndex;		  // 最小不能小于零
			minIndex = minIndex <= 0L? 0L : --minIndex;
			
			// 修复数据
			int priceDecimal = decimalUtil.getCoinConfigField(symbol, CoinPairConfigUtil.PRICE_DECIMAL);
			for(long index = minIndex; index < maxIndex; index++) {
				String indexJson = jedisClient.lindex(JedisDataSourceSignleton.DB0, klineKey, index);
				KlineDTO klineDTO = gsonClient.fromJson(indexJson, KlineDTO.class);
				if(klineDTO.verify()) {
					BigDecimal high = klineDTO.getHigh();
					BigDecimal low = klineDTO.getLow();
					
					klineDTO.setHigh(low.setScale(priceDecimal, BigDecimal.ROUND_DOWN));
					klineDTO.setLow(high.setScale(priceDecimal, BigDecimal.ROUND_DOWN));
					jedisClient.lset(JedisDataSourceSignleton.DB0, klineKey, index, JSON.toJSONString(klineDTO));
				}
			}
		}
	}

	@Override
	public void repairKeepUp(String symbol, BigDecimal maxPrice, BigDecimal minPrice, Long beginTime, Long endTime, ResultData<Object> result) {
		String[] timeNode = KlineTimeNodeConstant.convertToArray();
		for(String time : timeNode) {
			String klineKey = String.format(RedisKeyConstant.EXCHANGE_KLINE_DATA, symbol, time);
			// 查询出该时间节点最新的一个数据
			String lastValJson = jedisClient.lindex(JedisDataSourceSignleton.DB0, klineKey, 0);
			KlineDTO lastData = gsonClient.fromJson(lastValJson, KlineDTO.class);
			// 2.如果最新的一条数据时间小于指定索引的时间
			if(lastData == null || beginTime > lastData.getTime()) {
				continue;
			}
			
			// 寻找合适的时间节点
			Long maxDiffTime = lastData.getTime() - beginTime;
			Long to = endTime > lastData.getTime()? lastData.getTime() : endTime;
			Long minDiffTime = lastData.getTime() - to;
			
			Long maxIndex = 0L, minIndex = 0L;
			String m = CHS.M.getValue().toLowerCase();
			String h = CHS.H.getValue().toLowerCase();
			String d = CHS.D.getValue().toLowerCase();
			String w = CHS.W.getValue().toLowerCase();
			if(time.indexOf(m) > 0) { // 每分钟级别
				Long mantissa = Long.valueOf(time.substring(0, time.indexOf(m)));
				Long minute = 1000L * 60L;
				maxIndex = maxDiffTime / minute / mantissa;
				minIndex = minDiffTime / minute / mantissa;
			}else if(time.indexOf(h) > 0) { // 每小时级别
				Long mantissa = Long.valueOf(time.substring(0, time.indexOf(h)));
				Long hour = 1000L * 60L * 60L;
				maxIndex = maxDiffTime / hour / mantissa;
				minIndex = minDiffTime / hour / mantissa;
			}else if(time.indexOf(d) > 0) { // 每天级别
				Long mantissa = Long.valueOf(time.substring(0, time.indexOf(d)));
				Long day = 1000L * 60L * 60L * 24L;
				maxIndex = maxDiffTime / day / mantissa;
				minIndex = minDiffTime / day / mantissa;
			}else if(time.indexOf(w) > 0) { // 每星期级别
				Long mantissa = Long.valueOf(time.substring(0, time.indexOf(w)));
				Long week = 1000L * 60L * 60L * 24L * 7L;
				maxIndex = maxDiffTime / week / mantissa;
				minIndex = minDiffTime / week / mantissa;
			}
			maxIndex = maxIndex <= 0L? 0L : --maxIndex;		  // 最小不能小于零
			minIndex = minIndex <= 0L? 0L : --minIndex;
			
			int priceDecimal = decimalUtil.getCoinConfigField(symbol, CoinPairConfigUtil.PRICE_DECIMAL);
			
			BigDecimal diff = maxPrice.subtract(minPrice);
			System.out.println("差价=" + diff);
			
			BigDecimal rate = maxPrice.subtract(minPrice).divide(minPrice, priceDecimal, BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(100));
			System.out.println("涨幅=" + rate);
			
			// 节点数  //得到每节点差距比率
			BigDecimal domNum = BigDecimal.valueOf((maxIndex - minIndex));
			BigDecimal domRate = rate.divide(domNum, priceDecimal, BigDecimal.ROUND_DOWN);
			
			// 修复数据
			for(long index = maxIndex; index > minIndex; index--) {
				String indexJson = jedisClient.lindex(JedisDataSourceSignleton.DB0, klineKey, index);
				KlineDTO klineDTO = gsonClient.fromJson(indexJson, KlineDTO.class);
				if(klineDTO != null && klineDTO.getTime() > 0) {
					BigDecimal ex = BigDecimal.valueOf(maxIndex - index);
					BigDecimal increment = ex.multiply(domRate).multiply(minPrice).divide(BigDecimal.valueOf(100));
					
					BigDecimal open = increment.add(minPrice).setScale(priceDecimal, BigDecimal.ROUND_DOWN);
					klineDTO.setOpen(open); // 开盘价
					klineDTO.setHigh(open.add(BigDecimal.valueOf(Math.random() / 10000)).setScale(priceDecimal, BigDecimal.ROUND_DOWN)); // 最高价
					klineDTO.setLow(open); // 最低价
					klineDTO.setClose(open); // 最新价
					klineDTO.setVolume(klineDTO.getVolume().add(BigDecimal.valueOf(ran.nextInt(3000))));
					System.out.println(gsonClient.toJson(klineDTO));
					jedisClient.lset(JedisDataSourceSignleton.DB0, klineKey, index, gsonClient.toJson(klineDTO));
				}
			}
		}
	}

	@Override
	public void syncKlineRef(String refSymbol, String upSymbol, BigDecimal openPrice, Long beginTime, Long endTime, ResultData<Object> result) {
		String[] timeNode = KlineTimeNodeConstant.convertToArray();
		for(String time : timeNode) {
			// 校验更新价值
			String upKlineKey = String.format(RedisKeyConstant.EXCHANGE_KLINE_DATA, upSymbol, time);
			String upLastValJson = jedisClient.lindex(JedisDataSourceSignleton.DB0, upKlineKey, 0);
			KlineDTO upLastData = gsonClient.fromJson(upLastValJson, KlineDTO.class);
			
			String refKlineKey = String.format(RedisKeyConstant.EXCHANGE_KLINE_DATA, refSymbol, time);
			String refLastValJson = jedisClient.lindex(JedisDataSourceSignleton.DB0, refKlineKey, 0);
			KlineDTO refLastData = gsonClient.fromJson(refLastValJson, KlineDTO.class);
			if(upLastData == null || refLastData == null) {
				continue;
			}
			
			// 寻找合适的时间节点
			Long maxDiffTime = upLastData.getTime() - beginTime;
			Long to = endTime > upLastData.getTime()? upLastData.getTime() : endTime;
			Long minDiffTime = upLastData.getTime() - to;
			
			Long maxIndex = 0L, minIndex = 0L;
			String m = CHS.M.getValue().toLowerCase();
			String h = CHS.H.getValue().toLowerCase();
			String d = CHS.D.getValue().toLowerCase();
			String w = CHS.W.getValue().toLowerCase();
			Long mantissa = 1L;
			if(time.indexOf(m) > 0) { // 每分钟级别
				mantissa = Long.valueOf(time.substring(0, time.indexOf(m)));
				Long minute = 1000L * 60L;
				maxIndex = maxDiffTime / minute / mantissa;
				minIndex = minDiffTime / minute / mantissa;
			}else if(time.indexOf(h) > 0) { // 每小时级别
				mantissa = Long.valueOf(time.substring(0, time.indexOf(h)));
				Long hour = 1000L * 60L * 60L;
				maxIndex = maxDiffTime / hour / mantissa;
				minIndex = minDiffTime / hour / mantissa;
			}else if(time.indexOf(d) > 0) { // 每天级别
				mantissa = Long.valueOf(time.substring(0, time.indexOf(d)));
				Long day = 1000L * 60L * 60L * 24L;
				maxIndex = maxDiffTime / day / mantissa;
				minIndex = minDiffTime / day / mantissa;
			}else if(time.indexOf(w) > 0) { // 每星期级别
				mantissa = Long.valueOf(time.substring(0, time.indexOf(w)));
				Long week = 1000L * 60L * 60L * 24L * 7L;
				maxIndex = maxDiffTime / week / mantissa;
				minIndex = minDiffTime / week / mantissa;
			}
			maxIndex = maxIndex < 0L? 0L : maxIndex;
			minIndex = minIndex < 0L? 0L : minIndex;
			
			// 寻找被参考交易对在规定时间内第一个节点
			BigDecimal refOpenPrice = BigDecimal.ZERO;
			for(long index = maxIndex; index > 0; index--) {
				String indexJson = jedisClient.lindex(JedisDataSourceSignleton.DB0, refKlineKey, maxIndex);
				KlineDTO refFirstNode = gsonClient.fromJson(indexJson, KlineDTO.class);
				if(refFirstNode != null) {
					refOpenPrice = refFirstNode.getOpen();
					break;
				}
			}
			
			// 开始同步K线图
			for(long index = maxIndex; index > minIndex; index--) {
				// 被参考K线图数据
				String refIndexJson = jedisClient.lindex(JedisDataSourceSignleton.DB0, refKlineKey, index);
				KlineDTO refKlineDTO = gsonClient.fromJson(refIndexJson, KlineDTO.class);
				if(refKlineDTO == null) {
					continue;
				}
				
				// 计算高开低收涨跌幅
				BigDecimal closeRate = refKlineDTO.getClose().subtract(refOpenPrice).divide(refOpenPrice, 5, BigDecimal.ROUND_DOWN);
				BigDecimal highRate = refKlineDTO.getHigh().subtract(refOpenPrice).divide(refOpenPrice, 5, BigDecimal.ROUND_DOWN);
				BigDecimal lowRate = refKlineDTO.getLow().subtract(refOpenPrice).divide(refOpenPrice, 5, BigDecimal.ROUND_DOWN);
				BigDecimal openRate = refKlineDTO.getOpen().subtract(refOpenPrice).divide(refOpenPrice, 5, BigDecimal.ROUND_DOWN);
				
				// 待更新K线图数据
				int priceDecimal = decimalUtil.getCoinConfigField(upSymbol, CoinPairConfigUtil.PRICE_DECIMAL);
				String upIndexJson = jedisClient.lindex(JedisDataSourceSignleton.DB0, upKlineKey, index);
				KlineDTO upKlineDTO = gsonClient.fromJson(upIndexJson, KlineDTO.class);
				if(upKlineDTO != null && upKlineDTO.getTime() > 0) {
					// 最新价
					upKlineDTO.setClose(openPrice.multiply(closeRate).add(openPrice).setScale(priceDecimal, BigDecimal.ROUND_DOWN));
					
					// 最高价
					upKlineDTO.setHigh(openPrice.multiply(highRate).add(openPrice).setScale(priceDecimal, BigDecimal.ROUND_DOWN));
					
					// 最低价
					upKlineDTO.setLow(openPrice.multiply(lowRate).add(openPrice).setScale(priceDecimal, BigDecimal.ROUND_DOWN)); 
					
					// 开盘价
					upKlineDTO.setOpen(openPrice.multiply(openRate).add(openPrice).setScale(priceDecimal, BigDecimal.ROUND_DOWN));
					
					// 节点中成交总量
					BigDecimal initVolume = BigDecimal.valueOf(3000);
					if(time.indexOf(m) > 0) { // 分钟级别
						int ranVol = initVolume.multiply(BigDecimal.valueOf(mantissa)).intValue();
						upKlineDTO.setVolume(BigDecimal.valueOf(ran.nextInt(ranVol)));
					}else if(time.indexOf(h) > 0) { // 每小时级别
						BigDecimal multiple = BigDecimal.valueOf(60L);
						int ranVol = initVolume.multiply(BigDecimal.valueOf(mantissa)).multiply(multiple).intValue();
						upKlineDTO.setVolume(BigDecimal.valueOf(ran.nextInt(ranVol)));
					}else if(time.indexOf(d) > 0) { // 每天级别
						BigDecimal multiple = BigDecimal.valueOf(24L * 60L);
						int ranVol = initVolume.multiply(BigDecimal.valueOf(mantissa)).multiply(multiple).intValue();
						upKlineDTO.setVolume(BigDecimal.valueOf(ran.nextInt(ranVol)));
					}else if(time.indexOf(w) > 0) { // 每星期级别
						BigDecimal multiple = BigDecimal.valueOf(60L * 24L * 7L);
						int ranVol = initVolume.multiply(BigDecimal.valueOf(mantissa)).multiply(multiple).intValue();
						upKlineDTO.setVolume(BigDecimal.valueOf(ran.nextInt(ranVol)));
					}
					jedisClient.lset(JedisDataSourceSignleton.DB0, upKlineKey, index, gsonClient.toJson(upKlineDTO));
				}
			}
			
			
		}
		
	}
	
}
