package com.batsoft.app.member.service;

import com.batsoft.core.common.PageResult;

import java.util.Date;
import java.util.HashMap;

public interface UserBusService {
	
	
	/**
	 * 删除用户委托中的订单
	 * 
	 * @param userId
	 */
	void removeEntrustIng(String userId);
	
	/**
	 * 删除用户账户
	 * 
	 * @param userId
	 */
	void removeCustomerAccount(String userId); 
	
	/**
	 * 删除用户银行卡
	 * 
	 * @param userId
	 */
	void removeUserBankcard(String userId);
	
	/**
	 * 删除用户
	 * 
	 */
	void removeUser(String userId);

	PageResult findPageBySql(int page,int pageSize,HashMap<String,Object> params);
}
