package com.batsoft.enums;

/**
 * 交易挖矿奖励状态
 * 
 * @author simon
 */
public enum TradeDigAwardStatusCodeEnum {
	
	/**
	 * 已清算
	 * 
	 */
	HAS_CLEARING("已清算"),
	
	/**
	 * 未清算
	 * 
	 */
	NOT_CLEARING("未清算"),
	
	;
	
	private TradeDigAwardStatusCodeEnum(String message) {
		this.code = this.name();
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
