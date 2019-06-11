package com.batsoft.exception;

import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.core.exception.BusinessRuntimeException;

/**
 * module Business Exception
 * 
 * @author simon
 */
public class ModuleBusinessRuntimeException extends BusinessRuntimeException {
	
	private static final long serialVersionUID = 2161949219181059357L;

	/**
	 * 单参数构造器
	 * 
	 * @param message
	 */
	public ModuleBusinessRuntimeException(String message) {
		super(message);
	}

	/**
	 * 完全参数构造器
	 * 
	 * @param code
	 * @param message
	 */
	public ModuleBusinessRuntimeException(String code, String message) {
		super(message);
	}
	
	/**
	 * MessageEnum构造器
	 * 
	 * @param message
	 * 			提示代码枚举
	 */
	public ModuleBusinessRuntimeException(ModuleMessageEnum message) {
		super(message.getCode(), message.getMessage());
	}
}
