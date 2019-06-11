package com.batsoft.core.common.soket.http.behavior;

import java.util.Map;

public interface HttpRequestBehavior<T> {
	
	/**
	 * 发起请求
	 * 
	 * @param action
	 * 			请求地址
	 * @param param
	 * 			请求参数
	 * @return
	 */
	public T request(String action, Map<String, String> param);
	
}
