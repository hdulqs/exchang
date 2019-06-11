package com.batsoft.model.module.exchange.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 推送委托单
 * 
 * @author simon
 */
public class PushEntrustIngDTO implements Serializable {

	private static final long serialVersionUID = 6228910953579640950L;
	
	// 委托时间
	private Long entrustTime;
	
	// 委托单价
	private BigDecimal entrustPrice;
	
	// 委托总额
	private BigDecimal entrustAmout;
	
	// 委托交易币总额
	private BigDecimal entrustAmoutSql;
	
	// 交易币
	private	String tradeCoinCode;
	
	// 定价币
	private String pricingCoinCode;
	
	// 订单号
	private String orderId;
	
	// 委托类型
	private String entrustType;
	
	/**
	 * 0: "限价交易"
	 * 
	 * "1": "市价交易"
	 * 
	 * "2": "止损交易"
	 * 
	 * "3": "止损现价交易"
	 */
	private String category;
	
	// 成交率
	private BigDecimal ratio;
	
	// 委托状态
	private Integer entrustState;
	
	// 成交量
	private BigDecimal volume;
	
	// 交易额
	private BigDecimal transactionAmount;

	public Long getEntrustTime() {
		return entrustTime;
	}

	public void setEntrustTime(Long entrustTime) {
		this.entrustTime = entrustTime;
	}

	public BigDecimal getEntrustPrice() {
		return entrustPrice;
	}

	public void setEntrustPrice(BigDecimal entrustPrice) {
		this.entrustPrice = entrustPrice;
	}

	public BigDecimal getEntrustAmout() {
		return entrustAmout;
	}

	public void setEntrustAmout(BigDecimal entrustAmout) {
		this.entrustAmout = entrustAmout;
	}

	public BigDecimal getEntrustAmoutSql() {
		return entrustAmoutSql;
	}

	public void setEntrustAmoutSql(BigDecimal entrustAmoutSql) {
		this.entrustAmoutSql = entrustAmoutSql;
	}

	public String getTradeCoinCode() {
		return tradeCoinCode;
	}

	public void setTradeCoinCode(String tradeCoinCode) {
		this.tradeCoinCode = tradeCoinCode;
	}

	public String getPricingCoinCode() {
		return pricingCoinCode;
	}

	public void setPricingCoinCode(String pricingCoinCode) {
		this.pricingCoinCode = pricingCoinCode;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getEntrustType() {
		return entrustType;
	}

	public void setEntrustType(String entrustType) {
		this.entrustType = entrustType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	public Integer getEntrustState() {
		return entrustState;
	}

	public void setEntrustState(Integer entrustState) {
		this.entrustState = entrustState;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
}
