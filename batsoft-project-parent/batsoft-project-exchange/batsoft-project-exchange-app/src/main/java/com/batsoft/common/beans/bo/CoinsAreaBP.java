package com.batsoft.common.beans.bo;

import com.batsoft.common.base.BaseVO;

/**
 * 获取交易对
 * 
 * @author simon
 */
public class CoinsAreaBP extends BaseVO {
	
	private static final long serialVersionUID = 3686234481487424396L;
	
	// 定价币
	private String pricingCoinCode;

	public String getPricingCoinCode() {
		return pricingCoinCode;
	}

	public void setPricingCoinCode(String pricingCoinCode) {
		this.pricingCoinCode = pricingCoinCode;
	}
	
}
