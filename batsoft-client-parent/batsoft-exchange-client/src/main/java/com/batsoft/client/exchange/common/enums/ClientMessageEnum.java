package com.batsoft.client.exchange.common.enums;

import java.util.HashMap;
import java.util.Map;

import com.batsoft.core.common.enums.KeyEnum;
import com.batsoft.core.common.i18n.LanguageConvetSingleton;
import com.batsoft.utils.gson.GsonSingleton;

/**
 * 提示代码
 * 
 * @author simon
 */
public enum ClientMessageEnum {
	
	/**
	 * 货币数量非空
	 * 
	 */
	COIN_AMOUNT_NOT_NULL, 
	
	/**
	 * 货币类型非空
	 * 
	 */
	COIN_ORIGINAL_TYPE_NOT_NULL, 
	
	
	;
	
	private ClientMessageEnum() {
		this.code = this.name();
		this.message = LanguageConvetSingleton.getInstance().message(this.getCode());
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
	
	/**
	 * 将提示装换成Json
	 * 
	 * @return
	 */
	public String toJson() {
		return GsonSingleton.getInstance().toJson(toMap());
	}
	
	
}
