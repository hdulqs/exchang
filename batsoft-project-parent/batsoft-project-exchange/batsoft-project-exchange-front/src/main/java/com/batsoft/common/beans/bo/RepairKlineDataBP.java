package com.batsoft.common.beans.bo;

import java.math.BigDecimal;

import com.batsoft.common.base.BaseBP;

/**
 * 修复K线图
 * 
 * @author simon
 */
public class RepairKlineDataBP extends BaseBP {

	private static final long serialVersionUID = 329564905579957804L;
	
	// 交易对
	private String symbol;
	
	// 修复范围控制【最大值】
	private BigDecimal maxPrice;
	
	// 修复范围控制【最小值】
	private BigDecimal minPrice;
	
	// 开始修复时间
	private Long beginTime;
	
	// 结束修复时间
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
