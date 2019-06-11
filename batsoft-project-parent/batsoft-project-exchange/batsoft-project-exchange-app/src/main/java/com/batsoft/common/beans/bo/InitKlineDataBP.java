package com.batsoft.common.beans.bo;

import com.batsoft.common.base.BaseBP;

/**
 * K线图数据推送
 * 
 * @author simon
 */
public class InitKlineDataBP extends BaseBP {

	private static final long serialVersionUID = -609519101139328854L;
	
	// 交易对
	private String symbol;
	
	// 时间类型
	private String time; 
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "PushKlineDataBP [symbol=" + symbol + ", time=" + time + "]";
	}
	
}
