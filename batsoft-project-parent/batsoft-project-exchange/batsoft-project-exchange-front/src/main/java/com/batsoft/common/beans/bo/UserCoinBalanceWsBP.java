package com.batsoft.common.beans.bo;

import com.batsoft.common.base.BaseBP;

/**
 * UserInfoWs
 * 
 * @author simon
 */
public class UserCoinBalanceWsBP extends BaseBP {

	private static final long serialVersionUID = 8360590661326365396L;
	
	// 客户ID
	private String customerId;
	
	// 交易对
	private String symbol;
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
}
