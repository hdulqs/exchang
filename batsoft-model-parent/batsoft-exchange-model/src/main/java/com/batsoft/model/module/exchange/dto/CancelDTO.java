package com.batsoft.model.module.exchange.dto;

import java.io.Serializable;

/**
 * 撤销订单DTO
 * 
 * @author simon
 */
public class CancelDTO implements Serializable {

	private static final long serialVersionUID = 836123122700280680L;
	
	// 委托中的订单号
	private String orderId;
	
	// 用户ID
	private String userId;
	
	// 交易对
	private String coinPair;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCoinPair() {
		return coinPair;
	}

	public void setCoinPair(String coinPair) {
		this.coinPair = coinPair;
	}
}
