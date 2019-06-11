package com.batsoft.model.module.exchange.dto;

import java.io.Serializable;

/**
 * 取消全部订单DTO
 * 
 * @author simon
 */
public class CancelAllDTO implements Serializable {

	private static final long serialVersionUID = 6338202566665929199L;
	
	// 用户ID
	private String userId;
	
	// 交易对
	private String coinPair;

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
