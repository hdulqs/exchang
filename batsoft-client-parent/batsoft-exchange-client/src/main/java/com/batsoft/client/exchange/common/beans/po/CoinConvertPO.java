package com.batsoft.client.exchange.common.beans.po;

import java.math.BigDecimal;

import com.batsoft.client.exchange.common.base.BaspPO;

/**
 * 货币转换接口
 * 
 * @author simon
 */
public class CoinConvertPO extends BaspPO {

	private static final long serialVersionUID = -1978218178687506602L;
	
	private BigDecimal coinAmount;
	
	private String originalType;

	public BigDecimal getCoinAmount() {
		return coinAmount;
	}

	public void setCoinAmount(BigDecimal coinAmount) {
		this.coinAmount = coinAmount;
	}

	public String getOriginalType() {
		return originalType;
	}

	public void setOriginalType(String originalType) {
		this.originalType = originalType;
	}
	
}
