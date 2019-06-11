package com.batsoft.common.aspect;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.model.module.exchange.Coin;
import com.batsoft.model.module.exchange.EntrustHistory;
import com.batsoft.model.module.exchange.EntrustIng;
import com.batsoft.model.module.exchange.vo.CustomerAccountVo;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.exchange.service.EntrustHistoryService;
import com.batsoft.service.module.exchange.service.EntrustIngService;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;
import com.batsoft.service.module.exchange.trade.util.RedisUserUtil;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.utils.StringUtils;

/**
 * 登录初始化账户数据切面
 * 
 * @author simon
 */
@Aspect
@Order(3)
@Component
public class AccountInitAspect {
	
	@Autowired
    private EntrustIngService entrustIngService;
    
    @Resource
    private CustomerAccountService customerAccountService;
    
    @Resource
    private EntrustHistoryService entrustHistoryService;

    private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
    
    /**
     * 声明切面
     * 
     */
    @Pointcut("execution(* com.batsoft.*.LoginController.login(..))")
    public void login() {}

    @Before("login()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {}
    
    @AfterReturning(returning = "ret", pointcut = "login()")
    public void doAfterReturning(Object ret) throws Throwable {
    	String userId = UserUtils.getUser().getId();
    	
    	// 刷新货币账户余额
    	FlushRedisAccountRunnableImpl example = new FlushRedisAccountRunnableImpl(customerAccountService, userId);
    	Thread flushRedisAccountExecute = new Thread(example);
    	flushRedisAccountExecute.start(); 
    	
    	// 刷新账户委托单
        FlushUserRedisEntrustRunnable flushUserEntrust = new FlushUserRedisEntrustRunnable(userId, entrustIngService, entrustHistoryService);
        Thread flushUserEntrustExecute = new Thread(flushUserEntrust);
        flushUserEntrustExecute.start();
    }
    
    /**
     * 刷新货币账户余额
     * 
     * @author simon
     */
    private final class FlushRedisAccountRunnableImpl implements Runnable {
    	
    	private CustomerAccountService customerAccountService;
    	
    	private String userId;
    	
		public FlushRedisAccountRunnableImpl(CustomerAccountService customerAccountService, String userId) {
			this.customerAccountService = customerAccountService;
			this.userId = userId;
		}

		@Override
		public void run() {
        	//查询Redis中账户是否存在
        	String key = String.format(RedisKeyConstant.USER_DCACCOUNT, userId);
            String value = jedisClient.get(JedisDataSourceSignleton.DB1, key);

            //查询个人所有的币账户信息
            List<CustomerAccountVo> accounts = customerAccountService.findList( Coin.STATUS1);
            
            //为空直接初始化
            if(StringUtils.isEmpty(value)){
            	jedisClient.set(JedisDataSourceSignleton.DB1, key, JSON.toJSONString(accounts));
            }else {
                List<CustomerAccountVo> list = JSON.parseArray(value, CustomerAccountVo.class);
                for(CustomerAccountVo redisAccount : list){
                    //可用为0，重新初始化
                    if(redisAccount.getAvailable().compareTo(BigDecimal.ZERO)==0){
                        for(CustomerAccountVo sqlAccount : accounts){
                            if(redisAccount.getCoinCode().equals(sqlAccount.getCoinCode())){
                                redisAccount.setAvailable(sqlAccount.getAvailable());
                            }
                        }
                    }

                    //冻结为0,重新初始化
                    if(redisAccount.getFreeze().compareTo(BigDecimal.ZERO)==0){
                        for(CustomerAccountVo sqlAccount : accounts){
                            if(redisAccount.getCoinCode().equals(sqlAccount.getCoinCode())){
                                redisAccount.setFreeze(sqlAccount.getFreeze());
                            }
                        }
                    }
                }
                jedisClient.set(JedisDataSourceSignleton.DB1, key,JSON.toJSONString(accounts));
            }
		}
    }
    
    /**
     * 加载委托单【委托中、委托历史】
     * 
     * @author simon
     */
    private final class FlushUserRedisEntrustRunnable implements Runnable {
    	
    	private String userId;
    	
    	private EntrustIngService entrustIngService;
    	
    	private EntrustHistoryService entrustHistoryService;
    	
		public FlushUserRedisEntrustRunnable(String userId, EntrustIngService entrustIngService,
				EntrustHistoryService entrustHistoryService) {
			this.userId = userId;
			this.entrustIngService = entrustIngService;
			this.entrustHistoryService = entrustHistoryService;
		}

		@Override
		public void run() {
	        Map<String, String> coinPairs = jedisClient.hgetall(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COINPAIR);
	        if (coinPairs != null && !coinPairs.isEmpty()) {
	            Set<String> keySet = coinPairs.keySet();
	            Iterator<String> it = keySet.iterator();
	            while (it.hasNext()) {
	                String coinPair = it.next();
	                String[] split = coinPair.split(CHS.underline.getValue());
	                
	                // 设置委托中的订单数据
	                String key = String.format(RedisKeyConstant.USER_ENTRUSTING_S_S, coinPair, userId);
	                Long hlength = jedisClient.hlen(JedisDataSourceSignleton.DB1, key);
	                if (hlength == null || hlength == 0) {
	                    QueryFilter entrustingFilter = new QueryFilter(EntrustIng.class);
	                    entrustingFilter.addFilter("customer_id=", userId);
	                    entrustingFilter.addFilter("trade_coin_code=", split[0]);
	                    entrustingFilter.addFilter("pricing_coin_code=", split[1]);
	                    List<EntrustIng> entrustIngList = entrustIngService.find(entrustingFilter);
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
	                
	                // 设置委托历史数据
	                String historykey = String.format(RedisKeyConstant.USER_ENTRUSTHISTORY, coinPair, userId);
	                Long historyLength = jedisClient.llen(JedisDataSourceSignleton.DB1, historykey);
	                if (historyLength == null || historyLength == 0) {
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
	                            
	                            // 将委托历史数据推入Redis
	                            jedisClient.lpush(JedisDataSourceSignleton.DB1, historykey, JSON.toJSONString(tradeEntrust));
	                        }
	                    }
	                }
	            }
	        }
		}
    }
    
    
	
}
