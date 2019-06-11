package com.batsoft.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.batsoft.common.config.WebSocketSubscibeConfig;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.service.SocketBusService;
import com.batsoft.service.module.exchange.dao.EntrustInfoDao;
import com.batsoft.service.module.exchange.service.CoinPairService;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;
import com.batsoft.service.module.exchange.trade.model.TradeEntrustInfo;
import com.batsoft.socketjs.Response;

@Service(value = "socketBusService")
public class SocketBusServiceImpl implements SocketBusService {
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	@Resource
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @Resource
    private CoinPairService coinPairService;
    
    @Resource
    private EntrustInfoDao entrustInfoDao;
    
    @Override
	public void initKlineData(String symbol, String subject) {
		String ws_to = WebSocketSubscibeConfig.klineWs(symbol, subject);
		String klineKey = String.format(RedisKeyConstant.EXCHANGE_KLINE_DATA, symbol, subject);
		String lastValJson = jedisClient.lindex(JedisDataSourceSignleton.DB0, klineKey, 0);
		if(StringUtils.hasText(lastValJson)) {
			simpMessagingTemplate.convertAndSend(ws_to, new Response(lastValJson));
		}
	}
	
	@Override
    public void wsInitBook(String coinPair) {
        String ws_to = WebSocketSubscibeConfig.bookWs(coinPair);
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
        String ws_to = WebSocketSubscibeConfig.tradeWs(coinPair);
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
    public void wsInitTicker(String symbol) {
        String ws_to = WebSocketSubscibeConfig.tickerWs();
    	String key = String.format(RedisKeyConstant.EXCHANGE_TICKER, symbol);
        String value = jedisClient.get(JedisDataSourceSignleton.DB0, key); 
        if (StringUtils.hasText(value)) {
        	//["BTC_USDT 【交易对】","","","","","","378290.00【24h涨跌】 ","3783.89990【 最新价】","47400.74875 【24h成交量】","3961.16010 【24h 最高价】","37.0 【24h 最低价】","","","3942.49990","26033.23 【兑换成CNY的的价值】"]
        	JSONArray jsonData = JSONArray.parseArray(value);
            jsonData.set(8, new BigDecimal(String.valueOf(jsonData.get(8))).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
            simpMessagingTemplate.convertAndSend(ws_to, new Response(jsonData.toString()));
        }
    }

	@Override
	public void initUserCoin(String customerId, String symbol) {
		String ws_to = WebSocketSubscibeConfig.userCoinWs(symbol, customerId);
		// 推送账户货币余额
        String userAccountKey = String.format(RedisKeyConstant.USER_DCACCOUNT, customerId);
    	String userAccountValue = jedisClient.get(JedisDataSourceSignleton.DB1, userAccountKey);
        List<CustomerAccount> coinAccountList = new ArrayList<CustomerAccount>();
        if (!StringUtils.isEmpty(userAccountValue)) {
            coinAccountList = JSON.parseArray(userAccountValue, CustomerAccount.class);
        }
        
        // 定价币和交易币
        String tradeCoin = symbol.split(CHS.underline.getValue())[0];
        String pricingCoin = symbol.split(CHS.underline.getValue())[1]; 
        
        BigDecimal usablePricingCoin = BigDecimal.ZERO;
        BigDecimal usableTradeCoin = BigDecimal.ZERO;
        for (CustomerAccount coinAccount : coinAccountList) {
            if (tradeCoin.equals(coinAccount.getCoinCode())) {
                usableTradeCoin = coinAccount.getAvailable();
            } else if (pricingCoin.equals(coinAccount.getCoinCode())) {
                usablePricingCoin = coinAccount.getAvailable();
            }
        }
        // 推送账户的余额
    	Map<String, BigDecimal> userCoinAccountBalance = new HashMap<String, BigDecimal>();
        userCoinAccountBalance.put("lastNum", usableTradeCoin.setScale(5, BigDecimal.ROUND_HALF_UP));
        userCoinAccountBalance.put("lastMoney", usablePricingCoin.setScale(5, BigDecimal.ROUND_HALF_UP));
		simpMessagingTemplate.convertAndSend(ws_to, new Response(JSON.toJSONString(userCoinAccountBalance)));
	}
    
}
