package com.batsoft.core.cache;

import java.math.BigDecimal;

import com.batsoft.core.common.RedisKeyConstant;

/**
 * 数据业务逻辑锁
 * 
 * @author simon
 */
public class LogicLockSignleton {
	
	private static LogicLockSignleton instance = new LogicLockSignleton();

	public static LogicLockSignleton getInstance() {
		return instance;
	}
	
	private RedisCacheSignleton redisCache = RedisCacheSignleton.getInstance();
	
	/**
	 * 设置数据逻辑锁
	 * 
	 * @param key
	 * 			键
	 * @return
	 */
	public boolean setLogicLock(String key) {
		String logicKey = String.format(RedisKeyConstant.CANCEL_ENTRUSTING_LOGIC, key);
		if(redisCache.verifyKeyExist(logicKey)){
			return Boolean.FALSE;
		}
		return redisCache.set(logicKey, BigDecimal.ZERO, 60 * 60 * 24 * 7);
	}
	
	/**
	 * 设置数据逻辑锁
	 * 
	 * @param key
	 * 			键
	 * @return
	 */
	public boolean setLogicLock(String key, Long second) {
		String logicKey = String.format(RedisKeyConstant.CANCEL_ENTRUSTING_LOGIC, key);
		if(redisCache.verifyKeyExist(logicKey)){
			return Boolean.FALSE;
		}
		return redisCache.set(logicKey, BigDecimal.ZERO, second);
	}
	
	/**
	 * 设置数据逻辑锁
	 * 
	 * @param key
	 * 			键
	 * @param value
	 * 			值
	 * @return
	 */
	public boolean setLogicLock(String key, Object value) {
		String logicKey = String.format(RedisKeyConstant.CANCEL_ENTRUSTING_LOGIC, key);
		if(redisCache.verifyKeyExist(logicKey)){
			return Boolean.FALSE;
		}
		return redisCache.set(logicKey, value, 60 * 60 * 24 * 7);
	}
	
	/**
	 * 取消业务逻辑锁
	 * 
	 * @param key
	 */
	public void cencelLogicLock(String key) {
        String logicKey = String.format(RedisKeyConstant.CANCEL_ENTRUSTING_LOGIC, key);
        redisCache.del(logicKey);
	}
	
	/**
	 * 校验业务逻辑锁是否存在
	 * 
	 * @param key
	 * 
	 * @return 存在true : 不存在 false
	 */
	public boolean verifyKeyExist(String key) {
		String logicKey = String.format(RedisKeyConstant.CANCEL_ENTRUSTING_LOGIC, key);
		return redisCache.verifyKeyExist(logicKey);
	}
	
}
