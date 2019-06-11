package com.batsoft.common.beans.vo;

import java.math.BigDecimal;

import com.batsoft.core.common.bean.BaseVO;

/**
 * dataAll接口返回参数
 * 
 * @author simon
 */
public class DataAllVO extends BaseVO {

	private static final long serialVersionUID = 1L;
	
	private String symbol;
	
	private String anchor;
	
	private BigDecimal lastPrice;
	
	private BigDecimal volume_24h;
	
	private BigDecimal amount_24h;
	
	private long priceUpdatedAt;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	public BigDecimal getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(BigDecimal lastPrice) {
		this.lastPrice = lastPrice;
	}

	public BigDecimal getVolume_24h() {
		return volume_24h;
	}

	public void setVolume_24h(BigDecimal volume_24h) {
		this.volume_24h = volume_24h;
	}

	public BigDecimal getAmount_24h() {
		return amount_24h;
	}

	public void setAmount_24h(BigDecimal amount_24h) {
		this.amount_24h = amount_24h;
	}

	public long getPriceUpdatedAt() {
		return priceUpdatedAt;
	}

	public void setPriceUpdatedAt(long priceUpdatedAt) {
		this.priceUpdatedAt = priceUpdatedAt;
	}
}
