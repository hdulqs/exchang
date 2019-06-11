package com.batsoft.common.beans.vo;

import java.math.BigDecimal;

import com.batsoft.core.common.bean.BaseVO;

/**
 * all Ticker
 * 
 * @author simon
 */
public class TickerVO extends BaseVO {

	private static final long serialVersionUID = 1L;
	
	// 交易对
	private String symbol;

	// 交易币总量
	private BigDecimal amount;
	
	// 开盘价
	private BigDecimal open;
	
	// 最新价
	private BigDecimal close;
	
	// 最低价
	private BigDecimal low;
	
	// 最高价
	private BigDecimal high;
	
	// 定价币总量
	private BigDecimal vol;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getVol() {
		return vol;
	}

	public void setVol(BigDecimal vol) {
		this.vol = vol;
	}
	
}
