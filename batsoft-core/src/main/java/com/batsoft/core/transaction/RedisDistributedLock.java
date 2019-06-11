package com.batsoft.core.transaction;

import java.util.Collections;

import com.batsoft.core.cache.JedisDataSourceSignleton;

import redis.clients.jedis.Jedis;

/**
 * Redis分布式锁
 * 
 * @author simon
 */
public class RedisDistributedLock {
	
	/**
	 *  分布式事务锁键配置
	 * 
	 */
	public static enum RedisDistributedKeyEnum {
		
		/**
		 * 委托【交易对】锁
		 * 
		 */
		ENTRUST_TRADE_COIN_PAIR_KEY("EXCHANGE:TRADELOCK:COIN:PAIR:%s"),
		
		/**
		 * 委托交易客户锁
		 * 
		 */
		ENTRUST_TRADE_PERSION_KEY("EXCHANGE:TRADELOCK:PERSION:%s");
		
		
		private RedisDistributedKeyEnum(String value) {
			this.value = value;
		}
		
		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
	}
	
	/**
	 * 	数据插入成功返回OK
	 * 
	 */
    private static final String LOCK_VALUE = "OK";
    
    /**
     * 	NX：即当key不存在时，进行set操作；若key已经存在，则不做任何操作
     * 
     */
    private static final String SET_IF_NOT_EXIST = "NX";
    
    /**
     *	 数据过期时间交给time处理；PX代表过期时间单位为毫秒
     * 
     */
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    
    /**
     * 	释放锁成功返回的标识
     * 
     */
    private static final Long UN_LOCK_VALUE = 1L;
    
    /**
     * 	默认锁定时间
     * 
     */
    private static final int DEFAULT_LOCK_TIME = 5000;
    
    /**
     * 	重试获取锁的时间(ms)
     * 
     */
    private static final int RETRY_TIME_MILLISECOND = 5;
    
    /**
     * 	EMP:计算时间 
     * 
     */
    private long runTime = 0L; 
    
    public long getRunTime() {
		return runTime;
	}

	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}
	
	/**
     * 	获取分布式锁
     *
     * @param key        
     * 				锁key
     * @param value  
     * 				值
     * @return 获取锁的状态
     */
    public boolean lock(final String key, final String value) {
    	
        return lock(key, value, DEFAULT_LOCK_TIME);
    }

    /**
     * 	获取分布式锁
     *
     * @param key        
     * 				锁key
     * @param value  
     * 				值
     * @param expireTime 
     * 				过期销毁时间
     * @return 获取锁的状态
     */
    public boolean lock(final String key, final String value, int expireTime) {
    	Jedis jedis = JedisDataSourceSignleton.getInstance().getJedis();
        String result = jedis.set(key, value, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_VALUE.equals(result)) {
            if(jedis != null) {
            	jedis.close();
            }
        	return true;
        }
        try {
			Thread.sleep(RETRY_TIME_MILLISECOND);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if(jedis != null) {
	        	jedis.close();
	        }
		}
        return lock(key, value, expireTime);
    }
    
    /**
     * 	释放分布式锁
     *
     * @param key   
     * 			锁的KEY
     * @param value 
     * 			值
     * @return 释放成功
     */
    public boolean unLock(final String key, final String value, int count) {
    	if(count > 1) return false; // 尝试多次无果后释放递归
    	Jedis jedis = JedisDataSourceSignleton.getInstance().getJedis();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(value));
        if (UN_LOCK_VALUE.equals(result)) {
        	if(jedis != null) {
            	jedis.close();
            }
            return true;
        }
        try {
			Thread.sleep(RETRY_TIME_MILLISECOND);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if(jedis != null) {
	        	jedis.close();
	        }
		}
        return unLock(key, value, ++count);
    }
    
}
