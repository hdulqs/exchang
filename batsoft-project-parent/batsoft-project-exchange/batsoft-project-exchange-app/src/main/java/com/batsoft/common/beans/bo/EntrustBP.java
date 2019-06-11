package com.batsoft.common.beans.bo;

import com.batsoft.common.base.BaseBP;

/**
 * 委托下单
 * 
 * @author simon
 */
public class EntrustBP extends BaseBP {

	private static final long serialVersionUID = 6629525464563816015L;
	
	// 委托价格
	private String price;
	
	// 委托数量
	private String amout;
    
	// 委托类型
	private String type;
	
	// 交易币
	private String tradeCode;
    
	// 定价币
	private String pricingCode;
	
	/**
	 * 0: "限价交易",
	 * "1": "市价交易",
	 * "2": "止损交易",
	 * "3": "止损现价交易",
	 */
	private String category;
    
	// 交易密码
	private String tradePwd;

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAmout() {
		return amout;
	}

	public void setAmout(String amout) {
		this.amout = amout;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public String getPricingCode() {
		return pricingCode;
	}

	public void setPricingCode(String pricingCode) {
		this.pricingCode = pricingCode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTradePwd() {
		return tradePwd;
	}

	public void setTradePwd(String tradePwd) {
		this.tradePwd = tradePwd;
	}
 
}
