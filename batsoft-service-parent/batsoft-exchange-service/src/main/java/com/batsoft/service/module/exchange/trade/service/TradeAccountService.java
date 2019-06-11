package com.batsoft.service.module.exchange.trade.service;

import java.math.BigDecimal;
import java.util.List;

import com.batsoft.model.module.exchange.CustomerAccount;

public interface TradeAccountService {
	
	/**
	 * 货币账户
	 * 
	 * @param userId
	 * 			用户UID
	 * @return
	 */
	List<CustomerAccount> listByUserId(String userId);
	
	/**
	 * 货币账户
	 * 
	 * @param userId
	 * 			用户ID
	 * @param coinCode
	 * 			货币代码
	 * @return
	 */
	CustomerAccount getCustomerAccountByUserId(String userId, String coinCode);
	
	/**
	 * 设置货币账户到Redis中
	 * 
	 * @param userId
	 * 				用户ID
	 * @param coinAccount
	 * 				货币账户
	 * @return
	 */
	void setCoinAccountToRedis(String userId, CustomerAccount coinAccount);
	
	/**
	 * 更新账户可用余额
	 * 
	 * @param userId
	 * 			用户代码
	 * @param coinCode
	 * 			货币代码
	 * @param amount
	 * 			数量【为整数代表增加可用余额，为负数代表减少可用】
	 * @return
	 */
	CustomerAccount updateCoinAvailable(String userId, String coinCode, BigDecimal amount);
	
	/**
	 * 更新账户冻结余额
	 * 
	 * @param userId
	 * 			用户代码
	 * @param coinCode
	 * 			货币代码
	 * @param amount
	 * 			数量【为整数代表增加可用余额，为负数代表减少可用】
	 * @return
	 */
	CustomerAccount updateCoinFreeze(String userId, String coinCode, BigDecimal amount);
	
	/**
	 * 同时修改账户冻结和可用
	 * 
	 * @param userId
	 * 			用户代码
	 * @param coinCode
	 * 			货币代码
	 * @param available
	 * 			可用金额
	 * @param freeze
	 * 			冻结金额
	 * @return
	 */
	CustomerAccount updateCoinAmount(String userId, String coinCode, BigDecimal available, BigDecimal freeze);
}
