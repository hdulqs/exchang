package com.batsoft.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Cookie工具类
 * 
 * @author simon
 */
public class CookieUtil {
	
	/**
	 * 设置cookie
	 * 
	 * @param response
	 * @param name     
	 * 				cookie名字
	 * @param value    
	 * 				cookie值
	 * @param maxAge   
	 * 				cookie生命周期 以秒为单位
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		if (maxAge > 0) {
			cookie.setMaxAge(maxAge);
		}
		response.addCookie(cookie);
	}

	/**
	 * 根据名字获取cookie
	 * 
	 * @param request
	 * 				请求域
	 * @param name    
	 * 				cookie名字
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = ReadCookieMap(request);
		if (cookieMap.containsKey(name)) {
			Cookie cookie = (Cookie) cookieMap.get(name);
			return cookie;
		} else {
			return null;
		}
	}

	/**
	 * 将cookie封装到Map里面
	 * 
	 * @param request
	 * @return
	 */
	private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}
	
	/**
	 * 添加cookie(这种写入Cookie的方式，过期时间跟会话保持一致)
	 * 
	 * @param response
	 *            response 对象
	 * @param name
	 *            cookie名称
	 * @param value
	 *            cookie值
	 * 
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, String path, String domain, boolean httpOnly) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(path);
		cookie.setDomain(domain);
		cookie.setHttpOnly(httpOnly);
		response.addCookie(cookie);
	}

	/**
	 * 添加cookie （这中添加Cookie的方式会把cookie值写入客户端计算机并有过期时间）
	 * 
	 * @param response
	 *            response 对象
	 * @param name
	 *            cookie名称
	 * @param value
	 *            cookie值
	 * @param maxAge
	 *            cookie生存时间 设置Cookie最大生存时间,以秒为单位,负数的话为浏览器进程,关闭浏览器Cookie消失
	 *            不自己设置的情况下可空，默认为数据字典的设置,
	 * 
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, Integer maxAge, String path, String domain, boolean httpOnly) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge == null ? 1800 : maxAge);
		cookie.setPath(path);
		cookie.setDomain(domain);
		cookie.setHttpOnly(httpOnly);
		response.addCookie(cookie);
	}

	/**
	 * 删除cookie
	 * 
	 * @param response
	 *            请求响应
	 * 
	 * @param name
	 *            cookie Name
	 */
	public static void removeCookie(HttpServletResponse response, String name, String path, String domain, boolean httpOnly) {
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0);
		cookie.setPath(path);
		cookie.setDomain(domain);
		cookie.setHttpOnly(httpOnly);
		response.addCookie(cookie);
	}

	/**
	 * * 获取cookie值
	 * 
	 * @param request
	 *            请求域
	 * @param cookieName
	 *            cookie名称
	 * @return String cookie值
	 */
	public static String getUid(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}


}
