package com.batsoft.common.beans.bo;

import java.math.BigDecimal;

import com.batsoft.common.base.BaseBP;

/**
 * 同步K线图维度
 * 
 * @author simon
 */
public class SyncKlineBP extends BaseBP {

	private static final long serialVersionUID = -3320947389318646557L;
	
	// 授权码
	private String handlerToken;
	
	// 参照交易对
	private String refSymbol;
	
	// 被修改的交易对
	private String upSymbol;
	
	// 初始价格
	private BigDecimal openPrice;
	
	// 开始时间
	private Long beginTime;
	
	// 结束时间
	private Long endTime;
	
	public String getHandlerToken() {
		return handlerToken;
	}

	public void setHandlerToken(String handlerToken) {
		this.handlerToken = handlerToken;
	}

	public String getRefSymbol() {
		return refSymbol;
	}

	public void setRefSymbol(String refSymbol) {
		this.refSymbol = refSymbol;
	}

	public String getUpSymbol() {
		return upSymbol;
	}

	public void setUpSymbol(String upSymbol) {
		this.upSymbol = upSymbol;
	}

	public BigDecimal getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(BigDecimal openPrice) {
		this.openPrice = openPrice;
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
