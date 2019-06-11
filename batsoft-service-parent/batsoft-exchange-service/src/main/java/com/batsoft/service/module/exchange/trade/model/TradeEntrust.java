package com.batsoft.service.module.exchange.trade.model;


import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TradeEntrust implements Serializable {

	private static final long serialVersionUID = -2673845403572454569L;
	
	public static final String ENTRUST_TYPE_BUY = "buy";
	public static final String ENTRUST_TYPE_SELL = "sell";
	
	/**
	 * 委托状态
	 * 0:未成交
	 */
	public static final Integer ENTRUSTSTATE0 = 0;
	/**
	 * 委托状态
	 * 1:部分成交
	 */
	public static final Integer ENTRUSTSTATE1 = 1;
	/**
	 * 委托状态
	 * 2:全部成交
	 */
	public static final Integer ENTRUSTSTATE2 = 2;
	/**
	 * 委托状态
	 * 3:撤销
	 */
	public static final Integer ENTRUSTSTATE3 = 3;

	/**
	 * 客户id
	 */
	private String customerId;

	/**
	 * 账户id
	 */
	private String accountId;

	/**
	 * 交易币
	 */
	private String tradeCoinCode;

	/**
	 * 定价币
	 */
	private String pricingCoinCode;
	/**
	 * 委托单号
	 */
	private String orderId;

	/**
	 * 委托价格
	 */
	private BigDecimal entrustPrice;
	
	/**
	 * 成交均价
	 */
	private BigDecimal executedPrice;
	
	/**
	 * 委托数量,随匹配过程中变化完成侧消0
	 */
	private BigDecimal entrustAmout;

	/**
	 * 存入Sql中的原始委托数量
	 */
	private BigDecimal entrustAmoutSql;

	/**
	 * 委托类型 buy 买  sell卖
	 */
	private String entrustType;
	/**
	 * 0: "限价交易",
	 * "1": "市价交易",
	 * "2": "止损交易",
	 * "3": "止损现价交易",
	 */
	private String category;

	/**
	 * 委托状态
	 * /**
	 * 委托状态
	 * 0:未成交 1:部分成交 2:全部成交 3:撤销
	 */
	private Integer entrustState;

	/**
	 * 下单时间
	 */
	private long entrustTime;
	

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
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

	public BigDecimal getExecutedPrice() {
		return executedPrice;
	}

	public void setExecutedPrice(BigDecimal executedPrice) {
		this.executedPrice = executedPrice;
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

	public Integer getEntrustState() {
		return entrustState;
	}

	public void setEntrustState(Integer entrustState) {
		this.entrustState = entrustState;
	}

	public long getEntrustTime() {
		return entrustTime;
	}

	public void setEntrustTime(long entrustTime) {
		this.entrustTime = entrustTime;
	}

}
