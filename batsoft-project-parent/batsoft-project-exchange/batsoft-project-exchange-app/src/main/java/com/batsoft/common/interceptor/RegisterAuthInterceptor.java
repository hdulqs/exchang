package com.batsoft.common.interceptor;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.batsoft.common.beans.dto.AuthParamDTO;
import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.core.annotation.RegisterAuth;
import com.batsoft.core.cache.RedisCacheSignleton;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.JsonResult.ResultCode;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.utils.CookieUtil;
import com.batsoft.utils.MessageDigestUtil;
import com.batsoft.utils.date.BaseDate;
import com.batsoft.utils.date.compute.behavior.DateDiffBehavior;
import com.batsoft.utils.date.compute.behavior.DateMathBehavior;
import com.batsoft.utils.date.compute.motion.DateDiffMotion;
import com.batsoft.utils.date.compute.motion.DateMathMotion;
import com.batsoft.utils.gson.GsonSingleton;
import com.batsoft.utils.request.RequestParamUtil;
import com.batsoft.utils.request.requestip.RequestIpAddrBehavior;
import com.batsoft.utils.request.requestip.RequestIpAddrMotion;
import com.batsoft.utils.request.xmlhttprequest.XMLHttpRequestBehavior;
import com.batsoft.utils.request.xmlhttprequest.XMLHttpRequestMotion;

/**
 * 注册鉴权
 * 
 * @author simon
 */
public class RegisterAuthInterceptor extends HandlerInterceptorAdapter {
	
	private static final String REDIS_FOLDER = "AUTH:REGISTER:";
	
	private static final String REGISTER_AUTH_COOKIE_NAME = UUID.randomUUID().toString().substring(0, 10);
	
	private RedisCacheSignleton cacheClient = RedisCacheSignleton.getInstance();
	
	private RequestIpAddrBehavior ipMotion = new RequestIpAddrMotion();
	
	private XMLHttpRequestBehavior xmlMotion = new XMLHttpRequestMotion();
	
	private DateMathBehavior mathMotion = new DateMathMotion();
	
	private DateDiffBehavior diffMotion = new DateDiffMotion();
	
	private static final Integer MAX_COUNT = 10;
	
	private static final Integer DISABLED_TIME = 3600;
	
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(handler.getClass().equals(HandlerMethod.class)) {
			RegisterAuth passport = ((HandlerMethod)handler).getMethodAnnotation(RegisterAuth.class);
			if(passport != null && Objects.equals(passport.value(), RegisterAuth.SIMPLE_PATTERN)) {
				return simplePattern(request, response, handler);
			}
			if(passport != null && Objects.equals(passport.value(), RegisterAuth.ADD_NODE)) {
				this.addNode(request, response, handler);
			}
			if(passport != null && Objects.equals(passport.value(), RegisterAuth.CHECK_USER_REGISTER)){
				return checkRegister(request, response, handler);
			}
		}
		return Boolean.TRUE;
	}
	
	/**
	 * 简单的校验模式
	 * 
	 * @param handler
	 */
	private boolean simplePattern(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String ip = ipMotion.getIpAddr(request);
		AuthParamDTO param = cacheClient.get(REDIS_FOLDER + ip);
		if(param == null) { // 为空则增加一个统计容器
			param = new AuthParamDTO();
			param.setCount(BigInteger.ONE.intValue());
			cacheClient.set(REDIS_FOLDER + ip, param, DISABLED_TIME); 
		}else { 			// 不为空则校验过期时间和增加次数
			Date beginTime = BaseDate.getNowTime();
			Date endTime = param.getEndTime();
			if(endTime != null && diffMotion.dateDiffSecond(beginTime, endTime) > 0) {
				return buildValue(request, response, ModuleMessageEnum.VISIT_FREQUENTLY);
			}else {
				Integer count = param.getCount();
				if(count >= MAX_COUNT) {
					param.setBeginTime(beginTime);
					param.setEndTime(mathMotion.dateAddSecond(beginTime, DISABLED_TIME));
					param.setCount(0);
				}else {
					param.setCount(++count);
					param.setBeginTime(null);
					param.setEndTime(null);
				}
				cacheClient.set(REDIS_FOLDER + ip, param, DISABLED_TIME);
			}
		}
		return super.preHandle(request, response, handler);
	}
	
	/**
	 * 【埋点模式】添加节点
	 * 
	 * @param handler
	 */
	private void addNode(HttpServletRequest request, HttpServletResponse response, Object handler) {
		// 增加一个Cookie
		String cookieValue = MessageDigestUtil.md5(ipMotion.getIpAddr(request));
		String path = CHS.left_slash.getValue();
		String domain = request.getServerName();
		CookieUtil.addCookie(response, REGISTER_AUTH_COOKIE_NAME, cookieValue, path, domain, true);
		
		// 增加一个统计容器
		if(!cacheClient.verifyKeyExist(REDIS_FOLDER + cookieValue)) {
			AuthParamDTO param = new AuthParamDTO();
			param.setCount(0);
			cacheClient.set(REDIS_FOLDER + cookieValue, param, DISABLED_TIME);
		}
	}
	
	/**
	 * 【埋点模式】处理拦截
	 * 
	 * @param request
	 * 			请求域
	 * @param response
	 * 			响应域
	 * @param handler
	 * 			目标方法
	 * @return
	 * @throws Exception 
	 */
	private boolean checkRegister(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String ip = ipMotion.getIpAddr(request);
		RequestParamUtil handerUtil = new RequestParamUtil(request);
		// Referer
		if(!StringUtils.hasText(handerUtil.getReferer())) {
			return buildValue(request, response, ModuleMessageEnum.ILLEGAL_REQUEST);
		}
		// 是否为Ajax请求
		if(!xmlMotion.check(request)) {
			return buildValue(request, response, ModuleMessageEnum.ILLEGAL_REQUEST);
		}
		// 检查Cookie
		String cookieValue = CookieUtil.getUid(request, REGISTER_AUTH_COOKIE_NAME);
		if(!StringUtils.hasText(cookieValue) || !Objects.equals(cookieValue, MessageDigestUtil.md5(ip))) {
			return buildValue(request, response, ModuleMessageEnum.ILLEGAL_REQUEST);
		}
		// 检查统计次数
		AuthParamDTO param = cacheClient.get(REDIS_FOLDER + cookieValue);
		if(param == null) {
			return buildValue(request, response, ModuleMessageEnum.ILLEGAL_REQUEST);
		}else {
			Date beginTime = BaseDate.getNowTime();
			Date endTime = param.getEndTime();
			if(endTime != null && diffMotion.dateDiffSecond(beginTime, endTime) > 0) {
				return buildValue(request, response, ModuleMessageEnum.VISIT_FREQUENTLY);
			}else {
				Integer count = param.getCount();
				if(count >= MAX_COUNT) {
					param.setBeginTime(beginTime);
					param.setEndTime(mathMotion.dateAddSecond(beginTime, DISABLED_TIME));
					param.setCount(BigInteger.ONE.intValue());
				}else {
					param.setCount(++count);
					param.setBeginTime(null);
					param.setEndTime(null);
				}
				cacheClient.set(REDIS_FOLDER + cookieValue, param, DISABLED_TIME);
			}
		}
		return super.preHandle(request, response, handler);
	}
	
	/**
	 * 返回结果通知
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean buildValue(HttpServletRequest request, HttpServletResponse response, ModuleMessageEnum message) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		ServletOutputStream out = response.getOutputStream(); 
		try {
			GsonSingleton gsonClient = GsonSingleton.getInstance();
			JsonResult<String> result = new JsonResult<String>(false);
			result.setCode(ResultCode.FAILE);
			result.setMsg(message.getMessage());
			out.write(gsonClient.toJson(result).getBytes(Constants.UTF8));
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
		return false;
	}
	
}