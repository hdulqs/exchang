package com.batsoft.common.beans.bo;

import com.batsoft.common.base.BaseBP;

/**
 * 请求K线图数据
 * 
 * @author simon
 */
public class LoadKlineDataBP extends BaseBP {

	private static final long serialVersionUID = -7773300113368321844L;
	
	// 交易对
	private String symbol;
	
	// 时间类型
	private String subject; 
	
	// 开始时间戳
	private Long from; 
	
	// 结束时间戳
	private Long to;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Long getFrom() {
		return from;
	}

	public void setFrom(Long from) {
		this.from = from;
	}

	public Long getTo() {
		return to;
	}

	public void setTo(Long to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "LoadKlineDataBO [symbol=" + symbol + ", subject=" + subject + ", from=" + from + ", to=" + to + "]";
	}
	
}
