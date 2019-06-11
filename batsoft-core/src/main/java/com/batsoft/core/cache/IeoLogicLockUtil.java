package com.batsoft.core.cache;

import com.batsoft.core.cache.LogicLockSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.enums.CHS;

/**
 * ieo逻辑锁
 * 
 * @author simon
 */
public class IeoLogicLockUtil {
	
	private LogicLockSignleton logicLock = LogicLockSignleton.getInstance();
	
	// 交易币
	private String tradeCoinCode;
	
	// 定价币
	private String pricingCoinCode;
	
	public IeoLogicLockUtil(String tradeCoinCode, String pricingCoinCode) {
		super();
		this.tradeCoinCode = tradeCoinCode;
		this.pricingCoinCode = pricingCoinCode;
	}
	
	/**
	 * lock Key
	 * 
	 * @return
	 */
	public String lockKey() {
		String symbol = tradeCoinCode + CHS.underline.getValue() + pricingCoinCode;
		return String.format(RedisKeyConstant.HANDLER_LOGIC_LOCK_KEY, symbol);
	}
	
	/**
	 * ieo逻辑锁控制
	 * 
	 * @param count
	 * 			循环次数
	 * @return
	 */
	public boolean logicLock(int count) {
    	try {
    		
    		if(count > 200) {
    			return false;
    		}else if(logicLock.verifyKeyExist(lockKey())) {
    			Thread.sleep(10);
	    	}else {
	    		return true;
	    	}
    	} catch (InterruptedException e) {
			e.printStackTrace();
		}
        return logicLock(++count);
    }
	
}
