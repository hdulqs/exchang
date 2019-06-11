package com.batsoft.common.beans.bo;

import com.batsoft.common.base.BaseBP;

/**
 * 交易对小数位限制
 * 
 * @author simon
 */
public class SymbolDecimalBP extends BaseBP {

	private static final long serialVersionUID = -936924263799444224L;
	
	// 交易对
	private String symbol;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
}
