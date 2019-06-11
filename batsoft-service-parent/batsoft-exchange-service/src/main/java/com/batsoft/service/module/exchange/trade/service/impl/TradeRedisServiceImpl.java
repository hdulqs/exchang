package com.batsoft.service.module.exchange.trade.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.cache.LogicLockSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.SystemClock;
import com.batsoft.core.common.coin.CoinUtil;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.core.common.enums.KeyEnum;
import com.batsoft.core.common.utils.CoinPairConfigUtil;
import com.batsoft.core.transaction.RedisDistributedLock;
import com.batsoft.core.transaction.RedisDistributedLock.RedisDistributedKeyEnum;
import com.batsoft.model.module.exchange.CustomerAccountFreeze;
import com.batsoft.model.module.exchange.CustomerAccountRecord;
import com.batsoft.model.module.exchange.EntrustHistory;
import com.batsoft.model.module.exchange.EntrustInfo;
import com.batsoft.model.module.exchange.EntrustIng;
import com.batsoft.mq.RabbitMqSender;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;
import com.batsoft.service.module.exchange.trade.model.TradeEntrustInfo;
import com.batsoft.service.module.exchange.trade.service.TradeAccountService;
import com.batsoft.service.module.exchange.trade.service.TradeService;
import com.batsoft.service.module.exchange.trade.util.KlineUtil;
import com.batsoft.service.module.exchange.trade.util.RedisUserUtil;
import com.batsoft.service.module.exchange.trade.util.SqlUtil;
import com.batsoft.service.module.exchange.trade.util.ThreadPool;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.gson.GsonSingleton;

import lombok.extern.slf4j.Slf4j;

/**
 * 撮合交易
 * 
 * @author simon
 */
@Slf4j
@Service("tradeRedisService")
public class TradeRedisServiceImpl implements TradeService {
	
    private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
    
    private CoinPairConfigUtil decimalUtil = new CoinPairConfigUtil();
    
    private GsonSingleton gsonClient = GsonSingleton.getInstance();
	
	@Resource
	private CustomerAccountService customerAccountService;

    @Autowired
    private RabbitMqSender rabbitMqSender;
    
    @Resource
    private TradeAccountService tradeAccountService;
    
    @Resource
    private JdbcTemplate jdbcTemplate;
   
    @Override
    public void addEntrust(TradeEntrust tradeEntrust) {
        StringBuilder statement = new StringBuilder();
        List<TradeEntrustInfo> orderlist = new ArrayList<TradeEntrustInfo>();
        String tableDate = DateUtils.dateFormatToString(new Date(),DateUtils.TABLES_DAY_FIX);
        String coinPair = tradeEntrust.getTradeCoinCode() + CHS.underline.getValue() + tradeEntrust.getPricingCoinCode();
        // 转换价格位数
		int priceDecimal = decimalUtil.getCoinConfigField(coinPair, CoinPairConfigUtil.PRICE_DECIMAL);
		tradeEntrust.setEntrustPrice(tradeEntrust.getEntrustPrice().setScale(priceDecimal, BigDecimal.ROUND_DOWN));
		
		// 转换数量位数
		int amtDecimal = decimalUtil.getCoinConfigField(coinPair, CoinPairConfigUtil.AMT_DECIMAL);
		tradeEntrust.setEntrustAmout(tradeEntrust.getEntrustAmout().setScale(amtDecimal, BigDecimal.ROUND_DOWN));
        
        RedisDistributedLock lock = new RedisDistributedLock();
        String coinLockKey = String.format(RedisDistributedKeyEnum.ENTRUST_TRADE_COIN_PAIR_KEY.getValue(), coinPair);
        String personLockKey = String.format(RedisDistributedKeyEnum.ENTRUST_TRADE_PERSION_KEY.getValue(), tradeEntrust.getCustomerId());
        try {
        	if(RedisUserUtil.validateTradeEntrust(tradeEntrust)){
            	// log.error("原因=【非法下单】" + "当前用户=【" + tradeEntrust.getCustomerId() + "】委托数量=【" + tradeEntrust.getEntrustAmout() + "】");
                return;
            }
            // TO.给委托交易流程锁交易对 // TO.给匹配交易流程锁委托人
            lock.lock(coinLockKey, tradeEntrust.getOrderId());
            lock.lock(personLockKey, tradeEntrust.getOrderId());
            lock.setRunTime(SystemClock.millisClock().now());
            tradeEntrust.setEntrustTime(System.currentTimeMillis());
			long stime = SystemClock.millisClock().now();
            if (RedisUserUtil.validateAccount(tradeEntrust)) {
            	log.error("原因=【账户余额不足】" + "当前用户=【" + tradeEntrust.getCustomerId() + "】委托数量=【" + tradeEntrust.getEntrustAmout() + "】");
                return;
            }
            // log.info("addEntrust---验证账户余额时间【" + (SystemClock.millisClock().now() - stime) + "】毫秒");
            
            // 交易匹配
            matching(coinPair, tradeEntrust, orderlist);
            // log.info("addEntrust---匹配完成时间【" + (SystemClock.millisClock().now() - stime) + "】毫秒");
            
            // 没有匹配完成进入委托单列表
            pushUnsettledTradeEntrustRedis(coinPair, tradeEntrust);
            // log.info("addEntrust---保存公共委托单时间【" + (SystemClock.millisClock().now() - stime) + "】毫秒");
            
            // 当前委托单同步到个人委托单中
            saveRedisUserEntrust(tradeEntrust);
            // log.info("addEntrust---同步委托单到个人委托单【" + (SystemClock.millisClock().now() - stime) + "】毫秒");
            
            // 记账同步到账户
            updateRedisUserAccount(tradeEntrust, orderlist);
            // log.info("addEntrust---记账同步redis账户时间【" + (SystemClock.millisClock().now() - stime) + "】毫秒");

            // 成交信息修改对手单
            statement.append(updateRedisUserEntrust(orderlist, tableDate));
            // log.info("addEntrust---修改对手个人委托单【" + (SystemClock.millisClock().now() - stime) + "】毫秒");
            
            // end:生成账户数据SQL
            statement.append(createAccountSql(tradeEntrust, orderlist, tableDate));
            
            // end:生成委托信息SQL
            statement.append(createEntrustSql(tradeEntrust, orderlist, tableDate));
            
            // end:将SQL发送到MQ处理，这个步骤必须放在生成SQL之后
        	if(statement != null && statement.length() > 0) {
                jdbcTemplate.batchUpdate(String.valueOf(statement).split(CHS.semicolon.getValue()));
            }
        } finally {
            // To.委托交易流程解锁
        	lock.unLock(personLockKey, tradeEntrust.getOrderId(), 0);
            lock.unLock(coinLockKey, tradeEntrust.getOrderId(), 0);
            // log.info("委托交易流程结束最终时间【" + (SystemClock.millisClock().now() - lock.getRunTime()) + "】毫秒");
            
            // 异步推送
            ThreadPool.exe(new Runnable() {
				@Override
				public void run() {
					// 成交委托单设置到KlineOrder
		        	updateEntrustOrderToKlineOrder(orderlist, coinPair);
		        	
		        	// 高开低收和24小时数据
		            KlineUtil.calculateTimeZoneData(tradeEntrust, orderlist);
		            KlineUtil.data24hour(orderlist);
		        	
		        	// 订阅模式推送成交记录到前端交易大厅
		            Map<String, String> entrustOrderToFrontMq = new HashMap<String, String>();
		            entrustOrderToFrontMq.put(KeyEnum.coinPair.name(), coinPair);
		            entrustOrderToFrontMq.put(KeyEnum.tradeEntrust.name(), gsonClient.toJson(tradeEntrust));
		            entrustOrderToFrontMq.put(KeyEnum.orderlist.name(), gsonClient.toJson(orderlist));
		            rabbitMqSender.entrustOrderToFront(gsonClient.toJson(entrustOrderToFrontMq));
				}
			});
        }
    }
    
    /**
     * 	交易匹配
     * 
     * @param coinPair
     * 			    交易对
     * @param tradeEntrust
     * 			    委托交易
     * @param result
     * 			    匹配订单容器
     */
    private void matching(String coinPair, TradeEntrust tradeEntrust, List<TradeEntrustInfo> result) {
        String type = new String(); // 反转类型，取对手单
        if (TradeEntrust.ENTRUST_TYPE_BUY.equals(tradeEntrust.getEntrustType())) {
            type = TradeEntrust.ENTRUST_TYPE_SELL;
        } else {
            type = TradeEntrust.ENTRUST_TYPE_BUY;
        }
        
        // 查询价格集合长度 // 查出价格集合中的所有价格KEY
        String priceZsetKey = String.format(RedisKeyConstant.TRA_PRICE_ZSET, coinPair, type);
        Set<String> priceZSet = jedisClient.zrange(JedisDataSourceSignleton.DB1, priceZsetKey, 0, -1);
        if (priceZSet == null || priceZSet.size() == 0) {
        	return;
        }
        // 进入循环,遍历价格
        Iterator<String> it = priceZSet.iterator();
        ident:
        while (it.hasNext()) {
            //ZSet中的价格Key
            String currentPrice = it.next();
            
            // 买方价格委托单
            if (TradeEntrust.ENTRUST_TYPE_BUY.equals(tradeEntrust.getEntrustType())) {
                // 买入价大于等于卖出价
                if (tradeEntrust.getEntrustPrice().compareTo(new BigDecimal(currentPrice)) >= 0) {
                    // 查询价格委托单
                    String priceKey = String.format(RedisKeyConstant.TRA_COINPAIR_TRADE_KEY, coinPair, type, currentPrice);
                    long llen = jedisClient.llen(JedisDataSourceSignleton.DB1, priceKey);
                    
                    //list剩余长度   复制llen
                    Long copyLength = llen;
                    for (int index = 0; index < llen; index++) {
                    	
                    	// 根据时间最早的方式取出卖方委托单
                        String sell = jedisClient.lindex(JedisDataSourceSignleton.DB1, priceKey, copyLength - 1);
                        TradeEntrust redisSellEntrust = gsonClient.fromJson(sell, TradeEntrust.class);
                        
                		// 如果当前买入量小于Redis订单量,修改Redis订单信息,Redis订单标记部分成交
                        if (tradeEntrust.getEntrustAmout().compareTo(redisSellEntrust.getEntrustAmout()) < 0) {
                            // 创建成交单信息,此处订单成交数量为下单的剩余数量
                            result.add(createTraEntrustInfo(type, tradeEntrust, redisSellEntrust, redisSellEntrust.getEntrustPrice(), tradeEntrust.getEntrustAmout()));

                            // 设置当前匹配订单的订单数量
                            redisSellEntrust.setEntrustAmout(redisSellEntrust.getEntrustAmout().subtract(tradeEntrust.getEntrustAmout()));

                            // 修改当前匹配订单的订单数量,数量回填到Redis当中
                            jedisClient.lset(JedisDataSourceSignleton.DB1, priceKey, (copyLength - 1), JSON.toJSONString(redisSellEntrust));
                            
                            // 设置剩下的订单数据
                            tradeEntrust.setEntrustAmout(BigDecimal.ZERO);
                        } else { 
                        	// 如果买入量大于当前订单量,先吃掉当前订单最先进入委托的订单
                            jedisClient.rpop(JedisDataSourceSignleton.DB1, priceKey);
                            copyLength--;
                            
                            // 创建成交单信息
                            result.add(createTraEntrustInfo(type, tradeEntrust, redisSellEntrust, redisSellEntrust.getEntrustPrice(), redisSellEntrust.getEntrustAmout()));
                            
                            // 设置剩下的订单数据
                            tradeEntrust.setEntrustAmout(tradeEntrust.getEntrustAmout().subtract(redisSellEntrust.getEntrustAmout()));
                            
                            // 该价格下的订单全部被吃完，删除掉PriceZset
                            if (copyLength == 0 || copyLength < 0) {
                                jedisClient.zrem(JedisDataSourceSignleton.DB1, priceZsetKey, currentPrice);
                            }
                        }
                        
                        // 委托完成
                        if (BigDecimal.ZERO.compareTo(tradeEntrust.getEntrustAmout()) >= 0) {
                            break ident;
                        }
                    }
                }
            }else if (TradeEntrust.ENTRUST_TYPE_SELL.equals(tradeEntrust.getEntrustType())) {
                if (tradeEntrust.getEntrustPrice().compareTo(new BigDecimal(currentPrice)) <= 0) {
                    // 查询价格的key值
                    String priceKey = String.format(RedisKeyConstant.TRA_COINPAIR_TRADE_KEY, coinPair, type, currentPrice);
                    Long llen = jedisClient.llen(JedisDataSourceSignleton.DB1, priceKey); 
                    
                    //list剩余长度   复制llen
                    Long copyLength = llen;
                    for (int index = 0; index < llen; index++) {
                        
                        //根据时间最早的方式取出买方委托单
                        String buy = jedisClient.lindex(JedisDataSourceSignleton.DB1, priceKey, (copyLength - 1));
                        TradeEntrust redisBuyEntrust = gsonClient.fromJson(buy, TradeEntrust.class);
                        
                        // 如果买入量小于当前订单量,修改当前订单信息,标记部分成交
                        if (tradeEntrust.getEntrustAmout().compareTo(redisBuyEntrust.getEntrustAmout()) < 0) {
                            // 创建成交单信息,此处订单成交数量为下单的剩余数量
                            result.add(createTraEntrustInfo(type, redisBuyEntrust, tradeEntrust, redisBuyEntrust.getEntrustPrice(), tradeEntrust.getEntrustAmout()));

                            // 设置当前匹配订单的订单数量
                            redisBuyEntrust.setEntrustAmout(redisBuyEntrust.getEntrustAmout().subtract(tradeEntrust.getEntrustAmout()));
                            
                            // 修改当前匹配订单的订单数量,数量回填到Redis
                            jedisClient.lset(JedisDataSourceSignleton.DB1, priceKey, (copyLength - 1), JSON.toJSONString(redisBuyEntrust));
                            
                            // 设置剩下的订单数据
                            tradeEntrust.setEntrustAmout(BigDecimal.ZERO);
                        } else {
                        	// 如果买入量大于当前订单量,先吃掉当前订单，生成成交单信息，继续查询下一个订单
                            jedisClient.rpop(JedisDataSourceSignleton.DB1, priceKey);
                            copyLength--;
                            
                            // 创建成交单信息
                            result.add(createTraEntrustInfo(type, redisBuyEntrust, tradeEntrust, redisBuyEntrust.getEntrustPrice(), redisBuyEntrust.getEntrustAmout()));
                            
                            // 设置剩下的订单数据
                            tradeEntrust.setEntrustAmout(tradeEntrust.getEntrustAmout().subtract(redisBuyEntrust.getEntrustAmout()));
                            
                            // 该价格下的订单全部被吃完，删除掉PriceZset
                            if (copyLength == 0 || copyLength < 0) {
                                jedisClient.zrem(JedisDataSourceSignleton.DB1, priceZsetKey, currentPrice);
                            }
                        }
                        
                        // 委托完成
                        if (BigDecimal.ZERO.compareTo(tradeEntrust.getEntrustAmout()) >= 0) {
                            break ident;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 将未匹配成功的委托设置到Redis中
     * 
     */
    private void pushUnsettledTradeEntrustRedis(String coinPair, TradeEntrust tradeEntrust) {
    	if (tradeEntrust.getEntrustAmout().compareTo(BigDecimal.ZERO) > 0) {
	        // 未成交存入委托单队列
	        String key = String.format(RedisKeyConstant.TRA_COINPAIR_TRADE_KEY, coinPair, tradeEntrust.getEntrustType(), tradeEntrust.getEntrustPrice().toPlainString());
	        jedisClient.lpush(JedisDataSourceSignleton.DB1, key, JSON.toJSONString(tradeEntrust));
	        
	        // 委托单价存入价格集合中
	        String priceKey = String.format(RedisKeyConstant.TRA_PRICE_ZSET, coinPair, tradeEntrust.getEntrustType());
	        if (Objects.equals(TradeEntrust.ENTRUST_TYPE_SELL, tradeEntrust.getEntrustType())) {
	        	jedisClient.zadd(JedisDataSourceSignleton.DB1, priceKey, tradeEntrust.getEntrustPrice().doubleValue(), tradeEntrust.getEntrustPrice().toPlainString());
	        } else if (Objects.equals(TradeEntrust.ENTRUST_TYPE_BUY, tradeEntrust.getEntrustType())) {
	            // 如果是买单，价格队列按负分排序
	            jedisClient.zadd(JedisDataSourceSignleton.DB1, priceKey, tradeEntrust.getEntrustPrice().abs().negate().doubleValue(), tradeEntrust.getEntrustPrice().toPlainString());
	        }
    	}
    }
    
    /**
     *	 订单同步到用户个人的当前委托数据，和历史委托数据中
     *
     * @param traEntrustTO
     */
    private void saveRedisUserEntrust(TradeEntrust traEntrustTO) {
    	String coinPair = traEntrustTO.getTradeCoinCode() + CHS.underline.getValue() + traEntrustTO.getPricingCoinCode();
        //剩余数量大于0，委托单进入当前委托中
        if (traEntrustTO.getEntrustAmout().compareTo(BigDecimal.ZERO) > 0) {
            String entrusting = String.format(RedisKeyConstant.USER_ENTRUSTING, traEntrustTO.getTradeCoinCode(), traEntrustTO.getPricingCoinCode(), traEntrustTO.getCustomerId());
            jedisClient.hset(JedisDataSourceSignleton.DB1, entrusting, traEntrustTO.getOrderId(), JSON.toJSONString(traEntrustTO));
        } else {
        	//剩余数量==0，委托单进入历史委托中
            String entrusthistoryKey = String.format(RedisKeyConstant.USER_ENTRUSTHISTORY, coinPair, traEntrustTO.getCustomerId());
            long llen = jedisClient.llen(JedisDataSourceSignleton.DB1, entrusthistoryKey);
            if (llen >= 100) {
                jedisClient.rpop(JedisDataSourceSignleton.DB1, entrusthistoryKey);
            }
            jedisClient.lpush(JedisDataSourceSignleton.DB1, entrusthistoryKey, JSON.toJSONString(traEntrustTO));
        }
    }
    
    /**
     * 更新用户redis中的账户数据
     *
     * @param orderlist
     */
    private void updateRedisUserAccount(TradeEntrust tradeEntrust, List<TradeEntrustInfo> orderlist) {
        //交易币成交总数量
        BigDecimal tradeCoinTotal = BigDecimal.ZERO;
        //定价币成交总额
        BigDecimal pricingCoinTotal = BigDecimal.ZERO;
        //总买手续费
        BigDecimal buyFeeTotal = BigDecimal.ZERO;
        //总卖手续费
        BigDecimal sellFeeTotal = BigDecimal.ZERO;
        /**
         *  map结构如下
         *           userId_BTC-----map
         *                   available---BigDecimal
         *                   freeze ---- BigDecimal
         *           userId_ETH-----map
         *                   available---BigDecimal
         *                   freeze ---- BigDecimal
         */
        Map<String, Map<String, BigDecimal>> userMap = new ConcurrentHashMap<String, Map<String, BigDecimal>>();

        //统计参与人id
        List<String> customerList = new ArrayList<String>();
        customerList.add(tradeEntrust.getCustomerId());
        for (TradeEntrustInfo info : orderlist) {
            customerList.add(info.getBuyCustomerId());
            customerList.add(info.getSellCustomerId());
        }

        //初始化参与人账户盒子
        Iterator<String> it = customerList.iterator();
        while (it.hasNext()) {
            String userId = it.next();
            ConcurrentHashMap<String, BigDecimal> tradeCoin = new ConcurrentHashMap<>();
            tradeCoin.put("available", BigDecimal.ZERO);
            tradeCoin.put("freeze", BigDecimal.ZERO);
            ConcurrentHashMap<String, BigDecimal> pricingCoin = new ConcurrentHashMap<>();
            pricingCoin.put("available", BigDecimal.ZERO);
            pricingCoin.put("freeze", BigDecimal.ZERO);

            userMap.put(userId + CHS.underline.getValue() + tradeEntrust.getTradeCoinCode(), tradeCoin);
            userMap.put(userId + CHS.underline.getValue() + tradeEntrust.getPricingCoinCode(), pricingCoin);
        }
        
        //如果有成交信息
        if (orderlist != null && orderlist.size() > 0) {
            //遍历成交信息
            for (TradeEntrustInfo info : orderlist) {
                // 成交额
                BigDecimal volume = info.getEntrustPrice().multiply(info.getEntrustAmout());
                
                // 成交总数
                tradeCoinTotal = tradeCoinTotal.add(info.getEntrustAmout());
                
                // 成交总额【定价币成交总额=交易币数量 * 交易币对定价币的价格】
                pricingCoinTotal = pricingCoinTotal.add(volume);
                
                // 买方总手续费
                buyFeeTotal = buyFeeTotal.add(info.getBuyFee());
                
                // 卖方总手续费
                sellFeeTotal = sellFeeTotal.add(info.getSellFee());
                
                // 此时主动委托单的类型是卖单，匹配到的对手单都为买单
                if (TradeEntrust.ENTRUST_TYPE_BUY.equals(info.getType())) {
                	//定价币冻结减少
                    Map<String, BigDecimal> pricingCoin = userMap.get(info.getBuyCustomerId() + CHS.underline.getValue() + tradeEntrust.getPricingCoinCode());
                    pricingCoin.put("freeze", pricingCoin.get("freeze").subtract(volume));
                    
                    //交易币可用增加
                    Map<String, BigDecimal> tradeCoin = userMap.get(info.getBuyCustomerId() + CHS.underline.getValue() + tradeEntrust.getTradeCoinCode());
                    tradeCoin.put("available", tradeCoin.get("available").add(info.getEntrustAmout()).subtract(info.getBuyFee()));
              } else {
            	  // 此时主动委托单的类型是买单，匹配到的对手单都为卖单
                  // 卖家交易币冻结减少
                  Map<String, BigDecimal> tradeCoin = userMap.get(info.getSellCustomerId() + CHS.underline.getValue() + tradeEntrust.getTradeCoinCode());
                  tradeCoin.put("freeze", tradeCoin.get("freeze").subtract(info.getEntrustAmout()));
                  
                  // 卖家定价币可用增加
                  Map<String, BigDecimal> pricingCoin = userMap.get(info.getSellCustomerId() + CHS.underline.getValue() + tradeEntrust.getPricingCoinCode());
                  pricingCoin.put("available", pricingCoin.get("available").add(volume).subtract(info.getSellFee()));
                }
            }
        }

        //  把未成交订单 设置为挂单 并设置冻结金额
        if (TradeEntrust.ENTRUST_TYPE_BUY.equals(tradeEntrust.getEntrustType())) {//买单，支出定价币
            //定价币可用减少==【已交易总额+（未成交总数*委托价）】
        	BigDecimal availableSubtract = pricingCoinTotal.add(tradeEntrust.getEntrustAmout().multiply(tradeEntrust.getEntrustPrice()));
            Map<String, BigDecimal> pricingCoin = userMap.get(tradeEntrust.getCustomerId() + CHS.underline.getValue() + tradeEntrust.getPricingCoinCode());
            pricingCoin.put("available", pricingCoin.get("available").subtract(availableSubtract));
            
            //定价币冻结增加==【未成交总数*委托单价】
            BigDecimal freezeAdd = tradeEntrust.getEntrustAmout().multiply(tradeEntrust.getEntrustPrice());
            pricingCoin.put("freeze", pricingCoin.get("freeze").add(freezeAdd));

            //交易币可用增加==已成交总数
            Map<String, BigDecimal> tradeCoin = userMap.get(tradeEntrust.getCustomerId() + CHS.underline.getValue() + tradeEntrust.getTradeCoinCode());
            tradeCoin.put("available", tradeCoin.get("available").add(tradeCoinTotal).subtract(buyFeeTotal));
        } else {//卖单, 支出交易币
            //交易币可用减少==下单总数
            Map<String, BigDecimal> tradeCoin = userMap.get(tradeEntrust.getCustomerId() + CHS.underline.getValue() + tradeEntrust.getTradeCoinCode());
            tradeCoin.put("available", tradeCoin.get("available").subtract(tradeEntrust.getEntrustAmoutSql()));
            
            //交易币冻结增加==未成交总数
            BigDecimal freezeAmount = tradeEntrust.getEntrustAmout();
            tradeCoin.put("freeze", tradeCoin.get("freeze").add(freezeAmount));

            //定价币可用增加==成交总额
            Map<String, BigDecimal> pricingCoin = userMap.get(tradeEntrust.getCustomerId() + CHS.underline.getValue() + tradeEntrust.getPricingCoinCode());
            pricingCoin.put("available", pricingCoin.get("available").add(pricingCoinTotal).subtract(sellFeeTotal));
        }

        //遍历参与人账户修改REDIS账户
        Set<String> keySet = userMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String[] split = key.split(CHS.underline.getValue());
            
            Map<String, BigDecimal> accountMap = userMap.get(key);
            tradeAccountService.updateCoinAmount(split[0], split[1], accountMap.get("available"), accountMap.get("freeze"));
        }
    }
    

    /**
     * 修改用户redis中的当前委托和历史和委托
     *
     * @param orderlist
     */
    private String updateRedisUserEntrust(List<TradeEntrustInfo> orderlist, String tableDate) {
        StringBuilder statement = new StringBuilder();
        if (orderlist != null && orderlist.size() > 0) {
            //遍历成交信息，修改对手单委托信息
            for (TradeEntrustInfo info : orderlist) {
                //主动买单，修改对手当前委托单信息
            	if (TradeEntrust.ENTRUST_TYPE_BUY.equals(info.getType())) {
                    //主动卖单，修改对手当前委托单信息
                    String entrustingKey = String.format(RedisKeyConstant.USER_ENTRUSTING, info.getTradeCoinCode(), info.getPricingCoinCode(), info.getBuyCustomerId());
                    String buyEntrusting = jedisClient.hget(JedisDataSourceSignleton.DB1, entrustingKey, info.getBuyOrderId());
                    if(StringUtils.hasText(buyEntrusting)) {
                        TradeEntrust buyEntrust = gsonClient.fromJson(buyEntrusting, TradeEntrust.class);
                        if (buyEntrust.getEntrustAmout().compareTo(info.getEntrustAmout()) > 0) {//如果委托数量大于成交数量，直接修改当前数据
                            buyEntrust.setEntrustAmout(buyEntrust.getEntrustAmout().subtract(info.getEntrustAmout()));
                            //修改当前委托
                            jedisClient.hset(JedisDataSourceSignleton.DB1, entrustingKey, info.getBuyOrderId(), JSON.toJSONString(buyEntrust));
                            //生成修改委托的SQL 
                            statement.append(SqlUtil.updateEntrustIngSql(buyEntrust.getOrderId(), buyEntrust.getEntrustAmout()));
                        } else {
                            //删除当前委托
                            jedisClient.hdel(JedisDataSourceSignleton.DB1, entrustingKey, info.getBuyOrderId());
                            
                            //生成删除委托的SQL
                            statement.append(SqlUtil.deleteEntrustIngSql(buyEntrust.getOrderId()));
                            
                            //存入历史委托集合中
                            buyEntrust.setEntrustAmout(buyEntrust.getEntrustAmout().subtract(info.getEntrustAmout()));
                            saveRedisUserEntrust(buyEntrust);
                            
                            //生成历史委托SQL
                            EntrustHistory entrustHistory = new EntrustHistory();
                            entrustHistory.setAccountId(buyEntrust.getAccountId());
                            entrustHistory.setCustomerId(buyEntrust.getCustomerId());
                            entrustHistory.setTradeCoinCode(buyEntrust.getTradeCoinCode());
                            entrustHistory.setPricingCoinCode(buyEntrust.getPricingCoinCode());
                            entrustHistory.setEntrustPrice(buyEntrust.getEntrustPrice());
                            entrustHistory.setEntrustAmout(buyEntrust.getEntrustAmout());
                            entrustHistory.setEntrustAmoutSql(buyEntrust.getEntrustAmoutSql());
                            entrustHistory.setEntrustTime(new Date(buyEntrust.getEntrustTime()));
                            entrustHistory.setEntrustType(buyEntrust.getEntrustType());
                            entrustHistory.setOrderId(buyEntrust.getOrderId());
                            statement.append(SqlUtil.createSql(entrustHistory,tableDate));
                        }
                    }
                }else if (TradeEntrust.ENTRUST_TYPE_SELL.equals(info.getType())) {
                    String entrustingKey = String.format(RedisKeyConstant.USER_ENTRUSTING, info.getTradeCoinCode(), info.getPricingCoinCode(), info.getSellCustomerId());
                    String sellEntrusting = jedisClient.hget(JedisDataSourceSignleton.DB1, entrustingKey, info.getSellOrderId());
                    if(StringUtils.hasText(sellEntrusting)) {
                        TradeEntrust sellEntrust = gsonClient.fromJson(sellEntrusting, TradeEntrust.class);
                        //如果委托数量大于成交数量，直接修改当前数据
                        if (sellEntrust.getEntrustAmout().compareTo(info.getEntrustAmout()) > 0) {
                            sellEntrust.setEntrustAmout(sellEntrust.getEntrustAmout().subtract(info.getEntrustAmout()));
                            
                            //修改当前委托
                            jedisClient.hset(JedisDataSourceSignleton.DB1, entrustingKey, info.getSellOrderId(), JSON.toJSONString(sellEntrust));
                            
                            //生成修改委托的SQL
                            statement.append(SqlUtil.updateEntrustIngSql(sellEntrust.getOrderId(), sellEntrust.getEntrustAmout()));
                        } else {
                            //删除当前委托
                            jedisClient.hdel(JedisDataSourceSignleton.DB1, entrustingKey, info.getSellOrderId());
                            
                            //生成删除委托的SQL
                            statement.append(SqlUtil.deleteEntrustIngSql(sellEntrust.getOrderId()));

                            //存入历史委托集合中
                            sellEntrust.setEntrustAmout(sellEntrust.getEntrustAmout().subtract(info.getEntrustAmout()));
                            saveRedisUserEntrust(sellEntrust);

                            //生成历史委托SQL
                            EntrustHistory entrustHistory = new EntrustHistory();
                            entrustHistory.setAccountId(sellEntrust.getAccountId());
                            entrustHistory.setCustomerId(sellEntrust.getCustomerId());
                            entrustHistory.setTradeCoinCode(sellEntrust.getTradeCoinCode());
                            entrustHistory.setPricingCoinCode(sellEntrust.getPricingCoinCode());
                            entrustHistory.setEntrustPrice(sellEntrust.getEntrustPrice());
                            entrustHistory.setEntrustAmout(sellEntrust.getEntrustAmout());
                            entrustHistory.setEntrustAmoutSql(sellEntrust.getEntrustAmoutSql());
                            entrustHistory.setEntrustTime(new Date(sellEntrust.getEntrustTime()));
                            entrustHistory.setEntrustType(sellEntrust.getEntrustType());
                            entrustHistory.setOrderId(sellEntrust.getOrderId());
                            statement.append(SqlUtil.createSql(entrustHistory, tableDate));
                        }
                    }
                }
            }
        }
        return String.valueOf(statement);
    }
    
    /**
     * 生成sql脚本发送到mq队列中同步到mysql数据库
     *
     * @param traEntrust
     * @param orderlist
     */
    private String createAccountSql(TradeEntrust traEntrust, List<TradeEntrustInfo> orderlist, String tableDate) {
        StringBuilder statement = new StringBuilder();
        //总订单交易币成交总数量
        BigDecimal tradeCoinAccount = BigDecimal.ZERO;
        //总订单定价币成交总额
        BigDecimal pricingCoinAccount = BigDecimal.ZERO;
        
        List<String> customerIds = new ArrayList<String>();
        customerIds.add(traEntrust.getCustomerId());
        //遍历成交信息生成SQL
        if (orderlist != null && orderlist.size() > 0) {
            //遍历成交信息,记录
            for (TradeEntrustInfo info : orderlist) {
            	// 交易币
                BigDecimal totalMoney = info.getEntrustPrice().multiply(info.getEntrustAmout());
                // 总交易币
                tradeCoinAccount = tradeCoinAccount.add(totalMoney);
                // 总定价币
                pricingCoinAccount = pricingCoinAccount.add(info.getEntrustAmout());
                // 主动卖单=匹配到的对手单就是买单
                if (TradeEntrust.ENTRUST_TYPE_BUY.equals(info.getType())) {
                    // 买家实际流水扣减定价币
                    CustomerAccountRecord record = new CustomerAccountRecord();
                    record.setType(CustomerAccountRecord.ENTRUST);
                    record.setAccountId(info.getBuyAccountId());
                    record.setCustomerId(info.getBuyCustomerId());
                    record.setCoinCode(info.getPricingCoinCode());
                    record.setOrderId(traEntrust.getOrderId());
                    record.setMoney(totalMoney.multiply(new BigDecimal(-1)));
                    statement.append(SqlUtil.createSql(record, tableDate));

                    // 买家冻结流水扣减定价币
                    CustomerAccountFreeze record4 = new CustomerAccountFreeze();
                    record4.setFreezeType(CustomerAccountFreeze.ENTRUST);
                    record4.setAccountId(info.getBuyAccountId());
                    record4.setCustomerId(info.getBuyCustomerId());
                    record4.setCoinCode(info.getPricingCoinCode());
                    record4.setOrderId(traEntrust.getOrderId());
                    record4.setFreezeMoney(totalMoney.multiply(new BigDecimal(-1)));
                    statement.append(SqlUtil.createSql(record4, tableDate));

                    // 买家增加交易币
                    CustomerAccountRecord record2 = new CustomerAccountRecord();
                    record2.setType(CustomerAccountRecord.ENTRUST);
                    record2.setAccountId(info.getBuyAccountId());
                    record2.setCustomerId(info.getBuyCustomerId());
                    record2.setCoinCode(info.getTradeCoinCode());
                    record2.setOrderId(traEntrust.getOrderId());
                    record2.setMoney(info.getEntrustAmout());
                    statement.append(SqlUtil.createSql(record2, tableDate));

                    // 买家扣减手续费---交易币
                    if (info.getBuyFee().compareTo(BigDecimal.ZERO) > 0) {
                        CustomerAccountRecord record7 = new CustomerAccountRecord();
                        record7.setType(CustomerAccountRecord.HANDFEE);
                        record7.setAccountId(info.getBuyAccountId());
                        record7.setCustomerId(info.getBuyCustomerId());
                        record7.setCoinCode(info.getTradeCoinCode());
                        record7.setOrderId(traEntrust.getOrderId());
                        record7.setMoney(info.getBuyFee().multiply(new BigDecimal(-1)));
                        record7.setTradeCoinRealTimePrice(info.getEntrustPrice());
                        record7.setTradeCoinCode(info.getTradeCoinCode());
                        record7.setPricingCoinCode(info.getPricingCoinCode());
                        statement.append(SqlUtil.createSql(record7,tableDate));
                    }

                    // 卖家减少交易币
                    CustomerAccountRecord record6 = new CustomerAccountRecord();
                    record6.setType(CustomerAccountRecord.ENTRUST);
                    record6.setAccountId(info.getSellAccountId());
                    record6.setCustomerId(info.getSellCustomerId());
                    record6.setCoinCode(info.getTradeCoinCode());
                    record6.setOrderId(traEntrust.getOrderId());
                    record6.setMoney(info.getEntrustAmout().multiply(new BigDecimal(-1)));
                    statement.append(SqlUtil.createSql(record6,tableDate));

                    // 卖家实际流水增加定价币
                    CustomerAccountRecord record3 = new CustomerAccountRecord();
                    record3.setType(CustomerAccountRecord.ENTRUST);
                    record3.setAccountId(info.getSellAccountId());
                    record3.setCustomerId(info.getSellCustomerId());
                    record3.setCoinCode(info.getPricingCoinCode());
                    record3.setOrderId(traEntrust.getOrderId());
                    record3.setMoney(totalMoney);
                    statement.append(SqlUtil.createSql(record3,tableDate));

                    // 卖家冻结流水减少交易币
                    CustomerAccountFreeze record5 = new CustomerAccountFreeze();
                    record5.setFreezeType(CustomerAccountFreeze.ENTRUST);
                    record5.setAccountId(info.getSellAccountId());
                    record5.setCustomerId(info.getSellCustomerId());
                    record5.setCoinCode(info.getTradeCoinCode());
                    record5.setOrderId(traEntrust.getOrderId());
                    record5.setFreezeMoney(info.getEntrustAmout().multiply(new BigDecimal(-1)));
                    statement.append(SqlUtil.createSql(record5,tableDate));

                    // 卖家扣减手续费---定价币
                    if (info.getSellFee().compareTo(BigDecimal.ZERO) > 0) {
                        CustomerAccountRecord record8 = new CustomerAccountRecord();
                        record8.setType(CustomerAccountRecord.HANDFEE);
                        record8.setAccountId(info.getSellAccountId());
                        record8.setCustomerId(info.getSellCustomerId());
                        record8.setCoinCode(info.getPricingCoinCode());
                        record8.setOrderId(traEntrust.getOrderId());
                        record8.setMoney(info.getSellFee().multiply(new BigDecimal(-1)));
                        record8.setTradeCoinRealTimePrice(info.getEntrustPrice());
                        record8.setTradeCoinCode(info.getTradeCoinCode());
                        record8.setPricingCoinCode(info.getPricingCoinCode());
                        statement.append(SqlUtil.createSql(record8,tableDate));
                    }
                    customerIds.add(info.getSellCustomerId());
                } else {//主动买单，那么此时对手单为卖单
                    // 卖家实际流水扣减 成交交易币数量
                    CustomerAccountRecord record = new CustomerAccountRecord();
                    record.setType(CustomerAccountRecord.ENTRUST);
                    record.setAccountId(info.getSellAccountId());
                    record.setCustomerId(info.getSellCustomerId());
                    record.setCoinCode(info.getTradeCoinCode());
                    record.setOrderId(traEntrust.getOrderId());
                    record.setMoney(info.getEntrustAmout().multiply(new BigDecimal(-1)));
                    statement.append(SqlUtil.createSql(record,tableDate));

                    // 卖家冻结流水减少交易币
                    CustomerAccountFreeze record5 = new CustomerAccountFreeze();
                    record5.setFreezeType(CustomerAccountFreeze.ENTRUST);
                    record5.setCustomerId(info.getSellCustomerId());
                    record5.setAccountId(info.getSellAccountId());
                    record5.setCoinCode(info.getTradeCoinCode());
                    record5.setFreezeMoney(info.getEntrustAmout().multiply(new BigDecimal(-1)));
                    record5.setOrderId(traEntrust.getOrderId());
                    statement.append(SqlUtil.createSql(record5,tableDate));

                    // 卖家实际流水增加定价币
                    CustomerAccountRecord record2 = new CustomerAccountRecord();
                    record2.setType(CustomerAccountRecord.ENTRUST);
                    record2.setAccountId(info.getSellAccountId());
                    record2.setCustomerId(info.getSellCustomerId());
                    record2.setCoinCode(info.getPricingCoinCode());
                    record2.setOrderId(traEntrust.getOrderId());
                    record2.setMoney(totalMoney);
                    statement.append(SqlUtil.createSql(record2,tableDate));

                    //卖家扣减手续费---定价币
                    if (info.getSellFee().compareTo(BigDecimal.ZERO) > 0) {
                        CustomerAccountRecord record8 = new CustomerAccountRecord();
                        record8.setType(CustomerAccountRecord.HANDFEE);
                        record8.setAccountId(info.getSellAccountId());
                        record8.setCustomerId(info.getSellCustomerId());
                        record8.setCoinCode(info.getPricingCoinCode());
                        record8.setOrderId(traEntrust.getOrderId());
                        record8.setMoney(info.getSellFee().multiply(new BigDecimal(-1)));
                        record8.setTradeCoinRealTimePrice(info.getEntrustPrice());
                        record8.setTradeCoinCode(info.getTradeCoinCode());
                        record8.setPricingCoinCode(info.getPricingCoinCode());
                        statement.append(SqlUtil.createSql(record8,tableDate));
                    }

                    //买家实际减少定价币
                    CustomerAccountRecord record6 = new CustomerAccountRecord();
                    record6.setType(CustomerAccountRecord.ENTRUST);
                    record6.setAccountId(info.getBuyAccountId());
                    record6.setCustomerId(info.getBuyCustomerId());
                    record6.setCoinCode(info.getPricingCoinCode());
                    record6.setOrderId(traEntrust.getOrderId());
                    record6.setMoney(totalMoney.multiply(new BigDecimal(-1)));
                    statement.append(SqlUtil.createSql(record6,tableDate));

                    // 买家实际流水增加交易币
                    CustomerAccountRecord record3 = new CustomerAccountRecord();
                    record3.setType(CustomerAccountRecord.ENTRUST);
                    record3.setAccountId(info.getBuyAccountId());
                    record3.setCustomerId(info.getBuyCustomerId());
                    record3.setCoinCode(info.getTradeCoinCode());
                    record3.setOrderId(traEntrust.getOrderId());
                    record3.setMoney(info.getEntrustAmout());
                    statement.append(SqlUtil.createSql(record3,tableDate));

                    // 买家冻结流水减少定价币
                    CustomerAccountFreeze record4 = new CustomerAccountFreeze();
                    record4.setFreezeType(CustomerAccountFreeze.ENTRUST);
                    record4.setCustomerId(info.getBuyCustomerId());
                    record4.setAccountId(info.getBuyAccountId());
                    record4.setCoinCode(info.getPricingCoinCode());
                    record4.setFreezeMoney(totalMoney.multiply(new BigDecimal(-1)));
                    record4.setOrderId(traEntrust.getOrderId());
                    statement.append(SqlUtil.createSql(record4,tableDate));

                    //买家扣减手续费---交易币
                    if (info.getBuyFee().compareTo(BigDecimal.ZERO) > 0) {
                        CustomerAccountRecord record7 = new CustomerAccountRecord();
                        record7.setType(CustomerAccountRecord.HANDFEE);
                        record7.setAccountId(info.getBuyAccountId());
                        record7.setCustomerId(info.getBuyCustomerId());
                        record7.setCoinCode(info.getTradeCoinCode());
                        record7.setOrderId(traEntrust.getOrderId());
                        record7.setMoney(info.getBuyFee().multiply(new BigDecimal(-1)));
                        record7.setTradeCoinRealTimePrice(info.getEntrustPrice());
                        record7.setTradeCoinCode(info.getTradeCoinCode());
                        record7.setPricingCoinCode(info.getPricingCoinCode());
                        statement.append(SqlUtil.createSql(record7,tableDate));
                    }
                    customerIds.add(info.getBuyCustomerId());
                }
            }
        }

        //买单
        if (TradeEntrust.ENTRUST_TYPE_BUY.equals(traEntrust.getEntrustType())) {
            //增加冻结定价币
            //未成交的币*买入价格  +成交总额
            BigDecimal multiply = traEntrust.getEntrustAmout().multiply(traEntrust.getEntrustPrice()).add(tradeCoinAccount);
            CustomerAccountFreeze obj = new CustomerAccountFreeze();
            obj.setFreezeType(CustomerAccountFreeze.ENTRUST);
            obj.setAccountId(traEntrust.getAccountId());
            obj.setCoinCode(traEntrust.getPricingCoinCode());
            obj.setCustomerId(traEntrust.getCustomerId());
            obj.setFreezeMoney(multiply);
            obj.setOrderId(traEntrust.getOrderId());
            statement.append(SqlUtil.createSql(obj,tableDate));
        } else {//卖单
            //【增加冻结交易币】 【委托的币数量】
            CustomerAccountFreeze obj = new CustomerAccountFreeze();
            obj.setFreezeType(CustomerAccountFreeze.ENTRUST);
            obj.setAccountId(traEntrust.getAccountId());
            obj.setCoinCode(traEntrust.getTradeCoinCode());
            obj.setCustomerId(traEntrust.getCustomerId());
            obj.setFreezeMoney(traEntrust.getEntrustAmoutSql());
            obj.setOrderId(traEntrust.getOrderId());
            statement.append(SqlUtil.createSql(obj,tableDate));
        }
        return String.valueOf(statement);
    }
    
    /**
     * 	生成SQL脚本发送到MQ队列中同步到mysql数据库
     *
     * @param tradeEntrust
     * @param orderlist
     */
    private String createEntrustSql(TradeEntrust tradeEntrust, List<TradeEntrustInfo> orderlist, String tableDate) {
        StringBuilder statement = new StringBuilder(); 
        //剩余数量大于0，委托单进入当前委托中
        if (tradeEntrust.getEntrustAmout().compareTo(BigDecimal.ZERO) > 0) {
            EntrustIng traEntrustIng = new EntrustIng();
            traEntrustIng.setAccountId(tradeEntrust.getAccountId());
            traEntrustIng.setCustomerId(tradeEntrust.getCustomerId());
            traEntrustIng.setTradeCoinCode(tradeEntrust.getTradeCoinCode());
            traEntrustIng.setPricingCoinCode(tradeEntrust.getPricingCoinCode());
            traEntrustIng.setEntrustPrice(tradeEntrust.getEntrustPrice());
            traEntrustIng.setEntrustAmout(tradeEntrust.getEntrustAmout());
            traEntrustIng.setEntrustAmoutSql(tradeEntrust.getEntrustAmoutSql());
            traEntrustIng.setEntrustTime(new Date(tradeEntrust.getEntrustTime()));
            traEntrustIng.setEntrustType(tradeEntrust.getEntrustType());
            traEntrustIng.setOrderId(tradeEntrust.getOrderId());
            statement.append(SqlUtil.createSql(traEntrustIng));
        } else {//剩余数量==0，委托单进入历史委托中
            EntrustHistory traEntrustHistory = new EntrustHistory();
            traEntrustHistory.setAccountId(tradeEntrust.getAccountId());
            traEntrustHistory.setCustomerId(tradeEntrust.getCustomerId());
            traEntrustHistory.setTradeCoinCode(tradeEntrust.getTradeCoinCode());
            traEntrustHistory.setPricingCoinCode(tradeEntrust.getPricingCoinCode());
            traEntrustHistory.setEntrustPrice(tradeEntrust.getEntrustPrice());
            traEntrustHistory.setEntrustAmout(tradeEntrust.getEntrustAmout());
            traEntrustHistory.setEntrustAmoutSql(tradeEntrust.getEntrustAmoutSql());
            traEntrustHistory.setEntrustTime(new Date(tradeEntrust.getEntrustTime()));
            traEntrustHistory.setEntrustType(tradeEntrust.getEntrustType());
            traEntrustHistory.setOrderId(tradeEntrust.getOrderId());
            statement.append(SqlUtil.createSql(traEntrustHistory,tableDate));
        }
        //遍历成交信息生成SQL
        CoinUtil util = new CoinUtil();
        BigDecimal realTimeUsdtRate = BigDecimal.ZERO;
        for (TradeEntrustInfo infoTo : orderlist) {
            EntrustInfo info = new EntrustInfo();
            info.setBuyAccountId(infoTo.getBuyAccountId());
            info.setSellAccountId(infoTo.getSellAccountId());
            info.setBuyCustomerId(infoTo.getBuyCustomerId());
            info.setSellCustomerId(infoTo.getSellCustomerId());
            info.setEntrustAmout(infoTo.getEntrustAmout());
            info.setEntrustPrice(infoTo.getEntrustPrice());
            info.setEntrustTime(infoTo.getEntrustTime());
            info.setTradeCoinCode(infoTo.getTradeCoinCode());
            info.setPricingCoinCode(infoTo.getPricingCoinCode());
            info.setType(infoTo.getType());
            info.setBuyFee(infoTo.getBuyFee());
            info.setSellFee(infoTo.getSellFee());
            info.setBuyRate(infoTo.getBuyRate());
            info.setSellRate(infoTo.getSellRate());
            if(realTimeUsdtRate.compareTo(BigDecimal.ZERO) == 0) {
            	// 定价币转化成相对USDT当前实时汇率，如果当前定价币就是USDT,那么比率是1
                realTimeUsdtRate = util.selectUsdt(BigDecimal.ONE, infoTo.getPricingCoinCode());
            }
    		info.setRealTimeUsdtRate(realTimeUsdtRate);
            statement.append(SqlUtil.createSql(info, tableDate));
        }
        return String.valueOf(statement);
    }
    
    /**
     * 将委托订单更新到Kline委托订单中
     * 
     * @param orderlist
     * 			匹配成交委托订单
     * @param coinPair
     * 			交易对
     */
    private void updateEntrustOrderToKlineOrder(List<TradeEntrustInfo> orderlist,String coinPair) {
    	if (orderlist != null && orderlist.size() > 0) {
        	String klineOrderKey = String.format(RedisKeyConstant.KLINE_S_ORDER, coinPair);
            Long llen = jedisClient.llen(JedisDataSourceSignleton.DB1, klineOrderKey);
            for (TradeEntrustInfo info : orderlist) {
                if (llen >= 80) {
                    // 删除最早委托成交订单中的一条数据
                    jedisClient.rpop(JedisDataSourceSignleton.DB1, klineOrderKey);
                }
                // 设置一条数据到委托成交订单
                jedisClient.lpush(JedisDataSourceSignleton.DB1, klineOrderKey, JSON.toJSONString(info));
            }
        }
    }
    
    /**
     * 创建成交单信息
     *
     * @param type
     * @param tradeEntrust
     * @param sellTraEntrust
     * @param entrustPrice
     * @param entrustAmout
     * @return
     */
    private TradeEntrustInfo createTraEntrustInfo(String type, TradeEntrust tradeEntrust, TradeEntrust sellTraEntrust, BigDecimal entrustPrice, BigDecimal entrustAmout) {
        TradeEntrustInfo info = new TradeEntrustInfo();
        //主动买还是主动卖
        info.setType(type);

        //定价币
        info.setPricingCoinCode(tradeEntrust.getPricingCoinCode());

        //交易币
        info.setTradeCoinCode(tradeEntrust.getTradeCoinCode());

        //成交价格，成交数量
        info.setEntrustPrice(entrustPrice);
        info.setEntrustAmout(entrustAmout);

        //买方信息
        info.setBuyCustomerId(tradeEntrust.getCustomerId());
        info.setBuyAccountId(tradeEntrust.getAccountId());
        info.setBuyOrderId(tradeEntrust.getOrderId());

        //卖方信息
        info.setSellCustomerId(sellTraEntrust.getCustomerId());
        info.setSellAccountId(sellTraEntrust.getAccountId());
        info.setSellOrderId(sellTraEntrust.getOrderId());

        //交易币的配置
        JSONObject tradeCoinConf = RedisUserUtil.getCoinConf(tradeEntrust.getTradeCoinCode());

        BigDecimal buyRate = tradeCoinConf.getBigDecimal("buyRate").divide(new BigDecimal(100));
        BigDecimal sellRate = tradeCoinConf.getBigDecimal("sellRate").divide(new BigDecimal(100));
        if (buyRate == null) {
            buyRate = new BigDecimal(0);
        }
        if (sellRate == null) {
            sellRate = new BigDecimal(0);
        }
        info.setBuyRate(buyRate);
        info.setSellRate(sellRate);
        
		String coinPair = tradeEntrust.getTradeCoinCode() + CHS.underline.getValue() + tradeEntrust.getPricingCoinCode();
        int priceDecimal = decimalUtil.getCoinConfigField(coinPair, CoinPairConfigUtil.PRICE_DECIMAL);

        //买扣交易币
        info.setBuyFee((entrustAmout.multiply(buyRate)).setScale(priceDecimal, BigDecimal.ROUND_DOWN));

        //卖扣定价币
        info.setSellFee((entrustPrice.multiply(entrustAmout).multiply(sellRate)).setScale(priceDecimal, BigDecimal.ROUND_DOWN));

        //成交时间
        info.setEntrustTime(new Date());
        return info;
    }
    
    
    @Override
    public void cancel(String userId, String coinPair, String orderId) {
        String tableDate = DateUtils.dateFormatToString(new Date(), DateUtils.TABLES_DAY_FIX);
    	StringBuilder statement = new StringBuilder();
        RedisDistributedLock lock = new RedisDistributedLock();
        String coinLockKey = String.format(RedisDistributedKeyEnum.ENTRUST_TRADE_COIN_PAIR_KEY.getValue(), coinPair);
        String personLockKey = String.format(RedisDistributedKeyEnum.ENTRUST_TRADE_PERSION_KEY.getValue(), userId);
        try {
            // TO.取消委托交易锁交易对 // TO.取消委托交易锁委托用户
            lock.lock(coinLockKey, orderId);
            lock.lock(personLockKey, orderId);
            lock.setRunTime(System.currentTimeMillis());
            
            // 删除个人委托单
            String personEntrustKey = String.format(RedisKeyConstant.USER_ENTRUSTING_S_S, coinPair, userId);
            String entrusting = jedisClient.hget(JedisDataSourceSignleton.DB1, personEntrustKey, orderId);
            TradeEntrust tradeEntrust = gsonClient.fromJson(entrusting, TradeEntrust.class);
            if (tradeEntrust == null) { return; }
            
            //删除Redis中的当前委托
            jedisClient.hdel(JedisDataSourceSignleton.DB1, personEntrustKey, orderId);
            
            //将当前委托转到历史委托中
            jedisClient.lpush(JedisDataSourceSignleton.DB1, String.format(RedisKeyConstant.USER_ENTRUSTHISTORY, coinPair, userId), entrusting);
            
            //1.1删除公共委托单 
            String priceKey = String.format(RedisKeyConstant.TRA_COINPAIR_TRADE_KEY, coinPair, tradeEntrust.getEntrustType(), tradeEntrust.getEntrustPrice().toPlainString());
            List<String> priceOrderList = jedisClient.lrange(JedisDataSourceSignleton.DB1, priceKey, 0, -1);
            if (priceOrderList != null && priceOrderList.size() > 0) {
                for (String recordJson : priceOrderList) { //删除委托单
                    TradeEntrust redisEntrust = gsonClient.fromJson(recordJson, TradeEntrust.class);
                    if (redisEntrust != null && Objects.equals(redisEntrust.getOrderId(), orderId)) { 
                        jedisClient.lrem(JedisDataSourceSignleton.DB1, priceKey, 0, recordJson);
                        break;
                    }
                }
                
                // 及时清除PriceZset中的价格
                if(priceOrderList.size() <= 1) {
					Long remainLength = jedisClient.llen(JedisDataSourceSignleton.DB1, priceKey);
	        		if(remainLength == null || remainLength == 0) {
	        			String priceZsetKey = String.format(RedisKeyConstant.TRA_PRICE_ZSET, coinPair, tradeEntrust.getEntrustType());
	        			jedisClient.zrem(JedisDataSourceSignleton.DB1, priceZsetKey, tradeEntrust.getEntrustPrice().toPlainString());
	        		}
                }
            }

            //2修改账户
            if (TradeEntrust.ENTRUST_TYPE_BUY.equals(tradeEntrust.getEntrustType())) {
            	//买单：定价币=未成交数量*委托价格
                BigDecimal totalMoney = tradeEntrust.getEntrustAmout().multiply(tradeEntrust.getEntrustPrice());
                
                // [买单]：定价币可用增加，冻结减少
                tradeAccountService.updateCoinAmount(tradeEntrust.getCustomerId(), tradeEntrust.getPricingCoinCode(), totalMoney.abs(), totalMoney.abs().negate());
                
                //买单：生成定价币解冻记录
                CustomerAccountFreeze record = new CustomerAccountFreeze();
                record.setCustomerId(tradeEntrust.getCustomerId());
                record.setAccountId(tradeEntrust.getAccountId());
                record.setFreezeType(CustomerAccountFreeze.CANCEL);
                record.setOrderId(tradeEntrust.getOrderId());
                record.setCoinCode(tradeEntrust.getPricingCoinCode());
                record.setFreezeMoney(totalMoney.abs().negate());
                statement.append(SqlUtil.createSql(record, tableDate));
                
            } else if (TradeEntrust.ENTRUST_TYPE_SELL.equals(tradeEntrust.getEntrustType())){
                // [卖单]：交易币冻结减少，可用增加
                BigDecimal available = tradeEntrust.getEntrustAmout().abs();
                BigDecimal freeze = tradeEntrust.getEntrustAmout().abs().negate();
                tradeAccountService.updateCoinAmount(tradeEntrust.getCustomerId(), tradeEntrust.getTradeCoinCode(), available, freeze);
                
                //卖单：生成交易币减少冻结记录
                CustomerAccountFreeze record = new CustomerAccountFreeze();
                record.setCustomerId(tradeEntrust.getCustomerId());
                record.setAccountId(tradeEntrust.getAccountId());
                record.setFreezeType(CustomerAccountFreeze.CANCEL);
                record.setOrderId(tradeEntrust.getOrderId());
                record.setCoinCode(tradeEntrust.getTradeCoinCode());
                record.setFreezeMoney(freeze);
                statement.append(SqlUtil.createSql(record, tableDate));
            }

            //删除当前委托
            statement.append(SqlUtil.deleteEntrustIngSql(orderId));

            //生成历史委托SQL
            EntrustHistory entrustHistory = new EntrustHistory();
            entrustHistory.setAccountId(tradeEntrust.getAccountId());
            entrustHistory.setCustomerId(tradeEntrust.getCustomerId());
            entrustHistory.setTradeCoinCode(tradeEntrust.getTradeCoinCode());
            entrustHistory.setPricingCoinCode(tradeEntrust.getPricingCoinCode());
            entrustHistory.setEntrustPrice(tradeEntrust.getEntrustPrice());
            entrustHistory.setEntrustAmout(tradeEntrust.getEntrustAmout());
            entrustHistory.setEntrustAmoutSql(tradeEntrust.getEntrustAmoutSql());
            entrustHistory.setEntrustTime(new Date(tradeEntrust.getEntrustTime()));
            entrustHistory.setEntrustType(tradeEntrust.getEntrustType());
            entrustHistory.setOrderId(tradeEntrust.getOrderId());
            statement.append(SqlUtil.createSql(entrustHistory, tableDate));
            
            // RabbitMQ订阅模式推送取消委托到前端交易大厅
            rabbitMqSender.cancelEntrustOrderToFront(gsonClient.toJson(tradeEntrust));
            
            // 执行SQL
            if(statement != null && statement.length() > 0) {
                jdbcTemplate.batchUpdate(String.valueOf(statement).split(CHS.semicolon.getValue()));
            }
        } finally {
            // TO.取消委托交易流程解锁
            lock.unLock(personLockKey, orderId, 0);
            lock.unLock(coinLockKey, orderId, 0);
        }
        // TO.取消业务逻辑锁
        LogicLockSignleton.getInstance().cencelLogicLock(orderId);
    }
    
    @Override
    public void cancelAll(String userId,String coinPair) {
        String entrustingKey = String.format(RedisKeyConstant.USER_ENTRUSTING_S_S, coinPair, userId);
        Map<String, String> resultMap = jedisClient.hgetall(JedisDataSourceSignleton.DB1, entrustingKey);
        if(resultMap != null && resultMap.size() > 0) {
	        for(String key : resultMap.keySet()) {
	        	String jsonValue = resultMap.get(key);
	        	TradeEntrust redisEntrust = gsonClient.fromJson(jsonValue, TradeEntrust.class);
	            cancel(userId, coinPair, redisEntrust.getOrderId());
	        }
        }
    }
    
}
