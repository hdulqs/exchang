package com.batsoft.common.enums;

import java.util.HashMap;
import java.util.Map;

import com.batsoft.core.common.enums.KeyEnum;
import com.batsoft.core.common.i18n.LanguageConvetSingleton;
import com.batsoft.utils.gson.GsonSingleton;

/**
 * 模块响应码和响应信息
 * 
 * @author simon
 */
public enum ModuleMessageEnum {
	
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
	
	/**
	 * 交易对为空
	 * 
	 */
	COIN_PAIR_IS_NULL, 
	
	/**
	 * 参数为空
	 * 
	 */
	PARAM_IS_NULL, 
	
	/**
	 * 时间类型为空
	 * 
	 */
	KLINE_DATA_SUBJECT_IS_NULL, 
	
	/**
	 * 开始时间戳为空
	 * 
	 */
	KLINE_DATA_FROM_IS_NULL, 
	
	/**
	 * 结束时间戳为空
	 * 
	 */
	KLINE_DATA_TO_IS_NULL,
	
	/**
	 * 数据为空
	 * 
	 */
	NO_DATA, 
	
	/**
	 * 开始时间大于最后一条记录的时间
	 * 
	 */
	 VALID_DATA, 
	
	/**
	 * 用户名为空
	 * 
	 */
	USER_NAME_IS_NULL, 
	
	/**
	 * 密码为空
	 * 
	 */
	PASSWORD_IS_NULL, 
	
	/**
	 * 交易币为空
	 * 
	 */
	TRADE_COIN_CODE_IS_NULL, 
	
	/**
	 * 定价币为空
	 * 
	 */
	PRICING_COIN_CODE_IS_NULL, 
	
	/**
	 * 最小委托数量为空
	 * 
	 */
	MIN_ENTRUST_AMOUT_IS_NULL, 
	
	/**
	 * 最大委托数量为空
	 * 
	 */
	MAX_ENTRUST_AMOUT_IS_NULL, 
	
	/**
	 * 委托单价为空
	 * 
	 */
	ENTRUST_PRICE_IS_NULL, 
	
	/**
	 * 下单量为空
	 *  
	 */
	PORTION_RATE_IS_NULL, 
	
	/**
	 * 用户名或密码错误
	 * 
	 */
	USER_NAME_OR_PASSWORD_ERROR, 
	
	/**
	 * 交易授权码为空
	 * 
	 */
	TRADE_HANDLER_TOKEN_IS_NULL,  
	
	/**
	 * 交易授权码错误
	 * 
	 */
	TRADE_HANDLER_TOKEN_ERROR, 
	
	/**
	 * 操作类型为空
	 * 
	 */
	TRADE_HANDLE_TYPE_IS_NULL, 
	
	/**
	 * 委托类型为空
	 * 
	 */
	ENTRUST_TYPE_IS_NULL, 
	
	/**
	 * 交易对为空
	 * 
	 */
	SYMBOL_IS_NULL, 
	
	/**
	 * 开始时间为空
	 *  
	 */
	BEGIN_TIME_IS_NULL, 
	
	/**
	 * 结束时间为空
	 * 
	 */
	END_TIME_IS_NULL, 
	
	/**
	 * 开始时间大于最后数据节点时间
	 * 
	 */
	BEGIN_TIME_GT_LAST_NODE_TIME, 
	 
	/**
	 * 非法请求
	 * 
	 */
	ILLEGAL_REQUEST, 
	
	/**
	 * 访问频繁
	 * 
	 */
	VISIT_FREQUENTLY, 
	
	/**
	 * 订单号为空
	 * 
	 */
	ORDER_ID_IS_NULL, 
	
	/**
	 * 委托数量为空
	 * 
	 */
	ENTRUST_AMOUT_IS_NULL, 
	
	/**
	 * 未登录
	 * 
	 */
	NO_LOGIN, 
	
	/**
	 * 委托成功
	 * 
	 */
	ENTRUST_SUCCESSFUL, 
	
	/**
	 * 交易币为空
	 * 
	 */
	ENTRUST_TRADE_CODE_IS_NULL, 
	
	/**
	 * 定价币为空
	 * 
	 */ 
	ENTRUST_PRICING_CODE_IS_NULL, 
	
	/**
	 * 委托单类型为空
	 * 
	 */
	ENTRUST_CATEGORY_IS_NULL,
	
	     
	; 
	
	private ModuleMessageEnum() {
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
