package com.batsoft.common.beans.vo;

import java.math.BigDecimal;

import com.batsoft.common.base.BaseVO;

/**
 * 币余额
 * 
 * @author simon
 */
public class CoinBalanceVO extends BaseVO {

	private static final long serialVersionUID = -8878795885601097749L;

	// 币代码
	private String coin;

	// 余额
	private BigDecimal balance;

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}
