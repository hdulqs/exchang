package com.batsoft.common.beans.bo;

import java.math.BigDecimal;

import com.batsoft.common.base.BaseBP;

/**
 * 庄家挂单
 * 
 * @author simon
 */
public class BankerAddEntrustOrderBP extends BaseBP {

	private static final long serialVersionUID = -318352494031612787L;
	
	// 用户名
	private String userName;
	
	// 密码
	private String password;
	
	// 交易币
	private String tradeCoinCode;

	// 定价币
	private String pricingCoinCode;
	
	// 委托类型
	private String entrustType;
	
	// 委托数量
	private BigDecimal entrustAmout;
	
	// 委托单价
	private BigDecimal entrustPrice;

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

	public String getEntrustType() {
		return entrustType;
	}

	public void setEntrustType(String entrustType) {
		this.entrustType = entrustType;
	}

	public BigDecimal getEntrustAmout() {
		return entrustAmout;
	}

	public void setEntrustAmout(BigDecimal entrustAmout) {
		this.entrustAmout = entrustAmout;
	}

	public BigDecimal getEntrustPrice() {
		return entrustPrice;
	}

	public void setEntrustPrice(BigDecimal entrustPrice) {
		this.entrustPrice = entrustPrice;
	}
	
}
