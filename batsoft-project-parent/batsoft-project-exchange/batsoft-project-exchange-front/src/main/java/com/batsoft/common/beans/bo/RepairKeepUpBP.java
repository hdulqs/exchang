package com.batsoft.common.beans.bo;

import java.math.BigDecimal;

import com.batsoft.common.base.BaseBP;

/**
 * 修改BtUsdt数据成一直涨
 * 
 * @author simon
 */
public class RepairKeepUpBP extends BaseBP {

	private static final long serialVersionUID = -3840625102550136899L;
	
	private String symbol = "BT_USDT";
	
	private BigDecimal maxPrice;
	
	private BigDecimal minPrice;
	
	private Long beginTime;
	
	private Long endTime;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	
}
