package com.batsoft.common.beans.bo;

import com.batsoft.common.base.BaseBP;

/**
 * 收藏货币
 * 
 * @author simon
 */
public class SaveCoinsCollectionBP extends BaseBP {

	private static final long serialVersionUID = -1972145280082625700L;

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
