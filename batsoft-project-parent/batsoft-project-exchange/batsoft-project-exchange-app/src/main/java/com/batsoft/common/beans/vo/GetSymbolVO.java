package com.batsoft.common.beans.vo;

import java.util.List;

import com.batsoft.common.base.BaseVO;
import com.batsoft.common.beans.dto.GetSymbolDTO;

/**
 * 获取交易对
 * 
 * @author simon
 */
public class GetSymbolVO extends BaseVO {

	private static final long serialVersionUID = 5004542771157105652L;

	// 定价币代码
	private String coinCode;

	// 交易对
	private List<GetSymbolDTO> symbol;

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}

	public List<GetSymbolDTO> getSymbol() {
		return symbol;
	}

	public void setSymbol(List<GetSymbolDTO> symbol) {
		this.symbol = symbol;
	}
	
}