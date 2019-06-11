package com.batsoft.exception;

import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.core.exception.BusinessIllegalArgumentException;

/**
 * Module Business Param Exception
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
		super(code, message);
	}
	
	/**
	 * MessageEnum构造器【推荐使用，此方法在行参数构造器内兼容国际化】
	 * 
	 * @param message
	 * 			提示代码枚举
	 */
	public ModuleBusinessIllegalArgumentException(ModuleMessageEnum message) {
		super(message.getCode(), message.getMessage());
	}
	
}
