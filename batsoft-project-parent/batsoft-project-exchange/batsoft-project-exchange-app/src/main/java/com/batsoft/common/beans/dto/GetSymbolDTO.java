package com.batsoft.common.beans.dto;

import java.io.Serializable;

/**
 * 交易对模型
 * 
 * @author simon
 */
public class GetSymbolDTO implements Serializable {
	
	private static final long serialVersionUID = 1487186910375103647L;

	// 定价币
	private String pricingCoinCode;
	
	// 交易币
	private String tradeCoinCode;
	
	// 最新价
	private String newPrice;
	
	// 涨跌幅
	private String rate;
	
	// 是否收藏
	private String collectIdent;
	
	// IGO开始时间
	private Long beginTime;

	public String getPricingCoinCode() {
		return pricingCoinCode;
	}

	public void setPricingCoinCode(String pricingCoinCode) {
		this.pricingCoinCode = pricingCoinCode;
	}

	public String getTradeCoinCode() {
		return tradeCoinCode;
	}

	public void setTradeCoinCode(String tradeCoinCode) {
		this.tradeCoinCode = tradeCoinCode;
	}

	public String getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getCollectIdent() {
		return collectIdent;
	}

	public void setCollectIdent(String collectIdent) {
		this.collectIdent = collectIdent;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}
}
