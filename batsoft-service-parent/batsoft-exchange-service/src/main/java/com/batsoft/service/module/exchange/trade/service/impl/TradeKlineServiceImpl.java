package com.batsoft.service.module.exchange.trade.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.coin.CoinUtil;
import com.batsoft.core.common.coin.model.CoinConvertResult;
import com.batsoft.core.common.constant.KlineTimeNodeConstant;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.core.common.enums.CoinEnum;
import com.batsoft.model.module.exchange.dto.PushEntrustIngDTO;
import com.batsoft.model.module.exchange.vo.KlineVo;
import com.batsoft.model.module.exchange.vo.TickerVo;
import com.batsoft.service.module.exchange.dao.EntrustInfoDao;
import com.batsoft.service.module.exchange.service.CoinPairService;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;
import com.batsoft.service.module.exchange.trade.model.TradeEntrustInfo;
import com.batsoft.service.module.exchange.trade.service.TradeKlineService;
import com.batsoft.socketjs.Response;
import com.batsoft.socketjs.WebSocketProperties;

import redis.clients.jedis.Jedis;

/**
 * 交易数据推送
 * 
 * @author simon
 */
@Service("tradeKlineService")
public class TradeKlineServiceImpl implements TradeKlineService {
	
	@Autowired
    private CoinPairService coinPairService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @Autowired
    private WebSocketProperties webSocketProperties;
    
    @Autowired
    EntrustInfoDao entrustInfoDao;
    
    private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
    
    @Override
    public BigDecimal findBuySellFirst(String type, String coinPair) {
        //先判断币种是否存在不存在则返回null
        Map<String, String> map = jedisClient.hgetall(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COINPAIR);;
        if (!map.containsKey(coinPair)) {
            return null;
        }
        BigDecimal price = new BigDecimal(0);
        Set<String> priceZset = jedisClient.zrange(JedisDataSourceSignleton.DB1, "tra:" + coinPair + ":" + type + "PriceZset", 0L, 0L);;
        if (priceZset != null && !priceZset.isEmpty()) {
            price = new BigDecimal(priceZset.iterator().next());
        }
        return price;
    }


    /**
     * 推送委托单
     *
     * @param coinPair 交易对
     * @param type     委托类型  买 /卖
     * @param price    委托价格
     * @param vol      委托数量
     */
    @Override
    public void wsBook(String coinPair, String type, BigDecimal price, BigDecimal vol) {
        String ws_to = webSocketProperties.getBroker() + "/" + coinPair + "@book";
        Map<String, ArrayList<String[]>> data = new HashMap<>();
        ArrayList<String[]> list = new ArrayList<String[]>();
        String key = String.format(RedisKeyConstant.TRA_COINPAIR_TRADE_KEY, coinPair, type, price.toPlainString());
        //统计一个价格的委托数量
        BigDecimal amout = BigDecimal.ZERO;
        Long length = jedisClient.llen(JedisDataSourceSignleton.DB1, key);
        List<String> entrustList = jedisClient.lrange(JedisDataSourceSignleton.DB1, key, 0L, length); 
        for (String str : entrustList) {
            TradeEntrust traEntrustTO = JSON.parseObject(str, TradeEntrust.class);
            amout = amout.add(traEntrustTO.getEntrustAmout());
        }
        if (TradeEntrust.ENTRUST_TYPE_BUY.equals(type)) {
            list.add(new String[]{price.toPlainString(), amout.toPlainString()});
            data.put(TradeEntrust.ENTRUST_TYPE_BUY, list);
            data.put(TradeEntrust.ENTRUST_TYPE_SELL, new ArrayList<String[]>());
        } else {
            list.add(new String[]{price.toPlainString(), amout.toPlainString()});
            data.put(TradeEntrust.ENTRUST_TYPE_SELL, list);
            data.put(TradeEntrust.ENTRUST_TYPE_BUY, new ArrayList<String[]>());
        }
        simpMessagingTemplate.convertAndSend(ws_to, new Response(JSON.toJSONString(data)));
    }

    @Override
	public void pushTradeInfo(String type, String coinPair, Long time, BigDecimal price, BigDecimal vol) {
    	String ws_to = webSocketProperties.getBroker() + "/" + coinPair + "@trade";
    	ArrayList<String[]> list = new ArrayList<String[]>();
    	list.add(new String[]{String.valueOf(time), price.toPlainString(), vol.toPlainString(), type});
    	simpMessagingTemplate.convertAndSend(ws_to, new Response(JSON.toJSONString(list)));
	}

    @Override
    public void wsTicker(TickerVo ticker) {
    	//最后两个账户余额不能放在redis里面，不然每个用户初次刷新都会出现在页面上，这样不对，账户余额只有对应当前账户才有效
        String[] redisData = new String[]{ticker.getCoinPair(), "", "", "", "", "", ticker.getRate().toPlainString(),
                ticker.getLast().toPlainString(), ticker.getVolume().toPlainString(), ticker.getHigh().toPlainString(),
                ticker.getLow().toPlainString(), "", "", ticker.getPreLast().toPlainString(), ticker.getDuihuan().toPlainString()};
        saveTickerToRedis(ticker.getCoinPair(), JSON.toJSONString(redisData));
    	
        // "["GTO_ETH","0.000749","18.00","0.000750","699.00","-0.000002","-0.0027 24h涨跌","0.000749 最新价","1130.11 24h 交易量","0.000869 24h最高","0.000735 24h最低","1459708.34","3.78"]"
    	String ws_to = webSocketProperties.getBroker() + "/" + "all@ticker";
        String[] data = new String[]{ticker.getCoinPair(), "", "", "", "", "", ticker.getRate().toPlainString(),
                ticker.getLast().toPlainString(), ticker.getVolume().setScale(2, BigDecimal.ROUND_DOWN).toPlainString(), ticker.getHigh().toPlainString(),
                ticker.getLow().toPlainString(), "", "", ticker.getPreLast().toPlainString(), ticker.getDuihuan().toPlainString()};
        simpMessagingTemplate.convertAndSend(ws_to, new Response(JSON.toJSONString(data)));
    }

    @Override
    public void wsKline(String coinPair, String time, KlineVo kline) {
        String ws_to = webSocketProperties.getBroker() + "/market/" + coinPair + "@kline_" + time;
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("close", kline.getClose());
        result.put("high", kline.getHigh());
        result.put("low", kline.getLow());
        result.put("open", kline.getOpen());
        result.put("volume", kline.getVolume());
        result.put("time", kline.getCreatedDate());
        simpMessagingTemplate.convertAndSend(ws_to, new Response(JSON.toJSONString(result)));
    }

    /**
     * 	保存ticker 到缓存
     *
     * @param coinPair
     * @param value
     */
    private void saveTickerToRedis(String coinPair, String value) {
        String key = String.format(RedisKeyConstant.EXCHANGE_TICKER, coinPair);
        jedisClient.set(JedisDataSourceSignleton.DB0, key, value);
    }
    
    /**
     * 计算24h 最高 最低 涨跌 成交量 并且推送
     *
     * @param symbol
     * 			交易对
     */
    @Override
    public void mathTicker(String symbol) {
        Jedis jedis = jedisClient.getJedis(JedisDataSourceSignleton.DB1);
        try {
            TickerVo tickerVo = new TickerVo();
            tickerVo.setCoinPair(symbol);
            
            String kline24HdataKey = String.format(RedisKeyConstant.KLINE_S_24DATA, symbol);
            
            // 24H最高价格
            String highPrice = jedis.hget(kline24HdataKey, "highPrice");
            
            // 24H最低价格
            String lowPrice = jedis.hget(kline24HdataKey, "lowPrice");
            
            // 当前最新价格
            String newPrice = jedis.hget(kline24HdataKey, "newPrice");
            String pre_newPrice = jedis.hget(kline24HdataKey, "pre_newPrice");
            
            // 交易总量
            String amout = jedis.hget(kline24HdataKey, "amout");
            
            // 开盘价格
            String openPriceKey = String.format(RedisKeyConstant.KLINE_S_OPENPRICE, symbol);
            String openPrice = jedis.get(openPriceKey);
            
            // 24小时涨跌
            BigDecimal rate = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN);
            if (!StringUtils.isEmpty(openPrice) && !StringUtils.isEmpty(newPrice)) {
            	BigDecimal bigDecimalNewPrice = new BigDecimal(newPrice);
            	BigDecimal bigDecimalOpenPrice = new BigDecimal(openPrice);
            	BigDecimal diffPrice = bigDecimalNewPrice.subtract(bigDecimalOpenPrice);
            	BigDecimal finalRate = diffPrice.divide(bigDecimalOpenPrice, 4, BigDecimal.ROUND_HALF_UP);
        		rate = finalRate.multiply(new BigDecimal(100)).setScale(2);
            }
            tickerVo.setRate(rate);
            
            // 交易币价值转换成CNY
            CoinUtil coinUtil = new CoinUtil();
            String originalType = symbol.split(CHS.underline.getValue())[1];
            if(!Objects.equals(CoinEnum.USDT.getCode(), originalType)) {
                CoinConvertResult convertResult = coinUtil.convetUSDT(BigDecimal.ONE, originalType);
                BigDecimal usdtValue = convertResult.getTargetCoinAmount().multiply(new BigDecimal(newPrice));
                tickerVo.setDuihuan(usdtValue.multiply(convertResult.getUsdtToCnyRate()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
            }else {
            	CoinConvertResult result = new CoinConvertResult();
            	coinUtil.findUsdtToCnyPrice(result);
            	tickerVo.setDuihuan(new BigDecimal(newPrice).multiply(result.getUsdtToCnyRate()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
            }
            
            // 交易总量
            tickerVo.setVolume(BigDecimal.ZERO);
            if (!StringUtils.isEmpty(amout)) {
                tickerVo.setVolume(new BigDecimal(amout));
            }
            // 最新价格
            tickerVo.setLast(BigDecimal.ZERO);
            if (!StringUtils.isEmpty(newPrice)) {
                tickerVo.setLast(new BigDecimal(newPrice));
            }
            // 
            tickerVo.setPreLast(BigDecimal.ZERO);
            if (!StringUtils.isEmpty(pre_newPrice)) {
                tickerVo.setPreLast(new BigDecimal(pre_newPrice));
            }
            // 24H最高价
            tickerVo.setHigh(BigDecimal.ZERO);
            if (!StringUtils.isEmpty(highPrice)) {
                tickerVo.setHigh(new BigDecimal(highPrice));
            }
            // 24H最低价
            tickerVo.setLow(BigDecimal.ZERO);
            if (!StringUtils.isEmpty(lowPrice)) {
                tickerVo.setLow(new BigDecimal(lowPrice));
            }
            wsTicker(tickerVo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	jedisClient.close(jedis); 
        }
    }
    
    @Override
    public void mathKline(String coinPair) {
        String key =  String.format(RedisKeyConstant.KLINE_TIME_ZONE_DATA, coinPair);
        String[] zoneKey = KlineTimeNodeConstant.convertToArray();
        for (String zk : zoneKey) {
            String hget = jedisClient.hget(JedisDataSourceSignleton.DB1, key, zk);
            // 推送数据
            wsKline(coinPair, zk, JSON.parseObject(hget, KlineVo.class));
        }
    }

	@Override
	public void pushUserCoinAccountBalance(String coinPair, String customerId, Map<String, BigDecimal> userCoinAccountBalance) {
		String ws_to = webSocketProperties.getBroker() + "/" + coinPair + "@user_" + customerId;
		simpMessagingTemplate.convertAndSend(ws_to, new Response(JSON.toJSONString(userCoinAccountBalance)));
	}
	
	@Override
    public void wsInitBook(String coinPair) {
        String ws_to = webSocketProperties.getBroker() + "/" + coinPair + "@book";
        Map<String, ArrayList<String[]>> data = new HashMap<>();
        ArrayList<String[]> buyResult = new ArrayList<>();
        ArrayList<String[]> sellResult = new ArrayList<>();
        
        tradeList(TradeEntrust.ENTRUST_TYPE_BUY, coinPair, buyResult);
        tradeList(TradeEntrust.ENTRUST_TYPE_SELL, coinPair, sellResult);
        
        data.put(TradeEntrust.ENTRUST_TYPE_BUY, buyResult);
        data.put(TradeEntrust.ENTRUST_TYPE_SELL, sellResult);
        simpMessagingTemplate.convertAndSend(ws_to, new Response(JSON.toJSONString(data)));
    }
    public void tradeList(String type, String coinPair, ArrayList<String[]> result) {
    	String priceZsetKey = String.format(RedisKeyConstant.TRA_PRICE_ZSET, coinPair, type);
        Set<String> priceZset = jedisClient.zrange(JedisDataSourceSignleton.DB1, priceZsetKey, 0, 50); 
        Iterator<String> data = priceZset.iterator();
        while (data.hasNext()) {
        	String[] resultVe = new String[2];
            String price = data.next();
            String key = String.format(RedisKeyConstant.TRA_COINPAIR_TRADE_KEY, coinPair, type, price);
            List<String> priceOrders = jedisClient.lrange(JedisDataSourceSignleton.DB1, key, 0, 10000);
            
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
    public void wsInitTrade(String coinPair) {
    	String ws_to = webSocketProperties.getBroker() + "/" + coinPair + "@trade";
        
        String klineOrderKey = String.format(RedisKeyConstant.KLINE_S_ORDER, coinPair);
        List<String> klineOrderList = jedisClient.lrange(JedisDataSourceSignleton.DB1, klineOrderKey, 0, -1);
        
        // 装进集合中然后反转
        List<TradeEntrustInfo> finalResult = new ArrayList<TradeEntrustInfo>();
        for(String empValue : klineOrderList) {
        	TradeEntrustInfo record = JSON.parseObject(empValue, TradeEntrustInfo.class);
        	finalResult.add(record);
        }
        finalResult.sort((TradeEntrustInfo pre, TradeEntrustInfo rear) -> pre.getEntrustTime().compareTo(rear.getEntrustTime()));
        
        ArrayList<String[]> result = new ArrayList<String[]>();
        for(TradeEntrustInfo record : finalResult) {
        	Long time = record.getEntrustTime().getTime();
        	String entrustPrice = record.getEntrustPrice().toPlainString();
        	String entrustAmout = record.getEntrustAmout().toPlainString();
        	String type = record.getType();
        	result.add(new String[]{String.valueOf(time), entrustPrice, entrustAmout, type});
        }
        simpMessagingTemplate.convertAndSend(ws_to, new Response(JSON.toJSONString(result)));
    }
    
    @Override
    public void wsInitTicker() {
    	String ws_to = webSocketProperties.getBroker() + "/" + "all@ticker";
        List<String> list = coinPairService.findCoinPairStr();
        for (String coinPair : list) {
        	String key = String.format(RedisKeyConstant.EXCHANGE_TICKER, coinPair);
            String value = jedisClient.get(JedisDataSourceSignleton.DB0, key);
            if (StringUtils.hasText(value)) {
            	//["BTC_USDT 【交易对】","","","","","","378290.00【24h涨跌】 ","3783.89990【 最新价】","47400.74875 【24h成交量】","3961.16010 【24h 最高价】","37.0 【24h 最低价】","","","3942.49990","26033.23"]
            	JSONArray jsonData = JSONArray.parseArray(value);
                jsonData.set(8, new BigDecimal(String.valueOf(jsonData.get(8))).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
                simpMessagingTemplate.convertAndSend(ws_to, new Response(jsonData.toString()));
            }
        }
    }

	@Override
	public void pushTradeEntrustIng(String symbol, String userId, String orderId) {
		String ws_to = webSocketProperties.getBroker() + "/" + symbol + "@entrustIng_" + userId;
	 	String userEntrustingKey =  String.format(RedisKeyConstant.USER_ENTRUSTING_S_S, symbol, userId);
	 	String entrusting = jedisClient.hget(JedisDataSourceSignleton.DB1, userEntrustingKey, orderId);
	 	TradeEntrust tradeEntrust = JSON.parseObject(entrusting, TradeEntrust.class);
	 	if(tradeEntrust != null) {
	 		PushEntrustIngDTO data = new PushEntrustIngDTO();
	 		data.setEntrustTime(tradeEntrust.getEntrustTime() / 1000);
	 		data.setEntrustPrice(tradeEntrust.getEntrustPrice());
	 		data.setEntrustAmout(tradeEntrust.getEntrustAmout());
	 		data.setEntrustAmoutSql(tradeEntrust.getEntrustAmoutSql());
	 		data.setTradeCoinCode(tradeEntrust.getTradeCoinCode());
	 		data.setPricingCoinCode(tradeEntrust.getPricingCoinCode());
	 		data.setOrderId(tradeEntrust.getOrderId());
	 		data.setEntrustType(tradeEntrust.getEntrustType());
	 		data.setCategory(tradeEntrust.getCategory());
	 		BigDecimal volRate = (tradeEntrust.getEntrustAmoutSql().subtract(tradeEntrust.getEntrustAmout())).divide((tradeEntrust.getEntrustAmoutSql()), 2, RoundingMode.HALF_UP);
			data.setRatio(volRate);// 成交比率
			data.setEntrustState(tradeEntrust.getEntrustState());
			data.setVolume(tradeEntrust.getEntrustAmoutSql().subtract(tradeEntrust.getEntrustAmout()));
			data.setTransactionAmount((tradeEntrust.getEntrustAmoutSql().subtract(tradeEntrust.getEntrustAmout())).multiply(tradeEntrust.getEntrustPrice()));
	 		simpMessagingTemplate.convertAndSend(ws_to, new Response(JSON.toJSONString(data)));
	 	}
	}
    
}
