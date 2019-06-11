package com.batsoft.common.beans.bo;

import com.batsoft.common.base.BaseBP;

/**
 * K线图数据推送
 * 
 * @author simon
 */
public class PushKlineDataBP extends BaseBP {

	private static final long serialVersionUID = -609519101139328854L;
	
	// 交易对
	private String symbol;
	
	// 时间类型
	private String subject; 
	
	private String channel;

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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "PushKlineDataBP [symbol=" + symbol + ", subject=" + subject + ", channel=" + channel + "]";
	}
	
}
