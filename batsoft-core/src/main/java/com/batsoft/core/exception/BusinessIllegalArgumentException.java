package com.batsoft.core.exception;

import java.util.HashMap;
import java.util.Map;

import com.batsoft.core.common.enums.MessageEnum;
import com.batsoft.utils.gson.GsonSingleton;

/**
 * 参数异常
 * 
 * @author simon
 */
public class BusinessIllegalArgumentException extends IllegalArgumentException {

	private static final long serialVersionUID = -586421805538664983L;

	// 提示代码
	private String code;

	// 提示信息
	private String message;

	/**
	 * 单参数构造器
	 * 
	 * @param message
	 */
	public BusinessIllegalArgumentException(String message) {
		super(message);
		this.code = MessageEnum.FAIL.getCode();
		this.message = message;
	}

	/**
	 * 完全参数构造器
	 * 
	 * @param code
	 * @param message
	 */
	public BusinessIllegalArgumentException(String code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}
	
	/**
	 * MessageEnum构造器
	 * 
	 * @param message
	 * 			提示代码枚举
	 */
	public BusinessIllegalArgumentException(MessageEnum message) {
		super(message.getMessage());
		this.code = message.name();
		this.message = message.getMessage();
	}
	
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

	public final Map<String, String> toMap() {
		Map<String, String> result = new HashMap<String, String>();
		result.put("code", getCode());
		result.put("message", getMessage());
		return result;
	}
	
	public final String toJson() {
		return GsonSingleton.getInstance().toJson(toMap());
	}
	
}
