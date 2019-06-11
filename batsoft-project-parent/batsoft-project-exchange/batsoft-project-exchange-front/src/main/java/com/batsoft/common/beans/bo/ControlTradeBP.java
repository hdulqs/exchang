package com.batsoft.common.beans.bo;

import com.batsoft.common.base.BaseBP;
import com.batsoft.common.enums.ControlTradeHandleTypeEnum;

/**
 * 封禁交易
 * 
 * @author simon
 */
public class ControlTradeBP extends BaseBP {
	
	private static final long serialVersionUID = 2064491520515032766L;
	
	// 授权码
	private String handlerToken;
	
	// 操作类型
	private ControlTradeHandleTypeEnum handleType;
	
	// 交易币
	private String tradeCoinCode;

	// 定价币
	private String pricingCoinCode;
	
	public String getHandlerToken() {
		return handlerToken;
	}

	public void setHandlerToken(String handlerToken) {
		this.handlerToken = handlerToken;
	}
	
	public ControlTradeHandleTypeEnum getHandleType() {
		return handleType;
	}

	public void setHandleType(ControlTradeHandleTypeEnum handleType) {
		this.handleType = handleType;
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
	
}
