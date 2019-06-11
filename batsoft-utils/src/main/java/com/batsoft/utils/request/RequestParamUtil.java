package com.batsoft.utils.request;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.batsoft.utils.gson.GsonSingleton;

/**
 * 获取请求中所有参数
 * 
 * @author simon
 */
public class RequestParamUtil {
	
	private Map<String, String> headParam = new HashMap<String, String>();
	
	public RequestParamUtil(HttpServletRequest request) {
		Enumeration<String> hander = request.getHeaderNames();
		while (hander.hasMoreElements()) {
			String key = hander.nextElement();
			headParam.put(key, request.getHeader(key));
		}
	}
	
	public String getHander() {
		return GsonSingleton.getInstance().toJson(headParam);
	}
	
	public Map<String, String> getHeadParam() {
		return headParam;
	}
	
	public String getUserAgent() {
		String result = headParam.get("User-Agent");
		if(!StringUtils.hasText(result)) {
			headParam.get("user-agent");
		}
		return result;
	}
	
	public String getReferer() {
		String result = headParam.get("Referer");
		if(!StringUtils.hasText(result)) {
			result = headParam.get("referer");
		}
		return result;
	}
}
