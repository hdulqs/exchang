package com.batsoft.model.module.exchange.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *  查询每人每日usdt交易流水
 * 
 * @author simon
 */
public class FindCustomerEverydayRecordVO implements Serializable {

	private static final long serialVersionUID = -2538007402457566271L;
	
	// 奖励数量
	private BigDecimal awardAmount;
	
	// 交易货币代码
	private String coinCode;
	
	// 客户ID
	private String customerId;
	
	// 交易币
	private String tradeCoinCode;
	
	// 定价币
	private String pricingCoinCode;
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getAwardAmount() {
		return awardAmount;
	}

	public void setAwardAmount(BigDecimal awardAmount) {
		this.awardAmount = awardAmount;
	}

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
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

	@Override
	public String toString() {
		return "FindCustomerEverydayRecordVO [awardAmount=" + awardAmount + ", coinCode=" + coinCode + ", customerId="
				+ customerId + ", tradeCoinCode=" + tradeCoinCode + ", pricingCoinCode=" + pricingCoinCode + "]";
	}
	
}
