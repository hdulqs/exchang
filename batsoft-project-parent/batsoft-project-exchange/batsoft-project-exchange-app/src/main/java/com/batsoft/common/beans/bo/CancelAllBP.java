package com.batsoft.common.beans.bo;

import com.batsoft.common.base.BaseBP;

/**
 * 取消全部订单
 * 
 * @author simon
 */
public class CancelAllBP extends BaseBP {
	
	private static final long serialVersionUID = 5619805319596392032L;
	
	// 交易对
	private String symbol;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
