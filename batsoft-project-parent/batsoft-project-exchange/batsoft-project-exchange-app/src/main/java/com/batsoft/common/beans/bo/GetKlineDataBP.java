package com.batsoft.common.beans.bo;

import com.batsoft.common.base.BaseBP;

/**
 * 请求K线图数据
 * 
 * @author simon
 */
public class GetKlineDataBP extends BaseBP {

	private static final long serialVersionUID = -7773300113368321844L;

	// 交易对
	private String symbol;

	// 时间类型
	private String time;

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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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
		return "LoadKlineDataBO [symbol=" + symbol + ", time=" + time + ", from=" + from + ", to=" + to + "]";
	}

}
