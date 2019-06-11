package com.batsoft.utils.request.xmlhttprequest;

import javax.servlet.http.HttpServletRequest;

public interface XMLHttpRequestBehavior {
	
	/**
	 * 检查请求是否为ajax请求 <br/>
	 * 是：true <br/>
	 * 否：false
	 */
	boolean check(HttpServletRequest request);
	
}
