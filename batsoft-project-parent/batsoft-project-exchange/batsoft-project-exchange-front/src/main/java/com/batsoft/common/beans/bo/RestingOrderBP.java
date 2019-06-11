package com.batsoft.common.beans.bo;

import java.math.BigDecimal;

import com.batsoft.common.base.BaseBP;

/**
 * 提前下单指令
 * 
 * @author simon
 */
public class RestingOrderBP extends BaseBP {

	private static final long serialVersionUID = -7110250405159776334L;
	
	// 授权码
	private String handlerToken;

	// 用户名
	private String userName;
	
	// 密码
	private String password;
	
	// 交易币
	private String tradeCoinCode;

	// 定价币
	private String pricingCoinCode;
	
	// 最小委托数量
	private BigDecimal minEntrustAmout;
	
	// 最大委托数量
	private BigDecimal maxEntrustAmout;
	
	// 委托单价
	private BigDecimal entrustPrice;
	
	// 委托总数量
	private BigDecimal totalEntrustAmout;
	
	// 委托类型
	private String entrustType;
	
	// 下单间隔时间
	private Long sleepTime;
	
	public String getHandlerToken() {
		return handlerToken;
	}

	public void setHandlerToken(String handlerToken) {
		this.handlerToken = handlerToken;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTradeCoinCode() {
		return tradeCoinCode;
	}

	public void setTradeCoinCode(String tradeCoinCode) {
		this.tradeCoinCode = tradeCoinCode;
	}

	public String getPricingCoinCode() {
		return pricingCoinCode;
	}

	public void setPricingCoinCode(String pricingCoinCode) {
		this.pricingCoinCode = pricingCoinCode;
	}

	public BigDecimal getMinEntrustAmout() {
		return minEntrustAmout;
	}

	public void setMinEntrustAmout(BigDecimal minEntrustAmout) {
		this.minEntrustAmout = minEntrustAmout;
	}

	public BigDecimal getMaxEntrustAmout() {
		return maxEntrustAmout;
	}

	public void setMaxEntrustAmout(BigDecimal maxEntrustAmout) {
		this.maxEntrustAmout = maxEntrustAmout;
	}

	public BigDecimal getEntrustPrice() {
		return entrustPrice;
	}

	public void setEntrustPrice(BigDecimal entrustPrice) {
		this.entrustPrice = entrustPrice;
	}

	public BigDecimal getTotalEntrustAmout() {
		return totalEntrustAmout;
	}

	public void setTotalEntrustAmout(BigDecimal totalEntrustAmout) {
		this.totalEntrustAmout = totalEntrustAmout;
	}

	public String getEntrustType() {
		return entrustType;
	}

	public void setEntrustType(String entrustType) {
		this.entrustType = entrustType;
	}

	public Long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(Long sleepTime) {
		this.sleepTime = sleepTime;
	}
	
}
