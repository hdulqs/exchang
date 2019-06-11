package com.batsoft.rabbitmq;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.cache.IeoLogicLockUtil;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.core.common.enums.KeyEnum;
import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.model.module.exchange.dto.CancelAllDTO;
import com.batsoft.model.module.exchange.dto.CancelDTO;
import com.batsoft.mq.RabbitConfig;
import com.batsoft.mq.RabbitMqSender;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;
import com.batsoft.service.module.exchange.trade.model.TradeEntrustInfo;
import com.batsoft.service.module.exchange.trade.service.TradeKlineService;
import com.batsoft.service.module.exchange.trade.service.TradeService;
import com.batsoft.service.module.exchange.trade.service.TradeSqlService;
import com.batsoft.service.module.exchange.trade.util.SqlUtil;
import com.batsoft.utils.gson.GsonSingleton;
import com.google.common.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(Integer.MAX_VALUE)
public class RabbitMqReceiver {

    @Resource(name = "tradeRedisService")
    private TradeService tradeService;

    @Resource(name = "tradeSqlService")
    private TradeSqlService tradeSqlService;

    @Autowired
    private CustomerAccountService customerAccountService;
    
    @Autowired
    private RabbitMqSender rabbitMqSender;
    
    @Resource
    private TradeKlineService tradeKlineService;
    
    @Resource
    private JdbcTemplate jdbcTemplate;
    
    private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
    
    private GsonSingleton gsonClient = GsonSingleton.getInstance();
    
    // --------------------------------------------------------------------------------------------------
    
    /**
     * BTC_USDT
     * 
     * @param message
     */
    //@RabbitHandler
    //@RabbitListener(queues = RabbitConfig.BTC_USDT)
    public void BTC_USDT(String message) {
    	lock(message);
    }
    
    /**
     * ETH_USDT
     * 
     * @param message
     */
    //@RabbitHandler
    //@RabbitListener(queues = RabbitConfig.ETH_USDT)
    public void ETH_USDT(String message) {
    	lock(message);
    }
    
    /**
     * MT_BT
     * 
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.MT_BT)
    public void MT_BT(String message) {
    	lock(message);
    }
    
    
    /**
     * CPT_BT
     * 
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.CPT_BT)
    public void CPT_BT(String message) {
    	lock(message);
    }
    
    
    /**
     * JT_BT
     * 
     * @param message
     */
    //@RabbitHandler
    //@RabbitListener(queues = RabbitConfig.JT_BT)
    public void JT_BT(String message) {
    	lock(message);
    }
    
    
    /**
     * BT_USDT
     * 
     * @param message
     */
    //@RabbitHandler
    //@RabbitListener(queues = RabbitConfig.BT_USDT)
    public void BT_USDT(String message) {
    	lock(message);
    }
    
    
    /**
     * MTT_BT
     * 
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.MTT_BT)
    public void MTT_BT(String message) {
    	lock(message);
    }
    
    /**
     * HAT_BT
     * 
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.HAT_BT)
    public void HAT_BT(String message) {
    	lock(message);
    }
    
    /**
     * END：校验下单通行
     * 
     * @param message
     */
    private void lock(String message) {
    	try {
    		TradeEntrust tradeEntrust = gsonClient.fromJson(message, TradeEntrust.class);
        	IeoLogicLockUtil lock = new IeoLogicLockUtil(tradeEntrust.getTradeCoinCode(), tradeEntrust.getPricingCoinCode());
            if(lock.logicLock(0)) {
            	tradeService.addEntrust(tradeEntrust);
            }else {
            	rabbitMqSender.toAddEntrust(message, tradeEntrust.getTradeCoinCode(), tradeEntrust.getPricingCoinCode());
            }
		} catch (Exception e) {}
    }
    
    
    /**
     * cancel
     * 
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.CANCEL)
    public void cancel(String message) {
    	try {
    		CancelDTO cancelDTO = gsonClient.fromJson(message, CancelDTO.class);
        	String userId = cancelDTO.getUserId();
        	String coinPair = cancelDTO.getCoinPair();
        	String orderId = cancelDTO.getOrderId();
        	tradeService.cancel(userId, coinPair, orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * cancelAll
     * 
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.CANCEL_ALL)
    public void cancelAll(String message) {
    	try {
    		CancelAllDTO cancelAllDTO = gsonClient.fromJson(message, CancelAllDTO.class);
        	String userId = cancelAllDTO.getUserId();
        	String coinPair = cancelAllDTO.getCoinPair();
        	tradeService.cancelAll(userId, coinPair);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    // --------------------------------------------------------------------------------------------------
    
    /**
     * 处理逻辑产生的SQL
     * 
     * @param message
     * @throws InterruptedException 
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.SQL_QUEUE)
    public void onSql(String message) {
		jdbcTemplate.batchUpdate(message.split(CHS.semicolon.getValue()));
    }

    /**
     * 通过返回币地址信息 转为 用户信息
     *
     * @return
     */
    private JSONObject convertAddressToUser(JSONObject message) {
        JSONObject jsonObject = message;
        if (jsonObject.get("address") != null) {
            QueryFilter filter = new QueryFilter(CustomerAccount.class);
            filter.addFilter("coinAddress_EQ", jsonObject.get("address"));
            CustomerAccount account = customerAccountService.get(filter);
            if(account!=null){
                jsonObject.put("userId",account.getUserId());
            }
        }
        return jsonObject;
    }

    /**
     * 处理更新Redis账户
     * 
     * @param message
     */
    @Deprecated
    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.REDIS_ACCOUNT_QUEUE)
    public void onRedisAccount(String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        jsonObject = convertAddressToUser(jsonObject);
        CustomerAccount account = new CustomerAccount();
        
        // 用户ID
        String userId = jsonObject.getString("userId");
        
        // 货币代码
        String coinCode = jsonObject.getString("coinCode");
        
        // 货币数量
        BigDecimal money = jsonObject.getBigDecimal("money");
        
        //可用
        BigDecimal available = new BigDecimal(0);
        
        // 冻结
        BigDecimal freeze = new BigDecimal(0);
        
        if ("available".equals(jsonObject.getString("type"))) {
            account = addAvailable(userId, coinCode, money);
        } else if ("freeze".equals(jsonObject.getString("type"))) {
            account = addFreeze(userId, coinCode, money);
        }

        // 后台充币 或者coin 接口充币给用户 account 在缓冲中找不到
        if (account != null) {
            //发消息到sql
            rabbitMqSender.toSql(SqlUtil.updateAccountSql(userId, account.getCoinCode(), account.getAvailable(), account.getFreeze()));
        } else {
            account = customerAccountService.findCoinAccount(userId, coinCode);
            if (account != null) {
                if ("available".equals(jsonObject.getString("type"))) {
                    available = account.getAvailable().add(money);
                    freeze = account.getFreeze();
                } else {
                	freeze = account.getFreeze().add(money);
                    available = account.getAvailable();
                }
                rabbitMqSender.toSql(SqlUtil.updateAccountSql(userId, account.getCoinCode(), available, freeze));
            } else {
            	log.info("mining shop mq未查询到账户====" + userId + "    " + coinCode);
            }
        }
    }
    
    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.REGISTER_INIT_QUEUE)
    public void onRegisterInit(String message) {
    	customerAccountService.saveMultipleFinanceByMQ(message);
    }

    /**
     * MQ 新加币初始化账户
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.ADD_COIN_INIT_QUEUE)
    public void onAddCoinInit(String message) {
        customerAccountService.saveAccountByNewCoinOnMq(message);
    }

    /**
     * 增加或减少指定用户指定币的可用金额
     *
     * @param userId
     * @param coinCode
     * @param money
     */
    private CustomerAccount addAvailable(String userId, String coinCode, BigDecimal money) {
        CustomerAccount result = null;
        String key = String.format(RedisKeyConstant.USER_DCACCOUNT, userId);
        String value = jedisClient.get(JedisDataSourceSignleton.DB1, key);
        if (!StringUtils.isEmpty(value)) {
            List<CustomerAccount> list = JSON.parseArray(value, CustomerAccount.class);
            if (list != null && !list.isEmpty()) {
                for (CustomerAccount account : list) {
                    if (coinCode.equals(account.getCoinCode())) {
                        account.setAvailable(account.getAvailable().add(money));
                        result = account;
                        break;
                    }
                }
                jedisClient.set(JedisDataSourceSignleton.DB1, key, JSON.toJSONString(list));
            }
        }
        return result;
    }

    /**
     * 增加或减少指定用户指定币的冻结金额
     *
     * @param userId
     * 			用户ID
     * @param coinCode
     * 			货币代码
     * @param money
     * 			数量
     */
    private CustomerAccount addFreeze(String userId, String coinCode, BigDecimal money) {
        CustomerAccount result = null;
        String key = String.format(RedisKeyConstant.USER_DCACCOUNT, userId);
        String value = jedisClient.get(JedisDataSourceSignleton.DB1, key);
        if (!StringUtils.isEmpty(value)) {
            List<CustomerAccount> list = JSON.parseArray(value, CustomerAccount.class);
            if (list != null && !list.isEmpty()) {
                for (CustomerAccount account : list) {
                    if (coinCode.equals(account.getCoinCode())) {
                        account.setFreeze(account.getFreeze().add(money));
                        result = account;
                        break;
                    }
                }
                jedisClient.set(JedisDataSourceSignleton.DB1, key, JSON.toJSONString(list));
            }
        }
        return result;
    }
    
    /**
     * 	交易委托单推送至前端 【订阅模式】 添加一条消息到广播台
     * 
     * @param
     */
	@SuppressWarnings("serial")
	@RabbitHandler
	@RabbitListener(queues = "#{pushAddEntrustOrderToFront.name}")
    public void entrustOrderToFront(String message) {
    	Map<String, String> entrustOrderToFrontMq = new HashMap<String, String>();
        entrustOrderToFrontMq = gsonClient.fromJson(message, new TypeToken<Map<String, String>>() {}.getType());
        String coinPair = entrustOrderToFrontMq.get(KeyEnum.coinPair.name());
        
        String tradeEntrustJson = entrustOrderToFrontMq.get(KeyEnum.tradeEntrust.name());
        TradeEntrust tradeEntrust = new TradeEntrust();
        tradeEntrust = gsonClient.fromJson(tradeEntrustJson, tradeEntrust.getClass());
        
        String orderlistJson = entrustOrderToFrontMq.get(KeyEnum.orderlist.name());
        List<TradeEntrustInfo> orderlist = new ArrayList<TradeEntrustInfo>();
        orderlist = gsonClient.fromJson(orderlistJson, new TypeToken<ArrayList<TradeEntrustInfo>>() {}.getType());
        
        //================================================开始推送==============================================
        TradeKlineService tradeKlineService = (TradeKlineService) SpringContextUtil.getBean("tradeKlineService");
        
        //推送委托数据
        if (tradeEntrust.getEntrustAmout().compareTo(BigDecimal.ZERO) > 0) {
            //没匹配完则推送
            tradeKlineService.wsBook(coinPair, tradeEntrust.getEntrustType(), tradeEntrust.getEntrustPrice(), tradeEntrust.getEntrustAmout());
            
            //推送委托订单
            // tradeKlineService.pushTradeEntrustIng(coinPair, tradeEntrust.getCustomerId(), tradeEntrust.getOrderId());
        }
        
        //推送成交数据
        for (TradeEntrustInfo info : orderlist) {
            //推送对应成交的价格
            tradeKlineService.wsBook(coinPair, TradeEntrust.ENTRUST_TYPE_BUY, info.getEntrustPrice(), BigDecimal.ZERO);
            tradeKlineService.wsBook(coinPair, TradeEntrust.ENTRUST_TYPE_SELL, info.getEntrustPrice(), BigDecimal.ZERO);
            
            //推送成交记录
            tradeKlineService.pushTradeInfo(info.getType(), coinPair, info.getEntrustTime().getTime(), info.getEntrustPrice(), info.getEntrustAmout());
            
            //推送K线数据
            tradeKlineService.mathKline(coinPair);
            
            //推送委托订单
    		// tradeKlineService.pushTradeEntrustIng(coinPair, info.getBuyCustomerId(), info.getBuyOrderId());
            // tradeKlineService.pushTradeEntrustIng(coinPair, info.getSellCustomerId(), info.getSellOrderId());
        }
        
        //获取当前用户的账户余额
    	String userAccountKey = String.format(RedisKeyConstant.USER_DCACCOUNT, tradeEntrust.getCustomerId());
    	String userAccountValue = jedisClient.get(JedisDataSourceSignleton.DB1, userAccountKey);
        List<CustomerAccount> coinAccountList = new ArrayList<CustomerAccount>();
        if (!StringUtils.isEmpty(userAccountValue)) {
            coinAccountList = JSON.parseArray(userAccountValue, CustomerAccount.class);
        }
        // 定价币和交易币
        String tradeCoin = tradeEntrust.getTradeCoinCode();
        String pricingCoin = tradeEntrust.getPricingCoinCode(); 
        
        BigDecimal usablePricingCoin = BigDecimal.ZERO;
        BigDecimal usableTradeCoin = BigDecimal.ZERO;
        for (CustomerAccount ca : coinAccountList) {
            if (tradeCoin.equals(ca.getCoinCode())) {
                usableTradeCoin = ca.getAvailable();
            } else if (pricingCoin.equals(ca.getCoinCode())) {
                usablePricingCoin = ca.getAvailable();
            }
        }
        // 推送账户的余额
    	Map<String, BigDecimal> userCoinAccountBalance = new HashMap<String, BigDecimal>();
        userCoinAccountBalance.put("lastNum", usableTradeCoin.setScale(3, BigDecimal.ROUND_HALF_UP));
        userCoinAccountBalance.put("lastMoney", usablePricingCoin.setScale(3, BigDecimal.ROUND_HALF_UP));
        tradeKlineService.pushUserCoinAccountBalance(coinPair, tradeEntrust.getCustomerId(), userCoinAccountBalance);
        
        //推送高开低收数据
        tradeKlineService.mathTicker(coinPair);
	}
	
	 /**
     * 	取消交易委托单推送至前端 【订阅模式】 添加一条消息到广播台
     * 
     * @param
     */
	@RabbitHandler
	@RabbitListener(queues = "#{pushCancelOrderToFront.name}")
    public void cancelEntrustOrderToFront(String message) {
		TradeEntrust tradeEntrust = JSON.parseObject(message, TradeEntrust.class);

        // 推送委托单
        String coinPair = tradeEntrust.getTradeCoinCode() + CHS.underline.getValue() + tradeEntrust.getPricingCoinCode();
        tradeKlineService.wsBook(coinPair, tradeEntrust.getEntrustType(), tradeEntrust.getEntrustPrice(), BigDecimal.ZERO);
        
        // 推送账户货币余额
        String userAccountKey = String.format(RedisKeyConstant.USER_DCACCOUNT, tradeEntrust.getCustomerId());
    	String userAccountValue = jedisClient.get(JedisDataSourceSignleton.DB1, userAccountKey);
        List<CustomerAccount> coinAccountList = new ArrayList<CustomerAccount>();
        if (!StringUtils.isEmpty(userAccountValue)) {
            coinAccountList = JSON.parseArray(userAccountValue, CustomerAccount.class);
        }
        
        // 定价币和交易币
        String tradeCoin = tradeEntrust.getTradeCoinCode();
        String pricingCoin = tradeEntrust.getPricingCoinCode(); 
        
        BigDecimal usablePricingCoin = BigDecimal.ZERO;
        BigDecimal usableTradeCoin = BigDecimal.ZERO;
        for (CustomerAccount ca : coinAccountList) {
            if (tradeCoin.equals(ca.getCoinCode())) {
                usableTradeCoin = ca.getAvailable();
            } else if (pricingCoin.equals(ca.getCoinCode())) {
                usablePricingCoin = ca.getAvailable();
            }
        }
        // 推送账户的余额
    	Map<String, BigDecimal> userCoinAccountBalance = new HashMap<String, BigDecimal>();
        userCoinAccountBalance.put("lastNum", usableTradeCoin.setScale(3, BigDecimal.ROUND_HALF_UP));
        userCoinAccountBalance.put("lastMoney", usablePricingCoin.setScale(3, BigDecimal.ROUND_HALF_UP));
        tradeKlineService.pushUserCoinAccountBalance(coinPair, tradeEntrust.getCustomerId(), userCoinAccountBalance);
    }

}
