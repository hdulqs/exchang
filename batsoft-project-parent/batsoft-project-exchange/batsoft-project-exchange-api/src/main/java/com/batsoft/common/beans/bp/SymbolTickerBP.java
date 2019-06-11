package com.batsoft.common.beans.bp;

import com.batsoft.core.common.bean.BaseBP;

/**
 * 指定交易对行情数据
 * 
 * @author simon
 */
public class SymbolTickerBP extends BaseBP {

	private static final long serialVersionUID = 1L;
	
	private String symbol;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
}
