package com.batsoft.common.beans.bo;

import com.batsoft.common.base.BaseBP;

/**
 * 取消委托订单
 * 
 * @author simon
 */
public class CancelBP extends BaseBP {

	private static final long serialVersionUID = 735203987009176611L;
	
	// 订单号
	private String orderId;
	
	// 交易对
	private String symbol;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
