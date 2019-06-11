package com.batsoft.exception;

import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.core.exception.BusinessIllegalArgumentException;

/**
 * 模块参数异常
 * 
 * @author simon
 */
public class ModuleBusinessIllegalArgumentException extends BusinessIllegalArgumentException {
	
	private static final long serialVersionUID = 4372047199783679043L;

	/**
	 * 单参数构造器
	 * 
	 * @param message
	 */
	public ModuleBusinessIllegalArgumentException(String message) {
		super(message);
	}

	/**
	 * 完全参数构造器
	 * 
	 * @param code
	 * @param message
	 */
	public ModuleBusinessIllegalArgumentException(String code, String message) {
		super(message);
	}
	
	/**
	 * MessageEnum构造器
	 * 
	 * @param message
	 * 			提示代码枚举
	 */
	public ModuleBusinessIllegalArgumentException(ModuleMessageEnum message) {
		super(message.getCode(), message.getMessage());
	}
	
}
