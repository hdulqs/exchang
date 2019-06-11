package com.batsoft.model.module.exchange.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 查询USDT交易数量
 * 
 * @author simon
 */
public class IndexUsdtTradeAmount implements Serializable {

	private static final long serialVersionUID = 1670155273793098828L;
	
	// 交易货币数量
	private BigDecimal tradeCoinAmount;
	
	// 手续费货币数量
	private BigDecimal feeCoinAmount;

	public BigDecimal getTradeCoinAmount() {
		return tradeCoinAmount;
	}

	public void setTradeCoinAmount(BigDecimal tradeCoinAmount) {
		this.tradeCoinAmount = tradeCoinAmount;
	}

	public BigDecimal getFeeCoinAmount() {
		return feeCoinAmount;
	}

	public void setFeeCoinAmount(BigDecimal feeCoinAmount) {
		this.feeCoinAmount = feeCoinAmount;
	}
	
}
