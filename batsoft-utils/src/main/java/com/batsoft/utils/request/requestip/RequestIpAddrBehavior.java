package com.batsoft.utils.request.requestip;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取IP地址
 * 
 * @author simonall
 *
 */
public interface RequestIpAddrBehavior {
	
	/**
	 * 获取IP地址
	 * 
	 * @param request
	 * @return
	 */
	String getIpAddr(HttpServletRequest request);
	
}
