package com.batsoft.common.beans.vo;

import com.batsoft.common.base.BaseVO;

/**
 * 交易对小数位数限制
 * 
 * @author simon
 */
public class SymbolDecimalVO extends BaseVO {

	private static final long serialVersionUID = -4268857260811681892L;
	
	// 数量小数位
	private Integer amtDecimal;
	
	// 价格小数位
	private Integer priceDecimal;
	
	// 总量小数位
	private Integer amountDecimal;

	public Integer getAmtDecimal() {
		return amtDecimal;
	}

	public void setAmtDecimal(Integer amtDecimal) {
		this.amtDecimal = amtDecimal;
	}

	public Integer getPriceDecimal() {
		return priceDecimal;
	}

	public void setPriceDecimal(Integer priceDecimal) {
		this.priceDecimal = priceDecimal;
	}

	public Integer getAmountDecimal() {
		return amountDecimal;
	}

	public void setAmountDecimal(Integer amountDecimal) {
		this.amountDecimal = amountDecimal;
	}
	
}
