package com.batsoft.common.enums;

/**
 * 响应状态码
 * 
 * @author simon
 */
public enum ResponseStatusCodeEnum {
	
	/**
	 * 成功
	 * 
	 */
	SUCCESS("0000", "成功"),
	
	/**
	 * 失败
	 * 
	 */
	FAIL("-1","失败"),
	;
	
	private ResponseStatusCodeEnum(String value, String message) {
		this.code = this.name();
		this.value = value;
		this.message = message;
	}
	
	private String code;
	
	private String value;
	
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
