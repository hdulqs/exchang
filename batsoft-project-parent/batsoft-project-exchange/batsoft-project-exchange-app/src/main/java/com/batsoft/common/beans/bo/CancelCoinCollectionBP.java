package com.batsoft.common.beans.bo;

import com.batsoft.common.base.BaseBP;

/**
 * 取消收藏
 * 
 * @author simon
 */
public class CancelCoinCollectionBP extends BaseBP {

	private static final long serialVersionUID = 913672276602292346L;
	
	// 交易币
	private String tradeCoinCode;
	
	// 定价币
	private String pricingCoinCode;

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
	
}
