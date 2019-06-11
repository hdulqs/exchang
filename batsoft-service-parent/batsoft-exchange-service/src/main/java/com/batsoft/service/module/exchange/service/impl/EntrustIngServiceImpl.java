/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:23:08
 */
package com.batsoft.service.module.exchange.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.cache.LogicLockSignleton;
import com.batsoft.core.common.PageFactory;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.core.common.enums.EntrustTradeTypeEnum;
import com.batsoft.core.common.utils.CoinPairConfigUtil;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.CoinPair;
import com.batsoft.model.module.exchange.CustomerAccountFreeze;
import com.batsoft.model.module.exchange.EntrustHistory;
import com.batsoft.model.module.exchange.EntrustIng;
import com.batsoft.service.module.exchange.dao.EntrustIngDao;
import com.batsoft.service.module.exchange.service.CoinPairService;
import com.batsoft.service.module.exchange.service.EntrustHistoryService;
import com.batsoft.service.module.exchange.service.EntrustIngService;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;
import com.batsoft.service.module.exchange.trade.util.RedisUserUtil;
import com.batsoft.service.module.member.service.UserUtils;
import com.github.pagehelper.Page;

import redis.clients.jedis.Jedis;

/**
 * <p> EntrustIngServiceImpl </p>
 *
 * @author: Bat Admin
 * @Date :  2018-04-14 10:23:08
 */
@Service("entrustIngService")
public class EntrustIngServiceImpl extends BaseServiceImpl<EntrustIng, String> implements EntrustIngService {

    @Autowired
    private EntrustIngDao entrustIngDao;

    @Autowired
    private CoinPairService coinPairService;
    
    @Autowired
    private EntrustHistoryService entrustHistoryService;
    
    private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
    
    private CoinPairConfigUtil decimalUtil = new CoinPairConfigUtil();
    
    private LogicLockSignleton logicLock = LogicLockSignleton.getInstance();
    
    @Override
    public void ininRedis() {
    	//查询所有交易对，对每个交易对进行缓存
        List<CoinPair> coinPairList = coinPairService.findAll();
        for (CoinPair dcCoinPair : coinPairList) {
        	String symbol = dcCoinPair.getTradeCoinCode() + CHS.underline.getValue() + dcCoinPair.getPricingCoinCode();
        	String lockKey = String.format(RedisKeyConstant.HANDLER_LOGIC_LOCK_KEY, symbol);
        	try {
        		// E:禁用该交易对进行交易
            	logicLock.setLogicLock(lockKey, Long.valueOf(10));
            	
            	Integer amtDecimal = decimalUtil.getCoinConfigField(symbol, CoinPairConfigUtil.AMT_DECIMAL);
            	Integer priceDecimal = decimalUtil.getCoinConfigField(symbol, CoinPairConfigUtil.PRICE_DECIMAL);
                
            	//删除key值
            	String baseKey = "tra:" + dcCoinPair.getTradeCoinCode() + CHS.underline.getValue() + dcCoinPair.getPricingCoinCode();
                Set<String> keys = jedisClient.keys(JedisDataSourceSignleton.DB1, baseKey + ":*");
                if (keys != null && !keys.isEmpty()) {
                    Iterator<String> it = keys.iterator();
                    while (it.hasNext()) {
                        jedisClient.del(JedisDataSourceSignleton.DB1, it.next());
                    }
                }
            	
                //查询买单
                QueryFilter buyQF = new QueryFilter(EntrustIng.class);
                buyQF.addFilter("trade_coin_code=", dcCoinPair.getTradeCoinCode());
                buyQF.addFilter("pricing_coin_code=", dcCoinPair.getPricingCoinCode());
                buyQF.addFilter("entrust_type=", EntrustTradeTypeEnum.BUY.getCode());
                List<EntrustIng> buyEntrust = find(buyQF);
                if (buyEntrust != null && !buyEntrust.isEmpty()) {
                    for (EntrustIng traEntrustIng : buyEntrust) {
                        TradeEntrust traEntrustTO = new TradeEntrust();
                        traEntrustTO.setCustomerId(traEntrustIng.getCustomerId());
                        traEntrustTO.setEntrustAmout(traEntrustIng.getEntrustAmout().setScale(amtDecimal, BigDecimal.ROUND_DOWN));
                        traEntrustTO.setEntrustAmoutSql(traEntrustIng.getEntrustAmoutSql().setScale(amtDecimal, BigDecimal.ROUND_DOWN));
                        traEntrustTO.setEntrustPrice(traEntrustIng.getEntrustPrice().setScale(priceDecimal, BigDecimal.ROUND_DOWN));
                        traEntrustTO.setPricingCoinCode(traEntrustIng.getPricingCoinCode());
                        traEntrustTO.setTradeCoinCode(traEntrustIng.getTradeCoinCode());
                        traEntrustTO.setOrderId(traEntrustIng.getOrderId());
                        traEntrustTO.setEntrustType(EntrustTradeTypeEnum.BUY.getCode());
                        
                        String entrustPrice = traEntrustIng.getEntrustPrice().setScale(priceDecimal, BigDecimal.ROUND_DOWN).toPlainString();
                        // 覆盖价格下的订单tra:%s:%s:%s =》 tra:BTC_USDT:buy:7550.55
                        String key = String.format(RedisKeyConstant.TRA_COINPAIR_TRADE_KEY, symbol, EntrustTradeTypeEnum.BUY.getCode(), entrustPrice);
                    	jedisClient.lpush(JedisDataSourceSignleton.DB1, key, JSON.toJSONString(traEntrustTO));
                        
                        String priceZsetKey = String.format(RedisKeyConstant.TRA_PRICE_ZSET, symbol, EntrustTradeTypeEnum.BUY.getCode());
                    	Double score = traEntrustIng.getEntrustPrice().setScale(priceDecimal, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(-1)).doubleValue();
                    	jedisClient.zadd(JedisDataSourceSignleton.DB1, priceZsetKey, score, entrustPrice);
                    }
                }

                //查询卖单
                QueryFilter sellQF = new QueryFilter(EntrustIng.class);
                sellQF.addFilter("trade_coin_code=", dcCoinPair.getTradeCoinCode());
                sellQF.addFilter("pricing_coin_code=", dcCoinPair.getPricingCoinCode());
                sellQF.addFilter("entrust_type=", EntrustTradeTypeEnum.SELL.getCode());
                List<EntrustIng> sellEntrust = find(sellQF);
                if (sellEntrust != null && !sellEntrust.isEmpty()) {
                    for (EntrustIng traEntrustIng : sellEntrust) {
                        TradeEntrust traEntrustTO = new TradeEntrust();
                        traEntrustTO.setCustomerId(traEntrustIng.getCustomerId());
                        traEntrustTO.setEntrustAmout(traEntrustIng.getEntrustAmout().setScale(amtDecimal, BigDecimal.ROUND_DOWN));
                        traEntrustTO.setEntrustAmoutSql(traEntrustIng.getEntrustAmoutSql().setScale(amtDecimal, BigDecimal.ROUND_DOWN));
                        traEntrustTO.setEntrustPrice(traEntrustIng.getEntrustPrice().setScale(priceDecimal, BigDecimal.ROUND_DOWN));
                        traEntrustTO.setPricingCoinCode(traEntrustIng.getPricingCoinCode());
                        traEntrustTO.setTradeCoinCode(traEntrustIng.getTradeCoinCode());
                        traEntrustTO.setOrderId(traEntrustIng.getOrderId());
                        traEntrustTO.setEntrustType(EntrustTradeTypeEnum.SELL.getCode());
                        
                        String entrustPrice = traEntrustIng.getEntrustPrice().setScale(priceDecimal, BigDecimal.ROUND_DOWN).toPlainString();
                        // 覆盖价格下的委托单tra:BTC_USDT:buy:3415.44000
                        String key = String.format(RedisKeyConstant.TRA_COINPAIR_TRADE_KEY, symbol, EntrustTradeTypeEnum.SELL.getCode(), entrustPrice);
                    	jedisClient.lpush(JedisDataSourceSignleton.DB1, key, JSON.toJSONString(traEntrustTO));
                        
                    	// traEntrustIng.getEntrustPrice().setScale(priceDecimal, BigDecimal.ROUND_DOWN).toString()
                        String priceZsetKey = String.format(RedisKeyConstant.TRA_PRICE_ZSET, symbol, EntrustTradeTypeEnum.SELL.getCode());
                    	Double score = traEntrustIng.getEntrustPrice().setScale(priceDecimal, BigDecimal.ROUND_DOWN).doubleValue();
                    	jedisClient.zadd(JedisDataSourceSignleton.DB1, priceZsetKey, score, entrustPrice);
                    }
                }
			} finally {
				logicLock.cencelLogicLock(lockKey);
			}
        }
    }
    

    @Override
    public void flushRedisEntrustByCustomerId(String userId) {
        Jedis jedis = jedisClient.getJedis(JedisDataSourceSignleton.DB1);
        try {
            Map<String, String> coinPairs = jedis.hgetAll(RedisKeyConstant.TRA_COINPAIR);
            if (coinPairs != null && !coinPairs.isEmpty()) {
                Set<String> keySet = coinPairs.keySet();
                Iterator<String> it = keySet.iterator();
                while (it.hasNext()) {
                    String coinPair = it.next();
                    String[] split = coinPair.split("_");
                    String key = String.format(RedisKeyConstant.USER_ENTRUSTING_S_S, coinPair, userId);
                    Long hlength = jedisClient.hlen(JedisDataSourceSignleton.DB1, key);
                    if (hlength == null || hlength == 0) {
                        QueryFilter entrustingFilter = new QueryFilter(EntrustIng.class);
                        entrustingFilter.addFilter("customer_id=", userId);
                        entrustingFilter.addFilter("trade_coin_code=", split[0]);
                        entrustingFilter.addFilter("pricing_coin_code=", split[1]);
                        List<EntrustIng> entrustIngList = find(entrustingFilter);
                        if (entrustIngList != null && !entrustIngList.isEmpty()) {
                            for (EntrustIng entrustIng : entrustIngList) {
                                //交易币位数
                                Integer tradeCodeLength = RedisUserUtil.getAmountDecimal(entrustIng.getTradeCoinCode() + "_" + entrustIng.getPricingCoinCode());
                                
                                //定价币位数
                                Integer pricingCodeLength = RedisUserUtil.getPriceDecimal(entrustIng.getTradeCoinCode() + "_" + entrustIng.getPricingCoinCode());
                                
                                TradeEntrust tradeEntrust = new TradeEntrust();
                                tradeEntrust.setCategory(String.valueOf(entrustIng.getCategory()));
                                tradeEntrust.setEntrustState(entrustIng.getEntrustState());
                                tradeEntrust.setOrderId(entrustIng.getOrderId());
                                tradeEntrust.setEntrustTime(entrustIng.getEntrustTime().getTime());
                                tradeEntrust.setEntrustAmout(entrustIng.getEntrustAmout().setScale(tradeCodeLength, BigDecimal.ROUND_DOWN));
                                tradeEntrust.setEntrustAmoutSql(entrustIng.getEntrustAmoutSql().setScale(tradeCodeLength, BigDecimal.ROUND_DOWN));
                                tradeEntrust.setEntrustPrice(entrustIng.getEntrustPrice().setScale(pricingCodeLength, BigDecimal.ROUND_DOWN));
                                tradeEntrust.setEntrustType(entrustIng.getEntrustType());
                                tradeEntrust.setCustomerId(entrustIng.getCustomerId());
                                tradeEntrust.setPricingCoinCode(entrustIng.getPricingCoinCode());
                                tradeEntrust.setTradeCoinCode(entrustIng.getTradeCoinCode());
                                
                    			// 设置委托中的数据到Redis
                                jedisClient.hset(JedisDataSourceSignleton.DB1, key, tradeEntrust.getOrderId(), JSON.toJSONString(tradeEntrust));
                            }
                        }
                    }
                    
                    String historykey = String.format(RedisKeyConstant.USER_ENTRUSTHISTORY, coinPair, userId);
                    List<String> historylrange = jedis.lrange(historykey, 0, 1);
                    if (historylrange == null || historylrange.isEmpty()) {
                        QueryFilter entrustHistoryFilter = new QueryFilter(EntrustHistory.class);
                        entrustHistoryFilter.addFilter("customer_id=", userId);
                        entrustHistoryFilter.addFilter("trade_coin_code=",split[0]);
                        entrustHistoryFilter.addFilter("pricing_coin_code=",split[1]);
                        List<EntrustHistory> entrustHistoryList = entrustHistoryService.find(entrustHistoryFilter);
                        if (entrustHistoryList != null && !entrustHistoryList.isEmpty()) {
                            for (EntrustHistory entrustHistory : entrustHistoryList) {
                                //交易币位数
                                Integer tradeCodeLength = RedisUserUtil.getAmountDecimal(entrustHistory.getTradeCoinCode() + "_" + entrustHistory.getPricingCoinCode());
                                
                                //定价币位数
                                Integer pricingCodeLength = RedisUserUtil.getPriceDecimal(entrustHistory.getTradeCoinCode() + "_" + entrustHistory.getPricingCoinCode());
                                
                                TradeEntrust tradeEntrust = new TradeEntrust();
                                tradeEntrust.setCategory(String.valueOf(entrustHistory.getCategory()));
                                tradeEntrust.setCustomerId(entrustHistory.getCustomerId());
                                tradeEntrust.setEntrustAmout(entrustHistory.getEntrustAmout().setScale(tradeCodeLength, BigDecimal.ROUND_DOWN));
                                tradeEntrust.setEntrustAmoutSql(entrustHistory.getEntrustAmoutSql().setScale(tradeCodeLength, BigDecimal.ROUND_DOWN));
                                tradeEntrust.setEntrustPrice(entrustHistory.getEntrustPrice().setScale(pricingCodeLength, BigDecimal.ROUND_DOWN));
                                tradeEntrust.setEntrustState(entrustHistory.getEntrustState());
                                tradeEntrust.setEntrustTime(entrustHistory.getEntrustTime().getTime());
                                tradeEntrust.setEntrustType(entrustHistory.getEntrustType());
                                tradeEntrust.setOrderId(entrustHistory.getOrderId());
                                tradeEntrust.setPricingCoinCode(entrustHistory.getPricingCoinCode());
                                tradeEntrust.setTradeCoinCode(entrustHistory.getTradeCoinCode());
                                jedis.lpush(historykey, JSON.toJSONString(tradeEntrust));
                            }
                        }
                    }
                }
            }
        } finally {
            jedisClient.close(jedis);
        }
    }

    @Override
    public PageResult findPageBySql(HttpServletRequest request) {
        //封装必要参数
        QueryFilter filter = new QueryFilter(CustomerAccountFreeze.class, request);
        //分页插件
        Page<CustomerAccountFreeze> page = PageFactory.getPage(filter);

        Map<String, Object> map = new HashMap<String, Object>();
        //查询方法
        entrustIngDao.findPageBySql(map);

        return new PageResult(page, filter);
    }

    @Override
    public BigDecimal sumOfBuyAmoutByStartTimeAndId(String id, Date create_time,String coinCode,String priceCode) {
        Map<String,Object> mapMap = new HashMap<>();
        mapMap.put("id",id);
        mapMap.put("create_time",create_time);
        mapMap.put("coinCode",coinCode);
        mapMap.put("priceCode",priceCode);
        mapMap.put("type","buy");
        return entrustIngDao.sumOfAmoutByStartTimeAndId(mapMap);
    }

    @Override
    public BigDecimal sumOfSellAmoutByStartTimeAndId(String id, Date create_time,String coinCode,String priceCode) {
        Map<String,Object> mapMap = new HashMap<>();
        mapMap.put("id",id);
        mapMap.put("create_time",create_time);
        mapMap.put("coinCode",coinCode);
        mapMap.put("priceCode",priceCode);
        mapMap.put("type","sell");
        return entrustIngDao.sumOfAmoutByStartTimeAndId(mapMap);
    }

    /**
     * 根据用户id查询分页
     *
     * @param request
     * @return
     */
    @Override
    public PageResult findPageByUserId(HttpServletRequest request) {
        //封装必要参数
        QueryFilter filter = new QueryFilter(CustomerAccountFreeze.class, request);
        //分页插件
        Page<CustomerAccountFreeze> page = PageFactory.getPage(filter);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", UserUtils.getUser().getId());

        //查询方法
        entrustIngDao.findPageByUserId(map);

        return new PageResult(page, filter);
    }

    /**
     * 查询用户所有订单
     */
    @Override
    public List<EntrustIng> findByUserId() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", UserUtils.getUser().getId());
        //查询方法
        return entrustIngDao.findPageByUserId(map);
    }


	@Override
	public List<String> findCoinPairGroupByUserId(String userId) {
		return entrustIngDao.findCoinPairGroupByUserId(userId);
	}
}
