package com.batsoft.common.interceptor;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.batsoft.core.annotation.CheckLogin;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.model.module.member.vo.UserVo;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.utils.gson.GsonSingleton;


/**
 * 登录拦截器
 * 
 * @author simon
 */
public class CheckLoginInterceptor extends HandlerInterceptorAdapter  {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(handler.getClass().equals(HandlerMethod.class)) {
			CheckLogin passport = ((HandlerMethod)handler).getMethodAnnotation(CheckLogin.class);
			if(passport != null && Objects.equals(passport.value(), CheckLogin.CHECK_USER_LOGIN)){
				return checkUserLogin(request, response, handler);
			}
		}
		return Boolean.TRUE;
	}

	/**
	 * 检查用户是否登录
	 * 
	 * @param request
	 * 			  请求头
	 * @param response
	 * 			  响应头
	 * @param handler
	 * 			  被标识的方法
	 * @return
	 * @throws Exception 
	 */
	private boolean checkUserLogin(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		UserVo user = UserUtils.getUser();
		if(user == null || !StringUtils.hasText(user.getId())){
			return notification(request, response, handler);
		}
		return super.preHandle(request, response, handler);
	}
	
	/**
	 * 发送通知
	 * 
	 * @param request
	 * 			 请求域
	 * @param response
	 * 			 响应域
	 * @param handler
	 * 			 目标方法
	 * @return
	 * @throws IOException 
	 */
	private boolean notification(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException{
		response.setContentType("application/json;charset=UTF-8");
		ServletOutputStream out = response.getOutputStream();
		try {
			ResponseBody responseBody = ((HandlerMethod)handler).getMethodAnnotation(ResponseBody.class);
			if(responseBody != null){
				JsonResult<String> result = new JsonResult<String>(false);
	            result.setCode(JsonResult.ResultCode.NO_LOGIN);
	            result.setMsg(Language.L_Failed("msg_no_login"));
				out.write(GsonSingleton.getInstance().toJson(result).getBytes(Constants.UTF8));
			}
		} finally {
			if(out != null) {
				out.flush();
				out.close();
			}
		}
		return Boolean.FALSE;
	}
	
}
