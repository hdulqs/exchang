package com.batsoft.core.common.enums;

import java.util.HashMap;
import java.util.Map;

import com.batsoft.core.common.i18n.LanguageConvetSingleton;
import com.batsoft.utils.gson.GsonSingleton;

/**
 * 提示代码枚举类
 * 
 * @author simon
 */
public enum MessageEnum {
	
	/**
	 * 成功
	 * 
	 */
	SUCCESS,
	
	/**
	 * 失败 
	 * 
	 */
	FAIL, 
	
	/**
	 * 源货币类型无法直接装换成USDT
	 * 
	 */
	ORIGINAL_COIN_TYPE_NOT_TO_USDT_COIN,
	
	/**
	 * 未知异常
	 * 
	 */ 
	UNKNOWN_EXCETION, 
	
	/**
	 * 数据存在约束【SQL】执行失败
	 * 
	 */
	DATA_EXIST_RESTRAIN, 
	
	/**
	 * 空指针异常
	 * 
	 */
	NULL_POINTER_EXCEPTION,
	 
	;
	
	private MessageEnum() {
		this.code = this.name();
		this.message = LanguageConvetSingleton.getInstance().message(this.name());
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

	/**
	 * 将提示枚举转换成Map
	 * 
	 * @return
	 */
	public Map<String, String> toMap() {
		Map<String, String> result = new HashMap<String, String>();
		result.put(KeyEnum.code.name(), this.getCode());
		result.put(KeyEnum.message.name(), this.getMessage());
		return result;
	}

	public Map<String, String> toMap(Exception e) {
		Map<String, String> result = new HashMap<String, String>();
		result.put(KeyEnum.code.name(), this.getCode());
		result.put(KeyEnum.message.name(), e.getMessage());
		return result;
	}
	
	/**
	 * 将提示装换成Json
	 * 
	 * @return
	 */
	public String toJson() {
		return GsonSingleton.getInstance().toJson(toMap());
	}
	
	
}
