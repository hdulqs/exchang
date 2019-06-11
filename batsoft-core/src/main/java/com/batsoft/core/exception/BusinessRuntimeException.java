package com.batsoft.core.exception;

import java.util.HashMap;
import java.util.Map;

import com.batsoft.core.common.enums.MessageEnum;
import com.batsoft.utils.gson.GsonSingleton;

/**
 * 业务异常
 * 
 * @author simon
 */
public class BusinessRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -5540910595376107202L;

	// 提示代码
	private String code;

	// 提示信息
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

	/**
	 * 单描述构造器
	 * 
	 * @param message
	 */
	public BusinessRuntimeException(String message) {
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
	public BusinessRuntimeException(String code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}
	
	/**
	 * 系统代码构造器
	 * 
	 * @param message
	 */
	public BusinessRuntimeException(MessageEnum message) {
		super(message.getMessage());
		this.code = message.name();
		this.message = message.getMessage();
	}
	
	/**
	 * 将提示转换成Map
	 * 
	 * @return
	 */
	public final Map<String, String> toMap() {
		Map<String, String> result = new HashMap<String, String>();
		result.put("code", getCode());
		result.put("message", getMessage());
		return result;
	}
	
	/**
	 * 将提示转换成Json
	 * 
	 * @return
	 */
	public final String toJson() {
		return GsonSingleton.getInstance().toJson(toMap());
	}
	
}
