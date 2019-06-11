package com.batsoft.common.beans.vo;

import java.math.BigDecimal;

import com.batsoft.common.base.BaseVO;

/**
 * Get Ticker
 * 
 * @author simon
 */
public class GetTickerVO extends BaseVO{
	//["BTC_USDT 【交易对】","","","","","","378290.00【24h涨跌】 ","3783.89990【 最新价】","47400.74875 【24h成交量】","3961.16010 【24h 最高价】","37.0 【24h 最低价】","","","3942.49990","26033.23"]
	private static final long serialVersionUID = -983776131306252883L;
	
	// 交易对
	private String symbol;
	
	// 24H涨跌
	private String rate;
	
	// 24H最高价格
	private String high;
	
	// 24小时最低价格
	private String low;
	
	// 24H成交量[交易币]
	private String amount;
	
	// 最新价
	private String close;
	
	// 估价（CNY）
	private BigDecimal evaluateCny;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public BigDecimal getEvaluateCny() {
		return evaluateCny;
	}

	public void setEvaluateCny(BigDecimal evaluateCny) {
		this.evaluateCny = evaluateCny;
	}
	
}
