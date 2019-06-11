package com.batsoft.app.exchange.pattern.singleton;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.batsoft.app.exchange.pattern.adapter.RobotPriceBehavior;
import com.batsoft.app.exchange.pattern.adapter.motion.KeepAliveGtOneRobotMotion;
import com.batsoft.app.exchange.pattern.adapter.motion.KeepAliveLessThanOneRobotMotion;
import com.batsoft.app.exchange.pattern.adapter.motion.LinkageMakePriceMotion;
import com.batsoft.app.exchange.pattern.adapter.motion.MakePriceRobotMotion;
import com.batsoft.app.exchange.pattern.adapter.motion.ReverseLinkageMakePriceMotion;
import com.batsoft.blockchain.common.Coin;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.cache.LogicLockSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.core.common.enums.EntrustTradeTypeEnum;
import com.batsoft.core.common.utils.CoinPairConfigUtil;
import com.batsoft.model.module.exchange.RobotTrade;
import com.batsoft.model.module.exchange.dto.CancelDTO;
import com.batsoft.model.module.member.User;
import com.batsoft.mq.RabbitMqSender;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;
import com.batsoft.service.module.exchange.trade.util.UUIDUtil;
import com.batsoft.utils.gson.GsonSingleton;

public class RobotSingleton {
	
	private RobotSingleton() {}
	
	private static RobotSingleton instance = new RobotSingleton();

	public static RobotSingleton getInstance() {
		return instance;
	}
	
	private CoinPairConfigUtil decimalUtil = new CoinPairConfigUtil();
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	private GsonSingleton gsonClient = GsonSingleton.getInstance();
	
	private BigDecimal amplitude = BigDecimal.valueOf(0.05);
	
	// 线程组 
	private static String GROUP_NAME = "ROBOT_AUTO_ADD_ENTRUST";
    private static ThreadGroup RUN_THREAD_GROUP = new ThreadGroup(GROUP_NAME);
    private static Random RAND = new Random();
    
    // 匹配线程名称(用来存储某个交易对开启的线程名称)
    private static Map<String, List<String>> THREAD_NAME_MAP = new HashMap<String, List<String>>();
    
    public List<String> getThreadNamesByCoinPair(String key) {
		return THREAD_NAME_MAP.get(key);
	}

	public void setThreadName(String coinPair, String threadName) {
		List<String> threadNames = THREAD_NAME_MAP.get(coinPair);
		if(threadNames == null || threadNames.size() == 0) {
			threadNames = new ArrayList<String>();
		}
		threadNames.add(threadName);
		THREAD_NAME_MAP.put(coinPair, threadNames);
	}
	
	public void removeThreadName(String coinPair, String threadName) {
		List<String> threadNames = THREAD_NAME_MAP.get(coinPair);
		if(threadNames == null || threadNames.size() == 0) {
			return;
		}
		threadNames.remove(threadName);
		THREAD_NAME_MAP.put(coinPair, threadNames);
	}
	
	/**
	 * 运行一个机器人
	 * 
	 * @param user
	 * 			用户对象
	 * @param type
	 * 			挂单类型
	 * @param robotTrade
	 * 			机器人配置
	 * @return
	 */
	public boolean run(User user, RobotTrade robotTrade) {
		HuobiMarketDataSyncSingleton.getInstance().initialize();
		String[] types = {EntrustTradeTypeEnum.BUY.getCode(), EntrustTradeTypeEnum.SELL.getCode()};
		for(String type : types) {
			// 撮合成交机器人
			BargainRunnableImpl bargainExample = new BargainRunnableImpl(user, type, robotTrade);
			String threadName = type + user.getId() + robotTrade.getCoinPair();
			new Thread(RUN_THREAD_GROUP, bargainExample, threadName).start();
			this.setThreadName(robotTrade.getCoinPair(), threadName);
			
			// 挂单体面机器人
			KeepAliveEntrustOrderRunnable keepAliveExample = new KeepAliveEntrustOrderRunnable(user, type, robotTrade);
			threadName = type + robotTrade.getCoinPair() + user.getId();
			new Thread(RUN_THREAD_GROUP, keepAliveExample, threadName).start();
			this.setThreadName(robotTrade.getCoinPair(), threadName);
		}
		return true;
	}
	
	/**
	 * 取消运行机器人
	 * 
	 * @param robotTrade
	 * 			机器人配置
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public boolean cancel(RobotTrade robotTrade) {
		Thread[] listThread = new Thread[RUN_THREAD_GROUP.activeCount()];
		RUN_THREAD_GROUP.enumerate(listThread);
		
		List<String> threadNames = getThreadNamesByCoinPair(robotTrade.getCoinPair());
		if(threadNames == null || threadNames.size() == 0) {
			return false;
		}
		//停止线程
        for(Thread thread : listThread) {
        	for(String name : threadNames) {
        		if(name.equals(thread.getName())) {
                    if(!thread.isInterrupted()) {
                    	thread.stop();// 强制中断
                    }
                }
        	}
        }
        // 移除交易对的线程名称
        THREAD_NAME_MAP.remove(robotTrade.getCoinPair());
		return true;
	}
	
	/**
	 * 线程随机睡眠
	 * 
	 * @throws InterruptedException 
	 */
	public void sleepThread(Integer entrustTimeMin, Integer entrustTimeMax) throws InterruptedException {
        int sleep = 0;
        do {
        	if(entrustTimeMin.compareTo(entrustTimeMax) == 0) {
        		sleep = entrustTimeMax;
        		break;
        	}
        	if(entrustTimeMin.compareTo(entrustTimeMax) > 0) {
        		sleep = RAND.nextInt(entrustTimeMax);
        		break;
        	}
        	sleep = RAND.nextInt(entrustTimeMax) + entrustTimeMin;
		} while (!(sleep >= entrustTimeMin && sleep <= entrustTimeMax));
        Thread.sleep(sleep * 100);
	}
	
	/**
	 * 推送委托订单到队列中
	 * 
	 * @param randPrice
	 * 				随机价格
	 * @param robotTrade
	 * 				机器人配置
	 * @param customerId
	 * 				客户ID
	 * @param type
	 * 				挂单类型
	 */
	public void sendEntrust(BigDecimal entrustPrice, RobotTrade robotTrade, String customerId, String type) {
		if(entrustPrice != null && entrustPrice.compareTo(BigDecimal.ZERO) > 0) {
		
			// 委托数量
	        BigDecimal amout = entrustAmout(robotTrade.getCoinPair(), robotTrade.getEntrustNumMax(), robotTrade.getEntrustNumMin());
	        
	        // 准备交易委托数据
	        TradeEntrust traEntrustTO = new TradeEntrust();
	        
	        // 交易对
	        String[] tradeMatching = robotTrade.getCoinPair().split(CHS.underline.getValue());
	        
	        //交易币
	        String tradeCoinCode = tradeMatching[0];
	        traEntrustTO.setTradeCoinCode(tradeCoinCode);
	        
	        //定价币
	        String pricingCoinCode = tradeMatching[1];
	        traEntrustTO.setPricingCoinCode(pricingCoinCode);
	        
	        //委托人
	        traEntrustTO.setCustomerId(customerId);
	        
	        //委托类型 1:买或2:卖
	        traEntrustTO.setEntrustType(type);
	        traEntrustTO.setCategory(BigInteger.ZERO.toString());
	        
	        // 委托数量
	        Integer amtDecimal = decimalUtil.getCoinConfigField(robotTrade.getCoinPair(), CoinPairConfigUtil.AMT_DECIMAL);
	        traEntrustTO.setEntrustAmoutSql(amout.setScale(amtDecimal, BigDecimal.ROUND_DOWN));
	        traEntrustTO.setEntrustAmout(amout.setScale(amtDecimal, BigDecimal.ROUND_DOWN));
	        
	        //委托价格
	        Integer pricingCodeLength = decimalUtil.getCoinConfigField(robotTrade.getCoinPair(), CoinPairConfigUtil.PRICE_DECIMAL);
	        traEntrustTO.setEntrustPrice(entrustPrice.setScale(pricingCodeLength, BigDecimal.ROUND_DOWN));
	        
	        //委托单号
	        traEntrustTO.setOrderId(UUIDUtil.getUUID());
	        traEntrustTO.setEntrustState(TradeEntrust.ENTRUSTSTATE0);
	        traEntrustTO.setExecutedPrice(entrustPrice);
	        
	        //发送下单的消息
	        RabbitMqSender rabbitMqSender = (RabbitMqSender) SpringContextUtil.getBean("rabbitMqSender");
	        rabbitMqSender.toAddEntrust(JSON.toJSONString(traEntrustTO), traEntrustTO.getTradeCoinCode(), traEntrustTO.getPricingCoinCode());
		}
	}
	
	/**
	 * 委托数量
	 * 
	 * @param coinPair
	 * 				交易对
	 * @param maxEntrustNum
	 * 				最大委托数量
	 * @param minEntrustNum
	 * 				最小委托数量
	 * @return
	 */
	public BigDecimal entrustAmout(String coinPair, BigDecimal maxEntrustNum, BigDecimal minEntrustNum) {
        BigDecimal amout = BigDecimal.ZERO;
        int amtDecimal = decimalUtil.getCoinConfigField(coinPair, CoinPairConfigUtil.AMT_DECIMAL);
        do {
        	BigDecimal emp = BigDecimal.ZERO;
        	if(maxEntrustNum.compareTo(BigDecimal.ONE) < 0) {
        		emp = BigDecimal.valueOf(Math.random());
        	}else {
        		emp = BigDecimal.valueOf(RAND.nextInt(maxEntrustNum.add(BigDecimal.ONE).intValue()) * Math.random());
        	}
        	amout = emp.setScale(amtDecimal, BigDecimal.ROUND_HALF_DOWN);
        	
        	if(minEntrustNum.compareTo(maxEntrustNum) >= 0) {
        		break;
        	}
		} while (!(amout.compareTo(minEntrustNum) > 0 && amout.compareTo(maxEntrustNum) < 0));
        return amout;
	}
	
	/**
	 * 取消委托订单
	 * 
	 * @param releaseList
	 */
	public void sendCancelEntrust(TradeEntrust... releaseList) {
		if(releaseList != null && releaseList.length > 0) {
			for(TradeEntrust record : releaseList) {
				if(record != null && LogicLockSignleton.getInstance().setLogicLock(record.getOrderId())) {
					CancelDTO cancelDTO = new CancelDTO();
					cancelDTO.setOrderId(record.getOrderId());
					cancelDTO.setUserId(record.getCustomerId());
					cancelDTO.setCoinPair(record.getTradeCoinCode() + CHS.underline.getValue() + record.getPricingCoinCode());
					RabbitMqSender rabbitMqSender = (RabbitMqSender) SpringContextUtil.getBean("rabbitMqSender");
					rabbitMqSender.cancelEntrust(gsonClient.toJson(cancelDTO));
				}
			}
		}
	}
	

	// ------------------------------------------------------------【撮合挂单】-------------------------------------------------------------
	
	/**
	 * 撮合机器人执行类
	 * 
	 * @author simon
	 */
	private final class BargainRunnableImpl implements Runnable {
		
		// 用户对象
		private User user;
		
		// 交易类型
		private String type;
		
		// 机器人配置
		private RobotTrade robotTrade;
		
		public BargainRunnableImpl(User user, String type, RobotTrade robotTrade) {
			super();
			this.user = user;
			this.type = type;
			this.robotTrade = robotTrade;
		}
		
		@Override
		public void run() {
			BigDecimal maxEntrustPrice = robotTrade.getEntrustPriceMax();
			BigDecimal minEntrustPrice = robotTrade.getEntrustPriceMin();
	        do {
	        	try {
        			RobotSingleton.getInstance().sleepThread(robotTrade.getEntrustTimeMin(), robotTrade.getEntrustTimeMax());
        			BigDecimal randPrice = BigDecimal.ZERO;
        			RobotPriceBehavior behavior = null;
        			if(robotTrade.getFromThird() == RobotTrade.FROMTHIRD1) {
        				randPrice = HuobiMarketDataSyncSingleton.getInstance().getHuobiMarketPrice(robotTrade.getCoinPair());
        				if(randPrice == null || randPrice.compareTo(BigDecimal.ZERO) <= 0) {
        					String refSymbol = Coin.BTC.getType() + CHS.underline.getValue() + Coin.USDT.getType();
        					if(Objects.equals(Coin.BT.getType(), robotTrade.getCoinPair().split(CHS.underline.getValue())[1])) {
        						behavior = new ReverseLinkageMakePriceMotion(robotTrade.getEntrustPriceMin(), robotTrade.getCoinPair(), refSymbol);
        					}else {
        						behavior = new LinkageMakePriceMotion(robotTrade.getEntrustPriceMin(), robotTrade.getCoinPair(), refSymbol);
        					}
        					randPrice = behavior.getEntrustPrice();
        				}
        				minEntrustPrice = randPrice.subtract(randPrice.multiply(amplitude));
    					maxEntrustPrice = randPrice.add(randPrice.multiply(amplitude));
        				
        				// 步长波动单
        				if(Objects.equals(type, EntrustTradeTypeEnum.BUY.getCode())) {
        					RobotSingleton.getInstance().sendEntrust(randPrice.subtract(robotTrade.getBasePrice()), robotTrade, user.getId(), type);
        				}else {
        					RobotSingleton.getInstance().sendEntrust(randPrice.add(robotTrade.getBasePrice()), robotTrade, user.getId(), type);
        				}
        			}else {
        				if(BigDecimal.ONE.compareTo(minEntrustPrice) > 0 || BigDecimal.ONE.compareTo(maxEntrustPrice) > 0) {
        					behavior = new MakePriceRobotMotion(type, robotTrade.getEntrustPriceMax(), robotTrade.getEntrustPriceMin(), robotTrade.getCoinPair());
        				}else {
        					behavior = new MakePriceRobotMotion(type, robotTrade.getEntrustPriceMax(), robotTrade.getEntrustPriceMin(), robotTrade.getCoinPair());
        				}
        				randPrice = behavior.getEntrustPrice();
        			}
                	RobotSingleton.getInstance().sendEntrust(randPrice, robotTrade, user.getId(), type);
	                releaseEntrustTradeOder(releaseEntrust(maxEntrustPrice, minEntrustPrice));
		        } catch (Exception e) { }
	        } while (true);
		}
		
		/**
		 * 匹配取消挂单【跟保活的订单规则相反，这个方法要找价格在区间内的订单】
		 * 
		 * @param entrustPriceMax 
		 * 				区间最高价格
		 * 
		 * @param entrustPriceMin
		 * 				区间最低价格
		 */
		private List<TradeEntrust> releaseEntrust(BigDecimal entrustPriceMax, BigDecimal entrustPriceMin) {
			String priceKey = String.format(RedisKeyConstant.TRA_PRICE_ZSET, robotTrade.getCoinPair(), type);
			Set<String> priceSet = jedisClient.zrange(JedisDataSourceSignleton.DB1, priceKey, 0, -1);
			List<TradeEntrust> matching = new ArrayList<TradeEntrust>();
			if(priceSet != null && priceSet.size() > 0) {
				if(Objects.equals(EntrustTradeTypeEnum.BUY.getCode(), type)) {
					priceSet.forEach((String value) -> {
						BigDecimal price = new BigDecimal(value);
						if(price.compareTo(entrustPriceMin) >= 0) { // 价格比最小价格大
	        				matchingMethod(price, matching);
						}
					});
				}else {
					priceSet.forEach((String value) -> {
						BigDecimal price = new BigDecimal(value);
						if(price.compareTo(entrustPriceMax) <= 0) { // 价格比最大价格小
							matchingMethod(price, matching);
						}
					});
				}
			}
			return matching;
		}
		
		/**
		 * 匹配符合价格的机器人自己的委托单
		 * 
		 * @param price
		 * 				价格
		 * @param matching
		 * 				匹配容器
		 * @return
		 */
		private List<TradeEntrust> matchingMethod(BigDecimal price, List<TradeEntrust> matching){
			String entrustIngOrderKey = String.format(RedisKeyConstant.TRA_COINPAIR_TRADE_KEY, robotTrade.getCoinPair(), type, price);
			List<String> entrustOrderList = jedisClient.lrange(JedisDataSourceSignleton.DB1, entrustIngOrderKey , 0, -1);
			if(entrustOrderList != null && entrustOrderList.size() > 0) {
				for(String orderVal : entrustOrderList) {
					TradeEntrust tradeEntrust = JSON.parseObject(orderVal, TradeEntrust.class);
					if(Objects.equals(tradeEntrust.getCustomerId(), user.getId())) {
						matching.add(tradeEntrust);
					}
				}
			}
			return matching;
		}
		
		/**
		 * 根据时间倒序迭代取消
		 * 
		 * @param matching
		 */
		private void releaseEntrustTradeOder(List<TradeEntrust> matching) {
			if(matching != null && matching.size() >= 25) {
				matching.sort((TradeEntrust pre, TradeEntrust rear) -> Long.valueOf(pre.getEntrustTime()).compareTo(Long.valueOf(rear.getEntrustTime())));
				for(int i = 0; i < (matching.size() - 25); i++) {
					RobotSingleton.getInstance().sendCancelEntrust(matching.get(i));
				}
			}
		}
	}
	
	// ------------------------------------------------------------【保活挂单】----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	
	/**
	 * 保活挂单实例
	 * 
	 * @author simon
	 */
	private final class KeepAliveEntrustOrderRunnable implements Runnable {
		// 用户对象
		private User user;
		
		// 交易类型
		private String type;
		
		// 机器人配置
		private RobotTrade robotTrade;
		
		public KeepAliveEntrustOrderRunnable(User user, String type, RobotTrade robotTrade) {
			super();
			this.user = user;
			this.type = type;
			this.robotTrade = robotTrade;
		}
		
		@Override
		public void run() {
			BigDecimal maxEntrustPrice = robotTrade.getEntrustPriceMax();
			BigDecimal minEntrustPrice = robotTrade.getEntrustPriceMin();
	        do {
	        	try {
        			RobotSingleton.getInstance().sleepThread(robotTrade.getEntrustTimeMin(), robotTrade.getEntrustTimeMax());
        			RobotPriceBehavior behavior = null;
        			if(robotTrade.getFromThird() == RobotTrade.FROMTHIRD1) {
        				BigDecimal syncPrice = HuobiMarketDataSyncSingleton.getInstance().getHuobiMarketPrice(robotTrade.getCoinPair());
        				if(syncPrice == null || syncPrice.compareTo(BigDecimal.ZERO) <= 0) {
        					String refSymbol = Coin.BTC.getType() + CHS.underline.getValue() + Coin.USDT.getType();
        					if(Objects.equals(Coin.BT.getType(), robotTrade.getCoinPair().split(CHS.underline.getValue())[1])) {
        						behavior = new ReverseLinkageMakePriceMotion(robotTrade.getEntrustPriceMin(), robotTrade.getCoinPair(), refSymbol);
        					}else {
        						behavior = new LinkageMakePriceMotion(robotTrade.getEntrustPriceMin(), robotTrade.getCoinPair(), refSymbol);
        					}
        					syncPrice = behavior.getEntrustPrice();
        				}
        				maxEntrustPrice = syncPrice.add(syncPrice.multiply(amplitude));
        				minEntrustPrice = syncPrice.subtract(syncPrice.multiply(amplitude));
        			}
        			// 匹配价格区间内委托单
        			List<TradeEntrust> matching = this.checkReleaseEntrust(maxEntrustPrice, minEntrustPrice);
	                if(BigDecimal.ONE.compareTo(minEntrustPrice) > 0 || BigDecimal.ONE.compareTo(maxEntrustPrice) > 0) {
    					behavior = new KeepAliveLessThanOneRobotMotion(matching, type, maxEntrustPrice, minEntrustPrice, robotTrade.getCoinPair());
    				} else {
    					behavior = new KeepAliveGtOneRobotMotion(matching, type, maxEntrustPrice, minEntrustPrice, robotTrade.getCoinPair());
    				}
                	RobotSingleton.getInstance().sendEntrust(behavior.getEntrustPrice(), robotTrade, user.getId(), type);
	                releaseEntrustTradeOder(matching);
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
	        } while (true);
		}
		
		/**
		 * 匹配取消挂单
		 * 
		 * @param maxEntrustPrice 
		 * 				区间最高价格
		 * @param minEntrustPrice
		 * 				区间最低价格
		 */
		private List<TradeEntrust> checkReleaseEntrust(BigDecimal maxEntrustPrice, BigDecimal minEntrustPrice) {
			String priceKey = String.format(RedisKeyConstant.TRA_PRICE_ZSET, robotTrade.getCoinPair(), type);
			Set<String> priceSet = jedisClient.zrange(JedisDataSourceSignleton.DB1, priceKey, 0, -1);
			List<TradeEntrust> matching = new ArrayList<TradeEntrust>();
			if(priceSet != null && priceSet.size() > 0) {
				if(Objects.equals(EntrustTradeTypeEnum.BUY.getCode(), type)) {
					priceSet.forEach((String value) -> {
						BigDecimal price = new BigDecimal(value);
						if(minEntrustPrice.compareTo(price) > 0) { // 价格比最小价格小
	        				matchingMethod(price, matching);
						}
					});
				}else {
					priceSet.forEach((String value) -> {
						BigDecimal price = new BigDecimal(value);
						if(price.compareTo(maxEntrustPrice) > 0) { // 价格比最大价格大
							matchingMethod(price, matching);
						}
					});
				}
			}
			return matching;
		}
		
		/**
		 * 匹配符合价格的机器人自己的委托单
		 * 
		 * @param price
		 * 				价格
		 * @param matching
		 * 				匹配容器
		 * @return
		 */
		private List<TradeEntrust> matchingMethod(BigDecimal price, List<TradeEntrust> matching){
			String entrustIngOrderKey = String.format(RedisKeyConstant.TRA_COINPAIR_TRADE_KEY, robotTrade.getCoinPair(), type, price.toPlainString());
			List<String> entrustOrderList = jedisClient.lrange(JedisDataSourceSignleton.DB1, entrustIngOrderKey , 0, -1);
			if(entrustOrderList != null && entrustOrderList.size() > 0) {
				for(String orderVal : entrustOrderList) {
					TradeEntrust tradeEntrust = JSON.parseObject(orderVal, TradeEntrust.class);
					if(Objects.equals(tradeEntrust.getCustomerId(), user.getId())) {
						matching.add(tradeEntrust);
					}
				}
			}
			return matching;
		}
		
		/**
		 * 根据时间倒序迭代取消
		 * 
		 * @param matching
		 */
		private void releaseEntrustTradeOder(List<TradeEntrust> matching) {
			if(matching != null && matching.size() > 15) {
				matching.sort((TradeEntrust pre, TradeEntrust rear) -> Long.valueOf(pre.getEntrustTime()).compareTo(Long.valueOf(rear.getEntrustTime())));
				for(int i = 0; i < (matching.size() - 15); i++) {
					RobotSingleton.getInstance().sendCancelEntrust(matching.get(i));
				}
			}
		}
	}
}
