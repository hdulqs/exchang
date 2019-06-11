package com.batsoft.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.batsoft.common.beans.vo.GetBookVO;
import com.batsoft.common.beans.vo.GetKlineDataVO;
import com.batsoft.common.beans.vo.GetTickerVO;
import com.batsoft.common.beans.vo.GetTradeVO;
import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.common.util.result.ResultData;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.coin.CoinUtil;
import com.batsoft.core.common.coin.model.CoinConvertResult;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.model.module.exchange.dto.KlineDTO;
import com.batsoft.service.TradingBusService;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;
import com.batsoft.service.module.exchange.trade.model.TradeEntrustInfo;
import com.batsoft.utils.gson.GsonSingleton;

@Service(value = "tradingBusService")
public class TradingBusServiceImpl implements TradingBusService {
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	private GsonSingleton gsonClient = GsonSingleton.getInstance();

	@Override
	public void getKlineData(String symbol, String time, Long from, Long to, ResultData<List<GetKlineDataVO>> result) {
		List<GetKlineDataVO> data = new ArrayList<GetKlineDataVO>();
		String klineKey = String.format(RedisKeyConstant.EXCHANGE_KLINE_DATA, symbol, time);
		
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
			result.setMessage(ModuleMessageEnum.VALID_DATA);
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
		maxIndex = maxIndex > 10000L? 10000L : maxIndex;  // 最大限制一次性最多只能拉出一万个节点数据
		maxIndex = maxIndex <= 0L? 0L : --maxIndex;		  // 最小不能小于零
		
		List<String> finalReqJson = jedisClient.lrange(JedisDataSourceSignleton.DB0, klineKey, minIndex, maxIndex);
		if(finalReqJson == null || finalReqJson.size() == 0) {
			// 时间节点没有数据
			result.setMessage(ModuleMessageEnum.NO_DATA);
		}else {
			// 时间节点前面有数据
			result.setMessage(ModuleMessageEnum.VALID_DATA);
		}
		
		// 迭代数据
		for(String value : finalReqJson) {
			GetKlineDataVO vo = new GetKlineDataVO();
			vo = gsonClient.fromJson(value, vo.getClass());
			if(vo.valid()) {
				data.add(vo);
			}
		}
		result.setData(data);
	}

	@Override
	public void getBook(String symbol, ResultData<GetBookVO> result) {
		GetBookVO resultVo = result.getData(GetBookVO.class);
		bookList(TradeEntrust.ENTRUST_TYPE_BUY, symbol, resultVo.getBuy());
		bookList(TradeEntrust.ENTRUST_TYPE_SELL, symbol, resultVo.getSell());
	}
	private void bookList(String type, String symbol, List<String[]> result) {
    	String priceZsetKey = String.format(RedisKeyConstant.TRA_PRICE_ZSET, symbol, type);
        Set<String> priceZset = jedisClient.zrange(JedisDataSourceSignleton.DB1, priceZsetKey, 0, 50); 
        Iterator<String> data = priceZset.iterator();
        while (data.hasNext()) {
        	String[] resultVe = new String[2];
            String price = data.next();
            String key = String.format(RedisKeyConstant.TRA_COINPAIR_TRADE_KEY, symbol, type, price);
            List<String> priceOrders = jedisClient.lrange(JedisDataSourceSignleton.DB1, key, 0, 1000);
            
            //统计一个价格的委托数量
            BigDecimal amout = BigDecimal.ZERO;
            for (String order : priceOrders) {
                TradeEntrust traEntrustTO = JSON.parseObject(order, TradeEntrust.class);
                amout = amout.add(traEntrustTO.getEntrustAmout());
            }
            resultVe[0] = price;
            resultVe[1] = amout.toPlainString();
            result.add(resultVe);
        }
    }

	@Override
	public void getTrade(String symbol, ResultData<List<GetTradeVO>> result) {
		String klineOrderKey = String.format(RedisKeyConstant.KLINE_S_ORDER, symbol);
        List<String> klineOrderList = jedisClient.lrange(JedisDataSourceSignleton.DB1, klineOrderKey, 0, -1);
        
        // 装进集合中然后反转
        List<TradeEntrustInfo> finalResult = new ArrayList<TradeEntrustInfo>();
        for(String empValue : klineOrderList) {
        	finalResult.add(JSON.parseObject(empValue, TradeEntrustInfo.class));
        }
        finalResult.sort((TradeEntrustInfo pre, TradeEntrustInfo rear) -> pre.getEntrustTime().compareTo(rear.getEntrustTime()));
        
        // 迭代装入返回对象
        List<GetTradeVO> data = new ArrayList<GetTradeVO>();
        for(TradeEntrustInfo record : finalResult) {
        	GetTradeVO vo = new GetTradeVO();
        	vo.setTime(String.valueOf(record.getEntrustTime().getTime()));
        	vo.setEntrustPrice(String.valueOf(record.getEntrustPrice()));
        	vo.setEntrustAmout(String.valueOf(record.getEntrustAmout()));
        	vo.setType(record.getType());
        	data.add(vo);
        }
        result.setData(data);
	}

	@Override
	public void getTicker(String symbol, ResultData<GetTickerVO> result) {
    	String key = String.format(RedisKeyConstant.EXCHANGE_TICKER, symbol);
        String value = jedisClient.get(JedisDataSourceSignleton.DB0, key); 
        if (StringUtils.hasText(value)) { 	
        	//["BTC_USDT 【交易对】","","","","","","378290.00【24h涨跌】 ","3783.89990【 最新价】","47400.74875 【24h成交量】","3961.16010 【24h 最高价】","37.0 【24h 最低价】","","","3942.49990","26033.23"]
        	JSONArray tickerJson = JSONArray.parseArray(value);
        	GetTickerVO data = result.getData(GetTickerVO.class);
        	data.setAmount(String.valueOf(tickerJson.getBigDecimal(8).setScale(2, BigDecimal.ROUND_DOWN)));
        	data.setClose(tickerJson.getString(7));
        	data.setHigh(tickerJson.getString(9));
        	data.setLow(tickerJson.getString(10));
        	data.setRate(tickerJson.getString(6));
        	data.setSymbol(tickerJson.getString(0));
        	
        	// 单位换算成CNY
        	CoinUtil util = new CoinUtil();
        	CoinConvertResult convertResult = util.convetUSDT(BigDecimal.ONE, symbol.split(CHS.underline.getValue())[0]);
        	data.setEvaluateCny(convertResult.getTargetCoinAmount().multiply(convertResult.getUsdtToCnyRate()));
        }else {
        	result.setFailed();
        }
	}
	
}
