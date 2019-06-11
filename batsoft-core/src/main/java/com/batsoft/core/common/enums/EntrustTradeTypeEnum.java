package com.batsoft.core.common.enums;

/**
 * 委托交易类型
 * 
 * @author simon
 */
public enum EntrustTradeTypeEnum {
	
	/**
	 * 卖【sell】
	 * 
	 */
	SELL("sell", "卖"),
	
	/**
	 * 
	 * 
	 */
	BUY("buy", "买");
	
	private EntrustTradeTypeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	private String code;
	
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
