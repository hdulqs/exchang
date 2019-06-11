package com.batsoft.model.module.exchange.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 委托挂单DTO
 * 
 * @author simon
 */
public class EntrustTicKetDTO implements Serializable {
	
	private static final long serialVersionUID = 9214793987828186593L;

	// 委托客户ID
	private String customerId;
	
	// 交易币
	private String tradeCoinCode;
	
	// 定价币
	private String pricingCoinCode;
	
	// 订单号
	private String orderId;
	
	// 委托单价
	private BigDecimal entrustPrice;
	
	// 委托数量
	private BigDecimal entrustAmout;
	
	// 委托数量总额[不参与+-运算]
	private BigDecimal entrustAmoutSql;
	
	// 委托类型
	private String entrustType;
	
	// 委托订单类型
	private String category;
	
	// 委托状态
	private Long entrustState;
	
	// 委托时间
	private Long entrustTime;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

	public Long getEntrustState() {
		return entrustState;
	}

	public void setEntrustState(Long entrustState) {
		this.entrustState = entrustState;
	}

	public Long getEntrustTime() {
		return entrustTime;
	}

	public void setEntrustTime(Long entrustTime) {
		this.entrustTime = entrustTime;
	}
}
