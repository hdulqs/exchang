package com.batsoft.utils.request.xmlhttprequest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

/**
 * 检查AJAX请求
 * 
 * @author simon
 */
public class XMLHttpRequestMotion implements XMLHttpRequestBehavior {
	
	@Override
	public boolean check(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		if (StringUtils.hasText(requestType)) {
			if ("XMLHttpRequest".equalsIgnoreCase(requestType)) {
				return true;
			}
		} else {
			requestType = request.getHeader("x-requested-with");
			if ("XMLHttpRequest".equalsIgnoreCase(requestType)) {
				return true;
			}
		}
		return false;
	}
	
}
