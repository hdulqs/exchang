package com.batsoft.common.beans.vo;

import com.batsoft.common.base.BaseVO;

/**
 * Get 成交单
 * 
 * @author simon
 */
public class GetTradeVO extends BaseVO {

	private static final long serialVersionUID = -5508385748451180731L;
	
	// 成交时间（时间戳）
	private String time;
	
	// 成交单价
	private String entrustPrice;
	
	// 成交数量
	private String entrustAmout;
	
	// 订单类型
	private String type;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getEntrustPrice() {
		return entrustPrice;
	}

	public void setEntrustPrice(String entrustPrice) {
		this.entrustPrice = entrustPrice;
	}

	public String getEntrustAmout() {
		return entrustAmout;
	}

	public void setEntrustAmout(String entrustAmout) {
		this.entrustAmout = entrustAmout;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "GetTradeVO [time=" + time + ", entrustPrice=" + entrustPrice + ", entrustAmout=" + entrustAmout
				+ ", type=" + type + "]";
	}
	
}
